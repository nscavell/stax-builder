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

import org.gatein.staxbuilder.EnumAttribute;
import org.gatein.staxbuilder.EnumElement;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Calendar;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface StaxWriter
{
   StaxWriter writeStartDocument() throws XMLStreamException;

   StaxWriter writeEndDocument() throws XMLStreamException;

   StaxWriter writeStartElement(String localName) throws XMLStreamException;

   StaxWriter writeStartElement(String prefix, String namespaceURI, String localName) throws XMLStreamException;

   StaxWriter writeStartElement(String namespaceURI, String localName) throws XMLStreamException;

   StaxWriter writeStartElement(EnumElement element) throws XMLStreamException;

   StaxWriter writeStartElement(String prefix, String namespaceURI, EnumElement element) throws XMLStreamException;

   StaxWriter writeStartElement(String namespaceURI, EnumElement element) throws XMLStreamException;

   StaxWriter writeDefaultNamespace(String namespaceURI) throws XMLStreamException;

   StaxWriter writeNamespace(String prefix, String namespaceURI) throws XMLStreamException;

   StaxWriter writeEndElement() throws XMLStreamException;

   StaxWriter writeAttribute(String localName, String value) throws XMLStreamException;

   StaxWriter writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException;

   StaxWriter writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException;

   StaxWriter writeAttribute(EnumAttribute attribute, String value) throws XMLStreamException;

   StaxWriter writeAttribute(String prefix, String namespaceURI, EnumAttribute attribute, String value) throws XMLStreamException;

   StaxWriter writeAttribute(String namespaceURI, EnumAttribute attribute, String value) throws XMLStreamException;

   StaxWriter writeCharacters(String text) throws XMLStreamException;

   StaxWriter writeElement(String localName, String text) throws XMLStreamException;

   StaxWriter writeElement(EnumElement element, String text) throws XMLStreamException;

   StaxWriter writeOptionalElement(String localName, String text) throws XMLStreamException;

   StaxWriter writeOptionalElement(EnumElement element, String text) throws XMLStreamException;

   StaxWriter writeDate(Calendar calendar) throws XMLStreamException;

   StaxWriter writeDateTime(Calendar calendar) throws XMLStreamException;

   StaxWriter writeObject(QName qname, Object object) throws XMLStreamException;

   StaxWriter flush() throws XMLStreamException;

   void close() throws XMLStreamException;
}
