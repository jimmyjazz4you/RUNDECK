package com.amazonaws.http.apache.client.impl;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.DelegatingDnsResolver;
import com.amazonaws.http.SystemPropertyTlsKeyManagersProvider;
import com.amazonaws.http.TlsKeyManagersProvider;
import com.amazonaws.http.client.ConnectionManagerFactory;
import com.amazonaws.http.conn.SdkPlainSocketFactory;
import com.amazonaws.http.conn.ssl.SdkTLSSocketFactory;
import com.amazonaws.http.settings.HttpClientSettings;
import com.amazonaws.internal.SdkSSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

public class ApacheConnectionManagerFactory implements ConnectionManagerFactory<HttpClientConnectionManager> {
   private final Log LOG = LogFactory.getLog(AmazonHttpClient.class);

   public HttpClientConnectionManager create(HttpClientSettings settings) {
      ConnectionSocketFactory sslsf = this.getPreferredSocketFactory(settings);
      PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
         this.createSocketFactoryRegistry(sslsf),
         null,
         DefaultSchemePortResolver.INSTANCE,
         new DelegatingDnsResolver(settings.getDnsResolver()),
         settings.getConnectionPoolTTL(),
         TimeUnit.MILLISECONDS
      );
      cm.setValidateAfterInactivity(settings.getValidateAfterInactivityMillis());
      cm.setDefaultMaxPerRoute(settings.getMaxConnections());
      cm.setMaxTotal(settings.getMaxConnections());
      cm.setDefaultSocketConfig(this.buildSocketConfig(settings));
      cm.setDefaultConnectionConfig(this.buildConnectionConfig(settings));
      return cm;
   }

   private ConnectionSocketFactory getPreferredSocketFactory(HttpClientSettings settings) {
      ConnectionSocketFactory sslsf = settings.getApacheHttpClientConfig().getSslSocketFactory();
      return (ConnectionSocketFactory)(sslsf != null
         ? sslsf
         : new SdkTLSSocketFactory(
            SdkSSLContext.getPreferredSSLContext(this.getKeyManagers(settings), settings.getSecureRandom()), this.getHostNameVerifier(settings)
         ));
   }

   private SocketConfig buildSocketConfig(HttpClientSettings settings) {
      return SocketConfig.custom().setSoKeepAlive(settings.useTcpKeepAlive()).setSoTimeout(settings.getSocketTimeout()).setTcpNoDelay(true).build();
   }

   private ConnectionConfig buildConnectionConfig(HttpClientSettings settings) {
      int socketBufferSize = Math.max(settings.getSocketBufferSize()[0], settings.getSocketBufferSize()[1]);
      return socketBufferSize <= 0 ? null : ConnectionConfig.custom().setBufferSize(socketBufferSize).build();
   }

   private KeyManager[] getKeyManagers(HttpClientSettings settings) {
      TlsKeyManagersProvider provider = settings.getTlsKeyMangersProvider();
      if (provider == null) {
         provider = new SystemPropertyTlsKeyManagersProvider();
      }

      return provider.getKeyManagers();
   }

   private HostnameVerifier getHostNameVerifier(HttpClientSettings options) {
      return options.useBrowserCompatibleHostNameVerifier()
         ? SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
         : SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
   }

   private Registry<ConnectionSocketFactory> createSocketFactoryRegistry(ConnectionSocketFactory sslSocketFactory) {
      if (SDKGlobalConfiguration.isCertCheckingDisabled()) {
         if (this.LOG.isWarnEnabled()) {
            this.LOG.warn("SSL Certificate checking for endpoints has been explicitly disabled.");
         }

         sslSocketFactory = new ApacheConnectionManagerFactory.TrustingSocketFactory();
      }

      return RegistryBuilder.create().register("http", new SdkPlainSocketFactory()).register("https", sslSocketFactory).build();
   }

   private static class TrustingSocketFactory implements LayeredConnectionSocketFactory {
      private SSLContext sslcontext = null;

      private TrustingSocketFactory() {
      }

      private static SSLContext createSSLContext() throws IOException {
         try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new ApacheConnectionManagerFactory.TrustingX509TrustManager()}, null);
            return context;
         } catch (Exception var1) {
            throw new IOException(var1.getMessage(), var1);
         }
      }

      public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException, UnknownHostException {
         return this.getSSLContext().getSocketFactory().createSocket(socket, target, port, true);
      }

      public Socket createSocket(HttpContext context) throws IOException {
         return this.getSSLContext().getSocketFactory().createSocket();
      }

      public Socket connectSocket(
         int connectTimeout, Socket sock, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context
      ) throws IOException {
         SSLSocket sslsock = (SSLSocket)(sock != null ? sock : this.createSocket(context));
         if (localAddress != null) {
            sslsock.bind(localAddress);
         }

         sslsock.connect(remoteAddress, connectTimeout);
         return sslsock;
      }

      private SSLContext getSSLContext() throws IOException {
         if (this.sslcontext == null) {
            this.sslcontext = createSSLContext();
         }

         return this.sslcontext;
      }
   }

   private static class TrustingX509TrustManager implements X509TrustManager {
      private static final X509Certificate[] X509_CERTIFICATES = new X509Certificate[0];

      private TrustingX509TrustManager() {
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
         return X509_CERTIFICATES;
      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }
   }
}
