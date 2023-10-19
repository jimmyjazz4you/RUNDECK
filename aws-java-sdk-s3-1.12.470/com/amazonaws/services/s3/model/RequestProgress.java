package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class RequestProgress implements Serializable {
   private Boolean enabled;

   public Boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
   }

   public RequestProgress withEnabled(Boolean enabled) {
      this.setEnabled(enabled);
      return this;
   }
}
