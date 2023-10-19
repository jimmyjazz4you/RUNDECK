package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;

public class S3VersionHeaderHandler<T extends S3VersionResult> implements HeaderHandler<T> {
   public void handle(T result, HttpResponse response) {
      result.setVersionId((String)response.getHeaders().get("x-amz-version-id"));
   }
}
