package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ScanRange implements Serializable, Cloneable {
   private Long start = null;
   private Long end = null;

   public Long getStart() {
      return this.start;
   }

   public void setStart(long start) {
      this.start = start;
   }

   public ScanRange withStart(long start) {
      this.start = start;
      return this;
   }

   public Long getEnd() {
      return this.end;
   }

   public void setEnd(long end) {
      this.end = end;
   }

   public ScanRange withEnd(long end) {
      this.end = end;
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getStart() != null) {
         sb.append("Start: ").append(this.getStart()).append(',');
      }

      if (this.getEnd() != null) {
         sb.append("End: ").append(this.getEnd());
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getStart() == null ? 0 : this.getStart().hashCode());
      return 31 * hashCode + (this.getEnd() == null ? 0 : this.getEnd().hashCode());
   }

   public ScanRange clone() {
      try {
         return (ScanRange)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
