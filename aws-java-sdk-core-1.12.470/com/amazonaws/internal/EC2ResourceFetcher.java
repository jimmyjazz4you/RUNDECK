package com.amazonaws.internal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.retry.internal.CredentialsEndpointRetryParameters;
import com.amazonaws.retry.internal.CredentialsEndpointRetryPolicy;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.VersionInfoUtils;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public abstract class EC2ResourceFetcher {
   private static final Log LOG = LogFactory.getLog(EC2ResourceFetcher.class);
   private final ConnectionUtils connectionUtils;
   private static final String USER_AGENT = VersionInfoUtils.getUserAgent();

   EC2ResourceFetcher() {
      this.connectionUtils = ConnectionUtils.getInstance();
   }

   @SdkTestInternalApi
   EC2ResourceFetcher(ConnectionUtils connectionUtils) {
      this.connectionUtils = connectionUtils;
   }

   public static EC2ResourceFetcher defaultResourceFetcher() {
      return EC2ResourceFetcher.DefaultEC2ResourceFetcher.DEFAULT_BASE_RESOURCE_FETCHER;
   }

   public abstract String readResource(URI var1, CredentialsEndpointRetryPolicy var2, Map<String, String> var3);

   public final String readResource(URI endpoint) {
      return this.readResource(endpoint, CredentialsEndpointRetryPolicy.NO_RETRY, null);
   }

   public final String readResource(URI endpoint, CredentialsEndpointRetryPolicy retryPolicy) {
      return this.readResource(endpoint, retryPolicy, null);
   }

   final String doReadResource(URI endpoint, CredentialsEndpointRetryPolicy retryPolicy, Map<String, String> headers) {
      return this.doReadResource(endpoint, retryPolicy, headers, "GET");
   }

   final String doReadResource(URI endpoint, CredentialsEndpointRetryPolicy retryPolicy, Map<String, String> headers, String method) {
      int retriesAttempted = 0;
      InputStream inputStream = null;
      Map<String, String> headersToSent = this.addDefaultHeaders(headers);

      while(true) {
         try {
            long start = 0L;
            if (LOG.isDebugEnabled()) {
               LOG.debug("Executing " + method + " " + endpoint + " with headers " + headersToSent.keySet());
               start = System.currentTimeMillis();
            }

            HttpURLConnection connection = this.connectionUtils.connectToEndpoint(endpoint, headersToSent, method);
            int statusCode = connection.getResponseCode();
            if (LOG.isDebugEnabled()) {
               LOG.debug("Got response code " + statusCode + " from " + method + " " + endpoint);
            }

            if (statusCode == 200) {
               inputStream = connection.getInputStream();
               String result = IOUtils.toString(inputStream);
               if (LOG.isDebugEnabled()) {
                  long duration = System.currentTimeMillis() - start;
                  LOG.debug("Completed " + method + " " + endpoint + " after " + duration + "ms");
               }

               return result;
            }

            if (statusCode == 404) {
               throw new SdkClientException("The requested metadata is not found at " + connection.getURL());
            }

            if (!retryPolicy.shouldRetry(retriesAttempted++, CredentialsEndpointRetryParameters.builder().withStatusCode(statusCode).build())) {
               inputStream = connection.getErrorStream();
               this.handleErrorResponse(inputStream, statusCode, connection.getResponseMessage());
            }
         } catch (IOException var18) {
            if (!retryPolicy.shouldRetry(retriesAttempted++, CredentialsEndpointRetryParameters.builder().withException(var18).build())) {
               throw new SdkClientException("Failed to connect to service endpoint: ", var18);
            }

            LOG.debug("An IOException occurred when connecting to service endpoint: " + endpoint + "\n Retrying to connect again.");
         } finally {
            IOUtils.closeQuietly(inputStream, LOG);
         }
      }
   }

   protected final Map<String, String> addDefaultHeaders(Map<String, String> headers) {
      HashMap<String, String> map = new HashMap<>();
      if (headers != null) {
         map.putAll(headers);
      }

      this.putIfAbsent(map, "User-Agent", USER_AGENT);
      this.putIfAbsent(map, "Accept", "*/*");
      this.putIfAbsent(map, "Connection", "keep-alive");
      return map;
   }

   private <K, V> void putIfAbsent(Map<K, V> map, K key, V value) {
      if (map.get(key) == null) {
         map.put(key, value);
      }
   }

   private void handleErrorResponse(InputStream errorStream, int statusCode, String responseMessage) throws IOException {
      String errorCode = null;
      if (errorStream != null) {
         String errorResponse = IOUtils.toString(errorStream);

         try {
            JsonNode node = Jackson.jsonNodeOf(errorResponse);
            JsonNode code = node.get("code");
            JsonNode message = node.get("message");
            if (code != null && message != null) {
               errorCode = code.asText();
               responseMessage = message.asText();
            }
         } catch (Exception var9) {
            LOG.debug("Unable to parse error stream");
         }
      }

      AmazonServiceException ase = new AmazonServiceException(responseMessage);
      ase.setStatusCode(statusCode);
      ase.setErrorCode(errorCode);
      throw ase;
   }

   static final class DefaultEC2ResourceFetcher extends EC2ResourceFetcher {
      private static final EC2ResourceFetcher.DefaultEC2ResourceFetcher DEFAULT_BASE_RESOURCE_FETCHER = new EC2ResourceFetcher.DefaultEC2ResourceFetcher();

      DefaultEC2ResourceFetcher() {
      }

      @SdkTestInternalApi
      DefaultEC2ResourceFetcher(ConnectionUtils connectionUtils) {
         super(connectionUtils);
      }

      @Override
      public String readResource(URI endpoint, CredentialsEndpointRetryPolicy retryPolicy, Map<String, String> headers) {
         return this.doReadResource(endpoint, retryPolicy, headers);
      }
   }
}
