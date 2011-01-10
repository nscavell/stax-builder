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

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import java.util.Date;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface StaxReadEvent
{
   int getEventType() throws XMLStreamException;

   String elementText() throws XMLStreamException;

   String getLocalName() throws XMLStreamException;

   String getText() throws XMLStreamException;

   String getAttributeValue(int index);

   String getAttributeLocalName(int index);

   int getAttributeCount();

   boolean hasNext() throws XMLStreamException;

   Location getLocation() throws XMLStreamException;

   StaxReadEventMatchBuilder match();

   StaxReader and() throws XMLStreamException;

   <T> T convertText(QName qname, Class<T> clazz) throws XMLStreamException;

   <T> T convertElementText(QName qname, Class<T> clazz) throws XMLStreamException;
}
