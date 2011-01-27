package org.gatein.staxbuilder.writer;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class FormattingStaxWriterTest
{
   private static String nl;
   static{
      try
      {
         nl = System.getProperty("line.separator");
      }
      catch (Throwable ignored)
      {
         nl = "\n";
      }
   }
   
   @Test
   public void defaultFormattingWriter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").withDefaultFormatting().build();

      writer.writeStartDocument().writeStartElement("level-1");
      writer.writeStartElement("level-2-1").writeAttribute("attr", "value").writeCharacters("some text here").writeEndElement();
      writer.writeElement("level-2-2", "some long text here to test out same line character limit logic.");
      writer.writeElement("level-2-3", "multi"+nl+"line text"+nl+"here"+nl+"");
      writer.writeStartElement("level-2-4").writeCharacters("characters here too").writeElement("level-2-4-1", "more");
      writer.writeEndElement().writeEndElement().writeEndDocument();

      Assert.assertEquals(defaultExpected, sw.toString());
   }

   @Test
   public void customFormattingWriter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").
         withFormatting().indentSize(5).sameLineChracterLimit(25).and().build();

      writer.writeStartDocument().writeStartElement("level-1");
      writer.writeStartElement("level-2-1").writeAttribute("attr", "value").writeCharacters("some text here").writeEndElement();
      writer.writeElement("level-2-2", "some long text here to test out same line character limit logic.");
      writer.writeElement("level-2-3", "multi"+nl+"line text"+nl+"here"+nl+"");
      writer.writeStartElement("level-2-4").writeCharacters("characters here too").writeElement("level-2-4-1", "more");
      writer.writeEndElement().writeEndElement().writeEndDocument();

      Assert.assertEquals(customExpected, sw.toString());
   }

   @Test
   public void customFormattingWriterWithTab() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").
         withFormatting().indentSize(2).useTabCharacter(true).sameLineChracterLimit(25).and().build();

      writer.writeStartDocument().writeStartElement("level-1");
      writer.writeStartElement("level-2-1").writeAttribute("attr", "value").writeCharacters("some text here").writeEndElement();
      writer.writeElement("level-2-2", "some long text here to test out same line character limit logic.");
      writer.writeElement("level-2-3", "multi"+nl+"line text"+nl+"here"+nl+"");
      writer.writeStartElement("level-2-4").writeCharacters("characters here too").writeElement("level-2-4-1", "more");
      writer.writeEndElement().writeEndElement().writeEndDocument();

      Assert.assertEquals(customExpectedWithTab, sw.toString());
   }

   private static final String defaultExpected =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+nl+
      "<level-1>"+nl+
      "   <level-2-1 attr=\"value\">some text here</level-2-1>"+nl+
      "   <level-2-2>some long text here to test out same line character limit logic.</level-2-2>"+nl+
      "   <level-2-3>multi"+nl+
      "      line text"+nl+
      "      here"+nl+
      "   </level-2-3>"+nl+
      "   <level-2-4>"+nl+
      "      characters here too"+nl+
      "      <level-2-4-1>more</level-2-4-1>"+nl+
      "   </level-2-4>"+nl+
      "</level-1>";

   private static final String customExpected =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+nl+
      "<level-1>"+nl+
      "     <level-2-1 attr=\"value\">some text here</level-2-1>"+nl+
      "     <level-2-2>"+nl+""+
      "          some long text here to test out same line character limit logic."+nl+""+
      "     </level-2-2>"+nl+
      "     <level-2-3>multi"+nl+
      "          line text"+nl+
      "          here"+nl+
      "     </level-2-3>"+nl+
      "     <level-2-4>"+nl+
      "          characters here too"+nl+
      "          <level-2-4-1>more</level-2-4-1>"+nl+
      "     </level-2-4>"+nl+
      "</level-1>";

   private static final String customExpectedWithTab =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+nl+
      "<level-1>"+nl+
      "\t\t<level-2-1 attr=\"value\">some text here</level-2-1>"+nl+
      "\t\t<level-2-2>"+nl+""+
      "\t\t\t\tsome long text here to test out same line character limit logic."+nl+""+
      "\t\t</level-2-2>"+nl+
      "\t\t<level-2-3>multi"+nl+
      "\t\t\t\tline text"+nl+
      "\t\t\t\there"+nl+
      "\t\t</level-2-3>"+nl+
      "\t\t<level-2-4>"+nl+
      "\t\t\t\tcharacters here too"+nl+
      "\t\t\t\t<level-2-4-1>more</level-2-4-1>"+nl+
      "\t\t</level-2-4>"+nl+
      "</level-1>";
}
