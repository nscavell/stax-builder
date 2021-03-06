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

package org.gatein.staxbuilder.conversion;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public abstract class AbstractConverterProvider
{
   private Map<QName, DataTypeConverter> converters;

   public AbstractConverterProvider(Map<QName, DataTypeConverter> converters)
   {
      this.converters = converters;
   }

   protected DataTypeConverter getDataTypeConverter(QName namespace) throws XMLStreamException
   {
      DataTypeConverter dtc = converters.get(namespace);
      if (dtc == null) throw new XMLStreamException("No data type converter found for namespace " + namespace);

      return dtc;
   }

   @SuppressWarnings("unchecked")
   protected <T> DataTypeConverter<T> getDataTypeConverter(QName namespace, Class<T> type) throws XMLStreamException
   {
      DataTypeConverter dtc = getDataTypeConverter(namespace);

      try
      {
         return (DataTypeConverter<T>) dtc;
      }
      catch (ClassCastException cce)
      {
         throw new XMLStreamException("Was expecting converter of DataTypeConverter<" + type + "> for namespace " + namespace);
      }
   }
}
