package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.model.SetObjectTaggingResult;

public class SetObjectTaggingResponseHeaderHandler implements HeaderHandler<SetObjectTaggingResult> {
   public void handle(SetObjectTaggingResult result, HttpResponse response) {
      result.setVersionId((String)response.getHeaders().get("x-amz-version-id"));
   }
}
