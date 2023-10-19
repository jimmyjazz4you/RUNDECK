package com.amazonaws.arn;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface AwsResource {
   String getPartition();

   String getRegion();

   String getAccountId();
}
