package com.amazonaws.adapters.types;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.StringInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@SdkProtectedApi
public class StringToInputStreamAdapter implements TypeAdapter<String, InputStream> {
   public InputStream adapt(String source) {
      if (source == null) {
         return null;
      } else {
         try {
            return new StringInputStream(source);
         } catch (UnsupportedEncodingException var3) {
            throw new SdkClientException(var3);
         }
      }
   }
}
