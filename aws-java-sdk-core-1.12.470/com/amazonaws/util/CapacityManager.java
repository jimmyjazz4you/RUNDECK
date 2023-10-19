package com.amazonaws.util;

public class CapacityManager {
   private volatile int availableCapacity;
   private final int maxCapacity;
   private final Object lock = new Object();

   public CapacityManager(int maxCapacity) {
      this.maxCapacity = maxCapacity;
      this.availableCapacity = maxCapacity;
   }

   public boolean acquire() {
      return this.acquire(1);
   }

   public boolean acquire(int capacity) {
      if (capacity < 0) {
         throw new IllegalArgumentException("capacity to acquire cannot be negative");
      } else if (this.availableCapacity < 0) {
         return true;
      } else {
         synchronized(this.lock) {
            if (this.availableCapacity - capacity >= 0) {
               this.availableCapacity -= capacity;
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void release() {
      this.release(1);
   }

   public void release(int capacity) {
      if (capacity < 0) {
         throw new IllegalArgumentException("capacity to release cannot be negative");
      } else {
         if (this.availableCapacity >= 0 && this.availableCapacity != this.maxCapacity) {
            synchronized(this.lock) {
               this.availableCapacity = Math.min(this.availableCapacity + capacity, this.maxCapacity);
            }
         }
      }
   }

   public int consumedCapacity() {
      return this.availableCapacity < 0 ? 0 : this.maxCapacity - this.availableCapacity;
   }

   public int availableCapacity() {
      return this.availableCapacity;
   }
}
