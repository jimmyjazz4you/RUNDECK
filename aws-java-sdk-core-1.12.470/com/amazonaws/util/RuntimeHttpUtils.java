package com.amazonaws.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.Request;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.retry.RetryMode;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class RuntimeHttpUtils {
   private static final String COMMA = ", ";
   private static final String SPACE = " ";
   private static final String AWS_EXECUTION_ENV_PREFIX = "exec-env/";
   private static final String AWS_EXECUTION_ENV_NAME = "AWS_EXECUTION_ENV";
   private static final String RETRY_MODE_PREFIX = "cfg/retry-mode/";
   private static final String TRACE_ID_ENVIRONMENT_VARIABLE = "_X_AMZN_TRACE_ID";
   private static final String LAMBDA_FUNCTION_NAME_ENVIRONMENT_VARIABLE = "AWS_LAMBDA_FUNCTION_NAME";

   public static InputStream fetchFile(URI uri, ClientConfiguration config) throws IOException {
      HttpParams httpClientParams = new BasicHttpParams();
      HttpProtocolParams.setUserAgent(httpClientParams, getUserAgent(config, null));
      HttpConnectionParams.setConnectionTimeout(httpClientParams, getConnectionTimeout(config));
      HttpConnectionParams.setSoTimeout(httpClientParams, getSocketTimeout(config));
      DefaultHttpClient httpclient = new DefaultHttpClient(httpClientParams);
      if (config != null) {
         String proxyHost = config.getProxyHost();
         int proxyPort = config.getProxyPort();
         if (proxyHost != null && proxyPort > 0) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpclient.getParams().setParameter("http.route.default-proxy", proxy);
            if (config.getProxyUsername() != null && config.getProxyPassword() != null) {
               httpclient.getCredentialsProvider()
                  .setCredentials(
                     new AuthScope(proxyHost, proxyPort),
                     new NTCredentials(config.getProxyUsername(), config.getProxyPassword(), config.getProxyWorkstation(), config.getProxyDomain())
                  );
            }
         }
      }

      HttpResponse response = httpclient.execute(new HttpGet(uri));
      if (response.getStatusLine().getStatusCode() != 200) {
         throw new IOException("Error fetching file from " + uri + ": " + response);
      } else {
         return new HttpClientWrappingInputStream(httpclient, response.getEntity().getContent());
      }
   }

   public static String getUserAgent(ClientConfiguration config, String userAgentMarker) {
      String userDefinedPrefix = "";
      String userDefinedSuffix = "";
      String retryModeName = "";
      String awsExecutionEnvironment = getEnvironmentVariable("AWS_EXECUTION_ENV");
      if (config != null) {
         userDefinedPrefix = config.getUserAgentPrefix();
         userDefinedSuffix = config.getUserAgentSuffix();
         RetryMode retryMode = config.getRetryMode() == null ? config.getRetryPolicy().getRetryMode() : config.getRetryMode();
         retryModeName = retryMode != null ? retryMode.getName() : "";
      }

      StringBuilder userAgent = new StringBuilder(userDefinedPrefix.trim());
      if (!ClientConfiguration.DEFAULT_USER_AGENT.equals(userDefinedPrefix)) {
         userAgent.append(", ").append(ClientConfiguration.DEFAULT_USER_AGENT);
      }

      if (StringUtils.hasValue(retryModeName)) {
         userAgent.append(" ").append("cfg/retry-mode/").append(retryModeName.trim());
      }

      if (StringUtils.hasValue(userDefinedSuffix)) {
         userAgent.append(", ").append(userDefinedSuffix.trim());
      }

      if (StringUtils.hasValue(awsExecutionEnvironment)) {
         userAgent.append(" ").append("exec-env/").append(awsExecutionEnvironment.trim());
      }

      if (StringUtils.hasValue(userAgentMarker)) {
         userAgent.append(" ").append(userAgentMarker.trim());
      }

      return userAgent.toString();
   }

   private static String getEnvironmentVariable(String environmentVariableName) {
      try {
         return System.getenv(environmentVariableName);
      } catch (Exception var2) {
         return "";
      }
   }

   private static int getConnectionTimeout(ClientConfiguration config) {
      return config != null ? config.getConnectionTimeout() : 10000;
   }

   private static int getSocketTimeout(ClientConfiguration config) {
      return config != null ? config.getSocketTimeout() : 50000;
   }

   public static URI toUri(String endpoint, ClientConfiguration config) {
      if (config == null) {
         throw new IllegalArgumentException("ClientConfiguration cannot be null");
      } else {
         return toUri(endpoint, config.getProtocol());
      }
   }

   public static URI toUri(String endpoint, Protocol protocol) {
      if (endpoint == null) {
         throw new IllegalArgumentException("endpoint cannot be null");
      } else {
         if (!endpoint.contains("://")) {
            endpoint = protocol.toString() + "://" + endpoint;
         }

         try {
            return new URI(endpoint);
         } catch (URISyntaxException var3) {
            throw new IllegalArgumentException(var3);
         }
      }
   }

   @SdkProtectedApi
   public static URL convertRequestToUrl(Request<?> request, boolean removeLeadingSlashInResourcePath, boolean urlEncode) {
      String resourcePath = urlEncode ? SdkHttpUtils.urlEncode(request.getResourcePath(), true) : request.getResourcePath();
      if (removeLeadingSlashInResourcePath && resourcePath.startsWith("/")) {
         resourcePath = resourcePath.substring(1);
      }

      String urlPath = "/" + resourcePath;
      urlPath = urlPath.replaceAll("(?<=/)/", "%2F");
      StringBuilder url = new StringBuilder(request.getEndpoint().toString());
      url.append(urlPath);
      StringBuilder queryParams = new StringBuilder();
      Map<String, List<String>> requestParams = request.getParameters();

      for(Entry<String, List<String>> entry : requestParams.entrySet()) {
         for(String value : entry.getValue()) {
            queryParams = queryParams.length() > 0 ? queryParams.append("&") : queryParams.append("?");
            queryParams.append(SdkHttpUtils.urlEncode(entry.getKey(), false)).append("=").append(SdkHttpUtils.urlEncode(value, false));
         }
      }

      url.append(queryParams.toString());

      try {
         return new URL(url.toString());
      } catch (MalformedURLException var12) {
         throw new SdkClientException("Unable to convert request to well formed URL: " + var12.getMessage(), var12);
      }
   }

   public static String getLambdaEnvironmentTraceId() {
      String lambdafunctionName = getEnvironmentVariable("AWS_LAMBDA_FUNCTION_NAME");
      String traceId = getEnvironmentVariable("_X_AMZN_TRACE_ID");
      return !StringUtils.isNullOrEmpty(lambdafunctionName) && !StringUtils.isNullOrEmpty(traceId) ? traceId : null;
   }
}
