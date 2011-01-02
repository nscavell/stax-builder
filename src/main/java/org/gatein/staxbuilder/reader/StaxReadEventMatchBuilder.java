package org.gatein.staxbuilder.reader;

import org.gatein.staxbuilder.EnumElement;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface StaxReadEventMatchBuilder
{
   /*<E extends Enum<E> & EnumElement<E>> StaxReadEventMatcherBuilder onElement(E element);

   StaxReadEventMatcherBuilder onEvent(int eventType);

   <E extends Enum<E> & EnumElement<E>> StaxReadEventMatcherBuilder withNoMatch(E noMatch);

   <E extends Enum<E> & EnumElement<E>> StaxReadEventMatcherBuilder withNonMatchingEvent(E nonMatchingEvent);

   MatchingReadEvent build();*/

   <E extends Enum<E> & EnumElement<E>> E onElement(Class<E> enumType, E noMatch, E nonMatchingEvent);
}
