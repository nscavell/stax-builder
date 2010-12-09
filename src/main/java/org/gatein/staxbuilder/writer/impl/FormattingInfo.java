package org.gatein.staxbuilder.writer.impl;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public final class FormattingInfo
{
   private final char indentCharacter;
   private final int indentSize;
   private final int sameLineCharacterLimit;
   private final String newline;

   public FormattingInfo(char indentCharacter, int indentSize, int sameLineCharacterLimit, String newline)
   {
      this.indentCharacter = indentCharacter;

      if (indentSize < 0) throw new IllegalArgumentException("indentSize cannot be less then zero.");
      this.indentSize = indentSize;

      this.sameLineCharacterLimit = sameLineCharacterLimit;
      this.newline = newline;
   }

   public char getIndentCharacter()
   {
      return indentCharacter;
   }

   public int getIndentSize()
   {
      return indentSize;
   }

   public int getSameLineCharacterLimit()
   {
      return sameLineCharacterLimit;
   }

   public String getNewline()
   {
      return newline;
   }
}
