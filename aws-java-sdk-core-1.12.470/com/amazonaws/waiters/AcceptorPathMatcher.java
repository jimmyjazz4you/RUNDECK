package com.amazonaws.waiters;

import com.amazonaws.annotation.SdkProtectedApi;
import com.fasterxml.jackson.databind.JsonNode;

@SdkProtectedApi
public class AcceptorPathMatcher {
   public static boolean pathAll(JsonNode expectedResult, JsonNode finalResult) {
      if (finalResult.isNull()) {
         return false;
      } else if (!finalResult.isArray()) {
         throw new RuntimeException("Expected an array");
      } else {
         for(JsonNode element : finalResult) {
            if (!element.equals(expectedResult)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean pathAny(JsonNode expectedResult, JsonNode finalResult) {
      if (finalResult.isNull()) {
         return false;
      } else if (!finalResult.isArray()) {
         throw new RuntimeException("Expected an array");
      } else {
         for(JsonNode element : finalResult) {
            if (element.equals(expectedResult)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean path(JsonNode expectedResult, JsonNode finalResult) {
      return finalResult.equals(expectedResult);
   }
}
