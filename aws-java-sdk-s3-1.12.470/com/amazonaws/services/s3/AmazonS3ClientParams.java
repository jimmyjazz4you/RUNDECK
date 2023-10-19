package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.client.AwsSyncClientParams;

@SdkInternalApi
abstract class AmazonS3ClientParams {
   public abstract AwsSyncClientParams getClientParams();

   public abstract S3ClientOptions getS3ClientOptions();
}
