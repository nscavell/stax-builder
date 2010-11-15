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

package org.gatein.xml.stax.writer;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Simple wrapper around an XMLStreamWriter, allowing methods to be continually called.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxWrapper
{
   private XMLStreamWriter writer;

   public StaxWrapper(XMLStreamWriter writer)
   {
      this.writer = writer;
   }
   
   public StaxWrapper writeStartElement(String localName) throws XMLStreamException
   {
      writer.writeStartElement(localName);
      return this;
   }

   public StaxWrapper writeStartElement(String namespaceURI, String localName) throws XMLStreamException
   {
      writer.writeStartElement(namespaceURI, localName);
      return this;
   }

   public StaxWrapper writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
   {
      writer.writeStartElement(prefix, localName, namespaceURI);
      return this;
   }

   public StaxWrapper writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException
   {
      writer.writeEmptyElement(namespaceURI, localName);
      return this;
   }

   public StaxWrapper writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
   {
      writer.writeEmptyElement(prefix, localName, namespaceURI);
      return this;
   }

   public StaxWrapper writeEmptyElement(String localName) throws XMLStreamException
   {
      writer.writeEmptyElement(localName);
      return this;
   }

   public StaxWrapper writeEndElement() throws XMLStreamException
   {
      writer.writeEndElement();
      return this;
   }

   public StaxWrapper writeEndDocument() throws XMLStreamException
   {
      writer.writeEndDocument();
      return this;
   }

   public StaxWrapper close() throws XMLStreamException
   {
      writer.close();
      return this;
   }

   public StaxWrapper flush() throws XMLStreamException
   {
      writer.flush();
      return this;
   }

   public StaxWrapper writeAttribute(String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(localName, value);
      return this;
   }

   public StaxWrapper writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(prefix, namespaceURI, localName, value);
      return this;
   }

   public StaxWrapper writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
   {
      writer.writeAttribute(namespaceURI, localName, value);
      return this;
   }

   public StaxWrapper writeNamespace(String prefix, String namespaceURI) throws XMLStreamException
   {
      writer.writeNamespace(prefix, namespaceURI);
      return this;
   }

   public StaxWrapper writeDefaultNamespace(String namespaceURI) throws XMLStreamException
   {
      writer.writeDefaultNamespace(namespaceURI);
      return this;
   }

   public StaxWrapper writeComment(String data) throws XMLStreamException
   {
      writer.writeComment(data);
      return this;
   }

   public StaxWrapper writeProcessingInstruction(String target) throws XMLStreamException
   {
      writer.writeProcessingInstruction(target);
      return this;
   }

   public StaxWrapper writeProcessingInstruction(String target, String data) throws XMLStreamException
   {
      writer.writeProcessingInstruction(target, data);
      return this;
   }

   public StaxWrapper writeCData(String data) throws XMLStreamException
   {
      writer.writeCData(data);
      return this;
   }

   public StaxWrapper writeDTD(String dtd) throws XMLStreamException
   {
      writer.writeDTD(dtd);
      return this;
   }

   public StaxWrapper writeEntityRef(String name) throws XMLStreamException
   {
      writer.writeEntityRef(name);
      return this;
   }

   public StaxWrapper writeStartDocument() throws XMLStreamException
   {
      writer.writeStartDocument();
      return this;
   }

   public StaxWrapper writeStartDocument(String version) throws XMLStreamException
   {
      writer.writeStartDocument(version);
      return this;
   }

   public StaxWrapper writeStartDocument(String encoding, String version) throws XMLStreamException
   {
      writer.writeStartDocument(encoding, version);
      return this;
   }

   public StaxWrapper writeCharacters(String text) throws XMLStreamException
   {
      writer.writeCharacters(text);
      return this;
   }

   public StaxWrapper writeCharacters(char[] text, int start, int len) throws XMLStreamException
   {
      writer.writeCharacters(text, start, len);
      return this;
   }

   public String getPrefix(String uri) throws XMLStreamException
   {
      return writer.getPrefix(uri);
   }

   public StaxWrapper setPrefix(String prefix, String uri) throws XMLStreamException
   {
      writer.setPrefix(prefix, uri);
      return this;
   }

   public StaxWrapper setDefaultNamespace(String uri) throws XMLStreamException
   {
      writer.setDefaultNamespace(uri);
      return this;
   }

   public StaxWrapper setNamespaceContext(NamespaceContext context) throws XMLStreamException
   {
      writer.setNamespaceContext(context);
      return this;
   }

   public NamespaceContext getNamespaceContext()
   {
      return writer.getNamespaceContext();
   }

   public Object getProperty(String name) throws IllegalArgumentException
   {
      return writer.getProperty(name);
   }
}
