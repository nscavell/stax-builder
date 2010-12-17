package org.gatein.staxbuilder.conversion;

import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface DataTypeConverter<T>
{
   public String print(T object) throws XMLStreamException;

   public T parse(String text) throws XMLStreamException;
}
