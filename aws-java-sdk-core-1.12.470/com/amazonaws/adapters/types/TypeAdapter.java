package com.amazonaws.adapters.types;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface TypeAdapter<Source, Destination> {
   Destination adapt(Source var1);
}
