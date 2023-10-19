package com.amazonaws.protocol.json.internal;

import com.amazonaws.Request;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.protocol.MarshallingType;
import com.amazonaws.protocol.ProtocolMarshaller;
import com.amazonaws.protocol.json.StructuredJsonGenerator;

@SdkInternalApi
public class JsonMarshallerContext {
   private final StructuredJsonGenerator jsonGenerator;
   private final JsonProtocolMarshaller protocolHandler;
   private final MarshallerRegistry marshallerRegistry;
   private final Request<?> request;
   private final EmptyBodyJsonMarshaller emptyBodyMarshaller;

   private JsonMarshallerContext(JsonMarshallerContext.Builder builder) {
      this.jsonGenerator = builder.jsonGenerator;
      this.protocolHandler = builder.protocolHandler;
      this.marshallerRegistry = builder.marshallerRegistry;
      this.request = builder.request;
      this.emptyBodyMarshaller = builder.emptyBodyMarshaller != null ? builder.emptyBodyMarshaller : EmptyBodyJsonMarshaller.EMPTY;
   }

   public StructuredJsonGenerator jsonGenerator() {
      return this.jsonGenerator;
   }

   public ProtocolMarshaller protocolHandler() {
      return this.protocolHandler;
   }

   public MarshallerRegistry marshallerRegistry() {
      return this.marshallerRegistry;
   }

   public Request<?> request() {
      return this.request;
   }

   public EmptyBodyJsonMarshaller emptyBodyJsonMarshaller() {
      return this.emptyBodyMarshaller;
   }

   public void marshall(MarshallLocation marshallLocation, Object val) {
      this.marshallerRegistry().getMarshaller(marshallLocation, val).marshall(val, this, null);
   }

   public void marshall(MarshallLocation marshallLocation, Object val, MarshallingInfo marshallingInfo) {
      this.marshallerRegistry().getMarshaller(marshallLocation, val).marshall(val, this, marshallingInfo);
   }

   public void marshall(MarshallLocation marshallLocation, Object val, String paramName) {
      this.marshallerRegistry().getMarshaller(marshallLocation, val).marshall(val, this, MarshallingInfo.builder(new MarshallingType<Object>() {
         @Override
         public boolean isDefaultMarshallerForType(Class<?> type) {
            return false;
         }
      }).marshallLocationName(paramName).build());
   }

   public static JsonMarshallerContext.Builder builder() {
      return new JsonMarshallerContext.Builder();
   }

   public static final class Builder {
      private StructuredJsonGenerator jsonGenerator;
      private JsonProtocolMarshaller protocolHandler;
      private MarshallerRegistry marshallerRegistry;
      private Request<?> request;
      private EmptyBodyJsonMarshaller emptyBodyMarshaller;

      private Builder() {
      }

      public JsonMarshallerContext.Builder jsonGenerator(StructuredJsonGenerator jsonGenerator) {
         this.jsonGenerator = jsonGenerator;
         return this;
      }

      public JsonMarshallerContext.Builder protocolHandler(JsonProtocolMarshaller protocolHandler) {
         this.protocolHandler = protocolHandler;
         return this;
      }

      public JsonMarshallerContext.Builder marshallerRegistry(MarshallerRegistry marshallerRegistry) {
         this.marshallerRegistry = marshallerRegistry;
         return this;
      }

      public JsonMarshallerContext.Builder request(Request<?> request) {
         this.request = request;
         return this;
      }

      public JsonMarshallerContext.Builder emptyBodyJsonMarshaller(EmptyBodyJsonMarshaller emptyBodyJsonMarshaller) {
         this.emptyBodyMarshaller = emptyBodyJsonMarshaller;
         return this;
      }

      public JsonMarshallerContext build() {
         return new JsonMarshallerContext(this);
      }
   }
}
