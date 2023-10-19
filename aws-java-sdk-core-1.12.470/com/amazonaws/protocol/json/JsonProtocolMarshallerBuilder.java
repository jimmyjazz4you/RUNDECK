package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingType;
import com.amazonaws.protocol.OperationInfo;
import com.amazonaws.protocol.ProtocolRequestMarshaller;
import com.amazonaws.protocol.json.internal.EmptyBodyJsonMarshaller;
import com.amazonaws.protocol.json.internal.JsonProtocolMarshaller;
import com.amazonaws.protocol.json.internal.MarshallerRegistry;
import com.amazonaws.protocol.json.internal.SimpleTypeJsonMarshallers;

@SdkProtectedApi
public class JsonProtocolMarshallerBuilder<T> {
   private StructuredJsonGenerator jsonGenerator;
   private String contentType;
   private OperationInfo operationInfo;
   private T originalRequest;
   private MarshallerRegistry.Builder marshallerRegistry;
   private EmptyBodyJsonMarshaller emptyBodyMarshaller;

   public static <T> JsonProtocolMarshallerBuilder<T> standard() {
      return new JsonProtocolMarshallerBuilder<>();
   }

   public JsonProtocolMarshallerBuilder<T> jsonGenerator(StructuredJsonGenerator jsonGenerator) {
      this.jsonGenerator = jsonGenerator;
      return this;
   }

   public JsonProtocolMarshallerBuilder<T> contentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   public JsonProtocolMarshallerBuilder<T> operationInfo(OperationInfo operationInfo) {
      this.operationInfo = operationInfo;
      return this;
   }

   public JsonProtocolMarshallerBuilder<T> originalRequest(T originalRequest) {
      this.originalRequest = originalRequest;
      return this;
   }

   @Deprecated
   public JsonProtocolMarshallerBuilder<T> sendExplicitNullForPayload(boolean sendExplicitNullForPayload) {
      return this;
   }

   public JsonProtocolMarshallerBuilder<T> emptyBodyMarshaller(EmptyBodyJsonMarshaller emptyBodyMarshaller) {
      this.emptyBodyMarshaller = emptyBodyMarshaller;
      return this;
   }

   public <MarshallT> JsonProtocolMarshallerBuilder<T> marshallerOverride(
      MarshallLocation marshallLocation, MarshallingType<MarshallT> marshallingType, StructuredJsonMarshaller<MarshallT> marshaller
   ) {
      if (this.marshallerRegistry == null) {
         this.marshallerRegistry = MarshallerRegistry.builder();
      }

      this.marshallerRegistry.addMarshaller(marshallLocation, marshallingType, SimpleTypeJsonMarshallers.adapt(marshaller));
      return this;
   }

   public ProtocolRequestMarshaller<T> build() {
      return new JsonProtocolMarshaller<>(
         this.jsonGenerator, this.contentType, this.operationInfo, this.originalRequest, this.marshallerRegistry, this.emptyBodyMarshaller
      );
   }
}
