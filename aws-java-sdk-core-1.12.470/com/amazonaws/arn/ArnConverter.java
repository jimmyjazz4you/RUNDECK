package com.amazonaws.arn;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface ArnConverter<T extends AwsResource> {
   T convertArn(Arn var1);
}
