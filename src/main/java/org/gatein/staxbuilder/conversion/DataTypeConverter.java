package org.gatein.staxbuilder.conversion;

import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface DataTypeConverter<T>
{
   String print(T object) throws XMLStreamException;

   T parse(String text) throws XMLStreamException;
}
