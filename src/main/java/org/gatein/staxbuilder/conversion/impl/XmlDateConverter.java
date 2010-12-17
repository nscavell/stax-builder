package org.gatein.staxbuilder.conversion.impl;

import org.gatein.staxbuilder.conversion.DataTypeConverter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class XmlDateConverter implements DataTypeConverter<Calendar>
{
   private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

   @Override
   public String print(Calendar date) throws XMLStreamException
   {
      return sdf.format(date.getTime());
   }

   @Override
   public Calendar parse(String text) throws XMLStreamException
   {
      Calendar cal = Calendar.getInstance();
      Date date = null;
      try
      {
         date = sdf.parse(text);
         cal.setTime(date);
         return cal;
      }
      catch (ParseException e)
      {
         throw new XMLStreamException("Cannot parse date from text " + text);
      }
   }
}
