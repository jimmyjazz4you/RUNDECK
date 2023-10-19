package com.amazonaws.waiters;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface SdkFunction<Input, Output> {
   Output apply(Input var1);
}
