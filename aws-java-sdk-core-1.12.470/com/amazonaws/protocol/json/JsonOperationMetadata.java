package com.amazonaws.protocol.json;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;

@NotThreadSafe
@SdkProtectedApi
public class JsonOperationMetadata {
   private boolean hasStreamingSuccessResponse;
   private boolean isPayloadJson;

   public boolean isHasStreamingSuccessResponse() {
      return this.hasStreamingSuccessResponse;
   }

   public JsonOperationMetadata withHasStreamingSuccessResponse(boolean hasStreamingSuccessResponse) {
      this.hasStreamingSuccessResponse = hasStreamingSuccessResponse;
      return this;
   }

   public boolean isPayloadJson() {
      return this.isPayloadJson;
   }

   public JsonOperationMetadata withPayloadJson(boolean payloadJson) {
      this.isPayloadJson = payloadJson;
      return this;
   }
}
