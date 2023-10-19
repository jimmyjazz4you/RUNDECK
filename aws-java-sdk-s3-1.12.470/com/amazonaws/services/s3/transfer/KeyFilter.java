package com.amazonaws.services.s3.transfer;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public interface KeyFilter {
   KeyFilter INCLUDE_ALL = new KeyFilter() {
      @Override
      public boolean shouldInclude(S3ObjectSummary objectSummary) {
         return true;
      }
   };

   boolean shouldInclude(S3ObjectSummary var1);
}
