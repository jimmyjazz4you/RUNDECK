package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;

@SdkInternalApi
public class RequestCopyUtils {
   public static GetObjectMetadataRequest createGetObjectMetadataRequestFrom(GetObjectRequest getObjectRequest) {
      return (GetObjectMetadataRequest)new GetObjectMetadataRequest(getObjectRequest.getBucketName(), getObjectRequest.getKey())
         .withVersionId(getObjectRequest.getVersionId())
         .withRequesterPays(getObjectRequest.isRequesterPays())
         .withSSECustomerKey(getObjectRequest.getSSECustomerKey())
         .withPartNumber(getObjectRequest.getPartNumber())
         .withExpectedBucketOwner(getObjectRequest.getExpectedBucketOwner())
         .withRequestCredentialsProvider(getObjectRequest.getRequestCredentialsProvider());
   }
}
