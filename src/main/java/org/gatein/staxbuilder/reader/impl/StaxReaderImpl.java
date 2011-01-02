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

package org.gatein.staxbuilder.reader.impl;

import org.gatein.staxbuilder.EnumElement;
import org.gatein.staxbuilder.reader.NavigationReadEvent;
import org.gatein.staxbuilder.reader.NestedReadBuilder;
import org.gatein.staxbuilder.reader.StaxReadEvent;
import org.gatein.staxbuilder.reader.StaxReadEventBuilder;
import org.gatein.staxbuilder.reader.StaxReadEventMatchBuilder;
import org.gatein.staxbuilder.reader.StaxReader;
import org.gatein.staxbuilder.reader.impl.navigation.PushbackXMLStreamReader;

import javax.naming.ldap.StartTlsRequest;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayDeque;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxReaderImpl implements StaxReader, StaxReadEvent, NavigationReadEvent,
   StaxReadEventBuilder, StaxReadEventMatchBuilder, NestedReadBuilder, XMLStreamConstants
{
   private XMLStreamReader delegate;
   private PushbackXMLStreamReader pushbackReader;

   private ArrayDeque<NestedRead> nestedReadEvents = new ArrayDeque<NestedRead>();
   private NestedRead nestedReadEvent;
   private int level;

   private boolean success;

   public StaxReaderImpl(XMLStreamReader delegate)
   {
      this.delegate = delegate;
   }

   //------------------------------------------  StaxReader Impl ------------------------------------------//

   @Override
   public StaxReadEvent read() throws XMLStreamException
   {
      getDelegate().next();
      updateLevel();
      return this;
   }

   @Override
   public StaxReadEvent readNextTag() throws XMLStreamException
   {
      getDelegate().nextTag();
      updateLevel();
      return this;
   }

   @Override
   public StaxReadEvent currentReadEvent() throws XMLStreamException
   {
      return this;
   }

   @Override
   public boolean hasNext() throws XMLStreamException
   {
      NestedRead nestedRead = nestedReadEvents.peek();
      if (nestedRead == null) return getDelegate().hasNext();

      return nestedRead.hasNext();
   }

   @Override
   public StaxReadEventBuilder buildReadEvent() throws XMLStreamException
   {
      return this;
   }

   //----------------------------------------  StaxReadEvent Impl ----------------------------------------//

   @Override
   public int getEventType()
   {
      return getDelegate().getEventType();
   }

   @Override
   public String elementText() throws XMLStreamException
   {
      String text = getDelegate().getElementText();
      updateLevel();
      return text;
   }

   @Override
   public String getLocalName()
   {
      return getDelegate().getLocalName();
   }

   @Override
   public String getText() throws XMLStreamException
   {
      if (pushbackReader != null)
      {
         int currentLevel = level;
         // read ahead
         while (hasNext() && getEventType() != CHARACTERS && currentLevel == level)
         {
            read();
         }

         if (!getDelegate().hasText()) return null;

         if (getEventType() != CHARACTERS)
         {
            throw new XMLStreamException("Could not read text.");
         }
      }
      return getDelegate().getText();
   }

   @Override
   public Location getLocation() throws XMLStreamException
   {
      return getDelegate().getLocation();
   }

   @Override
   public StaxReadEventMatchBuilder match()
   {
      return this;
   }

   @Override
   public StaxReader and()
   {
      return this;
   }

   //--------------------------------------  NavigationReadEvent Impl --------------------------------------//

   @Override
   public boolean success()
   {
      return success;
   }

   @Override
   public NavigationReadEvent child() throws XMLStreamException
   {
      return _child(null);
   }

   @Override
   public NavigationReadEvent child(String localName) throws XMLStreamException
   {
      if (localName == null) throw new IllegalArgumentException("localName cannot be null.");

      return _child(localName);
   }

   @Override
   public NavigationReadEvent sibling() throws XMLStreamException
   {
      return _sibling(null);
   }

   @Override
   public NavigationReadEvent sibling(String localName) throws XMLStreamException
   {
      if (localName == null) throw new IllegalArgumentException("localName cannot be null.");

      return _sibling(localName);
   }

   @Override
   public int getLevel() throws XMLStreamException
   {
      return level;
   }

   //------------------------------------  StaxReadEventBuilder Impl ------------------------------------//

   @Override
   public NestedReadBuilder withNestedRead()
   {
      nestedReadEvent = new NestedRead();

      return this;
   }

   @Override
   public NavigationReadEvent withNavigation()
   {
      pushbackReader = new PushbackXMLStreamReader(delegate);
      return this;
   }

   //------------------------------------  StaxReadEventMatchBuilder Impl ------------------------------------//

   @Override
   public <E extends Enum<E> & EnumElement<E>> E onElement(Class<E> enumType, E noMatch, E nonMatchingEvent)
   {
      if (getDelegate().getEventType() != START_ELEMENT) return enumType.cast(nonMatchingEvent);

      for (E e : enumType.getEnumConstants())
      {
         String localName = e.getLocalName();
         if (localName != null && localName.equals(getDelegate().getLocalName()))
         {
            return e;
         }
      }

      return enumType.cast(noMatch);
   }

   //---------------------------------------  NestedReadBuilder Impl ---------------------------------------//


   @Override
   public NestedReadBuilder untilElement(EnumElement element)
   {
      nestedReadEvent.untilElement = element;

      return this;
   }

   @Override
   public void end()
   {
      nestedReadEvent.untilEvent = END_ELEMENT;

      nestedReadEvents.push(nestedReadEvent);
   }


   //---------------------------------------  Private ---------------------------------------//

   private void updateLevel()
   {
      NestedRead nestedRead = nestedReadEvents.peek();
      int eventType = getEventType();

      if (eventType == END_ELEMENT)
      {
         level--;
         if (nestedRead != null)
         {
            nestedRead.level--;
         }
      }
      else if (eventType == START_ELEMENT)
      {
         level++;
         if (nestedRead != null)
         {
            nestedRead.level++;
         }
      }
   }

   private NavigationReadEvent _child(String name) throws XMLStreamException
   {
      success = false;
      pushbackReader.wantMark();
      int currentLevel = level;
      boolean first = true;
      while (hasNext())
      {
         int eventType = (first) ? getEventType() : read().getEventType();

         if (eventType == START_ELEMENT)
         {
            if (currentLevel + 1 == level)
               {
                  if (name == null || name.equals(getLocalName()))
                  {
                     success = true;
                     break;
                  }
               }
         }
         else if (eventType == END_ELEMENT)
         {
            if (level < currentLevel)
            {
               break;
            }
         }
         first = false;
      }

      if (success)
      {
         pushbackReader.flushPushback();
      }
      else
      {
         if (pushbackReader.isMarked())
         {
            pushbackReader.rollbackToMark();
            level = currentLevel;
         }
      }

      return this;
   }

   private NavigationReadEvent _sibling(String name) throws XMLStreamException
   {
      success = false;
      pushbackReader.wantMark();
      int currentLevel = level;
      int siblingLevel = level;
      
      if (getEventType() != END_ELEMENT)
      {
         while (hasNext() && level >= currentLevel)
         {
            int event = read().getEventType();
            if (event == END_ELEMENT)
            {
               if (level == currentLevel - 1) break;
            }
         }
      }
      else
      {
         siblingLevel++;
      }

      boolean first = true;
      while (hasNext())
      {
         int eventType = (first) ? getEventType() : read().getEventType();
         if (eventType == START_ELEMENT && level == siblingLevel)
         {
            if (name == null || name.equals(getLocalName()))
            {
               success = true;
               break;
            }
         }
         else if (eventType == END_ELEMENT && level < siblingLevel - 1)
         {
            break;
         }
         first = false;
      }

      if (success)
      {
         pushbackReader.flushPushback();
      }
      else
      {
         if (pushbackReader.isMarked())
         {
            pushbackReader.rollbackToMark();
            level = currentLevel;
         }
      }
      return this;
   }

   private XMLStreamReader getDelegate()
   {
      if (pushbackReader != null) return pushbackReader;

      return delegate;
   }

   private class NestedRead
   {
      private EnumElement untilElement;
      private int untilEvent = -1;
      private int level = 1;

      public boolean hasNext() throws XMLStreamException
      {
         if (untilElement == null)
            throw new XMLStreamException("Nested read not properly built. Not sure what element to stop nested read on.  Try calling untilElement on NestedReadBuilder.");
         if (untilEvent == -1)
            throw new XMLStreamException("Nested read not properly built. Not sure what event to stop nested read. Try calling end() on NestedReadBuilder.");


         if (getDelegate().hasNext())
         {
            if (level == 0)
            {
               nestedReadEvents.pop();
               return false;
            }
            return true;
         }
         else
         {
            return false;
         }
      }
   }
}
