package com.amazonaws.http.impl.client;

import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.util.AWSRequestMetrics;
import java.io.IOException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class SdkHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
   public static final SdkHttpRequestRetryHandler Singleton = new SdkHttpRequestRetryHandler();

   private SdkHttpRequestRetryHandler() {
   }

   public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
      boolean retry = super.retryRequest(exception, executionCount, context);
      if (retry) {
         AWSRequestMetrics awsRequestMetrics = (AWSRequestMetrics)context.getAttribute(AWSRequestMetrics.SIMPLE_NAME);
         if (awsRequestMetrics != null) {
            awsRequestMetrics.incrementCounter(AWSRequestMetrics.Field.HttpClientRetryCount);
         }
      }

      return retry;
   }
}
