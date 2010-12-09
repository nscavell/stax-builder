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

import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxWriterTest
{
   @Test
   public void createSimpleWriter_withDefaults() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String s1 = sw.toString();
      String version = getVersionFromXMLDocument(s1);
      String encoding = getEncodingFromXMLDocument(s1);

      Assert.assertEquals("1.0", version);
      Assert.assertNull(encoding);

      StringWriter sw2 = new StringWriter();
      StaxWriter writer2 = new StaxWriterBuilder().withWriter(sw2).withDefaults().build();
      Assert.assertNotNull(writer2);
      writer2.writeStartDocument().writeEndDocument();

      String s2 = sw2.toString();
      String version2 = getVersionFromXMLDocument(s2);
      String encoding2 = getEncodingFromXMLDocument(s2);

      Assert.assertEquals("1.0", version2);
      Assert.assertNull(encoding2);
   }

   @Test
   public void createSimpleWriter_withEncoding() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("UTF-8").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertEquals("UTF-8", encoding);
   }

   @Test
   public void createSimpleWriter_withVersion() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withVersion("1.0").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertNull(encoding);
   }

   @Test
   public void createSimpleWriter_withEncodingAndVersion() throws XMLStreamException
   {
      StringWriter sw = new StringWriter();
      StaxWriter writer = new StaxWriterBuilder().withWriter(sw).withEncoding("ISO-8859-1").withVersion("1.0").build();
      Assert.assertNotNull(writer);
      writer.writeStartDocument().writeEndDocument();

      String xml = sw.toString();
      String version = getVersionFromXMLDocument(xml);
      String encoding = getEncodingFromXMLDocument(xml);

      Assert.assertEquals("1.0", version);
      Assert.assertEquals("ISO-8859-1", encoding);
   }

   //TODO: More tests...

   private String getVersionFromXMLDocument(String xml) throws XMLStreamException
   {
      XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
      return reader.getVersion();
   }

   private String getEncodingFromXMLDocument(String xml) throws XMLStreamException
   {
      XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
      return reader.getCharacterEncodingScheme();
   }
}
