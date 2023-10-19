package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;

public class S3RestoreOutputPathHeaderHandler<T extends S3RestoreOutputPathResult> implements HeaderHandler<T> {
   public void handle(T result, HttpResponse response) {
      result.setRestoreOutputPath((String)response.getHeaders().get("x-amz-restore-output-path"));
   }
}
