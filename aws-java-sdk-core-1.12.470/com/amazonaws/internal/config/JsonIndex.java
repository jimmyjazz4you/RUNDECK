package com.amazonaws.internal.config;

public final class JsonIndex<C extends Builder<T>, T> {
   private String key;
   private C config;

   public JsonIndex() {
   }

   public JsonIndex(String key) {
      this.key = key;
   }

   public JsonIndex(String key, C config) {
      this.key = key;
      this.config = config;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public C getConfig() {
      return this.config;
   }

   public void setConfig(C config) {
      this.config = config;
   }

   public T newReadOnlyConfig() {
      return this.config.build();
   }
}
