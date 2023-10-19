package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.PartETag;
import java.util.concurrent.Callable;

public class CopyPartCallable implements Callable<PartETag> {
   private final AmazonS3 s3;
   private final CopyPartRequest request;

   public CopyPartCallable(AmazonS3 s3, CopyPartRequest request) {
      this.s3 = s3;
      this.request = request;
   }

   public PartETag call() throws Exception {
      return this.s3.copyPart(this.request).getPartETag();
   }
}
