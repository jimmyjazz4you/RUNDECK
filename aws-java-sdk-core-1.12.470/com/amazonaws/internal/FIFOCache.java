package com.amazonaws.internal;

import com.amazonaws.annotation.ThreadSafe;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

@ThreadSafe
public final class FIFOCache<T> {
   private final BoundedLinkedHashMap<String, T> map;
   private final ReadLock rlock;
   private final WriteLock wlock;

   public FIFOCache(int maxSize) {
      if (maxSize < 1) {
         throw new IllegalArgumentException("maxSize " + maxSize + " must be at least 1");
      } else {
         this.map = new BoundedLinkedHashMap<>(maxSize);
         ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
         this.rlock = lock.readLock();
         this.wlock = lock.writeLock();
      }
   }

   public T add(String key, T value) {
      this.wlock.lock();

      Object var3;
      try {
         var3 = this.map.put(key, value);
      } finally {
         this.wlock.unlock();
      }

      return (T)var3;
   }

   public T get(String key) {
      this.rlock.lock();

      Object var2;
      try {
         var2 = this.map.get(key);
      } finally {
         this.rlock.unlock();
      }

      return (T)var2;
   }

   public int size() {
      this.rlock.lock();

      int var1;
      try {
         var1 = this.map.size();
      } finally {
         this.rlock.unlock();
      }

      return var1;
   }

   public int getMaxSize() {
      return this.map.getMaxSize();
   }

   @Override
   public String toString() {
      this.rlock.lock();

      String var1;
      try {
         var1 = this.map.toString();
      } finally {
         this.rlock.unlock();
      }

      return var1;
   }
}
