package com.amazonaws.util;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.annotation.SdkInternalApi;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

@SdkInternalApi
public class ResponseMetadataCache implements MetadataCache {
   private final ResponseMetadataCache.InternalCache internalCache;

   public ResponseMetadataCache(int maxEntries) {
      this.internalCache = new ResponseMetadataCache.InternalCache(maxEntries);
   }

   @Override
   public synchronized void add(Object obj, ResponseMetadata metadata) {
      if (obj != null) {
         this.internalCache.put(Integer.valueOf(System.identityHashCode(obj)), metadata);
      }
   }

   @Override
   public synchronized ResponseMetadata get(Object obj) {
      return this.internalCache.get(Integer.valueOf(System.identityHashCode(obj)));
   }

   private static final class InternalCache extends LinkedHashMap<Integer, ResponseMetadata> {
      private static final long serialVersionUID = 1L;
      private int maxSize;

      InternalCache(int maxSize) {
         super(maxSize);
         this.maxSize = maxSize;
      }

      @Override
      protected boolean removeEldestEntry(Entry<Integer, ResponseMetadata> eldest) {
         return this.size() > this.maxSize;
      }
   }
}
