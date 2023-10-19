package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallLocation;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.TimestampFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SdkInternalApi
public class QueryParamMarshallers {
   public static final JsonMarshaller<String> STRING = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_STRING);
   public static final JsonMarshaller<Integer> INTEGER = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_INTEGER);
   public static final JsonMarshaller<Long> LONG = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_LONG);
   public static final JsonMarshaller<Short> SHORT = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_SHORT);
   public static final JsonMarshaller<Double> DOUBLE = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_DOUBLE);
   public static final JsonMarshaller<Float> FLOAT = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_FLOAT);
   public static final JsonMarshaller<Boolean> BOOLEAN = new QueryParamMarshallers.SimpleQueryParamMarshaller<>(ValueToStringConverters.FROM_BOOLEAN);
   public static final JsonMarshaller<Date> DATE = new QueryParamMarshallers.SimpleQueryParamMarshaller<Date>(ValueToStringConverters.FROM_DATE) {
      public void marshall(Date val, JsonMarshallerContext context, MarshallingInfo<Date> marshallingInfo) {
         TimestampFormat timestampFormat = marshallingInfo.timestampFormat();
         context.request().addParameter(marshallingInfo.marshallLocationName(), StringUtils.fromDate(val, timestampFormat.getFormat()));
      }
   };
   public static final JsonMarshaller<List> LIST = new JsonMarshaller<List>() {
      public void marshall(List list, JsonMarshallerContext context, MarshallingInfo<List> marshallingInfo) {
         for(Object listVal : list) {
            context.marshall(MarshallLocation.QUERY_PARAM, listVal, marshallingInfo);
         }
      }
   };
   public static final JsonMarshaller<Map> MAP = new JsonMarshaller<Map>() {
      public void marshall(Map val, JsonMarshallerContext context, MarshallingInfo<Map> mapMarshallingInfo) {
         for(Entry<String, ?> mapEntry : val.entrySet()) {
            context.marshall(MarshallLocation.QUERY_PARAM, mapEntry.getValue(), mapEntry.getKey());
         }
      }
   };

   private static class SimpleQueryParamMarshaller<T> implements JsonMarshaller<T> {
      private final ValueToStringConverters.ValueToString<T> converter;

      private SimpleQueryParamMarshaller(ValueToStringConverters.ValueToString<T> converter) {
         this.converter = converter;
      }

      @Override
      public void marshall(T val, JsonMarshallerContext context, MarshallingInfo<T> marshallingInfo) {
         context.request().addParameter(marshallingInfo.marshallLocationName(), this.converter.convert(val));
      }
   }
}
