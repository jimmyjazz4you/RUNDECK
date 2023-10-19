package com.amazonaws.util;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface MetadataCache {
   void add(Object var1, ResponseMetadata var2);

   ResponseMetadata get(Object var1);
}
