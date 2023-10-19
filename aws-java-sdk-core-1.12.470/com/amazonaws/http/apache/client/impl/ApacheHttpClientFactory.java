package com.amazonaws.http.apache.client.impl;

import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.IdleConnectionReaper;
import com.amazonaws.http.apache.SdkProxyRoutePlanner;
import com.amazonaws.http.apache.utils.ApacheUtils;
import com.amazonaws.http.client.ConnectionManagerFactory;
import com.amazonaws.http.client.HttpClientFactory;
import com.amazonaws.http.conn.ClientConnectionManagerFactory;
import com.amazonaws.http.conn.SdkConnectionKeepAliveStrategy;
import com.amazonaws.http.protocol.SdkHttpRequestExecutor;
import com.amazonaws.http.settings.HttpClientSettings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class ApacheHttpClientFactory implements HttpClientFactory<ConnectionManagerAwareHttpClient> {
   private static final Log LOG = LogFactory.getLog(AmazonHttpClient.class);
   private final ConnectionManagerFactory<HttpClientConnectionManager> cmFactory = new ApacheConnectionManagerFactory();

   public ConnectionManagerAwareHttpClient create(HttpClientSettings settings) {
      HttpClientBuilder builder = HttpClients.custom();
      HttpClientConnectionManager cm = (HttpClientConnectionManager)this.cmFactory.create(settings);
      builder.setRequestExecutor(new SdkHttpRequestExecutor())
         .setKeepAliveStrategy(this.buildKeepAliveStrategy(settings))
         .disableRedirectHandling()
         .disableAutomaticRetries()
         .setConnectionManager(ClientConnectionManagerFactory.wrap(cm));
      if (!settings.useGzip()) {
         builder.disableContentCompression();
      }

      HttpResponseInterceptor itcp = new CRC32ChecksumResponseInterceptor();
      if (settings.calculateCRC32FromCompressedData()) {
         builder.addInterceptorFirst(itcp);
      } else {
         builder.addInterceptorLast(itcp);
      }

      this.addProxyConfig(builder, settings);
      ConnectionManagerAwareHttpClient httpClient = new SdkHttpClient(builder.build(), cm);
      if (settings.useReaper()) {
         IdleConnectionReaper.registerConnectionManager(cm, settings.getMaxIdleConnectionTime());
      }

      return httpClient;
   }

   private void addProxyConfig(HttpClientBuilder builder, HttpClientSettings settings) {
      if (settings.isProxyEnabled()) {
         LOG.info("Configuring Proxy. Proxy Host: " + settings.getProxyHost() + " Proxy Port: " + settings.getProxyPort());
         builder.setRoutePlanner(
            new SdkProxyRoutePlanner(settings.getProxyHost(), settings.getProxyPort(), settings.getProxyProtocol(), settings.getNonProxyHosts())
         );
         if (settings.isAuthenticatedProxy()) {
            builder.setDefaultCredentialsProvider(ApacheUtils.newProxyCredentialsProvider(settings));
         }
      }
   }

   private ConnectionKeepAliveStrategy buildKeepAliveStrategy(HttpClientSettings settings) {
      return settings.getMaxIdleConnectionTime() > 0L ? new SdkConnectionKeepAliveStrategy(settings.getMaxIdleConnectionTime()) : null;
   }
}
