package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class Stats implements Serializable {
   private Long bytesScanned;
   private Long bytesProcessed;
   private Long bytesReturned;

   public Long getBytesScanned() {
      return this.bytesScanned;
   }

   public void setBytesScanned(Long bytesScanned) {
      this.bytesScanned = bytesScanned;
   }

   public Stats withBytesScanned(Long readBytes) {
      this.setBytesScanned(readBytes);
      return this;
   }

   public Long getBytesReturned() {
      return this.bytesReturned;
   }

   public void setBytesReturned(Long bytesReturned) {
      this.bytesReturned = bytesReturned;
   }

   public Stats withBytesReturned(Long bytesReturned) {
      this.setBytesReturned(bytesReturned);
      return this;
   }

   public Long getBytesProcessed() {
      return this.bytesProcessed;
   }

   public void setBytesProcessed(Long bytesProcessed) {
      this.bytesProcessed = bytesProcessed;
   }

   public Stats withBytesProcessed(Long processedBytes) {
      this.setBytesProcessed(processedBytes);
      return this;
   }
}
