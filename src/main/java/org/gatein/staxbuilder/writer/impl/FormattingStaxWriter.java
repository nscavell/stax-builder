package org.gatein.staxbuilder.writer.impl;

import org.gatein.staxbuilder.writer.StaxWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class FormattingStaxWriter implements StaxWriter, XMLStreamConstants
{
   private final StaxWriter writer;
   private final FormattingInfo formattingInfo;
   private final String indentString;

   private int state = START_DOCUMENT;
   private int level;
   //private int charsToBeWritten;
   private StringBuilder buffer;

   public FormattingStaxWriter(StaxWriter writer, FormattingInfo formattingInfo)
   {
      this.writer = writer;

      // Cannot accept a null FormattingInfo. If no formatting is needed this writer shouldn't be used.
      if (formattingInfo == null) throw new IllegalArgumentException("FormattingInfo cannot be null.");
      this.formattingInfo = formattingInfo;

      // Compute indentation string.
      StringBuilder sb = new StringBuilder();
      for (int i=0; i<formattingInfo.getIndentSize(); i++)
      {
         sb.append(formattingInfo.getIndentCharacter());
      }
      indentString = sb.toString();
   }

   //------------------------- StaxWriter Impl -------------------------//

   @Override
   public StaxWriter writeStartDocument() throws XMLStreamException
   {
      changeState(START_DOCUMENT);

      writer.writeStartDocument();
      nl();

      return this;
   }

   @Override
   public StaxWriter writeEndDocument() throws XMLStreamException
   {
      changeState(END_DOCUMENT);

      writer.writeEndDocument();
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String localName) throws XMLStreamException
   {
      changeState(START_ELEMENT);
      writer.writeStartElement(localName);
      return this;
   }

   @Override
   public StaxWriter writeEndElement() throws XMLStreamException
   {
      changeState(END_ELEMENT);
      writer.writeEndElement();
      return this;
   }

   @Override
   public StaxWriter writeAttribute(String localName, String value) throws XMLStreamException
   {
      changeState(ATTRIBUTE);
      writer.writeAttribute(localName, value);
      return this;
   }

   @Override
   public StaxWriter writeCharacters(final String text) throws XMLStreamException
   {
      changeState(CHARACTERS);
      if (buffer == null)
      {
         buffer = new StringBuilder();
      }
      buffer.append(text);

      return this;
   }

   @Override
   public StaxWriter writeElement(String localName, String text) throws XMLStreamException
   {
      return writeStartElement(localName).writeCharacters(text).writeEndElement();
   }

   @Override
   public StaxWriter writeOptionalElement(String localName, String text) throws XMLStreamException
   {
      if (text == null) return this;

      return writeElement(localName, text);
   }

   @Override
   public StaxWriter flush() throws XMLStreamException
   {
      writer.flush();
      return this;
   }

   @Override
   public void close() throws XMLStreamException
   {
      writer.close();
   }

   //------------------------- Private Impl -------------------------//

   private FormattingStaxWriter nl() throws XMLStreamException
   {
      writer.writeCharacters(formattingInfo.getNewline());
      return this;
   }
   
   private FormattingStaxWriter indent() throws XMLStreamException
   {
      for (int i = 0; i < level; i++)
      {
         writer.writeCharacters(indentString);
      }
      return this;
   }

   private FormattingStaxWriter changeState(int newState) throws XMLStreamException
   {
      if (state != START_DOCUMENT)
      {
         if (newState != CHARACTERS && state == CHARACTERS)
         {
            flushCharacters(newState);
         }
         if (newState == START_ELEMENT)
         {
            if (state != END_ELEMENT)
            {
               level++;
            }
            nl().indent();
         }
         if (newState == END_ELEMENT && state == END_ELEMENT)
         {
            level--;
            nl().indent();
         }
      }

      this.state = newState;
      return this;
   }

   private void flushCharacters(int newState) throws XMLStreamException
   {
      if (buffer == null || buffer.length() == 0) return;

      int prevLevel = level;
      String characters = buffer.toString();
      String[] segments = characters.split(formattingInfo.getNewline());

      int limit = formattingInfo.getSameLineCharacterLimit();
      if (newState != END_ELEMENT)
      {
         level++;
         nl().indent();
      }
      else if (limit >=0 && buffer.length() > limit)
      {
         level++;
         nl().indent();
      }
      else if (segments.length > 1)
      {
         level++;
      }

      for (int i=0; i<segments.length; i++)
      {
         if (i > 0)
         {
            nl().indent();
         }
         writer.writeCharacters(segments[i]);
      }
      if (prevLevel < level)
      {
         level--;
         if (newState == END_ELEMENT)
         {
            nl().indent();
         }
      }

      buffer = new StringBuilder();
   }
}
