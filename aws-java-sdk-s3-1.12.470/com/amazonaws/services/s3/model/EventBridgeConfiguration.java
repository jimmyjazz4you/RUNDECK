package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class EventBridgeConfiguration implements Serializable {
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
      return 0;
   }

   @Override
   public String toString() {
      return "EventBridgeConfiguration()";
   }
}
