package com.amazonaws.http;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.Signer;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.timers.client.ClientExecutionAbortTrackerTask;
import com.amazonaws.http.timers.client.NoOpClientExecutionAbortTrackerTask;
import com.amazonaws.internal.auth.NoOpSignerProvider;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.internal.auth.SignerProviderContext;
import com.amazonaws.retry.internal.AuthErrorRetryStrategy;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.AWSRequestMetricsFullSupport;
import java.net.URI;
import java.util.List;

@NotThreadSafe
public class ExecutionContext {
   private final AWSRequestMetrics awsRequestMetrics;
   private final List<RequestHandler2> requestHandler2s;
   private final AmazonWebServiceClient awsClient;
   private final SignerProvider signerProvider;
   private boolean retryCapacityConsumed;
   private AWSCredentialsProvider credentialsProvider;
   private AuthErrorRetryStrategy authErrorRetryStrategy;
   private ClientExecutionAbortTrackerTask clientExecutionTrackerTask = NoOpClientExecutionAbortTrackerTask.INSTANCE;

   public ExecutionContext(boolean isMetricEnabled) {
      this(builder().withUseRequestMetrics(isMetricEnabled).withSignerProvider(new NoOpSignerProvider()));
   }

   public ExecutionContext() {
      this(builder().withSignerProvider(new NoOpSignerProvider()));
   }

   @Deprecated
   public ExecutionContext(List<RequestHandler2> requestHandler2s, boolean isMetricEnabled, AmazonWebServiceClient awsClient) {
      this.requestHandler2s = requestHandler2s;
      this.awsRequestMetrics = (AWSRequestMetrics)(isMetricEnabled ? new AWSRequestMetricsFullSupport() : new AWSRequestMetrics());
      this.awsClient = awsClient;
      this.signerProvider = new SignerProvider() {
         @Override
         public Signer getSigner(SignerProviderContext context) {
            return ExecutionContext.this.getSignerByURI(context.getUri());
         }
      };
   }

   private ExecutionContext(ExecutionContext.Builder builder) {
      this.requestHandler2s = builder.requestHandler2s;
      this.awsRequestMetrics = (AWSRequestMetrics)(builder.useRequestMetrics ? new AWSRequestMetricsFullSupport() : new AWSRequestMetrics());
      this.awsClient = builder.awsClient;
      this.signerProvider = builder.signerProvider;
   }

   public List<RequestHandler2> getRequestHandler2s() {
      return this.requestHandler2s;
   }

   public AWSRequestMetrics getAwsRequestMetrics() {
      return this.awsRequestMetrics;
   }

   protected AmazonWebServiceClient getAwsClient() {
      return this.awsClient;
   }

   @Deprecated
   public void setSigner(Signer signer) {
   }

   public boolean retryCapacityConsumed() {
      return this.retryCapacityConsumed;
   }

   public void markRetryCapacityConsumed() {
      this.retryCapacityConsumed = true;
   }

   public Signer getSigner(SignerProviderContext context) {
      return this.signerProvider.getSigner(context);
   }

   @Deprecated
   public Signer getSignerByURI(URI uri) {
      return this.awsClient == null ? null : this.awsClient.getSignerByURI(uri);
   }

   public void setCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
      this.credentialsProvider = credentialsProvider;
   }

   public AWSCredentialsProvider getCredentialsProvider() {
      return this.credentialsProvider;
   }

   public AuthErrorRetryStrategy getAuthErrorRetryStrategy() {
      return this.authErrorRetryStrategy;
   }

   public void setAuthErrorRetryStrategy(AuthErrorRetryStrategy authErrorRetryStrategy) {
      this.authErrorRetryStrategy = authErrorRetryStrategy;
   }

   public ClientExecutionAbortTrackerTask getClientExecutionTrackerTask() {
      return this.clientExecutionTrackerTask;
   }

   public void setClientExecutionTrackerTask(ClientExecutionAbortTrackerTask clientExecutionTrackerTask) {
      this.clientExecutionTrackerTask = clientExecutionTrackerTask;
   }

   public static ExecutionContext.Builder builder() {
      return new ExecutionContext.Builder();
   }

   public static class Builder {
      private boolean useRequestMetrics;
      private List<RequestHandler2> requestHandler2s;
      private AmazonWebServiceClient awsClient;
      private SignerProvider signerProvider = new NoOpSignerProvider();

      private Builder() {
      }

      public boolean useRequestMetrics() {
         return this.useRequestMetrics;
      }

      public void setUseRequestMetrics(boolean useRequestMetrics) {
         this.useRequestMetrics = useRequestMetrics;
      }

      public ExecutionContext.Builder withUseRequestMetrics(boolean withUseRequestMetrics) {
         this.setUseRequestMetrics(withUseRequestMetrics);
         return this;
      }

      public List<RequestHandler2> getRequestHandler2s() {
         return this.requestHandler2s;
      }

      public void setRequestHandler2s(List<RequestHandler2> requestHandler2s) {
         this.requestHandler2s = requestHandler2s;
      }

      public ExecutionContext.Builder withRequestHandler2s(List<RequestHandler2> requestHandler2s) {
         this.setRequestHandler2s(requestHandler2s);
         return this;
      }

      public AmazonWebServiceClient getAwsClient() {
         return this.awsClient;
      }

      public void setAwsClient(AmazonWebServiceClient awsClient) {
         this.awsClient = awsClient;
      }

      public ExecutionContext.Builder withAwsClient(AmazonWebServiceClient awsClient) {
         this.setAwsClient(awsClient);
         return this;
      }

      public SignerProvider getSignerProvider() {
         return this.signerProvider;
      }

      public void setSignerProvider(SignerProvider signerProvider) {
         this.signerProvider = signerProvider;
      }

      public ExecutionContext.Builder withSignerProvider(SignerProvider signerProvider) {
         this.setSignerProvider(signerProvider);
         return this;
      }

      public ExecutionContext build() {
         return new ExecutionContext(this);
      }
   }
}
