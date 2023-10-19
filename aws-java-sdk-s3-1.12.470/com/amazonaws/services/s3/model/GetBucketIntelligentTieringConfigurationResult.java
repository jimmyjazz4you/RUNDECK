package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import java.io.Serializable;

public class GetBucketIntelligentTieringConfigurationResult implements Serializable {
   private IntelligentTieringConfiguration intelligentTieringConfiguration;

   public IntelligentTieringConfiguration getIntelligentTieringConfiguration() {
      return this.intelligentTieringConfiguration;
   }

   public void setIntelligentTieringConfiguration(IntelligentTieringConfiguration intelligentTieringConfiguration) {
      this.intelligentTieringConfiguration = intelligentTieringConfiguration;
   }

   public GetBucketIntelligentTieringConfigurationResult withIntelligentTieringConfiguration(IntelligentTieringConfiguration intelligentTieringConfiguration) {
      this.setIntelligentTieringConfiguration(intelligentTieringConfiguration);
      return this;
   }
}
