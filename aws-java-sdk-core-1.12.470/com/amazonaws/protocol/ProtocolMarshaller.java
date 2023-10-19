package com.amazonaws.protocol;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface ProtocolMarshaller {
   <T> void marshall(T var1, MarshallingInfo<T> var2) throws SdkClientException;
}
