package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectExpirationHeaderHandler<T extends ObjectExpirationResult> implements HeaderHandler<T> {
   private static final Pattern datePattern = Pattern.compile("expiry-date=\"(.*?)\"");
   private static final Pattern rulePattern = Pattern.compile("rule-id=\"(.*?)\"");
   private static final Log log = LogFactory.getLog(ObjectExpirationHeaderHandler.class);

   public void handle(T result, HttpResponse response) {
      String expirationHeader = (String)response.getHeaders().get("x-amz-expiration");
      if (expirationHeader != null) {
         result.setExpirationTime(this.parseDate(expirationHeader));
         result.setExpirationTimeRuleId(this.parseRuleId(expirationHeader));
      }
   }

   private String parseRuleId(String expirationHeader) {
      Matcher matcher = rulePattern.matcher(expirationHeader);
      return matcher.find() ? matcher.group(1) : null;
   }

   private Date parseDate(String expirationHeader) {
      Matcher matcher = datePattern.matcher(expirationHeader);
      if (matcher.find()) {
         String date = matcher.group(1);

         try {
            return ServiceUtils.parseRfc822Date(date);
         } catch (Exception var5) {
            log.warn("Error parsing expiry-date from x-amz-expiration header.", var5);
         }
      }

      return null;
   }
}
