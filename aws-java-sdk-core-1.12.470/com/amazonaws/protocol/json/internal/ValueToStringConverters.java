package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.Base64;
import com.amazonaws.util.StringUtils;
import java.nio.charset.Charset;
import java.util.Date;

@SdkInternalApi
public class ValueToStringConverters {
   public static final ValueToStringConverters.ValueToString<String> FROM_STRING = new ValueToStringConverters.ValueToString<String>() {
      public String convert(String val) {
         return val;
      }
   };
   public static final ValueToStringConverters.ValueToString<Integer> FROM_INTEGER = new ValueToStringConverters.ValueToString<Integer>() {
      public String convert(Integer val) {
         return StringUtils.fromInteger(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Long> FROM_LONG = new ValueToStringConverters.ValueToString<Long>() {
      public String convert(Long val) {
         return StringUtils.fromLong(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Short> FROM_SHORT = new ValueToStringConverters.ValueToString<Short>() {
      public String convert(Short val) {
         return StringUtils.fromShort(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Float> FROM_FLOAT = new ValueToStringConverters.ValueToString<Float>() {
      public String convert(Float val) {
         return StringUtils.fromFloat(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Double> FROM_DOUBLE = new ValueToStringConverters.ValueToString<Double>() {
      public String convert(Double val) {
         return StringUtils.fromDouble(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Boolean> FROM_BOOLEAN = new ValueToStringConverters.ValueToString<Boolean>() {
      public String convert(Boolean val) {
         return StringUtils.fromBoolean(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<Date> FROM_DATE = new ValueToStringConverters.ValueToString<Date>() {
      public String convert(Date val) {
         return StringUtils.fromDate(val);
      }
   };
   public static final ValueToStringConverters.ValueToString<String> FROM_JSON_VALUE_HEADER = new ValueToStringConverters.ValueToString<String>() {
      public String convert(String val) {
         return Base64.encodeAsString(val.getBytes(Charset.forName("utf-8")));
      }
   };

   public interface ValueToString<T> {
      String convert(T var1);
   }
}
