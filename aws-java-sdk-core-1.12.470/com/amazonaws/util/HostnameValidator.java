package com.amazonaws.util;

import com.amazonaws.annotation.SdkProtectedApi;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SdkProtectedApi
public final class HostnameValidator {
   private static final Pattern HOSTNAME_COMPLIANT_PATTERN = Pattern.compile("[A-Za-z0-9\\-]+");
   private static final int HOSTNAME_MAX_LENGTH = 63;

   private HostnameValidator() {
   }

   public static void validateHostnameCompliant(String hostnameComponent, String paramName, String object) {
      if (StringUtils.isNullOrEmpty(hostnameComponent)) {
         throw new IllegalArgumentException(String.format("The provided %s is not valid: the required '%s' component is missing.", object, paramName));
      } else if (hostnameComponent.length() > 63) {
         throw new IllegalArgumentException(
            String.format("The provided %s is not valid: the '%s' component exceeds the maximum length of %d characters.", object, paramName, 63)
         );
      } else {
         Matcher m = HOSTNAME_COMPLIANT_PATTERN.matcher(hostnameComponent);
         if (!m.matches()) {
            throw new IllegalArgumentException(
               String.format("The provided %s is not valid: the '%s' component must only contain alphanumeric characters and dashes.", object, paramName)
            );
         }
      }
   }
}
