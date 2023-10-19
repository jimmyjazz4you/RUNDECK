package com.amazonaws.http.conn;

import com.amazonaws.http.apache.utils.HttpContextUtils;
import java.io.IOException;
import java.net.Proxy;
import java.net.Socket;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

public class SdkPlainSocketFactory extends PlainConnectionSocketFactory {
   public Socket createSocket(HttpContext ctx) throws IOException {
      return HttpContextUtils.disableSocketProxy(ctx) ? new Socket(Proxy.NO_PROXY) : super.createSocket(ctx);
   }
}
