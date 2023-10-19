package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;

public class GetObjectTaggingResponseHeaderHandler implements HeaderHandler<GetObjectTaggingResult> {
   public void handle(GetObjectTaggingResult result, HttpResponse response) {
      result.setVersionId((String)response.getHeaders().get("x-amz-version-id"));
   }
}
