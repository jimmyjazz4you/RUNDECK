package com.amazonaws.internal;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.SdkClock;
import com.amazonaws.auth.internal.AWS4SignerUtils;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.retry.ClockSkewAdjuster;
import com.amazonaws.retry.RetryPolicyAdapter;
import com.amazonaws.retry.v2.RetryPolicy;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@SdkInternalApi
public final class SdkRequestRetryHeaderProvider {
   private static final String SDK_REQUEST_RETRY_HEADER = "amz-sdk-request";
   private final ClientConfiguration config;
   private final Integer maxErrorRetry;
   private final ClockSkewAdjuster clockSkewAdjuster;

   public SdkRequestRetryHeaderProvider(ClientConfiguration config, RetryPolicy retryPolicy, ClockSkewAdjuster clockSkewAdjuster) {
      this.config = config;
      if (retryPolicy instanceof RetryPolicyAdapter) {
         this.maxErrorRetry = ((RetryPolicyAdapter)retryPolicy).getMaxErrorRetry() + 1;
      } else {
         this.maxErrorRetry = null;
      }

      this.clockSkewAdjuster = clockSkewAdjuster;
   }

   public void addSdkRequestRetryHeader(Request<?> request, int attemptNum) {
      List<SdkRequestRetryHeaderProvider.Pair> pairs = this.requestPairs(request, String.valueOf(attemptNum));
      StringBuilder headerValue = new StringBuilder();

      for(SdkRequestRetryHeaderProvider.Pair pair : pairs) {
         headerValue.append(pair.name).append("=").append(pair.value).append(";");
      }

      String header = headerValue.toString().substring(0, headerValue.length() - 1);
      request.addHeader("amz-sdk-request", header);
   }

   private List<SdkRequestRetryHeaderProvider.Pair> requestPairs(Request<?> request, String attemptNum) {
      List<SdkRequestRetryHeaderProvider.Pair> requestPairs = new ArrayList<>();
      String optionalTtl = this.calculateTtl(request);
      if (optionalTtl != null) {
         requestPairs.add(new SdkRequestRetryHeaderProvider.Pair("ttl", optionalTtl));
      }

      requestPairs.add(new SdkRequestRetryHeaderProvider.Pair("attempt", attemptNum));
      if (this.maxErrorRetry != null) {
         requestPairs.add(new SdkRequestRetryHeaderProvider.Pair("max", String.valueOf(this.maxErrorRetry)));
      }

      return requestPairs;
   }

   private String calculateTtl(Request<?> request) {
      if (this.isStreaming(request)) {
         return null;
      } else {
         Integer estimatedSkew = this.clockSkewAdjuster.getEstimatedSkew();
         if (estimatedSkew == null) {
            return null;
         } else {
            long currentTimeMillis = SdkClock.Instance.get().currentTimeMillis();
            long ttl = currentTimeMillis + (long)this.config.getSocketTimeout() - (long)(estimatedSkew * 1000);
            return AWS4SignerUtils.formatTimestamp(ttl);
         }
      }
   }

   private boolean isStreaming(Request<?> request) {
      return Boolean.TRUE.equals(request.getHandlerContext(HandlerContextKey.HAS_STREAMING_INPUT))
         || Boolean.TRUE.equals(request.getHandlerContext(HandlerContextKey.HAS_STREAMING_OUTPUT));
   }

   private static final class Pair {
      private String name;
      private String value;

      Pair(String name, String value) {
         this.name = name;
         this.value = value;
      }
   }
}
