package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;

@Deprecated
@SdkProtectedApi
public interface SdkJsonMarshallerFactory {
   StructuredJsonGenerator createGenerator();

   String getContentType();
}
