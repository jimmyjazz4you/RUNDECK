package com.amazonaws.auth.policy.resources;

import com.amazonaws.auth.policy.Resource;

public class S3ObjectResource extends Resource {
   public S3ObjectResource(String bucketName, String keyPattern) {
      this("aws", bucketName, keyPattern);
   }

   public S3ObjectResource(String partitionName, String bucketName, String keyPattern) {
      super(String.format("arn:%s:s3:::%s/%s", partitionName, bucketName, keyPattern));
   }
}
