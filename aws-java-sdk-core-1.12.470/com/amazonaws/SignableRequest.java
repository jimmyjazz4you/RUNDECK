package com.amazonaws;

import java.io.InputStream;

public interface SignableRequest<T> extends ImmutableRequest<T> {
   void addHeader(String var1, String var2);

   void addParameter(String var1, String var2);

   void setContent(InputStream var1);
}
