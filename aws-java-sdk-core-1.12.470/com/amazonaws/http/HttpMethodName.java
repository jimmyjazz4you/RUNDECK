package com.amazonaws.http;

import com.amazonaws.util.StringUtils;

public enum HttpMethodName {
   GET,
   POST,
   PUT,
   DELETE,
   HEAD,
   PATCH,
   OPTIONS;

   public static HttpMethodName fromValue(String value) {
      if (StringUtils.isNullOrEmpty(value)) {
         return null;
      } else {
         String upperCaseValue = StringUtils.upperCase(value);

         for(HttpMethodName httpMethodName : values()) {
            if (httpMethodName.name().equals(upperCaseValue)) {
               return httpMethodName;
            }
         }

         throw new IllegalArgumentException("Unsupported HTTP method name " + value);
      }
   }
}
