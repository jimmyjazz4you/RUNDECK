package com.amazonaws.cache;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface CacheLoader<K, V> {
   V load(K var1);
}
