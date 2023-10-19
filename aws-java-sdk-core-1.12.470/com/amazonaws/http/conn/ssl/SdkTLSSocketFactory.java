package com.amazonaws.http.conn.ssl;

import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.http.apache.utils.HttpContextUtils;
import com.amazonaws.internal.SdkMetricsSocket;
import com.amazonaws.internal.SdkSSLMetricsSocket;
import com.amazonaws.internal.SdkSSLSocket;
import com.amazonaws.internal.SdkSocket;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.util.JavaVersionParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class SdkTLSSocketFactory extends SSLConnectionSocketFactory {
   private static final Log LOG = LogFactory.getLog(SdkTLSSocketFactory.class);
   private final SSLContext sslContext;
   private final MasterSecretValidators.MasterSecretValidator masterSecretValidator;
   private final ShouldClearSslSessionPredicate shouldClearSslSessionsPredicate;

   public SdkTLSSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
      super(sslContext, hostnameVerifier);
      if (sslContext == null) {
         throw new IllegalArgumentException("sslContext must not be null. Use SSLContext.getDefault() if you are unsure.");
      } else {
         this.sslContext = sslContext;
         this.masterSecretValidator = MasterSecretValidators.getMasterSecretValidator();
         this.shouldClearSslSessionsPredicate = new ShouldClearSslSessionPredicate(JavaVersionParser.getCurrentJavaVersion());
      }
   }

   public Socket createSocket(HttpContext ctx) throws IOException {
      return HttpContextUtils.disableSocketProxy(ctx) ? new Socket(Proxy.NO_PROXY) : super.createSocket(ctx);
   }

   protected final void prepareSocket(SSLSocket socket) {
      String[] supported = socket.getSupportedProtocols();
      String[] enabled = socket.getEnabledProtocols();
      if (LOG.isDebugEnabled()) {
         LOG.debug(
            "socket.getSupportedProtocols(): " + Arrays.toString((Object[])supported) + ", socket.getEnabledProtocols(): " + Arrays.toString((Object[])enabled)
         );
      }

      List<String> target = new ArrayList<>();
      if (supported != null) {
         TLSProtocol[] values = TLSProtocol.values();

         for(int i = 0; i < values.length; ++i) {
            String pname = values[i].getProtocolName();
            if (this.existsIn(pname, supported)) {
               target.add(pname);
            }
         }
      }

      if (enabled != null) {
         for(String pname : enabled) {
            if (!target.contains(pname)) {
               target.add(pname);
            }
         }
      }

      if (target.size() > 0) {
         String[] enabling = target.toArray(new String[target.size()]);
         socket.setEnabledProtocols(enabling);
         if (LOG.isDebugEnabled()) {
            LOG.debug("TLS protocol enabled for SSL handshake: " + Arrays.toString((Object[])enabling));
         }
      }
   }

   private boolean existsIn(String element, String[] a) {
      for(String s : a) {
         if (element.equals(s)) {
            return true;
         }
      }

      return false;
   }

   public Socket connectSocket(
      int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context
   ) throws IOException {
      if (LOG.isDebugEnabled()) {
         LOG.debug("connecting to " + remoteAddress.getAddress() + ":" + remoteAddress.getPort());
      }

      Socket connectedSocket;
      try {
         connectedSocket = super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
         if (!this.masterSecretValidator.isMasterSecretValid(connectedSocket)) {
            throw (IllegalStateException)this.log(new IllegalStateException("Invalid SSL master secret"));
         }
      } catch (SSLException var9) {
         if (this.shouldClearSslSessionsPredicate.test(var9)) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("connection failed due to SSL error, clearing TLS session cache", var9);
            }

            this.clearSessionCache(this.sslContext.getClientSessionContext(), remoteAddress);
         }

         throw var9;
      }

      if (connectedSocket instanceof SSLSocket) {
         SdkSSLSocket sslSocket = new SdkSSLSocket((SSLSocket)connectedSocket);
         return (Socket)(AwsSdkMetrics.isHttpSocketReadMetricEnabled() ? new SdkSSLMetricsSocket(sslSocket) : sslSocket);
      } else {
         SdkSocket sdkSocket = new SdkSocket(connectedSocket);
         return (Socket)(AwsSdkMetrics.isHttpSocketReadMetricEnabled() ? new SdkMetricsSocket(sdkSocket) : sdkSocket);
      }
   }

   private void clearSessionCache(SSLSessionContext sessionContext, InetSocketAddress remoteAddress) {
      String hostName = remoteAddress.getHostName();
      int port = remoteAddress.getPort();
      Enumeration<byte[]> ids = sessionContext.getIds();
      if (ids != null) {
         while(ids.hasMoreElements()) {
            byte[] id = (byte[])ids.nextElement();
            SSLSession session = sessionContext.getSession(id);
            if (session != null && session.getPeerHost() != null && session.getPeerHost().equalsIgnoreCase(hostName) && session.getPeerPort() == port) {
               session.invalidate();
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Invalidated session " + session);
               }
            }
         }
      }
   }

   private <T extends Throwable> T log(T t) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("", t);
      }

      return t;
   }
}
