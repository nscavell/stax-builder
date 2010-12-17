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

package org.gatein.staxbuilder.writer;

import org.gatein.staxbuilder.conversion.DataTypeConverter;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxWriterTest
{
   @Test
   public void createSimpleWriter_withDefaults() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String s1 = sw.toString();
      String version = getVersionFromXMLDocument(s1);
      String encoding = getEncodingFromXMLDocument(s1);

      Assert.assertEquals("1.0", version);
      Assert.assertNull(encoding);

      StringWriter sw2 = new StringWriter();
      StaxWriter writer2 = new StaxWriterBuilder().withWriter(sw2).withDefaults().build();
      Assert.assertNotNull(writer2);
      writer2.writeStartDocument().writeEndDocument();

      String s2 = sw2.toString();
      String version2 = getVersionFromXMLDocument(s2);
      String encoding2 = getEncodingFromXMLDocument(s2);

      Assert.assertEquals("1.0", version2);
      Assert.assertNull(encoding2);
   }

   @Test
   public void createSimpleWriter_withEncoding() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertEquals("UTF-8", encoding);
   }

   @Test
   public void createSimpleWriter_withVersion() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withVersion("1.0").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertNull(encoding);
   }

   @Test
   public void createSimpleWriter_withEncodingAndVersion() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("ISO-8859-1").withVersion("1.0").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertEquals("ISO-8859-1", encoding);
   }

   private static Calendar calendar = Calendar.getInstance();
   static
   {
      calendar.set(Calendar.YEAR, 2010);
      calendar.set(Calendar.MONTH, Calendar.DECEMBER);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 15);
      calendar.set(Calendar.MINUTE, 30);
      calendar.set(Calendar.SECOND, 10);
      calendar.set(Calendar.MILLISECOND, 100);
      calendar.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
   }

   @Test
   public void dataTypeConverter_defaultDateConverter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).build();
      writer.writeStartDocument().writeStartElement("date-test").writeDate(calendar).writeEndElement();

      Assert.assertTrue(sw.toString().contains("2010-12-01"));
   }

   @Test
   public void dataTypeConverter_defaultDateTimeConverter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).build();
      writer.writeStartDocument().writeStartElement("date-test").writeDateTime(calendar).writeEndElement();

      Assert.assertTrue(sw.toString().contains("2010-12-01T15:30:10.100-05:00"));
   }

   @Test
   public void dataTypeConverter_customPojoConverter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).
         registerDataTypeConverter(PojoConverter.namespace, new PojoConverter()).build();

      String id = UUID.randomUUID().toString();
      writer.writeStartElement("pojo-test").writeObject(PojoConverter.namespace, new Pojo(id)).writeEndElement();

      Assert.assertTrue(sw.toString().contains(id));
   }

   @Test
   public void dataTypeConverter_overrideDateTimeConverter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).
         registerDataTypeConverter(DatatypeConstants.DATETIME, new CustomDateTimeConverter()).build();

      writer.writeStartDocument().writeStartElement("date-test").writeDateTime(calendar).writeEndElement();

      Assert.assertTrue(sw.toString().contains("2010-12-01 03:30:10 PM EST"));
   }

   //TODO: More tests...

   private String getVersionFromXMLDocument(String xml) throws XMLStreamException
   {
      XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
      return reader.getVersion();
   }

   private String getEncodingFromXMLDocument(String xml) throws XMLStreamException
   {
      XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
      return reader.getCharacterEncodingScheme();
   }

   private static class Pojo
   {
      private String id;
      Pojo(String id)
      {
         this.id = id;
      }
   }

   private static class PojoConverter implements DataTypeConverter<Pojo>
   {
      
      private static final QName namespace = new QName("http://www.gatein.org/xml/ns/gatein_test_1_0", "pojo");

      @Override
      public String print(Pojo object) throws XMLStreamException
      {
         return object.id;
      }

      @Override
      public Pojo parse(String text) throws XMLStreamException
      {
         return new Pojo(text);
      }
   }

   private static class CustomDateTimeConverter implements DataTypeConverter<Calendar>
   {
      private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a z");

      @Override
      public String print(Calendar object) throws XMLStreamException
      {
         return sdf.format(object.getTime());
      }

      @Override
      public Calendar parse(String text) throws XMLStreamException
      {
         try
         {
            Calendar c = Calendar.getInstance();
            Date d = sdf.parse(text);
            c.setTime(d);
            return c;
         }
         catch (ParseException e)
         {
            throw new XMLStreamException(e);
         }
      }
   }
}
