package org.gatein.staxbuilder.reader;

import org.gatein.staxbuilder.EnumElement;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface NestedReadBuilder
{
   NestedReadBuilder untilElement(EnumElement element);

   void end();
}
