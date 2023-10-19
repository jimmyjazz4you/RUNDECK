package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class WriteGetObjectResponseResult implements Serializable {
   private static final long serialVersionUID = 1L;

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && this.getClass() == o.getClass();
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode();
   }
}
