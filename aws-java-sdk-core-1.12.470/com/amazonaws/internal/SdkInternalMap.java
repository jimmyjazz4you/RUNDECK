package com.amazonaws.internal;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SdkInternalMap<K, V> implements Map<K, V>, Serializable {
   private static final long serialVersionUID = 1L;
   private final Map<K, V> map;
   private final boolean autoConstruct;

   public SdkInternalMap() {
      this.map = new HashMap<>();
      this.autoConstruct = true;
   }

   public SdkInternalMap(Map<K, V> m) {
      this.map = m;
      this.autoConstruct = false;
   }

   public boolean isAutoConstruct() {
      return this.autoConstruct;
   }

   @Override
   public int size() {
      return this.map.size();
   }

   @Override
   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   @Override
   public boolean containsKey(Object key) {
      return this.map.containsKey(key);
   }

   @Override
   public boolean containsValue(Object value) {
      return this.map.containsValue(value);
   }

   @Override
   public V get(Object key) {
      return this.map.get(key);
   }

   @Override
   public V put(K key, V value) {
      return this.map.put(key, value);
   }

   @Override
   public V remove(Object key) {
      return this.map.remove(key);
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> m) {
      this.map.putAll(m);
   }

   @Override
   public void clear() {
      this.map.clear();
   }

   @Override
   public Set<K> keySet() {
      return this.map.keySet();
   }

   @Override
   public Collection<V> values() {
      return this.map.values();
   }

   @Override
   public Set<Entry<K, V>> entrySet() {
      return this.map.entrySet();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (this.map == null) {
         return o == null;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map<?, ?> that = (Map)o;
         return this.map.equals(that);
      }
   }

   @Override
   public int hashCode() {
      return this.map == null ? 0 : this.map.hashCode();
   }

   @Override
   public String toString() {
      return this.map == null ? null : this.map.toString();
   }
}
