package com.amazonaws.internal;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

final class BoundedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
   private static final long serialVersionUID = 1L;
   private final int maxSize;

   BoundedLinkedHashMap(int maxSize) {
      this.maxSize = maxSize;
   }

   @Override
   protected boolean removeEldestEntry(Entry<K, V> eldest) {
      return this.size() > this.maxSize;
   }

   int getMaxSize() {
      return this.maxSize;
   }
}
