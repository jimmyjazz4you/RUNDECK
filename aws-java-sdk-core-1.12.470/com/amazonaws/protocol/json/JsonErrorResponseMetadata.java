package com.amazonaws.protocol.json;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import java.util.List;

@NotThreadSafe
@SdkProtectedApi
public class JsonErrorResponseMetadata {
   private String customErrorCodeFieldName;
   private List<JsonErrorShapeMetadata> errorShapes;
   private boolean hasAwsQueryCompatible;

   public String getCustomErrorCodeFieldName() {
      return this.customErrorCodeFieldName;
   }

   public JsonErrorResponseMetadata withCustomErrorCodeFieldName(String errorCodeFieldName) {
      this.customErrorCodeFieldName = errorCodeFieldName;
      return this;
   }

   public List<JsonErrorShapeMetadata> getErrorShapes() {
      return this.errorShapes;
   }

   public JsonErrorResponseMetadata withErrorShapes(List<JsonErrorShapeMetadata> errorShapes) {
      this.errorShapes = errorShapes;
      return this;
   }

   public boolean getAwsQueryCompatible() {
      return this.hasAwsQueryCompatible;
   }

   public JsonErrorResponseMetadata withAwsQueryCompatible(Boolean hasAwsQueryCompatible) {
      this.hasAwsQueryCompatible = hasAwsQueryCompatible != null && hasAwsQueryCompatible;
      return this;
   }
}
