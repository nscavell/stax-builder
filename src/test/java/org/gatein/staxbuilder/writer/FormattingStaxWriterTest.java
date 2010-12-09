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
   @Test
   public void defaultFormattingWriter() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").withDefaultFormatting().build();

      writer.writeStartDocument().writeStartElement("level-1");
      writer.writeStartElement("level-2-1").writeAttribute("attr", "value").writeCharacters("some text here").writeEndElement();
      writer.writeElement("level-2-2", "some long text here to test out same line character limit logic.");
      writer.writeElement("level-2-3", "multi\nline text\nhere\n");
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
      writer.writeElement("level-2-3", "multi\nline text\nhere\n");
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
      writer.writeElement("level-2-3", "multi\nline text\nhere\n");
      writer.writeStartElement("level-2-4").writeCharacters("characters here too").writeElement("level-2-4-1", "more");
      writer.writeEndElement().writeEndElement().writeEndDocument();

      Assert.assertEquals(customExpectedWithTab, sw.toString());
   }

   private static final String defaultExpected =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<level-1>\n" +
      "   <level-2-1 attr=\"value\">some text here</level-2-1>\n" +
      "   <level-2-2>some long text here to test out same line character limit logic.</level-2-2>\n" +
      "   <level-2-3>multi\n" +
      "      line text\n" +
      "      here\n" +
      "   </level-2-3>\n" +
      "   <level-2-4>\n" +
      "      characters here too\n" +
      "      <level-2-4-1>more</level-2-4-1>\n" +
      "   </level-2-4>\n" +
      "</level-1>";

   private static final String customExpected =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<level-1>\n" +
      "     <level-2-1 attr=\"value\">some text here</level-2-1>\n" +
      "     <level-2-2>\n"+
      "          some long text here to test out same line character limit logic.\n"+
      "     </level-2-2>\n" +
      "     <level-2-3>multi\n" +
      "          line text\n" +
      "          here\n" +
      "     </level-2-3>\n" +
      "     <level-2-4>\n" +
      "          characters here too\n" +
      "          <level-2-4-1>more</level-2-4-1>\n" +
      "     </level-2-4>\n" +
      "</level-1>";

   private static final String customExpectedWithTab =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<level-1>\n" +
      "\t\t<level-2-1 attr=\"value\">some text here</level-2-1>\n" +
      "\t\t<level-2-2>\n"+
      "\t\t\t\tsome long text here to test out same line character limit logic.\n"+
      "\t\t</level-2-2>\n" +
      "\t\t<level-2-3>multi\n" +
      "\t\t\t\tline text\n" +
      "\t\t\t\there\n" +
      "\t\t</level-2-3>\n" +
      "\t\t<level-2-4>\n" +
      "\t\t\t\tcharacters here too\n" +
      "\t\t\t\t<level-2-4-1>more</level-2-4-1>\n" +
      "\t\t</level-2-4>\n" +
      "</level-1>";
}
