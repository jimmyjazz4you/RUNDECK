package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.json.StructuredJsonGenerator;

@SdkInternalApi
public interface EmptyBodyJsonMarshaller {
   EmptyBodyJsonMarshaller NULL = new EmptyBodyJsonMarshaller() {
      @Override
      public void marshall(StructuredJsonGenerator generator) {
         generator.writeNull();
      }
   };
   EmptyBodyJsonMarshaller EMPTY = new EmptyBodyJsonMarshaller() {
      @Override
      public void marshall(StructuredJsonGenerator generator) {
         generator.writeStartObject();
         generator.writeEndObject();
      }
   };

   void marshall(StructuredJsonGenerator var1);
}
