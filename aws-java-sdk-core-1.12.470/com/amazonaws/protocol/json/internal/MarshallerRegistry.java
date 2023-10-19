package com.amazonaws.protocol.json.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingType;
import com.amazonaws.protocol.StructuredPojo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@SdkInternalApi
public class MarshallerRegistry {
   private final Map<MarshallLocation, Map<MarshallingType, JsonMarshaller<?>>> marshallers;
   private final Set<MarshallingType<?>> marshallingTypes;
   private final Map<Class<?>, MarshallingType<?>> marshallingTypeCache;

   private MarshallerRegistry(MarshallerRegistry.Builder builder) {
      this.marshallers = builder.marshallers;
      this.marshallingTypes = builder.marshallingTypes;
      this.marshallingTypeCache = new HashMap<>(this.marshallingTypes.size());
   }

   public <T> JsonMarshaller<T> getMarshaller(MarshallLocation marshallLocation, T val) {
      return this.getMarshaller(marshallLocation, this.toMarshallingType(val));
   }

   public <T> JsonMarshaller<T> getMarshaller(MarshallLocation marshallLocation, MarshallingType<T> marshallingType, T val) {
      return this.getMarshaller(marshallLocation, val == null ? MarshallingType.NULL : marshallingType);
   }

   private <T> JsonMarshaller<T> getMarshaller(MarshallLocation marshallLocation, MarshallingType<?> marshallingType) {
      return (JsonMarshaller<T>)this.marshallers.get(marshallLocation).get(marshallingType);
   }

   public <T> MarshallingType<T> toMarshallingType(T val) {
      if (val == null) {
         return MarshallingType.NULL;
      } else if (val instanceof StructuredPojo) {
         return MarshallingType.STRUCTURED;
      } else {
         return !this.marshallingTypeCache.containsKey(val.getClass())
            ? this.populateMarshallingTypeCache(val.getClass())
            : (MarshallingType)this.marshallingTypeCache.get(val.getClass());
      }
   }

   private MarshallingType<?> populateMarshallingTypeCache(Class<?> clzz) {
      synchronized(this.marshallingTypeCache) {
         if (!this.marshallingTypeCache.containsKey(clzz)) {
            for(MarshallingType<?> marshallingType : this.marshallingTypes) {
               if (marshallingType.isDefaultMarshallerForType(clzz)) {
                  this.marshallingTypeCache.put(clzz, marshallingType);
                  return marshallingType;
               }
            }

            throw new SdkClientException("MarshallingType not found for class " + clzz);
         }
      }

      return this.marshallingTypeCache.get(clzz);
   }

   public MarshallerRegistry merge(MarshallerRegistry.Builder marshallerRegistryOverrides) {
      if (marshallerRegistryOverrides == null) {
         return this;
      } else {
         MarshallerRegistry.Builder merged = builder();
         merged.copyMarshallersFromRegistry(this.marshallers);
         merged.copyMarshallersFromRegistry(marshallerRegistryOverrides.marshallers);
         return merged.build();
      }
   }

   public static MarshallerRegistry.Builder builder() {
      return new MarshallerRegistry.Builder();
   }

   public static final class Builder {
      private final Map<MarshallLocation, Map<MarshallingType, JsonMarshaller<?>>> marshallers = new HashMap<>();
      private final Set<MarshallingType<?>> marshallingTypes = new HashSet<>();

      private Builder() {
      }

      public <T> MarshallerRegistry.Builder payloadMarshaller(MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         return this.addMarshaller(MarshallLocation.PAYLOAD, marshallingType, marshaller);
      }

      public <T> MarshallerRegistry.Builder headerMarshaller(MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         return this.addMarshaller(MarshallLocation.HEADER, marshallingType, marshaller);
      }

      public <T> MarshallerRegistry.Builder queryParamMarshaller(MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         return this.addMarshaller(MarshallLocation.QUERY_PARAM, marshallingType, marshaller);
      }

      public <T> MarshallerRegistry.Builder pathParamMarshaller(MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         return this.addMarshaller(MarshallLocation.PATH, marshallingType, marshaller);
      }

      public <T> MarshallerRegistry.Builder greedyPathParamMarshaller(MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         return this.addMarshaller(MarshallLocation.GREEDY_PATH, marshallingType, marshaller);
      }

      public <T> MarshallerRegistry.Builder addMarshaller(MarshallLocation marshallLocation, MarshallingType<T> marshallingType, JsonMarshaller<T> marshaller) {
         this.marshallingTypes.add(marshallingType);
         if (!this.marshallers.containsKey(marshallLocation)) {
            this.marshallers.put(marshallLocation, new HashMap<>());
         }

         this.marshallers.get(marshallLocation).put(marshallingType, marshaller);
         return this;
      }

      public MarshallerRegistry build() {
         return new MarshallerRegistry(this);
      }

      private void copyMarshallersFromRegistry(Map<MarshallLocation, Map<MarshallingType, JsonMarshaller<?>>> sourceMarshallers) {
         for(Entry<MarshallLocation, Map<MarshallingType, JsonMarshaller<?>>> byLocationEntry : sourceMarshallers.entrySet()) {
            for(Entry<MarshallingType, JsonMarshaller<?>> byTypeEntry : byLocationEntry.getValue().entrySet()) {
               this.addMarshaller(byLocationEntry.getKey(), byTypeEntry.getKey(), byTypeEntry.getValue());
            }
         }
      }
   }
}
