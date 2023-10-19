package com.amazonaws.protocol.json;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.transform.JsonErrorUnmarshaller;

@NotThreadSafe
@SdkProtectedApi
public class JsonErrorShapeMetadata {
   private String errorCode;
   private Integer httpStatusCode;
   private Class<? extends RuntimeException> modeledClass;
   private JsonErrorUnmarshaller exceptionUnmarshaller;

   public String getErrorCode() {
      return this.errorCode;
   }

   public JsonErrorShapeMetadata withErrorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
   }

   public Integer getHttpStatusCode() {
      return this.httpStatusCode;
   }

   public JsonErrorShapeMetadata withHttpStatusCode(Integer httpStatusCode) {
      this.httpStatusCode = httpStatusCode;
      return this;
   }

   public Class<? extends RuntimeException> getModeledClass() {
      return this.modeledClass;
   }

   public JsonErrorShapeMetadata withModeledClass(Class<? extends RuntimeException> modeledClass) {
      this.modeledClass = modeledClass;
      return this;
   }

   public JsonErrorShapeMetadata withExceptionUnmarshaller(JsonErrorUnmarshaller exceptionUnmarshaller) {
      this.exceptionUnmarshaller = exceptionUnmarshaller;
      return this;
   }

   public JsonErrorUnmarshaller getExceptionUnmarshaller() {
      return this.exceptionUnmarshaller;
   }
}
