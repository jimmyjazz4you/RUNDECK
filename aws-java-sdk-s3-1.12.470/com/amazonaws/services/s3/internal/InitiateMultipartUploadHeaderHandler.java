package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;

public class InitiateMultipartUploadHeaderHandler implements HeaderHandler<InitiateMultipartUploadResult> {
   public void handle(InitiateMultipartUploadResult result, HttpResponse response) {
      result.setAbortDate(ServiceUtils.parseRfc822Date((String)response.getHeaders().get("x-amz-abort-date")));
      result.setAbortRuleId((String)response.getHeaders().get("x-amz-abort-rule-id"));
   }
}
