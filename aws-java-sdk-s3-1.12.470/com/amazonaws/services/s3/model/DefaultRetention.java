package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class DefaultRetention implements Serializable {
   private String mode;
   private Integer days;
   private Integer years;

   public String getMode() {
      return this.mode;
   }

   public DefaultRetention withMode(String mode) {
      this.mode = mode;
      return this;
   }

   public DefaultRetention withMode(ObjectLockRetentionMode mode) {
      return this.withMode(mode.toString());
   }

   public void setMode(ObjectLockRetentionMode mode) {
      this.withMode(mode);
   }

   public void setMode(String mode) {
      this.withMode(mode);
   }

   public Integer getDays() {
      return this.days;
   }

   public DefaultRetention withDays(Integer days) {
      this.days = days;
      return this;
   }

   public void setDays(Integer days) {
      this.withDays(days);
   }

   public Integer getYears() {
      return this.years;
   }

   public DefaultRetention withYears(Integer years) {
      this.years = years;
      return this;
   }

   public void setYears(Integer years) {
      this.withYears(years);
   }
}
