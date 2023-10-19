package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.transform.JsonUnmarshallerContext;
import com.amazonaws.transform.SimpleTypeJsonUnmarshallers;
import com.amazonaws.transform.Unmarshaller;
import com.amazonaws.util.ImmutableMapParameter;
import com.fasterxml.jackson.core.JsonFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;

@SdkProtectedApi
public class SdkStructuredPlainJsonFactory {
   public static final JsonFactory JSON_FACTORY = new JsonFactory();
   @SdkTestInternalApi
   public static final Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> JSON_SCALAR_UNMARSHALLERS = new ImmutableMapParameter.Builder<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>>(
         
      )
      .put(String.class, SimpleTypeJsonUnmarshallers.StringJsonUnmarshaller.getInstance())
      .put(Double.class, SimpleTypeJsonUnmarshallers.DoubleJsonUnmarshaller.getInstance())
      .put(Integer.class, SimpleTypeJsonUnmarshallers.IntegerJsonUnmarshaller.getInstance())
      .put(BigInteger.class, SimpleTypeJsonUnmarshallers.BigIntegerJsonUnmarshaller.getInstance())
      .put(BigDecimal.class, SimpleTypeJsonUnmarshallers.BigDecimalJsonUnmarshaller.getInstance())
      .put(Boolean.class, SimpleTypeJsonUnmarshallers.BooleanJsonUnmarshaller.getInstance())
      .put(Float.class, SimpleTypeJsonUnmarshallers.FloatJsonUnmarshaller.getInstance())
      .put(Long.class, SimpleTypeJsonUnmarshallers.LongJsonUnmarshaller.getInstance())
      .put(Byte.class, SimpleTypeJsonUnmarshallers.ByteJsonUnmarshaller.getInstance())
      .put(Date.class, SimpleTypeJsonUnmarshallers.DateJsonUnmarshaller.getInstance())
      .put(ByteBuffer.class, SimpleTypeJsonUnmarshallers.ByteBufferJsonUnmarshaller.getInstance())
      .put(Character.class, SimpleTypeJsonUnmarshallers.CharacterJsonUnmarshaller.getInstance())
      .put(Short.class, SimpleTypeJsonUnmarshallers.ShortJsonUnmarshaller.getInstance())
      .build();
   @SdkTestInternalApi
   public static final Map<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>> JSON_CUSTOM_TYPE_UNMARSHALLERS = new ImmutableMapParameter.Builder<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>>(
         
      )
      .put(JsonUnmarshallerContext.UnmarshallerType.JSON_VALUE, SimpleTypeJsonUnmarshallers.JsonValueStringUnmarshaller.getInstance())
      .build();
   public static final SdkStructuredJsonFactory SDK_JSON_FACTORY = new SdkStructuredJsonFactoryImpl(
      JSON_FACTORY, JSON_SCALAR_UNMARSHALLERS, JSON_CUSTOM_TYPE_UNMARSHALLERS
   ) {
      @Override
      protected StructuredJsonGenerator createWriter(JsonFactory jsonFactory, String contentType) {
         return new SdkJsonGenerator(jsonFactory, contentType);
      }
   };
}
