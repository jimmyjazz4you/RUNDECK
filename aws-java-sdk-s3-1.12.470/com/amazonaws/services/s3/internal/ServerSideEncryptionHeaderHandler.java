package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;

public class ServerSideEncryptionHeaderHandler<T extends ServerSideEncryptionResult> implements HeaderHandler<T> {
   public void handle(T result, HttpResponse response) {
      result.setSSEAlgorithm((String)response.getHeaders().get("x-amz-server-side-encryption"));
      result.setSSECustomerAlgorithm((String)response.getHeaders().get("x-amz-server-side-encryption-customer-algorithm"));
      result.setSSECustomerKeyMd5((String)response.getHeaders().get("x-amz-server-side-encryption-customer-key-MD5"));
      String bucketKeyEnabled = (String)response.getHeaders().get("x-amz-server-side-encryption-bucket-key-enabled");
      if (bucketKeyEnabled != null) {
         result.setBucketKeyEnabled("true".equals(bucketKeyEnabled));
      }
   }
}
