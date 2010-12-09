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

import org.gatein.staxbuilder.writer.StaxWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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

   public StaxWriterImpl(final XMLStreamWriter writer, final String encoding, final String version)
   {
      this.writer = writer;
      this.encoding = encoding;
      this.version = version;
   }

   @Override
   public StaxWriter startDocument() throws XMLStreamException
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

   public StaxWriter endDocument() throws XMLStreamException
   {
      writer.writeEndDocument();
      return this;
   }

   @Override
   public StaxWriter startElement(String localName) throws XMLStreamException
   {
      writer.writeStartElement(localName);
      return this;
   }

   @Override
   public StaxWriter endElement() throws XMLStreamException
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
   public StaxWriter writeCharacters(String text) throws XMLStreamException
   {
      writer.writeCharacters(text);
      return this;
   }

   @Override
   public StaxWriter writeElement(String localName, String text) throws XMLStreamException
   {
      return startElement(localName).writeCharacters(text).endElement();
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
}
