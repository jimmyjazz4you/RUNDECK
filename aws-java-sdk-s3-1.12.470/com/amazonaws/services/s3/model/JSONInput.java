package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class JSONInput implements Serializable {
   private String type;

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public JSONInput withType(String type) {
      this.setType(type);
      return this;
   }

   public void setType(JSONType type) {
      this.setType(type == null ? null : type.toString());
   }

   public JSONInput withType(JSONType type) {
      this.setType(type);
      return this;
   }
}
