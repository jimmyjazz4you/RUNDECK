package com.amazonaws.util;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

class NamespaceRemovingInputStream extends SdkFilterInputStream {
   private byte[] lookAheadData = new byte[200];
   private boolean hasRemovedNamespace = false;

   public NamespaceRemovingInputStream(InputStream in) {
      super(new BufferedInputStream(in));
   }

   @Override
   public int read() throws IOException {
      this.abortIfNeeded();
      int b = this.in.read();
      if (b == 120 && !this.hasRemovedNamespace) {
         this.lookAheadData[0] = (byte)b;
         this.in.mark(this.lookAheadData.length);
         int bytesRead = this.in.read(this.lookAheadData, 1, this.lookAheadData.length - 1);
         this.in.reset();
         String string = new String(this.lookAheadData, 0, bytesRead + 1, StringUtils.UTF8);
         int numberCharsMatched = this.matchXmlNamespaceAttribute(string);
         if (numberCharsMatched > 0) {
            for(int i = 0; i < numberCharsMatched - 1; ++i) {
               this.in.read();
            }

            b = this.in.read();
            this.hasRemovedNamespace = true;
         }
      }

      return b;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         int j = this.read();
         if (j == -1) {
            if (i == 0) {
               return -1;
            }

            return i;
         }

         b[i + off] = (byte)j;
      }

      return len;
   }

   @Override
   public int read(byte[] b) throws IOException {
      return this.read(b, 0, b.length);
   }

   private int matchXmlNamespaceAttribute(String s) {
      NamespaceRemovingInputStream.StringPrefixSlicer stringSlicer = new NamespaceRemovingInputStream.StringPrefixSlicer(s);
      if (!stringSlicer.removePrefix("xmlns")) {
         return -1;
      } else {
         stringSlicer.removeRepeatingPrefix(" ");
         if (!stringSlicer.removePrefix("=")) {
            return -1;
         } else {
            stringSlicer.removeRepeatingPrefix(" ");
            if (!stringSlicer.removePrefix("\"")) {
               return -1;
            } else {
               return !stringSlicer.removePrefixEndingWith("\"") ? -1 : s.length() - stringSlicer.getString().length();
            }
         }
      }
   }

   private static final class StringPrefixSlicer {
      private String s;

      public StringPrefixSlicer(String s) {
         this.s = s;
      }

      public String getString() {
         return this.s;
      }

      public boolean removePrefix(String prefix) {
         if (!this.s.startsWith(prefix)) {
            return false;
         } else {
            this.s = this.s.substring(prefix.length());
            return true;
         }
      }

      public boolean removeRepeatingPrefix(String prefix) {
         if (!this.s.startsWith(prefix)) {
            return false;
         } else {
            while(this.s.startsWith(prefix)) {
               this.s = this.s.substring(prefix.length());
            }

            return true;
         }
      }

      public boolean removePrefixEndingWith(String marker) {
         int i = this.s.indexOf(marker);
         if (i < 0) {
            return false;
         } else {
            this.s = this.s.substring(i + marker.length());
            return true;
         }
      }
   }
}
