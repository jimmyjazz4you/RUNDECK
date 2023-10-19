package com.amazonaws;

import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.http.TlsKeyManagersProvider;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryMode;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.ValidationUtils;
import com.amazonaws.util.VersionInfoUtils;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@NotThreadSafe
public class ClientConfiguration {
   private static final Log log = LogFactory.getLog(ClientConfiguration.class);
   public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
   public static final int DEFAULT_SOCKET_TIMEOUT = 50000;
   public static final int DEFAULT_REQUEST_TIMEOUT = 0;
   public static final int DEFAULT_CLIENT_EXECUTION_TIMEOUT = 0;
   public static final boolean DEFAULT_DISABLE_SOCKET_PROXY = false;
   public static final int DEFAULT_MAX_CONNECTIONS = 50;
   public static final boolean DEFAULT_USE_EXPECT_CONTINUE = true;
   public static final String DEFAULT_USER_AGENT = VersionInfoUtils.getUserAgent();
   public static final RetryPolicy DEFAULT_RETRY_POLICY = PredefinedRetryPolicies.DEFAULT;
   public static final boolean DEFAULT_USE_REAPER = true;
   public static final boolean DEFAULT_USE_GZIP = false;
   public static final long DEFAULT_CONNECTION_TTL = -1L;
   public static final long DEFAULT_CONNECTION_MAX_IDLE_MILLIS = 60000L;
   public static final int DEFAULT_VALIDATE_AFTER_INACTIVITY_MILLIS = 5000;
   public static final boolean DEFAULT_TCP_KEEP_ALIVE = false;
   public static final boolean DEFAULT_THROTTLE_RETRIES = true;
   public static final boolean DEFAULT_CACHE_RESPONSE_METADATA = true;
   public static final int DEFAULT_RESPONSE_METADATA_CACHE_SIZE = 50;
   public static final int DEFAULT_MAX_CONSECUTIVE_RETRIES_BEFORE_THROTTLING = 100;
   private String userAgentPrefix = DEFAULT_USER_AGENT;
   private String userAgentSuffix;
   private int maxErrorRetry = -1;
   private RetryPolicy retryPolicy = DEFAULT_RETRY_POLICY;
   private InetAddress localAddress;
   private Protocol protocol = Protocol.HTTPS;
   private Protocol proxyProtocol = Protocol.HTTP;
   private String proxyHost = null;
   private int proxyPort = -1;
   private String proxyUsername = null;
   private String proxyPassword = null;
   private String proxyDomain = null;
   private String proxyWorkstation = null;
   private String nonProxyHosts = null;
   private List<ProxyAuthenticationMethod> proxyAuthenticationMethods = null;
   private boolean disableSocketProxy = false;
   private boolean preemptiveBasicProxyAuth;
   private int maxConnections = 50;
   private int socketTimeout = 50000;
   private int connectionTimeout = 10000;
   private int requestTimeout = 0;
   private int clientExecutionTimeout = 0;
   private boolean throttleRetries = true;
   private int socketSendBufferSizeHint = 0;
   private int socketReceiveBufferSizeHint = 0;
   private boolean useReaper = true;
   private boolean useGzip = false;
   private String signerOverride;
   private long connectionTTL = -1L;
   private long connectionMaxIdleMillis = 60000L;
   private int validateAfterInactivityMillis = 5000;
   private boolean tcpKeepAlive = false;
   private boolean cacheResponseMetadata = true;
   private int responseMetadataCacheSize = 50;
   private DnsResolver dnsResolver = new SystemDefaultDnsResolver();
   private SecureRandom secureRandom;
   private Map<String, String> headers = new HashMap<>();
   private boolean useExpectContinue = true;
   private int maxConsecutiveRetriesBeforeThrottling = 100;
   private final ApacheHttpClientConfig apacheHttpClientConfig;
   private boolean disableHostPrefixInjection;
   private final AtomicReference<ClientConfiguration.URLHolder> httpProxyHolder = new AtomicReference<>();
   private final AtomicReference<ClientConfiguration.URLHolder> httpsProxyHolder = new AtomicReference<>();
   private TlsKeyManagersProvider tlsKeyManagersProvider;
   private RetryMode retryMode;

   public ClientConfiguration() {
      this.apacheHttpClientConfig = new ApacheHttpClientConfig();
   }

   public ClientConfiguration(ClientConfiguration other) {
      this.connectionTimeout = other.getConnectionTimeout();
      this.maxConnections = other.getMaxConnections();
      this.maxErrorRetry = other.getMaxErrorRetry();
      this.retryPolicy = other.getRetryPolicy();
      this.throttleRetries = other.useThrottledRetries();
      this.localAddress = other.getLocalAddress();
      this.protocol = other.getProtocol();
      this.proxyProtocol = other.getProxyProtocol();
      this.proxyDomain = other.getProxyDomain();
      this.proxyHost = other.getProxyHost();
      this.proxyPassword = other.getProxyPassword();
      this.proxyPort = other.getProxyPort();
      this.proxyUsername = other.getProxyUsername();
      this.proxyWorkstation = other.getProxyWorkstation();
      this.nonProxyHosts = other.getNonProxyHosts();
      this.disableSocketProxy = other.disableSocketProxy();
      this.proxyAuthenticationMethods = other.getProxyAuthenticationMethods();
      this.preemptiveBasicProxyAuth = other.isPreemptiveBasicProxyAuth();
      this.socketTimeout = other.getSocketTimeout();
      this.requestTimeout = other.getRequestTimeout();
      this.clientExecutionTimeout = other.getClientExecutionTimeout();
      this.userAgentPrefix = other.getUserAgentPrefix();
      this.userAgentSuffix = other.getUserAgentSuffix();
      this.useReaper = other.useReaper();
      this.useGzip = other.useGzip();
      this.socketSendBufferSizeHint = other.getSocketBufferSizeHints()[0];
      this.socketReceiveBufferSizeHint = other.getSocketBufferSizeHints()[1];
      this.signerOverride = other.getSignerOverride();
      this.responseMetadataCacheSize = other.getResponseMetadataCacheSize();
      this.dnsResolver = other.getDnsResolver();
      this.useExpectContinue = other.isUseExpectContinue();
      this.apacheHttpClientConfig = new ApacheHttpClientConfig(other.getApacheHttpClientConfig());
      this.cacheResponseMetadata = other.getCacheResponseMetadata();
      this.connectionTTL = other.getConnectionTTL();
      this.connectionMaxIdleMillis = other.getConnectionMaxIdleMillis();
      this.validateAfterInactivityMillis = other.getValidateAfterInactivityMillis();
      this.tcpKeepAlive = other.useTcpKeepAlive();
      this.secureRandom = other.getSecureRandom();
      this.headers.clear();
      this.headers.putAll(other.getHeaders());
      this.maxConsecutiveRetriesBeforeThrottling = other.getMaxConsecutiveRetriesBeforeThrottling();
      this.disableHostPrefixInjection = other.disableHostPrefixInjection;
      this.httpProxyHolder.set(other.httpProxyHolder.get());
      this.httpsProxyHolder.set(other.httpsProxyHolder.get());
      this.tlsKeyManagersProvider = other.tlsKeyManagersProvider;
      this.retryMode = other.retryMode;
   }

   public Protocol getProtocol() {
      return this.protocol;
   }

   public void setProtocol(Protocol protocol) {
      this.protocol = protocol;
   }

   public ClientConfiguration withProtocol(Protocol protocol) {
      this.setProtocol(protocol);
      return this;
   }

   public int getMaxConnections() {
      return this.maxConnections;
   }

   public void setMaxConnections(int maxConnections) {
      this.maxConnections = maxConnections;
   }

   public ClientConfiguration withMaxConnections(int maxConnections) {
      this.setMaxConnections(maxConnections);
      return this;
   }

   @Deprecated
   public String getUserAgent() {
      return this.getUserAgentPrefix();
   }

   @Deprecated
   public void setUserAgent(String userAgent) {
      this.setUserAgentPrefix(userAgent);
   }

   @Deprecated
   public ClientConfiguration withUserAgent(String userAgent) {
      return this.withUserAgentPrefix(userAgent);
   }

   public String getUserAgentPrefix() {
      return this.userAgentPrefix;
   }

   public void setUserAgentPrefix(String prefix) {
      this.userAgentPrefix = prefix;
   }

   public ClientConfiguration withUserAgentPrefix(String prefix) {
      this.setUserAgentPrefix(prefix);
      return this;
   }

   public String getUserAgentSuffix() {
      return this.userAgentSuffix;
   }

   public void setUserAgentSuffix(String suffix) {
      this.userAgentSuffix = suffix;
   }

   public ClientConfiguration withUserAgentSuffix(String suffix) {
      this.setUserAgentSuffix(suffix);
      return this;
   }

   public InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public void setLocalAddress(InetAddress localAddress) {
      this.localAddress = localAddress;
   }

   public ClientConfiguration withLocalAddress(InetAddress localAddress) {
      this.setLocalAddress(localAddress);
      return this;
   }

   private String getSystemProperty(String property) {
      return System.getProperty(property);
   }

   private String getEnvironmentVariable(String environmentVariable) {
      String value = StringUtils.trim(System.getenv(environmentVariable));
      return StringUtils.hasValue(value) ? value : null;
   }

   private String getEnvironmentVariableCaseInsensitive(String environmentVariable) {
      String result = this.getEnvironmentVariable(environmentVariable);
      return result != null ? result : this.getEnvironmentVariable(environmentVariable.toLowerCase());
   }

   public Protocol getProxyProtocol() {
      return this.proxyProtocol;
   }

   public ClientConfiguration withProxyProtocol(Protocol proxyProtocol) {
      this.proxyProtocol = proxyProtocol == null ? Protocol.HTTP : proxyProtocol;
      return this;
   }

   public void setProxyProtocol(Protocol proxyProtocol) {
      this.withProxyProtocol(proxyProtocol);
   }

   private String getProxyHostProperty() {
      return this.getProtocol() == Protocol.HTTPS ? this.getSystemProperty("https.proxyHost") : this.getSystemProperty("http.proxyHost");
   }

   private String getProxyHostEnvironment() {
      URL httpProxy = this.getHttpProxyEnvironmentVariable();
      return httpProxy != null ? httpProxy.getHost() : null;
   }

   public String getProxyHost() {
      if (this.proxyHost != null) {
         return this.proxyHost;
      } else {
         return this.getProxyHostProperty() != null ? this.getProxyHostProperty() : this.getProxyHostEnvironment();
      }
   }

   public void setProxyHost(String proxyHost) {
      this.proxyHost = proxyHost;
   }

   public ClientConfiguration withProxyHost(String proxyHost) {
      this.setProxyHost(proxyHost);
      return this;
   }

   private int getProxyPortProperty() {
      try {
         return this.getProtocol() == Protocol.HTTPS
            ? Integer.parseInt(this.getSystemProperty("https.proxyPort"))
            : Integer.parseInt(this.getSystemProperty("http.proxyPort"));
      } catch (NumberFormatException var2) {
         return this.proxyPort;
      }
   }

   private int getProxyPortEnvironment() {
      URL httpProxy = this.getHttpProxyEnvironmentVariable();
      return httpProxy != null ? httpProxy.getPort() : this.proxyPort;
   }

   public int getProxyPort() {
      if (this.proxyPort >= 0) {
         return this.proxyPort;
      } else {
         return this.getProxyPortProperty() >= 0 ? this.getProxyPortProperty() : this.getProxyPortEnvironment();
      }
   }

   public void setProxyPort(int proxyPort) {
      this.proxyPort = proxyPort;
   }

   public ClientConfiguration withProxyPort(int proxyPort) {
      this.setProxyPort(proxyPort);
      return this;
   }

   public ClientConfiguration withDisableSocketProxy(boolean disableSocketProxy) {
      this.disableSocketProxy = disableSocketProxy;
      return this;
   }

   public void setDisableSocketProxy(boolean disableSocketProxy) {
      this.withDisableSocketProxy(disableSocketProxy);
   }

   public boolean disableSocketProxy() {
      return this.disableSocketProxy;
   }

   private String getProxyUsernameProperty() {
      return this.getProtocol() == Protocol.HTTPS ? this.getSystemProperty("https.proxyUser") : this.getSystemProperty("http.proxyUser");
   }

   private String getProxyUsernameEnvironment() {
      URL httpProxy = this.getHttpProxyEnvironmentVariable();
      if (httpProxy != null) {
         try {
            return httpProxy.getUserInfo().split(":", 2)[0];
         } catch (Exception var3) {
         }
      }

      return null;
   }

   public String getProxyUsername() {
      if (this.proxyUsername != null) {
         return this.proxyUsername;
      } else {
         return this.getProxyUsernameProperty() != null ? this.getProxyUsernameProperty() : this.getProxyUsernameEnvironment();
      }
   }

   public void setProxyUsername(String proxyUsername) {
      this.proxyUsername = proxyUsername;
   }

   public ClientConfiguration withProxyUsername(String proxyUsername) {
      this.setProxyUsername(proxyUsername);
      return this;
   }

   private String getProxyPasswordProperty() {
      return this.getProtocol() == Protocol.HTTPS ? this.getSystemProperty("https.proxyPassword") : this.getSystemProperty("http.proxyPassword");
   }

   private String getProxyPasswordEnvironment() {
      URL httpProxy = this.getHttpProxyEnvironmentVariable();
      if (httpProxy != null) {
         try {
            return httpProxy.getUserInfo().split(":", 2)[1];
         } catch (Exception var3) {
         }
      }

      return null;
   }

   public String getProxyPassword() {
      if (this.proxyPassword != null) {
         return this.proxyPassword;
      } else {
         return this.getProxyPasswordProperty() != null ? this.getProxyPasswordProperty() : this.getProxyPasswordEnvironment();
      }
   }

   public void setProxyPassword(String proxyPassword) {
      this.proxyPassword = proxyPassword;
   }

   public ClientConfiguration withProxyPassword(String proxyPassword) {
      this.setProxyPassword(proxyPassword);
      return this;
   }

   public String getProxyDomain() {
      return this.proxyDomain;
   }

   public void setProxyDomain(String proxyDomain) {
      this.proxyDomain = proxyDomain;
   }

   public ClientConfiguration withProxyDomain(String proxyDomain) {
      this.setProxyDomain(proxyDomain);
      return this;
   }

   public String getProxyWorkstation() {
      return this.proxyWorkstation;
   }

   public void setProxyWorkstation(String proxyWorkstation) {
      this.proxyWorkstation = proxyWorkstation;
   }

   public ClientConfiguration withProxyWorkstation(String proxyWorkstation) {
      this.setProxyWorkstation(proxyWorkstation);
      return this;
   }

   private String getNonProxyHostsProperty() {
      return this.getSystemProperty("http.nonProxyHosts");
   }

   private String getNonProxyHostsEnvironment() {
      String nonProxyHosts = this.getEnvironmentVariableCaseInsensitive("NO_PROXY");
      if (nonProxyHosts != null) {
         nonProxyHosts = nonProxyHosts.replace(",", "|");
      }

      return nonProxyHosts;
   }

   public String getNonProxyHosts() {
      if (this.nonProxyHosts != null) {
         return this.nonProxyHosts;
      } else {
         return this.getNonProxyHostsProperty() != null ? this.getNonProxyHostsProperty() : this.getNonProxyHostsEnvironment();
      }
   }

   public void setNonProxyHosts(String nonProxyHosts) {
      this.nonProxyHosts = nonProxyHosts;
   }

   public ClientConfiguration withNonProxyHosts(String nonProxyHosts) {
      this.setNonProxyHosts(nonProxyHosts);
      return this;
   }

   public List<ProxyAuthenticationMethod> getProxyAuthenticationMethods() {
      return this.proxyAuthenticationMethods;
   }

   public void setProxyAuthenticationMethods(List<ProxyAuthenticationMethod> proxyAuthenticationMethods) {
      if (proxyAuthenticationMethods == null) {
         this.proxyAuthenticationMethods = null;
      } else {
         ValidationUtils.assertNotEmpty(proxyAuthenticationMethods, "proxyAuthenticationMethods");
         this.proxyAuthenticationMethods = Collections.unmodifiableList(new ArrayList<>(proxyAuthenticationMethods));
      }
   }

   public ClientConfiguration withProxyAuthenticationMethods(List<ProxyAuthenticationMethod> proxyAuthenticationMethods) {
      this.setProxyAuthenticationMethods(proxyAuthenticationMethods);
      return this;
   }

   public RetryPolicy getRetryPolicy() {
      return this.retryPolicy;
   }

   public void setRetryPolicy(RetryPolicy retryPolicy) {
      this.retryPolicy = retryPolicy;
   }

   public ClientConfiguration withRetryPolicy(RetryPolicy retryPolicy) {
      this.setRetryPolicy(retryPolicy);
      return this;
   }

   public int getMaxErrorRetry() {
      return this.maxErrorRetry;
   }

   public void setMaxErrorRetry(int maxErrorRetry) {
      if (maxErrorRetry < 0) {
         throw new IllegalArgumentException("maxErrorRetry should be non-negative");
      } else {
         this.maxErrorRetry = maxErrorRetry;
      }
   }

   public ClientConfiguration withMaxErrorRetry(int maxErrorRetry) {
      this.setMaxErrorRetry(maxErrorRetry);
      return this;
   }

   public ClientConfiguration withRetryMode(RetryMode retryMode) {
      this.setRetryMode(retryMode);
      return this;
   }

   public void setRetryMode(RetryMode retryMode) {
      this.retryMode = retryMode;
   }

   public RetryMode getRetryMode() {
      return this.retryMode;
   }

   public int getSocketTimeout() {
      return this.socketTimeout;
   }

   public void setSocketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
   }

   public ClientConfiguration withSocketTimeout(int socketTimeout) {
      this.setSocketTimeout(socketTimeout);
      return this;
   }

   public int getConnectionTimeout() {
      return this.connectionTimeout;
   }

   public void setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
   }

   public ClientConfiguration withConnectionTimeout(int connectionTimeout) {
      this.setConnectionTimeout(connectionTimeout);
      return this;
   }

   public int getRequestTimeout() {
      return this.requestTimeout;
   }

   public void setRequestTimeout(int requestTimeout) {
      this.requestTimeout = requestTimeout;
   }

   public ClientConfiguration withRequestTimeout(int requestTimeout) {
      this.setRequestTimeout(requestTimeout);
      return this;
   }

   public int getClientExecutionTimeout() {
      return this.clientExecutionTimeout;
   }

   public void setClientExecutionTimeout(int clientExecutionTimeout) {
      this.clientExecutionTimeout = clientExecutionTimeout;
   }

   public ClientConfiguration withClientExecutionTimeout(int clientExecutionTimeout) {
      this.setClientExecutionTimeout(clientExecutionTimeout);
      return this;
   }

   public boolean useReaper() {
      return this.useReaper;
   }

   public void setUseReaper(boolean use) {
      this.useReaper = use;
   }

   public ClientConfiguration withReaper(boolean use) {
      this.setUseReaper(use);
      return this;
   }

   public boolean useThrottledRetries() {
      return this.throttleRetries || this.getSystemProperty("com.amazonaws.sdk.enableThrottledRetry") != null;
   }

   public void setUseThrottleRetries(boolean use) {
      this.throttleRetries = use;
   }

   public ClientConfiguration withThrottledRetries(boolean use) {
      this.setUseThrottleRetries(use);
      return this;
   }

   public void setMaxConsecutiveRetriesBeforeThrottling(int maxConsecutiveRetriesBeforeThrottling) {
      this.maxConsecutiveRetriesBeforeThrottling = ValidationUtils.assertIsPositive(
         maxConsecutiveRetriesBeforeThrottling, "maxConsecutiveRetriesBeforeThrottling"
      );
   }

   public ClientConfiguration withMaxConsecutiveRetriesBeforeThrottling(int maxConsecutiveRetriesBeforeThrottling) {
      this.setMaxConsecutiveRetriesBeforeThrottling(maxConsecutiveRetriesBeforeThrottling);
      return this;
   }

   public int getMaxConsecutiveRetriesBeforeThrottling() {
      return this.maxConsecutiveRetriesBeforeThrottling;
   }

   public boolean useGzip() {
      return this.useGzip;
   }

   public void setUseGzip(boolean use) {
      this.useGzip = use;
   }

   public ClientConfiguration withGzip(boolean use) {
      this.setUseGzip(use);
      return this;
   }

   public int[] getSocketBufferSizeHints() {
      return new int[]{this.socketSendBufferSizeHint, this.socketReceiveBufferSizeHint};
   }

   public void setSocketBufferSizeHints(int socketSendBufferSizeHint, int socketReceiveBufferSizeHint) {
      this.socketSendBufferSizeHint = socketSendBufferSizeHint;
      this.socketReceiveBufferSizeHint = socketReceiveBufferSizeHint;
   }

   public ClientConfiguration withSocketBufferSizeHints(int socketSendBufferSizeHint, int socketReceiveBufferSizeHint) {
      this.setSocketBufferSizeHints(socketSendBufferSizeHint, socketReceiveBufferSizeHint);
      return this;
   }

   public String getSignerOverride() {
      return this.signerOverride;
   }

   public void setSignerOverride(String value) {
      this.signerOverride = value;
   }

   public ClientConfiguration withSignerOverride(String value) {
      this.setSignerOverride(value);
      return this;
   }

   public boolean isPreemptiveBasicProxyAuth() {
      return this.preemptiveBasicProxyAuth;
   }

   public void setPreemptiveBasicProxyAuth(Boolean preemptiveBasicProxyAuth) {
      this.preemptiveBasicProxyAuth = preemptiveBasicProxyAuth;
   }

   public ClientConfiguration withPreemptiveBasicProxyAuth(boolean preemptiveBasicProxyAuth) {
      this.setPreemptiveBasicProxyAuth(preemptiveBasicProxyAuth);
      return this;
   }

   public long getConnectionTTL() {
      return this.connectionTTL;
   }

   public void setConnectionTTL(long connectionTTL) {
      this.connectionTTL = connectionTTL;
   }

   public ClientConfiguration withConnectionTTL(long connectionTTL) {
      this.setConnectionTTL(connectionTTL);
      return this;
   }

   public long getConnectionMaxIdleMillis() {
      return this.connectionMaxIdleMillis;
   }

   public void setConnectionMaxIdleMillis(long connectionMaxIdleMillis) {
      this.connectionMaxIdleMillis = connectionMaxIdleMillis;
   }

   public ClientConfiguration withConnectionMaxIdleMillis(long connectionMaxIdleMillis) {
      this.setConnectionMaxIdleMillis(connectionMaxIdleMillis);
      return this;
   }

   public int getValidateAfterInactivityMillis() {
      return this.validateAfterInactivityMillis;
   }

   public void setValidateAfterInactivityMillis(int validateAfterInactivityMillis) {
      this.validateAfterInactivityMillis = validateAfterInactivityMillis;
   }

   public ClientConfiguration withValidateAfterInactivityMillis(int validateAfterInactivityMillis) {
      this.setValidateAfterInactivityMillis(validateAfterInactivityMillis);
      return this;
   }

   public boolean useTcpKeepAlive() {
      return this.tcpKeepAlive;
   }

   public void setUseTcpKeepAlive(boolean use) {
      this.tcpKeepAlive = use;
   }

   public ClientConfiguration withTcpKeepAlive(boolean use) {
      this.setUseTcpKeepAlive(use);
      return this;
   }

   public DnsResolver getDnsResolver() {
      return this.dnsResolver;
   }

   public void setDnsResolver(DnsResolver resolver) {
      if (resolver == null) {
         throw new IllegalArgumentException("resolver cannot be null");
      } else {
         this.dnsResolver = resolver;
      }
   }

   public ClientConfiguration withDnsResolver(DnsResolver resolver) {
      this.setDnsResolver(resolver);
      return this;
   }

   public boolean getCacheResponseMetadata() {
      return this.cacheResponseMetadata;
   }

   public void setCacheResponseMetadata(boolean shouldCache) {
      this.cacheResponseMetadata = shouldCache;
   }

   public ClientConfiguration withCacheResponseMetadata(boolean shouldCache) {
      this.setCacheResponseMetadata(shouldCache);
      return this;
   }

   public int getResponseMetadataCacheSize() {
      return this.responseMetadataCacheSize;
   }

   public void setResponseMetadataCacheSize(int responseMetadataCacheSize) {
      this.responseMetadataCacheSize = responseMetadataCacheSize;
   }

   public ClientConfiguration withResponseMetadataCacheSize(int responseMetadataCacheSize) {
      this.setResponseMetadataCacheSize(responseMetadataCacheSize);
      return this;
   }

   public ApacheHttpClientConfig getApacheHttpClientConfig() {
      return this.apacheHttpClientConfig;
   }

   public SecureRandom getSecureRandom() {
      if (this.secureRandom == null) {
         this.secureRandom = new SecureRandom();
      }

      return this.secureRandom;
   }

   public void setSecureRandom(SecureRandom secureRandom) {
      this.secureRandom = secureRandom;
   }

   public ClientConfiguration withSecureRandom(SecureRandom secureRandom) {
      this.setSecureRandom(secureRandom);
      return this;
   }

   public boolean isUseExpectContinue() {
      return this.useExpectContinue;
   }

   public void setUseExpectContinue(boolean useExpectContinue) {
      this.useExpectContinue = useExpectContinue;
   }

   public ClientConfiguration withUseExpectContinue(boolean useExpectContinue) {
      this.setUseExpectContinue(useExpectContinue);
      return this;
   }

   public ClientConfiguration withHeader(String name, String value) {
      this.addHeader(name, value);
      return this;
   }

   public void addHeader(String name, String value) {
      this.headers.put(name, value);
   }

   public Map<String, String> getHeaders() {
      return Collections.unmodifiableMap(this.headers);
   }

   public boolean isDisableHostPrefixInjection() {
      return this.disableHostPrefixInjection;
   }

   public void setDisableHostPrefixInjection(boolean disableHostPrefixInjection) {
      this.disableHostPrefixInjection = disableHostPrefixInjection;
   }

   public ClientConfiguration withDisableHostPrefixInjection(boolean disableHostPrefixInjection) {
      this.setDisableHostPrefixInjection(disableHostPrefixInjection);
      return this;
   }

   public TlsKeyManagersProvider getTlsKeyManagersProvider() {
      return this.tlsKeyManagersProvider;
   }

   public ClientConfiguration withTlsKeyManagersProvider(TlsKeyManagersProvider tlsKeyManagersProvider) {
      this.tlsKeyManagersProvider = tlsKeyManagersProvider;
      return this;
   }

   public void setTlsKeyManagersProvider(TlsKeyManagersProvider tlsKeyManagersProvider) {
      this.withTlsKeyManagersProvider(tlsKeyManagersProvider);
   }

   private URL getHttpProxyEnvironmentVariable() {
      return this.getProtocol() == Protocol.HTTP
         ? this.getUrlEnvVar(this.httpProxyHolder, "HTTP_PROXY")
         : this.getUrlEnvVar(this.httpsProxyHolder, "HTTPS_PROXY");
   }

   private URL getUrlEnvVar(AtomicReference<ClientConfiguration.URLHolder> cache, String name) {
      if (cache.get() == null) {
         ClientConfiguration.URLHolder holder = new ClientConfiguration.URLHolder();
         String value = this.getEnvironmentVariableCaseInsensitive(name);
         if (value != null) {
            try {
               holder.url = new URL(value);
            } catch (MalformedURLException var6) {
               if (log.isWarnEnabled()) {
                  log.warn(String.format("Unable to parse %s environment variable value '%s' as URL. It is malformed.", name, value), var6);
               }
            }
         }

         cache.compareAndSet(null, holder);
      }

      return cache.get().url;
   }

   static class URLHolder {
      private URL url;
   }
}
