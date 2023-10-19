package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.model.DeleteObjectTaggingResult;

public class DeleteObjectTaggingHeaderHandler implements HeaderHandler<DeleteObjectTaggingResult> {
   public void handle(DeleteObjectTaggingResult result, HttpResponse response) {
      result.setVersionId((String)response.getHeaders().get("x-amz-version-id"));
   }
}
