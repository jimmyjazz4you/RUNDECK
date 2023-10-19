package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectRestoreHeaderHandler<T extends ObjectRestoreResult> implements HeaderHandler<T> {
   private static final Pattern datePattern = Pattern.compile("expiry-date=\"(.*?)\"");
   private static final Pattern ongoingPattern = Pattern.compile("ongoing-request=\"(.*?)\"");
   private static final Log log = LogFactory.getLog(ObjectRestoreHeaderHandler.class);

   public void handle(T result, HttpResponse response) {
      String restoreHeader = (String)response.getHeaders().get("x-amz-restore");
      if (restoreHeader != null) {
         result.setRestoreExpirationTime(this.parseDate(restoreHeader));
         Boolean onGoingRestore = this.parseBoolean(restoreHeader);
         if (onGoingRestore != null) {
            result.setOngoingRestore(onGoingRestore);
         }
      }
   }

   private Date parseDate(String restoreHeader) {
      Matcher matcher = datePattern.matcher(restoreHeader);
      if (matcher.find()) {
         String date = matcher.group(1);

         try {
            return ServiceUtils.parseRfc822Date(date);
         } catch (Exception var5) {
            log.warn("Error parsing expiry-date from x-amz-restore header.", var5);
         }
      }

      return null;
   }

   private Boolean parseBoolean(String restoreHeader) {
      Matcher matcher = ongoingPattern.matcher(restoreHeader);
      if (matcher.find()) {
         String ongoingRestore = matcher.group(1);
         return Boolean.parseBoolean(ongoingRestore);
      } else {
         return null;
      }
   }
}
