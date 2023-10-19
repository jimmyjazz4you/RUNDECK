package com.amazonaws.protocol;

import com.amazonaws.annotation.SdkProtectedApi;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SdkProtectedApi
public interface MarshallingType<T> {
   MarshallingType<Void> NULL = new DefaultMarshallingType<>(Void.class);
   MarshallingType<String> STRING = new DefaultMarshallingType<>(String.class);
   MarshallingType<Integer> INTEGER = new DefaultMarshallingType<>(Integer.class);
   MarshallingType<Long> LONG = new DefaultMarshallingType<>(Long.class);
   MarshallingType<Short> SHORT = new DefaultMarshallingType<>(Short.class);
   MarshallingType<Float> FLOAT = new DefaultMarshallingType<>(Float.class);
   MarshallingType<Double> DOUBLE = new DefaultMarshallingType<>(Double.class);
   MarshallingType<BigDecimal> BIG_DECIMAL = new DefaultMarshallingType<>(BigDecimal.class);
   MarshallingType<Boolean> BOOLEAN = new DefaultMarshallingType<>(Boolean.class);
   MarshallingType<Date> DATE = new DefaultMarshallingType<>(Date.class);
   MarshallingType<ByteBuffer> BYTE_BUFFER = new DefaultMarshallingType<>(ByteBuffer.class);
   MarshallingType<InputStream> STREAM = new DefaultMarshallingType<>(InputStream.class);
   MarshallingType<StructuredPojo> STRUCTURED = new DefaultMarshallingType<>(StructuredPojo.class);
   MarshallingType<List> LIST = new DefaultMarshallingType<>(List.class);
   MarshallingType<Map> MAP = new DefaultMarshallingType<>(Map.class);
   MarshallingType<String> JSON_VALUE = new MarshallingType<String>() {
      @Override
      public boolean isDefaultMarshallerForType(Class<?> type) {
         return false;
      }
   };

   boolean isDefaultMarshallerForType(Class<?> var1);
}
