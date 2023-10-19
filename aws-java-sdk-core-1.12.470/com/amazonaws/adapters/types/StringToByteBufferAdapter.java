package com.amazonaws.adapters.types;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.StringUtils;
import java.nio.ByteBuffer;

@SdkProtectedApi
public class StringToByteBufferAdapter implements TypeAdapter<String, ByteBuffer> {
   public ByteBuffer adapt(String source) {
      return source == null ? null : ByteBuffer.wrap(source.getBytes(StringUtils.UTF8));
   }
}
