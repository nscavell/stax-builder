package org.gatein.staxbuilder.writer;

import org.gatein.staxbuilder.writer.impl.FormattingInfo;

/**
 * This class is to be used by the {@link org.gatein.staxbuilder.writer.StaxWriterBuilder} only.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class FormatterBuilder
{
   private StaxWriterBuilder staxWriterBuilder;
   private int indentSize = -1;
   private boolean useTab;
   private int sameLineCharacterLimit = -1;
   private String newline = null;

   FormatterBuilder(StaxWriterBuilder staxWriterBuilder)
   {
      this.staxWriterBuilder = staxWriterBuilder;
   }

   public FormatterBuilder indentSize(int indentSize)
   {
      this.indentSize = indentSize;
      return this;
   }

   public FormatterBuilder useTabCharacter(boolean useTab)
   {
      this.useTab = useTab;
      return this;
   }

   public FormatterBuilder sameLineChracterLimit(int sameLineCharacterLimit)
   {
      this.sameLineCharacterLimit = sameLineCharacterLimit;
      return this;
   }

   public FormatterBuilder newLine(String newline)
   {
      this.newline = newline;
      return this;
   }

   /**
    * Gives us a way to get back to our {@link StaxWriterBuilder}
    * @return the StaxWriterBuilder used which initiated the {@link FormatterBuilder}
    */
   public StaxWriterBuilder and()
   {
      return staxWriterBuilder;
   }

   FormattingInfo build()
   {
      if (indentSize == -1) throw new IllegalStateException("Indent size was not specified.");
      char indent;

      // Use spaces unless useTabCharacter was called
      if (useTab)
      {
         indent = '\t';
      }
      else
      {
         indent = ' ';
      }

      // Get line separator
      if (newline == null)
      {
         try
         {
            newline = System.getProperty("line.separator");
         }
         catch (Throwable ignored)
         {
         }
      }
      if (newline == null) newline = "\n";

      return new FormattingInfo(indent, indentSize, sameLineCharacterLimit, newline);
   }
}
