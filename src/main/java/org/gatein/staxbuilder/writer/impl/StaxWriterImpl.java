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

package org.gatein.staxbuilder.writer.impl;

import org.gatein.staxbuilder.EnumAttribute;
import org.gatein.staxbuilder.EnumElement;
import org.gatein.staxbuilder.conversion.DataTypeConverter;
import org.gatein.staxbuilder.writer.StaxWriter;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Calendar;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
//TODO: Support all XMLStreamWriter methods, ie namepsaces, comments, cdata, etc
public class StaxWriterImpl implements StaxWriter
{
   private final XMLStreamWriter writer;
   private final String encoding;
   private final String version;
   private final Map<QName, DataTypeConverter> converters;

   public StaxWriterImpl(final XMLStreamWriter writer, final String encoding, final String version, Map<QName, DataTypeConverter> converters)
   {
      this.writer = writer;
      this.encoding = encoding;
      this.version = version;
      this.converters = converters;
   }

   @Override
   public StaxWriter writeStartDocument() throws XMLStreamException
   {
      if (encoding == null && version == null)
      {
         writer.writeStartDocument();
         return this;
      }

      if (encoding == null)
      {
         writer.writeStartDocument(version);
         return this;
      }

      writer.writeStartDocument(encoding, version);
      return this;
   }

   public StaxWriter writeEndDocument() throws XMLStreamException
   {
      writer.writeEndDocument();
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String localName) throws XMLStreamException
   {
      writer.writeStartElement(localName);
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String prefix, String namespaceURI, String localName) throws XMLStreamException
   {
      writer.writeStartElement(prefix, namespaceURI, localName);
      return this;
   }

   @Override
   public StaxWriter writeStartElement(String namespaceURI, String localName) throws XMLStreamException
   {
      writer.writeStartElement(namespaceURI, localName);
      return this;
   }

   @Override
   public StaxWriter writeStartElement(EnumElement element) throws XMLStreamException
   {
      return writeStartElement(element.getLocalName());
   }

   @Override
   public StaxWriter writeStartElement(String prefix, String namespaceURI, EnumElement element) throws XMLStreamException
   {
      return writeStartElement(prefix, namespaceURI, element.getLocalName());
   }

   @Override
   public StaxWriter writeStartElement(String namespaceURI, EnumElement element) throws XMLStreamException
   {
      return writeStartElement(namespaceURI, element.getLocalName());
   }

   @Override
   public StaxWriter writeDefaultNamespace(String namespaceURI) throws XMLStreamException
   {
      writer.writeDefaultNamespace(namespaceURI);
      return this;
   }

   @Override
   public StaxWriter writeNamespace(String prefix, String namespaceURI) throws XMLStreamException
   {
      writer.writeNamespace(prefix, namespaceURI);
      return this;
   }

   @Override
   public StaxWriter writeEndElement() throws XMLStreamException
   {
      writer.writeEndElement();
      return this;
   }

   @Override
   public StaxWriter writeAttribute(String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(localName, value);
      return this;
   }

   @Override
   public StaxWriter writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(prefix, namespaceURI, localName, value);
      return this;
   }

   @Override
   public StaxWriter writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(namespaceURI, localName, value);
      return this;
   }

   @Override
   public StaxWriter writeAttribute(EnumAttribute attribute, String value) throws XMLStreamException
   {
      return writeAttribute(attribute.getLocalName(), value);
   }

   @Override
   public StaxWriter writeAttribute(String prefix, String namespaceURI, EnumAttribute attribute, String value) throws XMLStreamException
   {
      return writeAttribute(prefix, namespaceURI, attribute.getLocalName(), value);
   }

   @Override
   public StaxWriter writeAttribute(String namespaceURI, EnumAttribute attribute, String value) throws XMLStreamException
   {
      return writeAttribute(namespaceURI, attribute.getLocalName(), value);
   }

   @Override
   public StaxWriter writeCharacters(String text) throws XMLStreamException
   {
      writer.writeCharacters(text);
      return this;
   }

   @Override
   public StaxWriter writeElement(String localName, String text) throws XMLStreamException
   {
      return writeStartElement(localName).writeCharacters(text).writeEndElement();
   }

   @Override
   public StaxWriter writeElement(EnumElement element, String text) throws XMLStreamException
   {
      return writeElement(element.getLocalName(), text);
   }

   @Override
   public StaxWriter writeOptionalElement(String localName, String text) throws XMLStreamException
   {
      if (text == null) return this;

      return writeElement(localName, text);
   }

   @Override
   public StaxWriter writeOptionalElement(EnumElement element, String text) throws XMLStreamException
   {
      return writeOptionalElement(element.getLocalName(), text);
   }

   @Override
   public StaxWriter writeDate(Calendar date) throws XMLStreamException
   {
      DataTypeConverter<Calendar> converter = getDataTypeConverter(DatatypeConstants.DATE, Calendar.class);
      return writeCharacters(converter.print(date));
   }

   @Override
   public StaxWriter writeDateTime(Calendar date) throws XMLStreamException
   {
      DataTypeConverter<Calendar> converter = getDataTypeConverter(DatatypeConstants.DATETIME, Calendar.class);
      return writeCharacters(converter.print(date));
   }

   @Override
   @SuppressWarnings("unchecked")
   public StaxWriter writeObject(QName qname, Object object) throws XMLStreamException
   {
      DataTypeConverter converter = getDataTypeConverter(qname);
      writeCharacters(converter.print(object));
      return this;
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

   private DataTypeConverter getDataTypeConverter(QName namespace) throws XMLStreamException
   {
      DataTypeConverter dtc = converters.get(namespace);
      if (dtc == null) throw new XMLStreamException("No data type converter found for namespace " + namespace);

      return dtc;
   }

   @SuppressWarnings("unchecked")
   private <T> DataTypeConverter<T> getDataTypeConverter(QName namespace, Class<T> type) throws XMLStreamException
   {
      DataTypeConverter dtc = getDataTypeConverter(namespace);

      try
      {
         return (DataTypeConverter<T>) dtc;
      }
      catch (ClassCastException cce)
      {
         throw new XMLStreamException("Was expecting converter of DataTypeConverter<" + type + "> for namespace " + namespace);
      }
   }
}
