package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class GlacierJobParameters implements Serializable {
   private String tier;

   public String getTier() {
      return this.tier;
   }

   public void setTier(String tier) {
      this.tier = tier;
   }

   public void setTier(Tier tier) {
      this.setTier(tier == null ? null : tier.toString());
   }

   public GlacierJobParameters withTier(String tier) {
      this.setTier(tier);
      return this;
   }

   public GlacierJobParameters withTier(Tier tier) {
      this.setTier(tier);
      return this;
   }
}
