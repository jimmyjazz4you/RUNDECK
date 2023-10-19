package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class DeletePublicAccessBlockResult implements Serializable, Cloneable {
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      sb.append("}");
      return sb.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else {
         return obj instanceof DeletePublicAccessBlockResult;
      }
   }

   @Override
   public int hashCode() {
      return 1;
   }

   public DeletePublicAccessBlockResult clone() {
      try {
         return (DeletePublicAccessBlockResult)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
