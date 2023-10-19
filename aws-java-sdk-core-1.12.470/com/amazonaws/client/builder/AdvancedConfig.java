package com.amazonaws.client.builder;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkProtectedApi;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SdkProtectedApi
@Immutable
public final class AdvancedConfig {
   public static final AdvancedConfig EMPTY = builder().build();
   private final Map<AdvancedConfig.Key<?>, Object> config;

   private AdvancedConfig(AdvancedConfig.Builder builder) {
      this.config = Collections.unmodifiableMap(new HashMap<>(builder.config));
   }

   public <T> T get(AdvancedConfig.Key<T> key) {
      return (T)this.config.get(key);
   }

   public static AdvancedConfig.Builder builder() {
      return new AdvancedConfig.Builder();
   }

   public static class Builder {
      private final Map<AdvancedConfig.Key<?>, Object> config = new HashMap<>();

      private Builder() {
      }

      public <T> T get(AdvancedConfig.Key<T> key) {
         return (T)this.config.get(key);
      }

      public <T> AdvancedConfig.Builder put(AdvancedConfig.Key<T> key, T value) {
         this.config.put(key, value);
         return this;
      }

      public AdvancedConfig build() {
         return new AdvancedConfig(this);
      }
   }

   public static class Key<T> {
   }
}
