package com.amazonaws.protocol;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface DefaultValueSupplier<T> {
   T get();
}
