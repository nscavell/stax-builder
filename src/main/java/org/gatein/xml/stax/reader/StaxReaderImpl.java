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

package org.gatein.xml.stax.reader;

import org.gatein.xml.stax.EnumElement;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayDeque;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxReaderImpl implements StaxReader, StaxReadEvent, StaxReadEventBuilder, XMLStreamConstants
{
   private XMLStreamReader delegate;
   private ArrayDeque<NestedReadEvent> nestedReadEvents = new ArrayDeque<NestedReadEvent>();
   private NestedReadEvent nestedReadEvent;

   public StaxReaderImpl(XMLStreamReader delegate)
   {
      this.delegate = delegate;
   }

   @Override
   public StaxReadEventBuilder buildReadEvent() throws XMLStreamException
   {
      StaxReadEvent parent = nestedReadEvents.peek();
      if (parent == null) parent = this;

      nestedReadEvent = new NestedReadEvent(parent);

      return this;
   }

   @Override
   public StaxReadEventBuilder until(EnumElement element)
   {
      nestedReadEvent.untilElement = element;
      return this;
   }

   @Override
   public StaxReadEventBuilder end()
   {
      nestedReadEvent.untilEvent = END_ELEMENT;
      return this;
   }

   @Override
   public StaxReadEventBuilder start()
   {
      nestedReadEvent.untilEvent = START_ELEMENT;
      return this;
   }

   @Override
   public StaxReadEvent build()
   {
      nestedReadEvents.push(nestedReadEvent);
      return nestedReadEvent;
   }

   @Override
   public StaxReadEvent read() throws XMLStreamException
   {
      delegate.next();
      return this;
   }

   @Override
   public StaxReadEvent currentReadEvent() throws XMLStreamException
   {
      if (nestedReadEvents.isEmpty()) return this;

      return nestedReadEvents.peek();
   }

   @Override
   public StaxReadEvent readNextTag() throws XMLStreamException
   {
      delegate.nextTag();
      return this;
   }

   @Override
   public boolean hasNext() throws XMLStreamException
   {
      if (nestedReadEvents.isEmpty()) return delegate.hasNext();

      return nestedReadEvents.peek().hasNext();
   }

   @Override
   public <E extends Enum<E> & EnumElement> E match(Class<E> enumType, E nomatch) throws XMLStreamException
   {
      if (delegate.getEventType() != START_ELEMENT) return nomatch;

      for (E e : enumType.getEnumConstants())
      {
         @SuppressWarnings("unchecked")
         E found = (E) e.forName(delegate.getLocalName());
         if (found != null)
         {
            return found;
         }
      }
      return nomatch;
   }

   @Override
   public StaxReadEvent next() throws XMLStreamException
   {
      return read();
   }

   @Override
   public StaxReader and()
   {
      return this;
   }

   @Override
   public int getEventType()
   {
      return delegate.getEventType();
   }

   @Override
   public String elementText() throws XMLStreamException
   {
      delegate.require(START_ELEMENT, null, null);

      return delegate.getElementText();
   }

   @Override
   public String getLocalName()
   {
      return delegate.getLocalName();
   }

   @Override
   public String getText() throws XMLStreamException
   {
      return delegate.getText();
   }

   private class NestedReadEvent implements StaxReadEvent
   {
      private StaxReadEvent parent;
      private EnumElement untilElement;
      private int untilEvent;
      boolean hasNext = true;

      public NestedReadEvent(StaxReadEvent parent)
      {
         this.parent = parent;
      }

      @Override
      public StaxReadEvent next() throws XMLStreamException
      {
         return parent.next();
      }

      @Override
      public <E extends Enum<E> & EnumElement> E match(Class<E> enumType, E nomatch) throws XMLStreamException
      {
         return parent.match(enumType, nomatch);
      }

      @Override
      public StaxReader and() throws XMLStreamException
      {
         return parent.and();
      }

      @Override
      public int getEventType() throws XMLStreamException
      {
         return parent.getEventType();
      }

      @Override
      public String elementText() throws XMLStreamException
      {
         return parent.elementText();
      }

      @Override
      public String getLocalName() throws XMLStreamException
      {
         return parent.getLocalName();
      }

      @Override
      public String getText() throws XMLStreamException
      {
         return parent.getText();
      }

      @Override
      public boolean hasNext() throws XMLStreamException
      {
         if (delegate.hasNext() && !(getEventType() == untilEvent && getLocalName().equals(untilElement.getLocalName())))
         {
            return true;
         }
         else
         {
            nestedReadEvents.pop();
            return false;
         }
      }
   }
}
