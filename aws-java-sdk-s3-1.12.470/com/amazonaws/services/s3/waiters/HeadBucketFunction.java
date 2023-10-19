package com.amazonaws.services.s3.waiters;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.services.s3.model.HeadBucketResult;
import com.amazonaws.waiters.SdkFunction;

@SdkInternalApi
public class HeadBucketFunction implements SdkFunction<HeadBucketRequest, HeadBucketResult> {
   private final AmazonS3 client;

   public HeadBucketFunction(AmazonS3 client) {
      this.client = client;
   }

   public HeadBucketResult apply(HeadBucketRequest headBucketRequest) {
      return this.client.headBucket(headBucketRequest);
   }
}
