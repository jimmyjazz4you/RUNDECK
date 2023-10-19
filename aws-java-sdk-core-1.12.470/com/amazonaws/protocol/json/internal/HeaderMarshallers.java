package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.TimestampFormat;
import java.util.Date;

@SdkInternalApi
public class HeaderMarshallers {
   public static final JsonMarshaller<String> STRING = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_STRING);
   public static final JsonMarshaller<String> JSON_VALUE = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_JSON_VALUE_HEADER);
   public static final JsonMarshaller<Integer> INTEGER = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_INTEGER);
   public static final JsonMarshaller<Long> LONG = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_LONG);
   public static final JsonMarshaller<Double> DOUBLE = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_DOUBLE);
   public static final JsonMarshaller<Float> FLOAT = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_FLOAT);
   public static final JsonMarshaller<Boolean> BOOLEAN = new HeaderMarshallers.SimpleHeaderMarshaller<>(ValueToStringConverters.FROM_BOOLEAN);
   public static final JsonMarshaller<Date> DATE = new HeaderMarshallers.SimpleHeaderMarshaller<Date>(ValueToStringConverters.FROM_DATE) {
      public void marshall(Date val, JsonMarshallerContext context, MarshallingInfo<Date> marshallingInfo) {
         TimestampFormat timestampFormat = marshallingInfo.timestampFormat();
         if (TimestampFormat.UNKNOWN.equals(timestampFormat)) {
            timestampFormat = TimestampFormat.ISO_8601;
         }

         context.request().addHeader(marshallingInfo.marshallLocationName(), StringUtils.fromDate(val, timestampFormat.getFormat()));
      }
   };

   private static class SimpleHeaderMarshaller<T> implements JsonMarshaller<T> {
      private final ValueToStringConverters.ValueToString<T> converter;

      private SimpleHeaderMarshaller(ValueToStringConverters.ValueToString<T> converter) {
         this.converter = converter;
      }

      @Override
      public void marshall(T val, JsonMarshallerContext context, MarshallingInfo<T> marshallingInfo) {
         context.request().addHeader(marshallingInfo.marshallLocationName(), this.converter.convert(val));
      }
   }
}
