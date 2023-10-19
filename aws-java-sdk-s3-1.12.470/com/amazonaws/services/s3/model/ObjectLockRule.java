package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ObjectLockRule implements Serializable {
   private DefaultRetention defaultRetention;

   public DefaultRetention getDefaultRetention() {
      return this.defaultRetention;
   }

   public ObjectLockRule withDefaultRetention(DefaultRetention defaultRetention) {
      this.defaultRetention = defaultRetention;
      return this;
   }

   public void setDefaultRetention(DefaultRetention defaultRetention) {
      this.withDefaultRetention(defaultRetention);
   }
}
