package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallingInfo;

@SdkInternalApi
public interface JsonMarshaller<T> {
   JsonMarshaller<Void> NULL = new JsonMarshaller<Void>() {
      public void marshall(Void val, JsonMarshallerContext context, MarshallingInfo marshallingInfo) {
      }
   };

   void marshall(T var1, JsonMarshallerContext var2, MarshallingInfo<T> var3);
}
