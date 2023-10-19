package com.amazonaws.transform;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.NullNode;

@SdkInternalApi
@ThreadSafe
public class JsonErrorUnmarshaller extends AbstractErrorUnmarshaller<JsonNode> {
   public static final JsonErrorUnmarshaller DEFAULT_UNMARSHALLER = new JsonErrorUnmarshaller(AmazonServiceException.class, null);
   private static final ObjectMapper MAPPER = new ObjectMapper();
   private final String handledErrorCode;

   public JsonErrorUnmarshaller(Class<? extends AmazonServiceException> exceptionClass, String handledErrorCode) {
      super(exceptionClass);
      this.handledErrorCode = handledErrorCode;
   }

   public AmazonServiceException unmarshall(JsonNode jsonContent) throws Exception {
      return jsonContent != null && !NullNode.instance.equals(jsonContent)
         ? (AmazonServiceException)MAPPER.treeToValue(jsonContent, this.exceptionClass)
         : null;
   }

   public boolean matchErrorCode(String actualErrorCode) {
      return this.handledErrorCode == null ? true : this.handledErrorCode.equals(actualErrorCode);
   }

   static {
      MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      try {
         MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
      } catch (LinkageError var1) {
         MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE);
      }
   }
}
