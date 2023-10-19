package com.amazonaws.http.apache.utils;

import com.amazonaws.annotation.SdkInternalApi;
import org.apache.http.protocol.HttpContext;

@SdkInternalApi
public final class HttpContextUtils {
   public static final String DISABLE_SOCKET_PROXY_PROPERTY = "com.amazonaws.disableSocketProxy";

   private HttpContextUtils() {
   }

   public static boolean disableSocketProxy(HttpContext ctx) {
      Object v = ctx.getAttribute("com.amazonaws.disableSocketProxy");
      return v != null && (Boolean)v;
   }
}
