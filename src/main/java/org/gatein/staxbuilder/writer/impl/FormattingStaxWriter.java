package org.gatein.staxbuilder.writer.impl;

import org.gatein.staxbuilder.conversion.DataTypeConverter;
import org.gatein.staxbuilder.writer.StaxWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class FormattingStaxWriter extends StaxWriterImpl implements XMLStreamConstants
{
   private final FormattingInfo formattingInfo;
   private final String indentString;

   private int state = START_DOCUMENT;
   private int level;
   private StringBuilder buffer;

   public FormattingStaxWriter(final XMLStreamWriter writer, final String encoding, final String version, Map<QName, DataTypeConverter> converters, FormattingInfo formattingInfo)
   {
      super(writer, encoding, version, converters);

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

      super.writeStartDocument();
      nl();

      return this;
   }

   @Override
   public StaxWriter writeEndDocument() throws XMLStreamException
   {
      changeState(END_DOCUMENT);

      super.writeEndDocument();
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String localName) throws XMLStreamException
   {
      changeState(START_ELEMENT);
      super.writeStartElement(localName);
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String prefix, String namespaceURI, String localName) throws XMLStreamException
   {
      changeState(START_ELEMENT);
      super.writeStartElement(prefix, namespaceURI, localName);
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String namespaceURI, String localName) throws XMLStreamException
   {
      changeState(START_ELEMENT);
      super.writeStartElement(namespaceURI, localName);
      return this;
   }

   @Override
   public StaxWriter writeEndElement() throws XMLStreamException
   {
      changeState(END_ELEMENT);
      super.writeEndElement();
      return this;
   }

   @Override
   public StaxWriter writeAttribute(String localName, String value) throws XMLStreamException
   {
      changeState(ATTRIBUTE);
      super.writeAttribute(localName, value);
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

   //------------------------- Private Impl -------------------------//

   private FormattingStaxWriter nl() throws XMLStreamException
   {
      super.writeCharacters(formattingInfo.getNewline());
      return this;
   }
   
   private FormattingStaxWriter indent() throws XMLStreamException
   {
      for (int i = 0; i < level; i++)
      {
         super.writeCharacters(indentString);
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
         super.writeCharacters(segments[i]);
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
