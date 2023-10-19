package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.internal.SdkInternalList;
import com.amazonaws.internal.SdkInternalMap;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.protocol.StructuredPojo;
import com.amazonaws.protocol.json.StructuredJsonGenerator;
import com.amazonaws.protocol.json.StructuredJsonMarshaller;
import com.amazonaws.util.TimestampFormat;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SdkInternalApi
public class SimpleTypeJsonMarshallers {
   public static final JsonMarshaller<Void> NULL = new JsonMarshaller<Void>() {
      public void marshall(Void val, JsonMarshallerContext context, MarshallingInfo<Void> marshallingInfo) {
         if (marshallingInfo == null) {
            context.jsonGenerator().writeNull();
         } else if (marshallingInfo.isExplicitPayloadMember()) {
            if (marshallingInfo.marshallLocationName() != null) {
               throw new IllegalStateException("Expected marshalling location name to be null if explicit member is null");
            }

            context.emptyBodyJsonMarshaller().marshall(context.jsonGenerator());
         }
      }
   };
   public static final JsonMarshaller<String> STRING = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<String>() {
      public void marshall(String val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<String> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Integer> INTEGER = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Integer>() {
      public void marshall(Integer val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Integer> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Long> LONG = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Long>() {
      public void marshall(Long val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Long> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Short> SHORT = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Short>() {
      public void marshall(Short val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Short> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Float> FLOAT = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Float>() {
      public void marshall(Float val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Float> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<BigDecimal> BIG_DECIMAL = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<BigDecimal>() {
      public void marshall(BigDecimal val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<BigDecimal> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Double> DOUBLE = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Double>() {
      public void marshall(Double val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Double> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Boolean> BOOLEAN = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Boolean>() {
      public void marshall(Boolean val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Boolean> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<Date> DATE = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Date>() {
      public void marshall(Date val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Date> marshallingInfo) {
         TimestampFormat timestampFormat = TimestampFormat.UNIX_TIMESTAMP;
         if (marshallingInfo != null && marshallingInfo.timestampFormat() != null) {
            timestampFormat = marshallingInfo.timestampFormat();
         }

         jsonGenerator.writeValue(val, timestampFormat);
      }
   };
   public static final JsonMarshaller<ByteBuffer> BYTE_BUFFER = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<ByteBuffer>() {
      public void marshall(ByteBuffer val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<ByteBuffer> marshallingInfo) {
         jsonGenerator.writeValue(val);
      }
   };
   public static final JsonMarshaller<StructuredPojo> STRUCTURED = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<StructuredPojo>() {
      public void marshall(
         StructuredPojo val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<StructuredPojo> marshallingInfo
      ) {
         jsonGenerator.writeStartObject();
         val.marshall(context.protocolHandler());
         jsonGenerator.writeEndObject();
      }
   };
   public static final JsonMarshaller<List> LIST = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<List>() {
      public void marshall(List list, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<List> marshallingInfo) {
         jsonGenerator.writeStartArray();

         for(Object listValue : list) {
            context.marshall(MarshallLocation.PAYLOAD, listValue);
         }

         jsonGenerator.writeEndArray();
      }

      protected boolean shouldEmit(List list) {
         return !list.isEmpty() || !(list instanceof SdkInternalList) || !((SdkInternalList)list).isAutoConstruct();
      }
   };
   public static final JsonMarshaller<Map> MAP = new SimpleTypeJsonMarshallers.BaseJsonMarshaller<Map>() {
      public void marshall(Map map, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<Map> mapMarshallingInfo) {
         jsonGenerator.writeStartObject();

         for(Entry<String, ?> entry : map.entrySet()) {
            if (entry.getValue() != null) {
               Object value = entry.getValue();
               jsonGenerator.writeFieldName(entry.getKey());
               context.marshall(MarshallLocation.PAYLOAD, value);
            }
         }

         jsonGenerator.writeEndObject();
      }

      protected boolean shouldEmit(Map map) {
         return !map.isEmpty() || !(map instanceof SdkInternalMap) || !((SdkInternalMap)map).isAutoConstruct();
      }
   };

   public static <T> JsonMarshaller<T> adapt(final StructuredJsonMarshaller<T> toAdapt) {
      return new SimpleTypeJsonMarshallers.BaseJsonMarshaller<T>() {
         @Override
         public void marshall(T val, StructuredJsonGenerator jsonGenerator, JsonMarshallerContext context, MarshallingInfo<T> marshallingInfo) {
            toAdapt.marshall(val, jsonGenerator);
         }
      };
   }

   private abstract static class BaseJsonMarshaller<T> implements JsonMarshaller<T> {
      private BaseJsonMarshaller() {
      }

      @Override
      public final void marshall(T val, JsonMarshallerContext context, MarshallingInfo<T> marshallingInfo) {
         if (this.shouldEmit(val)) {
            if (marshallingInfo != null && marshallingInfo.marshallLocationName() != null) {
               context.jsonGenerator().writeFieldName(marshallingInfo.marshallLocationName());
            }

            this.marshall(val, context.jsonGenerator(), context, marshallingInfo);
         }
      }

      public abstract void marshall(T var1, StructuredJsonGenerator var2, JsonMarshallerContext var3, MarshallingInfo<T> var4);

      protected boolean shouldEmit(T val) {
         return true;
      }
   }
}
