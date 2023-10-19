package com.amazonaws.services.s3.waiters;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.waiters.SdkFunction;

@SdkInternalApi
public class HeadObjectFunction implements SdkFunction<GetObjectMetadataRequest, ObjectMetadata> {
   private final AmazonS3 client;

   public HeadObjectFunction(AmazonS3 client) {
      this.client = client;
   }

   public ObjectMetadata apply(GetObjectMetadataRequest headObjectRequest) {
      return this.client.getObjectMetadata(headObjectRequest);
   }
}
