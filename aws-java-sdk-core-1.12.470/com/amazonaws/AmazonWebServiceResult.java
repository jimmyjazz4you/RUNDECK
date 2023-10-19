package com.amazonaws;

import com.amazonaws.http.SdkHttpMetadata;

public class AmazonWebServiceResult<T extends ResponseMetadata> {
   private T sdkResponseMetadata;
   private SdkHttpMetadata sdkHttpMetadata;

   public T getSdkResponseMetadata() {
      return this.sdkResponseMetadata;
   }

   public AmazonWebServiceResult<T> setSdkResponseMetadata(T sdkResponseMetadata) {
      this.sdkResponseMetadata = sdkResponseMetadata;
      return this;
   }

   public SdkHttpMetadata getSdkHttpMetadata() {
      return this.sdkHttpMetadata;
   }

   public AmazonWebServiceResult<T> setSdkHttpMetadata(SdkHttpMetadata sdkHttpMetadata) {
      this.sdkHttpMetadata = sdkHttpMetadata;
      return this;
   }
}
