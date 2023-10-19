package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import java.io.Serializable;

public class SetObjectRetentionResult implements Serializable, S3RequesterChargedResult {
   private boolean requesterCharged;

   @Override
   public boolean isRequesterCharged() {
      return this.requesterCharged;
   }

   public SetObjectRetentionResult withRequesterCharged(Boolean requesterCharged) {
      this.requesterCharged = requesterCharged;
      return this;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.withRequesterCharged(isRequesterCharged);
   }
}
