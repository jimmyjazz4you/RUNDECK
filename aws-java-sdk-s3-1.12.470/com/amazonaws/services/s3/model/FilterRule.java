package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class FilterRule implements Serializable {
   private String name;
   private String value;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if (name == null) {
         throw new IllegalArgumentException("FilterRule Name is a required argument");
      } else {
         this.name = name;
      }
   }

   public FilterRule withName(String name) {
      this.setName(name);
      return this;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public FilterRule withValue(String value) {
      this.setValue(value);
      return this;
   }
}
