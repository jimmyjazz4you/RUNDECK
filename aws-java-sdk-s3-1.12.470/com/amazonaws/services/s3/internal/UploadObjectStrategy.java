package com.amazonaws.services.s3.internal;

import com.amazonaws.Request;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.model.ObjectMetadata;

@SdkInternalApi
public interface UploadObjectStrategy<RequestT, ResponseT> {
   ObjectMetadata invokeServiceCall(Request<RequestT> var1);

   ResponseT createResult(ObjectMetadata var1, String var2);

   String md5ValidationErrorSuffix();
}
