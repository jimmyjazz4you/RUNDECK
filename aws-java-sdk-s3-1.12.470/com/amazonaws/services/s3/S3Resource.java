package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.arn.AwsResource;

@SdkInternalApi
public interface S3Resource extends AwsResource {
   String getType();

   S3Resource getParentS3Resource();
}
