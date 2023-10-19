package com.amazonaws.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.ValidationUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

@SdkInternalApi
public class ConnectionUtils {
   private static final int DEFAULT_TIMEOUT_MILLIS = 1000;
   private final int timeoutMillis = ValidationUtils.assertIsPositive(readTimeoutMillisConfiguration(), "AWS_METADATA_SERVICE_TIMEOUT");

   @SdkTestInternalApi
   ConnectionUtils() {
   }

   public static ConnectionUtils getInstance() {
      return ConnectionUtils.ConnectionUtilsSingletonHolder.INSTANCE;
   }

   private static int readTimeoutMillisConfiguration() {
      String stringTimeout = System.getenv("AWS_METADATA_SERVICE_TIMEOUT");
      if (StringUtils.isNullOrEmpty(stringTimeout)) {
         return 1000;
      } else {
         try {
            int timeoutSeconds = Integer.parseInt(stringTimeout);
            return timeoutSeconds * 1000;
         } catch (NumberFormatException var5) {
            try {
               double timeoutSecondsx = Double.parseDouble(stringTimeout);
               return toIntExact(Math.round(timeoutSecondsx * 1000.0));
            } catch (NumberFormatException var4) {
               throw new IllegalStateException(
                  "AWS_METADATA_SERVICE_TIMEOUT environment variable value does not appear to be an integer or a double: " + stringTimeout
               );
            }
         }
      }
   }

   private static int toIntExact(long value) {
      if ((long)((int)value) != value) {
         throw new ArithmeticException("integer overflow");
      } else {
         return (int)value;
      }
   }

   public int getTimeoutMillis() {
      return this.timeoutMillis;
   }

   public HttpURLConnection connectToEndpoint(URI endpoint, Map<String, String> headers) throws IOException {
      return this.connectToEndpoint(endpoint, headers, "GET");
   }

   public HttpURLConnection connectToEndpoint(URI endpoint, Map<String, String> headers, String method) throws IOException {
      HttpURLConnection connection = (HttpURLConnection)endpoint.toURL().openConnection(Proxy.NO_PROXY);
      connection.setConnectTimeout(this.timeoutMillis);
      connection.setReadTimeout(this.timeoutMillis);
      connection.setRequestMethod(method);
      connection.setDoOutput(true);

      for(Entry<String, String> header : headers.entrySet()) {
         connection.addRequestProperty(header.getKey(), header.getValue());
      }

      connection.connect();
      return connection;
   }

   private static final class ConnectionUtilsSingletonHolder {
      private static final ConnectionUtils INSTANCE = new ConnectionUtils();
   }
}
