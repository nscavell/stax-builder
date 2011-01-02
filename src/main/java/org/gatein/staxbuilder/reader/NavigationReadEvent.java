package org.gatein.staxbuilder.reader;

import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface NavigationReadEvent extends StaxReadEvent
{
   boolean success();

   NavigationReadEvent child() throws XMLStreamException;

   NavigationReadEvent child(String localName) throws XMLStreamException;

   NavigationReadEvent sibling() throws XMLStreamException;

   NavigationReadEvent sibling(String name) throws XMLStreamException;

   int getLevel() throws XMLStreamException;

   //TODO: Attributes
}
