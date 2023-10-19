package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;

public class S3RequesterChargedHeaderHandler<T extends S3RequesterChargedResult> implements HeaderHandler<T> {
   public void handle(T result, HttpResponse response) {
      result.setRequesterCharged(response.getHeaders().get("x-amz-request-charged") != null);
   }
}
