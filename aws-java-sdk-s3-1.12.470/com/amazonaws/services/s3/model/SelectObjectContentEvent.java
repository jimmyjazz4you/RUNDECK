package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class SelectObjectContentEvent implements Serializable, Cloneable {
   public void visit(SelectObjectContentEventVisitor visitor) {
      visitor.visitDefault(this);
   }

   public SelectObjectContentEvent clone() {
      try {
         return (SelectObjectContentEvent)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static class ContinuationEvent extends SelectObjectContentEvent {
      @Override
      public void visit(SelectObjectContentEventVisitor visitor) {
         visitor.visit(this);
      }
   }

   public static class EndEvent extends SelectObjectContentEvent {
      @Override
      public void visit(SelectObjectContentEventVisitor visitor) {
         visitor.visit(this);
      }
   }

   public static class ProgressEvent extends SelectObjectContentEvent {
      private Progress details;

      public Progress getDetails() {
         return this.details;
      }

      public void setDetails(Progress details) {
         this.details = details;
      }

      public SelectObjectContentEvent.ProgressEvent withDetails(Progress details) {
         this.setDetails(details);
         return this;
      }

      @Override
      public void visit(SelectObjectContentEventVisitor visitor) {
         visitor.visit(this);
      }
   }

   public static class RecordsEvent extends SelectObjectContentEvent {
      private ByteBuffer payload;

      public ByteBuffer getPayload() {
         return this.payload;
      }

      public void setPayload(ByteBuffer payload) {
         this.payload = payload;
      }

      public SelectObjectContentEvent.RecordsEvent withPayload(ByteBuffer payload) {
         this.setPayload(payload);
         return this;
      }

      @Override
      public void visit(SelectObjectContentEventVisitor visitor) {
         visitor.visit(this);
      }
   }

   public static class StatsEvent extends SelectObjectContentEvent {
      private Stats details;

      public Stats getDetails() {
         return this.details;
      }

      public void setDetails(Stats details) {
         this.details = details;
      }

      public SelectObjectContentEvent.StatsEvent withDetails(Stats details) {
         this.setDetails(details);
         return this;
      }

      @Override
      public void visit(SelectObjectContentEventVisitor visitor) {
         visitor.visit(this);
      }
   }
}
