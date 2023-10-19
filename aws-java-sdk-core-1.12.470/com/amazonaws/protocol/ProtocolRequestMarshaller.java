package com.amazonaws.protocol;

import com.amazonaws.Request;
import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public interface ProtocolRequestMarshaller<OrigRequest> extends ProtocolMarshaller {
   void startMarshalling();

   Request<OrigRequest> finishMarshalling();
}
