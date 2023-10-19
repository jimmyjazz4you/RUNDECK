package com.amazonaws.transform;

import com.amazonaws.util.IdempotentUtils;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.ValidationUtils;

public class PathMarshallers {
   public static final PathMarshallers.PathMarshaller NON_GREEDY = new PathMarshallers.NonGreedyPathMarshaller();
   public static final PathMarshallers.PathMarshaller GREEDY = new PathMarshallers.GreedyPathMarshaller();
   public static final PathMarshallers.PathMarshaller IDEMPOTENCY = new PathMarshallers.IdempotencyPathMarshaller();

   private static String trimLeadingSlash(String value) {
      return value.startsWith("/") ? value.replaceFirst("/", "") : value;
   }

   private static class GreedyPathMarshaller implements PathMarshallers.PathMarshaller {
      private GreedyPathMarshaller() {
      }

      @Override
      public String marshall(String resourcePath, String paramName, String pathValue) {
         ValidationUtils.assertStringNotEmpty(pathValue, paramName);
         return resourcePath.replace(String.format("{%s+}", paramName), PathMarshallers.trimLeadingSlash(pathValue));
      }

      @Override
      public String marshall(String resourcePath, String paramName, Integer pathValue) {
         ValidationUtils.assertNotNull(pathValue, paramName);
         return this.marshall(resourcePath, paramName, StringUtils.fromInteger(pathValue));
      }

      @Override
      public String marshall(String resourcePath, String paramName, Long pathValue) {
         ValidationUtils.assertNotNull(pathValue, paramName);
         return this.marshall(resourcePath, paramName, StringUtils.fromLong(pathValue));
      }
   }

   private static class IdempotencyPathMarshaller implements PathMarshallers.PathMarshaller {
      private IdempotencyPathMarshaller() {
      }

      @Override
      public String marshall(String resourcePath, String paramName, String pathValue) {
         if (pathValue != null && pathValue.isEmpty()) {
            throw new IllegalArgumentException(paramName + " must not be empty. If not set a value will be auto generated");
         } else {
            return resourcePath.replace(String.format("{%s}", paramName), SdkHttpUtils.urlEncode(IdempotentUtils.resolveString(pathValue), false));
         }
      }

      @Override
      public String marshall(String resourcePath, String paramName, Integer pathValue) {
         throw new UnsupportedOperationException("Integer idempotency tokens not yet supported");
      }

      @Override
      public String marshall(String resourcePath, String paramName, Long pathValue) {
         throw new UnsupportedOperationException("Long idempotency tokens not yet supported");
      }
   }

   private static class NonGreedyPathMarshaller implements PathMarshallers.PathMarshaller {
      private NonGreedyPathMarshaller() {
      }

      @Override
      public String marshall(String resourcePath, String paramName, String pathValue) {
         ValidationUtils.assertStringNotEmpty(pathValue, paramName);
         return resourcePath.replace(String.format("{%s}", paramName), SdkHttpUtils.urlEncode(pathValue, false));
      }

      @Override
      public String marshall(String resourcePath, String paramName, Integer pathValue) {
         ValidationUtils.assertNotNull(pathValue, paramName);
         return this.marshall(resourcePath, paramName, StringUtils.fromInteger(pathValue));
      }

      @Override
      public String marshall(String resourcePath, String paramName, Long pathValue) {
         ValidationUtils.assertNotNull(pathValue, paramName);
         return this.marshall(resourcePath, paramName, StringUtils.fromLong(pathValue));
      }
   }

   public interface PathMarshaller {
      String marshall(String var1, String var2, String var3);

      String marshall(String var1, String var2, Integer var3);

      String marshall(String var1, String var2, Long var3);
   }
}
