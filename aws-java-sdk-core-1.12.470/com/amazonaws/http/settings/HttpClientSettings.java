package com.amazonaws.http.settings;

import com.amazonaws.ApacheHttpClientConfig;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DnsResolver;
import com.amazonaws.Protocol;
import com.amazonaws.ProxyAuthenticationMethod;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.TlsKeyManagersProvider;
import com.amazonaws.util.ValidationUtils;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.List;

@SdkInternalApi
public class HttpClientSettings {
   private final ClientConfiguration config;
   private final boolean useBrowserCompatibleHostNameVerifier;
   private final boolean calculateCRC32FromCompressedData;

   HttpClientSettings(ClientConfiguration config, boolean useBrowserCompatibleHostNameVerifier, boolean calculateCRC32FromCompressedData) {
      this.config = ValidationUtils.assertNotNull(config, "client configuration");
      this.useBrowserCompatibleHostNameVerifier = useBrowserCompatibleHostNameVerifier;
      this.calculateCRC32FromCompressedData = calculateCRC32FromCompressedData;
   }

   public static HttpClientSettings adapt(ClientConfiguration config, boolean useBrowserCompatibleHostNameVerifier, boolean calculateCRC32FromCompressedData) {
      return new HttpClientSettings(config, useBrowserCompatibleHostNameVerifier, calculateCRC32FromCompressedData);
   }

   public static HttpClientSettings adapt(ClientConfiguration config, boolean useBrowserCompatibleHostNameVerifier) {
      return adapt(config, useBrowserCompatibleHostNameVerifier, false);
   }

   public static HttpClientSettings adapt(ClientConfiguration config) {
      return adapt(config, false);
   }

   public boolean useBrowserCompatibleHostNameVerifier() {
      return this.useBrowserCompatibleHostNameVerifier;
   }

   public boolean calculateCRC32FromCompressedData() {
      return this.calculateCRC32FromCompressedData;
   }

   public int getMaxConnections() {
      return this.config.getMaxConnections();
   }

   public InetAddress getLocalAddress() {
      return this.config.getLocalAddress();
   }

   public String getProxyHost() {
      return this.config.getProxyHost();
   }

   public int getProxyPort() {
      return this.config.getProxyPort();
   }

   public String getProxyUsername() {
      return this.config.getProxyUsername();
   }

   public String getProxyPassword() {
      return this.config.getProxyPassword();
   }

   public String getNonProxyHosts() {
      return this.config.getNonProxyHosts();
   }

   public List<ProxyAuthenticationMethod> getProxyAuthenticationMethods() {
      return this.config.getProxyAuthenticationMethods();
   }

   public boolean useReaper() {
      return this.config.useReaper();
   }

   public boolean useGzip() {
      return this.config.useGzip();
   }

   public DnsResolver getDnsResolver() {
      return this.config.getDnsResolver();
   }

   public ApacheHttpClientConfig getApacheHttpClientConfig() {
      return this.config.getApacheHttpClientConfig();
   }

   public int getSocketTimeout() {
      return this.config.getSocketTimeout();
   }

   public int[] getSocketBufferSize() {
      return this.config.getSocketBufferSizeHints();
   }

   public boolean useTcpKeepAlive() {
      return this.config.useTcpKeepAlive();
   }

   public SecureRandom getSecureRandom() {
      return this.config.getSecureRandom();
   }

   public int getConnectionTimeout() {
      return this.config.getConnectionTimeout();
   }

   public int getConnectionPoolRequestTimeout() {
      return this.config.getConnectionTimeout();
   }

   public long getConnectionPoolTTL() {
      return this.config.getConnectionTTL();
   }

   public long getMaxIdleConnectionTime() {
      return this.config.getConnectionMaxIdleMillis();
   }

   public int getValidateAfterInactivityMillis() {
      return this.config.getValidateAfterInactivityMillis();
   }

   public String getProxyWorkstation() {
      return this.config.getProxyWorkstation();
   }

   public String getProxyDomain() {
      return this.config.getProxyDomain();
   }

   public boolean isPreemptiveBasicProxyAuth() {
      return this.config.isPreemptiveBasicProxyAuth();
   }

   public boolean isUseExpectContinue() {
      return this.config.isUseExpectContinue();
   }

   public boolean isProxyEnabled() {
      return this.getProxyHost() != null && this.getProxyPort() > 0;
   }

   public boolean disableSocketProxy() {
      return this.config.disableSocketProxy();
   }

   public boolean isAuthenticatedProxy() {
      return this.getProxyUsername() != null && this.getProxyPassword() != null;
   }

   public Protocol getProxyProtocol() {
      return this.config.getProxyProtocol();
   }

   public TlsKeyManagersProvider getTlsKeyMangersProvider() {
      return this.config.getTlsKeyManagersProvider();
   }
}
