package com.amazonaws.services.s3.model.inventory;

import java.io.Serializable;

public class InventorySchedule implements Serializable {
   private String frequency;

   public String getFrequency() {
      return this.frequency;
   }

   public void setFrequency(String frequency) {
      this.frequency = frequency;
   }

   public void setFrequency(InventoryFrequency frequency) {
      this.setFrequency(frequency == null ? (String)null : frequency.toString());
   }

   public InventorySchedule withFrequency(String frequency) {
      this.setFrequency(frequency);
      return this;
   }

   public InventorySchedule withFrequency(InventoryFrequency frequency) {
      this.setFrequency(frequency);
      return this;
   }
}
