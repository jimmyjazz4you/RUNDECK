package com.amazonaws.http.conn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.pool.ConnPoolControl;

public class ClientConnectionManagerFactory {
   private static final Log log = LogFactory.getLog(ClientConnectionManagerFactory.class);

   public static HttpClientConnectionManager wrap(HttpClientConnectionManager orig) {
      if (orig instanceof Wrapped) {
         throw new IllegalArgumentException();
      } else {
         Class<?>[] interfaces;
         if (orig instanceof ConnPoolControl) {
            interfaces = new Class[]{HttpClientConnectionManager.class, ConnPoolControl.class, Wrapped.class};
         } else {
            interfaces = new Class[]{HttpClientConnectionManager.class, Wrapped.class};
         }

         return (HttpClientConnectionManager)Proxy.newProxyInstance(
            ClientConnectionManagerFactory.class.getClassLoader(), interfaces, new ClientConnectionManagerFactory.Handler(orig)
         );
      }
   }

   private static class Handler implements InvocationHandler {
      private final HttpClientConnectionManager orig;

      Handler(HttpClientConnectionManager real) {
         this.orig = real;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         try {
            Object ret = method.invoke(this.orig, args);
            return ret instanceof ConnectionRequest ? ClientConnectionRequestFactory.wrap((ConnectionRequest)ret) : ret;
         } catch (InvocationTargetException var5) {
            ClientConnectionManagerFactory.log.debug("", var5);
            throw var5.getCause();
         }
      }
   }
}
