package org.gatein.staxbuilder.conversion.impl;

import org.gatein.staxbuilder.conversion.DataTypeConverter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLStreamException;
import java.util.Calendar;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class XmlDateTimeConverter implements DataTypeConverter<Calendar>
{
   @Override
   public String print(Calendar date) throws XMLStreamException
   {
      return DatatypeConverter.printDateTime(date);
   }

   @Override
   public Calendar parse(String text) throws XMLStreamException
   {
      return DatatypeConverter.parseDateTime(text);
   }
}
