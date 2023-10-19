package com.amazonaws.transform;

import java.util.Map.Entry;

public class MapEntry<K, V> implements Entry<K, V> {
   private K key;
   private V value;

   @Override
   public K getKey() {
      return this.key;
   }

   @Override
   public V getValue() {
      return this.value;
   }

   @Override
   public V setValue(V value) {
      this.value = value;
      return this.value;
   }

   public K setKey(K key) {
      this.key = key;
      return this.key;
   }
}
