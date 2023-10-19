package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.model.PartListing;

public class ListPartsHeaderHandler implements HeaderHandler<PartListing> {
   public void handle(PartListing result, HttpResponse response) {
      result.setAbortDate(ServiceUtils.parseRfc822Date((String)response.getHeaders().get("x-amz-abort-date")));
      result.setAbortRuleId((String)response.getHeaders().get("x-amz-abort-rule-id"));
   }
}
