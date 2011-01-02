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

import org.gatein.staxbuilder.reader.impl.StaxReaderImpl;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxReaderBuilder
{
   private XMLStreamReader writer;
   private Object input;

   public StaxReaderBuilder()
   {
   }

   public StaxReaderBuilder withInputStream(InputStream is) throws XMLStreamException
   {
      if (is == null) throw new IllegalArgumentException("InputStream cannot be null.");
      input = is;
      return this;
   }

   public StaxReaderBuilder withReader(Reader reader) throws XMLStreamException
   {
      if (reader == null) throw new IllegalArgumentException("Reader cannot be null.");
      input = reader;
      return this;
   }

   public StaxReaderBuilder withXMLStreamReader(XMLStreamReader writer) throws XMLStreamException
   {
      if (writer == null) throw new IllegalArgumentException("XMLStreamReader cannot be null.");
      this.writer = writer;
      return this;
   }

   public StaxReader build() throws XMLStreamException
   {
      if (writer == null && input == null) throw new IllegalStateException("Cannot build stax reader, try calling withInputStream, withReader, or withXMLStreamReader.");

      if (writer == null)
      {
         if (input instanceof InputStream)
         {
            writer = XMLInputFactory.newInstance().createXMLStreamReader((InputStream) input);
         }
         else if (input instanceof Reader)
         {
            writer = XMLInputFactory.newInstance().createXMLStreamReader((Reader) input);
         }
         else
         {
            throw new IllegalStateException("Unkown InputStream/Writer " + input); // should never happen...
         }
      }
      return new StaxReaderImpl(writer);
   }
}
