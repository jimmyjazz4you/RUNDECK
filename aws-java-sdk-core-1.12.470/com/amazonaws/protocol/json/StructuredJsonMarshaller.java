package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface StructuredJsonMarshaller<T> {
   void marshall(T var1, StructuredJsonGenerator var2);
}
