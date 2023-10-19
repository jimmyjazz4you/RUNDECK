package com.amazonaws.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.Date;
import java.util.Locale;

public class StringUtils {
   private static final String DEFAULT_ENCODING = "UTF-8";
   public static final String COMMA_SEPARATOR = ",";
   public static final Charset UTF8 = Charset.forName("UTF-8");
   private static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
   private static final char CHAR_SPACE = ' ';
   private static final char CHAR_TAB = '\t';
   private static final char CHAR_NEW_LINE = '\n';
   private static final char CHAR_VERTICAL_TAB = '\u000b';
   private static final char CHAR_CARRIAGE_RETURN = '\r';
   private static final char CHAR_FORM_FEED = '\f';

   public static Integer toInteger(StringBuilder value) {
      return Integer.parseInt(value.toString());
   }

   public static String toString(StringBuilder value) {
      return value.toString();
   }

   public static Boolean toBoolean(StringBuilder value) {
      return Boolean.valueOf(value.toString());
   }

   public static String fromInteger(Integer value) {
      return Integer.toString(value);
   }

   public static String fromLong(Long value) {
      return Long.toString(value);
   }

   public static String fromShort(Short value) {
      return Short.toString(value);
   }

   public static String fromString(String value) {
      return value;
   }

   public static String fromBoolean(Boolean value) {
      return Boolean.toString(value);
   }

   public static String fromBigInteger(BigInteger value) {
      return value.toString();
   }

   public static String fromBigDecimal(BigDecimal value) {
      return value.toString();
   }

   public static BigInteger toBigInteger(String s) {
      return new BigInteger(s);
   }

   public static BigDecimal toBigDecimal(String s) {
      return new BigDecimal(s);
   }

   public static String fromFloat(Float value) {
      return Float.toString(value);
   }

   public static String fromDate(Date value) {
      return DateUtils.formatISO8601Date(value);
   }

   public static String fromDate(Date date, String timestampFormat) {
      if ("unixTimestamp".equalsIgnoreCase(timestampFormat)) {
         return DateUtils.formatServiceSpecificDate(date);
      } else if ("iso8601".equalsIgnoreCase(timestampFormat)) {
         return DateUtils.formatISO8601Date(date);
      } else if ("rfc822".equalsIgnoreCase(timestampFormat)) {
         return DateUtils.formatRFC822Date(date);
      } else if ("unixTimestampInMillis".equalsIgnoreCase(timestampFormat)) {
         return DateUtils.formatUnixTimestampInMills(date);
      } else {
         throw new IllegalArgumentException("unsupported timestamp format");
      }
   }

   public static String fromDouble(Double d) {
      return Double.toString(d);
   }

   public static String fromByte(Byte b) {
      return Byte.toString(b);
   }

   public static String fromByteBuffer(ByteBuffer byteBuffer) {
      return Base64.encodeAsString(BinaryUtils.copyBytesFrom(byteBuffer));
   }

   public static String replace(String originalString, String partToMatch, String replacement) {
      StringBuilder buffer = new StringBuilder(originalString.length());
      buffer.append(originalString);

      for(int indexOf = buffer.indexOf(partToMatch); indexOf != -1; indexOf = buffer.indexOf(partToMatch, indexOf + replacement.length())) {
         buffer = buffer.replace(indexOf, indexOf + partToMatch.length(), replacement);
      }

      return buffer.toString();
   }

   public static String join(String joiner, String... parts) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < parts.length; ++i) {
         builder.append(parts[i]);
         if (i < parts.length - 1) {
            builder.append(joiner);
         }
      }

      return builder.toString();
   }

   public static String trim(String value) {
      return value == null ? null : value.trim();
   }

   public static boolean isNullOrEmpty(String value) {
      return value == null || value.isEmpty();
   }

   public static boolean hasValue(String str) {
      return !isNullOrEmpty(str);
   }

   public static String lowerCase(String str) {
      return isNullOrEmpty(str) ? str : str.toLowerCase(LOCALE_ENGLISH);
   }

   public static String upperCase(String str) {
      return isNullOrEmpty(str) ? str : str.toUpperCase(LOCALE_ENGLISH);
   }

   public static int compare(String str1, String str2) {
      if (str1 != null && str2 != null) {
         Collator collator = Collator.getInstance(LOCALE_ENGLISH);
         return collator.compare(str1, str2);
      } else {
         throw new IllegalArgumentException("Arguments cannot be null");
      }
   }

   private static boolean isWhiteSpace(char ch) {
      if (ch == ' ') {
         return true;
      } else if (ch == '\t') {
         return true;
      } else if (ch == '\n') {
         return true;
      } else if (ch == 11) {
         return true;
      } else if (ch == '\r') {
         return true;
      } else {
         return ch == '\f';
      }
   }

   public static void appendCompactedString(StringBuilder destination, String source) {
      boolean previousIsWhiteSpace = false;
      int length = source.length();

      for(int i = 0; i < length; ++i) {
         char ch = source.charAt(i);
         if (isWhiteSpace(ch)) {
            if (!previousIsWhiteSpace) {
               destination.append(' ');
               previousIsWhiteSpace = true;
            }
         } else {
            destination.append(ch);
            previousIsWhiteSpace = false;
         }
      }
   }

   public static boolean beginsWithIgnoreCase(String data, String seq) {
      return data.regionMatches(true, 0, seq, 0, seq.length());
   }

   public static Character findFirstOccurrence(String s, char... charsToMatch) {
      int lowestIndex = Integer.MAX_VALUE;

      for(char toMatch : charsToMatch) {
         int currentIndex = s.indexOf(toMatch);
         if (currentIndex != -1 && currentIndex < lowestIndex) {
            lowestIndex = currentIndex;
         }
      }

      return lowestIndex == Integer.MAX_VALUE ? null : s.charAt(lowestIndex);
   }
}
