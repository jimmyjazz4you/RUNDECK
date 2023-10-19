package com.amazonaws.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.ThreadSafe;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.tz.FixedDateTimeZone;

@ThreadSafe
public class DateUtils {
   private static final DateTimeZone GMT = new FixedDateTimeZone("GMT", "GMT", 0, 0);
   private static final long MILLI_SECONDS_OF_365_DAYS = 31536000000L;
   private static final int AWS_DATE_MILLI_SECOND_PRECISION = 3;
   protected static final DateTimeFormatter iso8601DateFormat = ISODateTimeFormat.dateTime().withZone(GMT);
   protected static final DateTimeFormatter alternateIso8601DateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(GMT);
   protected static final DateTimeFormatter ISO8601_DATE_FORMAT_WITH_OFFSET = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
   private static final List<DateTimeFormatter> ALTERNATE_ISO8601_FORMATTERS = Arrays.asList(alternateIso8601DateFormat, ISO8601_DATE_FORMAT_WITH_OFFSET);
   protected static final DateTimeFormatter rfc822DateFormat = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.US).withZone(GMT);
   protected static final DateTimeFormatter compressedIso8601DateFormat = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss'Z'").withZone(GMT);

   public static Date parseISO8601Date(String dateString) {
      try {
         return doParseISO8601Date(dateString);
      } catch (RuntimeException var2) {
         throw handleException(var2);
      }
   }

   static Date doParseISO8601Date(String dateStringOrig) {
      String dateString = dateStringOrig;
      if (dateStringOrig.endsWith("+0000")) {
         dateString = dateStringOrig.substring(0, dateStringOrig.length() - 5).concat("Z");
      }

      String temp = tempDateStringForJodaTime(dateString);

      try {
         if (temp.equals(dateString)) {
            return new Date(iso8601DateFormat.parseMillis(dateString));
         } else {
            long milliLess365Days = iso8601DateFormat.parseMillis(temp);
            long milli = milliLess365Days + 31536000000L;
            return milli < 0L ? new Date(iso8601DateFormat.parseMillis(dateString)) : new Date(milli);
         }
      } catch (IllegalArgumentException var8) {
         for(DateTimeFormatter dateTimeFormatter : ALTERNATE_ISO8601_FORMATTERS) {
            try {
               return new Date(dateTimeFormatter.parseMillis(dateString));
            } catch (Exception var7) {
            }
         }

         throw var8;
      }
   }

   private static String tempDateStringForJodaTime(String dateString) {
      String fromPrefix = "292278994-";
      String toPrefix = "292278993-";
      return dateString.startsWith("292278994-") ? "292278993-" + dateString.substring("292278994-".length()) : dateString;
   }

   private static <E extends RuntimeException> E handleException(E ex) {
      if (JodaTime.hasExpectedBehavior()) {
         return ex;
      } else {
         throw new IllegalStateException("Joda-time 2.2 or later version is required, but found version: " + JodaTime.getVersion(), ex);
      }
   }

   public static String formatISO8601Date(Date date) {
      try {
         return iso8601DateFormat.print(date.getTime());
      } catch (RuntimeException var2) {
         throw handleException(var2);
      }
   }

   public static String formatISO8601Date(DateTime date) {
      try {
         return iso8601DateFormat.print(date);
      } catch (RuntimeException var2) {
         throw handleException(var2);
      }
   }

   public static Date parseRFC822Date(String dateString) {
      if (dateString == null) {
         return null;
      } else {
         try {
            return new Date(rfc822DateFormat.parseMillis(dateString));
         } catch (RuntimeException var2) {
            throw handleException(var2);
         }
      }
   }

   public static String formatRFC822Date(Date date) {
      try {
         return rfc822DateFormat.print(date.getTime());
      } catch (RuntimeException var2) {
         throw handleException(var2);
      }
   }

   public static Date parseCompressedISO8601Date(String dateString) {
      try {
         return new Date(compressedIso8601DateFormat.parseMillis(dateString));
      } catch (RuntimeException var2) {
         throw handleException(var2);
      }
   }

   public static Date parseServiceSpecificDate(String dateString) {
      if (dateString == null) {
         return null;
      } else {
         try {
            BigDecimal dateValue = new BigDecimal(dateString);
            return new Date(dateValue.scaleByPowerOfTen(3).longValue());
         } catch (NumberFormatException var2) {
            throw new SdkClientException("Unable to parse date : " + dateString, var2);
         }
      }
   }

   public static Date parseUnixTimestampInMillis(String dateString) {
      if (dateString == null) {
         return null;
      } else {
         try {
            BigDecimal dateValue = new BigDecimal(dateString);
            return new Date(dateValue.longValue());
         } catch (NumberFormatException var2) {
            throw new SdkClientException("Unable to parse date : " + dateString, var2);
         }
      }
   }

   public static String formatServiceSpecificDate(Date date) {
      if (date == null) {
         return null;
      } else {
         BigDecimal dateValue = BigDecimal.valueOf(date.getTime());
         return dateValue.scaleByPowerOfTen(-3).toPlainString();
      }
   }

   public static String formatUnixTimestampInMills(Date date) {
      if (date == null) {
         return null;
      } else {
         BigDecimal dateValue = BigDecimal.valueOf(date.getTime());
         return dateValue.toPlainString();
      }
   }

   public static Date cloneDate(Date date) {
      return date == null ? null : new Date(date.getTime());
   }

   public static long numberOfDaysSinceEpoch(long milliSinceEpoch) {
      return TimeUnit.MILLISECONDS.toDays(milliSinceEpoch);
   }
}
