package com.amazonaws.util;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.protocol.DefaultValueSupplier;
import java.util.UUID;

@SdkProtectedApi
public final class IdempotentUtils {
   private static DefaultValueSupplier<String> generator = new DefaultValueSupplier<String>() {
      public String get() {
         return UUID.randomUUID().toString();
      }
   };

   @Deprecated
   @SdkProtectedApi
   public static String resolveString(String token) {
      return token != null ? token : generator.get();
   }

   @SdkProtectedApi
   public static DefaultValueSupplier<String> getGenerator() {
      return generator;
   }

   @SdkTestInternalApi
   public static void setGenerator(DefaultValueSupplier<String> newGenerator) {
      generator = newGenerator;
   }
}
