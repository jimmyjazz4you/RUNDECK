package com.amazonaws.services.s3.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InventoryConfiguration implements Serializable {
   private String id;
   private InventoryDestination destination;
   private Boolean isEnabled;
   private InventoryFilter inventoryFilter;
   private String includedObjectVersions;
   private List<String> optionalFields;
   private InventorySchedule schedule;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public InventoryConfiguration withId(String id) {
      this.setId(id);
      return this;
   }

   public InventoryDestination getDestination() {
      return this.destination;
   }

   public void setDestination(InventoryDestination destination) {
      this.destination = destination;
   }

   public InventoryConfiguration withDestination(InventoryDestination destination) {
      this.setDestination(destination);
      return this;
   }

   public Boolean isEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(Boolean enabled) {
      this.isEnabled = enabled;
   }

   public InventoryConfiguration withEnabled(Boolean enabled) {
      this.setEnabled(enabled);
      return this;
   }

   public InventoryFilter getInventoryFilter() {
      return this.inventoryFilter;
   }

   public void setInventoryFilter(InventoryFilter inventoryFilter) {
      this.inventoryFilter = inventoryFilter;
   }

   public InventoryConfiguration withFilter(InventoryFilter inventoryFilter) {
      this.setInventoryFilter(inventoryFilter);
      return this;
   }

   public String getIncludedObjectVersions() {
      return this.includedObjectVersions;
   }

   public void setIncludedObjectVersions(String includedObjectVersions) {
      this.includedObjectVersions = includedObjectVersions;
   }

   public InventoryConfiguration withIncludedObjectVersions(String includedObjectVersions) {
      this.setIncludedObjectVersions(includedObjectVersions);
      return this;
   }

   public void setIncludedObjectVersions(InventoryIncludedObjectVersions includedObjectVersions) {
      this.setIncludedObjectVersions(includedObjectVersions == null ? (String)null : includedObjectVersions.toString());
   }

   public InventoryConfiguration withIncludedObjectVersions(InventoryIncludedObjectVersions includedObjectVersions) {
      this.setIncludedObjectVersions(includedObjectVersions);
      return this;
   }

   public List<String> getOptionalFields() {
      return this.optionalFields;
   }

   public void setOptionalFields(List<String> optionalFields) {
      this.optionalFields = optionalFields;
   }

   public InventoryConfiguration withOptionalFields(List<String> optionalFields) {
      this.setOptionalFields(optionalFields);
      return this;
   }

   public void addOptionalField(String optionalField) {
      if (optionalField != null) {
         if (this.optionalFields == null) {
            this.optionalFields = new ArrayList<>();
         }

         this.optionalFields.add(optionalField);
      }
   }

   public void addOptionalField(InventoryOptionalField optionalField) {
      this.addOptionalField(optionalField == null ? (String)null : optionalField.toString());
   }

   public InventorySchedule getSchedule() {
      return this.schedule;
   }

   public void setSchedule(InventorySchedule schedule) {
      this.schedule = schedule;
   }

   public InventoryConfiguration withSchedule(InventorySchedule schedule) {
      this.setSchedule(schedule);
      return this;
   }
}
