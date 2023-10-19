package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import java.io.Serializable;

public class GetBucketOwnershipControlsResult implements Serializable {
   private OwnershipControls OwnershipControls;

   public OwnershipControls getOwnershipControls() {
      return this.OwnershipControls;
   }

   public void setOwnershipControls(OwnershipControls OwnershipControls) {
      this.OwnershipControls = OwnershipControls;
   }

   public GetBucketOwnershipControlsResult withOwnershipControls(OwnershipControls OwnershipControls) {
      this.setOwnershipControls(OwnershipControls);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("GetBucketOwnershipControlsOutput{");
      sb.append("OwnershipControls=").append(this.OwnershipControls);
      sb.append('}');
      return sb.toString();
   }
}
