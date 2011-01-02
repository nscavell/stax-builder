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

import org.gatein.staxbuilder.EnumElement;

import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface StaxReader
{
   /**
    * Performs one read operation.
    * @return a {@link StaxReadEvent} for further processing
    * @throws javax.xml.stream.XMLStreamException if an exception occurs
    *
    * @see javax.xml.stream.XMLStreamReader#next()
    */
   StaxReadEvent read() throws XMLStreamException;

   /**
    * Will read the next START_ELEMENT or END_ELEMENT skipping whitespace.
    * @return a {@link StaxReadEvent} for further processing
    * @throws javax.xml.stream.XMLStreamException if an exception occurs
    *
    * @see javax.xml.stream.XMLStreamReader#nextTag()
    */
   StaxReadEvent readNextTag() throws XMLStreamException;

   /**
    * Returns the current read event of the reader.
    * @return current {@link StaxReadEvent}
    * @throws javax.xml.stream.XMLStreamException if an exception occurs
    */
   StaxReadEvent currentReadEvent() throws XMLStreamException;

   /**
    * Indicates whether more read events can be processed.
    * @return true if more read events are available
    * @throws javax.xml.stream.XMLStreamException if an exception occurs
    *
    * @see javax.xml.stream.XMLStreamReader#hasNext()
    */
   boolean hasNext() throws XMLStreamException;

   StaxReadEventBuilder buildReadEvent() throws XMLStreamException;
}
