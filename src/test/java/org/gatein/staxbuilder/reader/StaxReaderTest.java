/*
 * JBoss, a division of Red Hat
 * Copyright 2010, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.staxbuilder.reader;

import org.gatein.staxbuilder.EnumElement;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxReaderTest
{
   @Test
   public void simpleRead() throws XMLStreamException
   {
      StringReader sr = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo><bar>some text here</bar></foo>");
      StaxReader reader = new StaxReaderBuilder().withReader(sr).build();
      String foo = reader.readNextTag().getLocalName();
      Assert.assertEquals("foo", foo);
      String bar = reader.readNextTag().getLocalName();
      Assert.assertEquals("bar", bar);
      Assert.assertEquals("some text here", reader.currentReadEvent().elementText());
   }

   @Test
   public void nestedRead() throws Exception
   {
      StringReader sr = new StringReader(
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<books>\n" +
         "   <book>\n" +
         "      <title>Title A</title>\n" +
         "      <author>Author A</author>\n" +
         "      <price>$15.99</price>\n" +
         "      <samples>\n" +
         "         <sample>" +
         "            <chapter>1</chapter>\n" +
         "            <text>Some chapter 1 text here, and so on and so on...\n</text>" +
         "         </sample>\n" +
         "         <sample>" +
         "            <chapter>2</chapter>\n" +
         "            <text>Chapter 2 text blah blah blah\n</text>" +
         "         </sample>\n" +
         "      </samples>\n" +
         "   </book>\n" +
         "   <book>\n" +
         "      <title>Title B</title>\n" +
         "      <author>Author B</author>\n" +
         "      <price>$29.99</price>\n" +
         "   </book>\n" +
         "</books>");

      StaxReader reader = new StaxReaderBuilder().withReader(sr).build();
      List<Book> books = new ArrayList<Book>();
      if (reader.readNextTag().getLocalName().equals(Element.BOOKS.getLocalName()))
      {
         while (reader.hasNext())
         {
            switch(reader.read().match().onElement(Element.class, Element.UNKNOWN, Element.UNKNOWN))
            {
               case BOOK:
                  books.add(parseBook(reader, new Book())); // nested read inside parseBook
                  break;
               default:
            }
         }
      }
      else
      {
         throw new Exception(Element.BOOKS.name() + " root element not found.");
      }

      Assert.assertEquals(2, books.size());
      Assert.assertEquals("Title A", books.get(0).title);
      Assert.assertEquals("Author A", books.get(0).author);
      Assert.assertEquals("$15.99", books.get(0).price);

      Assert.assertEquals("Title B", books.get(1).title);
      Assert.assertEquals("Author B", books.get(1).author);
      Assert.assertEquals("$29.99", books.get(1).price);
   }

   @Test
   public void navigationRead() throws XMLStreamException
   {
      StringReader sr = new StringReader(
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<books>\n" +
         "   <book>\n" +
         "      <title>Title A</title>\n" +
         "      <author>Author A</author>\n" +
         "      <price>$15.99</price>\n" +
         "   </book>\n" +
         "   <book>\n" +
         "      <title>Title B</title>\n" +
         "      <author>Author B</author>\n" +
         "      <price>$29.99</price>\n" +
         "   </book>\n" +
         "</books>");

      StaxReader reader = new StaxReaderBuilder().withReader(sr).build();
      reader.readNextTag();
      NavigationReadEvent nav = reader.buildReadEvent().withNavigation();
      Assert.assertTrue(nav.child("book").success());
      Assert.assertEquals(2, nav.getLevel());
      Assert.assertTrue(nav.child("title").success());
      Assert.assertEquals(3, nav.getLevel());
      Assert.assertEquals("Title A", nav.getText());
      Assert.assertTrue(nav.sibling("author").success());
      Assert.assertEquals(3, nav.getLevel());
      Assert.assertEquals("Author A", nav.getText());
      Assert.assertTrue(nav.sibling("price").success());
      Assert.assertEquals(3, nav.getLevel());
      Assert.assertEquals("$15.99", nav.getText());
   }

   @Test
   public void navigationRead_ChildSkip() throws XMLStreamException
   {
      StringReader sr = new StringReader(
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<books>\n" +
         "   <book>\n" +
         "      <title>Title A</title>\n" +
         "      <author>Author A</author>\n" +
         "      <price>$15.99</price>\n" +
         "      <isbn>123456789</isbn>\n" +
         "      <cc>1990</cc>\n" +
         "   </book>\n" +
         "</books>");

      StaxReader reader = new StaxReaderBuilder().withReader(sr).build();
      reader.readNextTag();
      NavigationReadEvent nav = reader.buildReadEvent().withNavigation();
      Assert.assertTrue(nav.child("book").success());
      Assert.assertEquals(2, nav.getLevel());
      Assert.assertTrue(nav.child("price").success());
      Assert.assertEquals("price", nav.getLocalName());
      Assert.assertEquals("$15.99", nav.getText());
   }

   @Test
   public void navigationRead_SiblingSkip() throws XMLStreamException
   {
      StringReader sr = new StringReader(
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<books>\n" +
         "   <book>\n" +
         "      <title>Title A</title>\n" +
         "      <author>Author A</author>\n" +
         "      <price>$15.99</price>\n" +
         "      <isbn>123456789</isbn>\n" +
         "      <cc>1990</cc>\n" +
         "   </book>\n" +
         "</books>");

      StaxReader reader = new StaxReaderBuilder().withReader(sr).build();
      reader.readNextTag();
      NavigationReadEvent nav = reader.buildReadEvent().withNavigation();
      Assert.assertTrue(nav.child("book").success());
      Assert.assertTrue(nav.child("title").success());
      Assert.assertTrue(nav.sibling("price").success());
      Assert.assertEquals("price", nav.getLocalName());
      Assert.assertEquals("$15.99", nav.getText());
      Assert.assertTrue(nav.sibling("cc").success());
      Assert.assertEquals("cc", nav.getLocalName());
      Assert.assertEquals("1990", nav.getText());
   }

   private Book parseBook(StaxReader reader, Book book) throws XMLStreamException
   {
      int titleCount = 0;
      int authorCount = 0;
      int priceCount = 0;
      // This marks a nested read and calling hasNext will return true until the end of the book element has been reached.
      reader.buildReadEvent().withNestedRead().untilElement(Element.BOOK).end();
      while (reader.hasNext())
      {
         switch (reader.read().match().onElement(Element.class, Element.UNKNOWN, Element.UNKNOWN))
         {
            case TITLE:
               book.title = reader.currentReadEvent().elementText();
               titleCount++;
               break;
            case AUTHOR:
               book.author = reader.currentReadEvent().elementText();
               authorCount++;
               break;
            case PRICE:
               book.price = reader.currentReadEvent().elementText();
               priceCount++;
               break;
            case SAMPLES:
               book.samples = new ArrayList<Sample>();
               break;
            case SAMPLE:
               int chapterCount = 0;
               int textCount = 0;
               Sample sample = new Sample();
               reader.buildReadEvent().withNestedRead().untilElement(Element.SAMPLE).end();
               while (reader.hasNext())
               {
                  switch(reader.read().match().onElement(Element.class, Element.UNKNOWN, Element.UNKNOWN))
                  {
                     case CHAPTER:
                        sample.chapter = reader.currentReadEvent().elementText();
                        chapterCount++;
                        break;
                     case TEXT:
                        sample.text = reader.currentReadEvent().elementText();
                        textCount++;
                        break;
                  }
               }
               Assert.assertEquals("Nested read should have set chapter once.", 1, chapterCount);
               Assert.assertEquals("Nested read should have set text once.", 1, textCount);
               book.samples.add(sample);
            default:
               break;
         }
      }
      Assert.assertEquals("Nested read should have set title once.",  1, titleCount);
      Assert.assertEquals("Nested read should have set author once.", 1, authorCount);
      Assert.assertEquals("Nested read should have set price once.",  1, priceCount);
      if (book.samples != null)
      {
         Assert.assertEquals("Nested read should have created 2 samples.", 2, book.samples.size());
      }
      return book;
   }

   private static class Book
   {
      private String title;
      private String author;
      private String price;
      private List<Sample> samples;
   }

   private static class Sample
   {
      String chapter;
      String text;
   }

   private static enum Element implements EnumElement<Element>
   {
      UNKNOWN(null),
      BOOKS("books"),
      BOOK("book"),
      TITLE("title"),
      AUTHOR("author"),
      PRICE("price"),
      SAMPLES("samples"),
      SAMPLE("sample"),
      CHAPTER("chapter"),
      TEXT("text");


      private static final Map<String, Element> MAP;

      static {
         final Map<String, Element> map = new HashMap<String, Element>();
         for (Element element : values()) {
            final String name = element.getLocalName();
            map.put(name, element);
         }
         MAP = map;
      }

      private String name;

      Element(String name)
      {
         this.name = name;
      }

      @Override
      public String getLocalName()
      {
         return name;
      }

      public static Element forName(String name)
      {
         return MAP.get(name);
      }
   }
}
