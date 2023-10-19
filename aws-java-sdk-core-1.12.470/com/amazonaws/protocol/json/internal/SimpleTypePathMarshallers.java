package com.amazonaws.protocol.json.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.transform.PathMarshallers;

@SdkInternalApi
public class SimpleTypePathMarshallers {
   public static final JsonMarshaller<String> STRING = new SimpleTypePathMarshallers.SimplePathMarshaller<>(
      ValueToStringConverters.FROM_STRING, PathMarshallers.NON_GREEDY
   );
   public static final JsonMarshaller<Integer> INTEGER = new SimpleTypePathMarshallers.SimplePathMarshaller<>(
      ValueToStringConverters.FROM_INTEGER, PathMarshallers.NON_GREEDY
   );
   public static final JsonMarshaller<Long> LONG = new SimpleTypePathMarshallers.SimplePathMarshaller<>(
      ValueToStringConverters.FROM_LONG, PathMarshallers.NON_GREEDY
   );
   public static final JsonMarshaller<String> GREEDY_STRING = new SimpleTypePathMarshallers.SimplePathMarshaller<>(
      ValueToStringConverters.FROM_STRING, PathMarshallers.GREEDY
   );
   public static final JsonMarshaller<Void> NULL = new JsonMarshaller<Void>() {
      public void marshall(Void val, JsonMarshallerContext context, MarshallingInfo<Void> marshallingInfo) {
         throw new IllegalArgumentException(String.format("Parameter '%s' must not be null", marshallingInfo.marshallLocationName()));
      }
   };

   private static class SimplePathMarshaller<T> implements JsonMarshaller<T> {
      private final ValueToStringConverters.ValueToString<T> converter;
      private final PathMarshallers.PathMarshaller pathMarshaller;

      private SimplePathMarshaller(ValueToStringConverters.ValueToString<T> converter, PathMarshallers.PathMarshaller pathMarshaller) {
         this.converter = converter;
         this.pathMarshaller = pathMarshaller;
      }

      @Override
      public void marshall(T val, JsonMarshallerContext context, MarshallingInfo<T> marshallingInfo) {
         context.request()
            .setResourcePath(
               this.pathMarshaller.marshall(context.request().getResourcePath(), marshallingInfo.marshallLocationName(), this.converter.convert(val))
            );
      }
   }
}
