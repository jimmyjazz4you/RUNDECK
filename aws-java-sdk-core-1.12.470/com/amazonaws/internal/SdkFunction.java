package com.amazonaws.internal;

public interface SdkFunction<Input, Output> {
   Output apply(Input var1);
}
