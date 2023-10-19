package com.amazonaws.cache;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface Cache<K, V> {
   V get(K var1);

   void put(K var1, V var2);
}
