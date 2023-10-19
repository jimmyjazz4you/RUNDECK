package com.amazonaws.http.conn;

import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.metrics.ServiceLatencyProvider;
import com.amazonaws.metrics.ServiceMetricCollector;
import com.amazonaws.util.AWSServiceMetrics;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ConnectionRequest;

class ClientConnectionRequestFactory {
   private static final Log log = LogFactory.getLog(ClientConnectionRequestFactory.class);
   private static final Class<?>[] interfaces = new Class[]{ConnectionRequest.class, Wrapped.class};

   static ConnectionRequest wrap(ConnectionRequest orig) {
      if (orig instanceof Wrapped) {
         throw new IllegalArgumentException();
      } else {
         return (ConnectionRequest)Proxy.newProxyInstance(
            ClientConnectionRequestFactory.class.getClassLoader(), interfaces, new ClientConnectionRequestFactory.Handler(orig)
         );
      }
   }

   private static class Handler implements InvocationHandler {
      private final ConnectionRequest orig;

      Handler(ConnectionRequest orig) {
         this.orig = orig;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         try {
            if ("get".equals(method.getName())) {
               ServiceLatencyProvider latencyProvider = new ServiceLatencyProvider(AWSServiceMetrics.HttpClientGetConnectionTime);

               Object var5;
               try {
                  var5 = method.invoke(this.orig, args);
               } finally {
                  AwsSdkMetrics.<ServiceMetricCollector>getServiceMetricCollector().collectLatency(latencyProvider.endTiming());
               }

               return var5;
            } else {
               return method.invoke(this.orig, args);
            }
         } catch (InvocationTargetException var10) {
            ClientConnectionRequestFactory.log.debug("", var10);
            throw var10.getCause();
         }
      }
   }
}
