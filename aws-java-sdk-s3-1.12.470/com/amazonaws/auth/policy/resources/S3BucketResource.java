package com.amazonaws.auth.policy.resources;

import com.amazonaws.auth.policy.Resource;

public class S3BucketResource extends Resource {
   public S3BucketResource(String bucketName) {
      this("aws", bucketName);
   }

   public S3BucketResource(String partitionName, String bucketName) {
      super(String.format("arn:%s:s3:::%s", partitionName, bucketName));
   }
}
