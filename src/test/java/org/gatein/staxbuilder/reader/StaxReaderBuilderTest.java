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

import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxReaderBuilderTest
{
   public void createBuilder_Nothing() throws XMLStreamException
   {
      try
      {
         new StaxReaderBuilder().build();
         Assert.fail("Expected IllegalStateException with empty builder.");
      }
      catch (IllegalStateException ise)
      {
         // pass
      }
   }

   @Test
   public void createBuilder_NullArguments() throws XMLStreamException
   {
      try
      {
         new StaxReaderBuilder().withReader(null).build();
         Assert.fail("Cannot pass null Reader, IllegalArgumentException must be thrown.");
      }
      catch (IllegalArgumentException iae)
      {
         // pass
      }

      try
      {
         new StaxReaderBuilder().withInputStream(null).build();
         Assert.fail("Cannot pass null InputStream, IllegalArgumentException must be thrown.");
      }
      catch (IllegalArgumentException iae)
      {
         // pass
      }

      try
      {
         new StaxReaderBuilder().withXMLStreamReader(null).build();
         Assert.fail("Cannot pass null XMLStreamReader, IllegalArgumentException must be thrown.");
      }
      catch (IllegalArgumentException iae)
      {
         // pass
      }
   }


}
