package com.amazonaws.services.s3.model.intelligenttiering;

import java.io.Serializable;
import java.util.Objects;

public class Tiering implements Serializable {
   private Integer days;
   private IntelligentTieringAccessTier accessTier;

   public Integer getDays() {
      return this.days;
   }

   public void setDays(Integer days) {
      this.days = days;
   }

   public Tiering withDays(Integer days) {
      this.setDays(days);
      return this;
   }

   public IntelligentTieringAccessTier getAccessTier() {
      return this.accessTier;
   }

   public void setAccessTier(IntelligentTieringAccessTier accessTier) {
      this.accessTier = accessTier;
   }

   public Tiering withIntelligentTieringAccessTier(IntelligentTieringAccessTier intelligentTieringAccessTier) {
      this.setAccessTier(intelligentTieringAccessTier);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Tiering tiering = (Tiering)o;
         return Objects.equals(this.days, tiering.days) && this.accessTier == tiering.accessTier;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.days, this.accessTier);
   }
}
