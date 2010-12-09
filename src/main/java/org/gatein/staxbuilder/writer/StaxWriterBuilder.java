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

import org.gatein.staxbuilder.writer.impl.FormattingStaxWriter;
import org.gatein.staxbuilder.writer.impl.StaxWriterImpl;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class StaxWriterBuilder
{
   //TODO: Add formatting option to builder, ie newline/tabbing

   private XMLStreamWriter writer;
   private Object output;
   private String version;
   private String encoding;

   private FormatterBuilder formatterBuilder;
   private FormatterBuilder defaultFormatterBuilder;

   /*
   private int indent;
   private boolean useTabCharacter;
   private int maxColumns;
   */
   public StaxWriterBuilder()
   {
   }

   public StaxWriterBuilder withOutputStream(OutputStream out)
   {
      if (out == null) throw new IllegalArgumentException("OutputStream cannot be null.");
      output = out;
      return this;
   }

   public StaxWriterBuilder withWriter(Writer writer)
   {
	  if (writer == null) throw new IllegalArgumentException("Writer cannot be null.");
      this.output = writer;
      return this;
   }

   public StaxWriterBuilder withXMLStreamWriter(XMLStreamWriter writer)
   {
      if (writer == null) throw new IllegalArgumentException("XMLStreamWriter cannot be null.");
      this.writer = writer;
      return this;
   }

   public StaxWriterBuilder withDefaults()
   {
      this.encoding = null;
      this.version = null;
      return this;
   }

   public StaxWriterBuilder withEncoding(String encoding)
   {
      this.encoding = encoding;
      return this;
   }

   public StaxWriterBuilder withVersion(String version)
   {
      this.version = version;
      return this;
   }

   public FormatterBuilder withFormatting()
   {
      // Keep current formatter if it's not the default one. This allows the unpredictability the developer will call this multiple times
      if (formatterBuilder == null || formatterBuilder == defaultFormatterBuilder)
      {
         this.formatterBuilder = new FormatterBuilder(this);
      }

      return formatterBuilder;
   }

   public StaxWriterBuilder withDefaultFormatting()
   {
      if (defaultFormatterBuilder == null)
      {
         defaultFormatterBuilder = new FormatterBuilder(this).indentSize(3).sameLineChracterLimit(80);
      }

      this.formatterBuilder = defaultFormatterBuilder;
      return this;
   }

   public StaxWriterBuilder withNoFormatting()
   {
      this.formatterBuilder = null;
      return this;
   }

   public StaxWriter build() throws XMLStreamException, IllegalStateException
   {
      if (writer == null && output == null) throw new IllegalStateException("Cannot build stax writer. Try calling withOutputStream or withWriter.");

      if (writer == null)
      {
         if (output instanceof OutputStream)
         {
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter((OutputStream) output);
         }
         else if (output instanceof Writer)
         {
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter((Writer) output);
         }
         else
         {
            throw new IllegalStateException("Unknown OutputStream/Writer " + output); // should never happen...
         }
      }

      StaxWriterImpl staxWriterImpl = new StaxWriterImpl(writer, encoding, version);

      if (formatterBuilder != null)
      {
         return new FormattingStaxWriter(staxWriterImpl, formatterBuilder.build());
      }
      else
      {
         return staxWriterImpl;
      }
   }
}
