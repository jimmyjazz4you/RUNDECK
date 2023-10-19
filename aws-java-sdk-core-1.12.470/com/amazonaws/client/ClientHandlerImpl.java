package com.amazonaws.client;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.RequestConfig;
import com.amazonaws.Response;
import com.amazonaws.SdkBaseException;
import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.CredentialUtils;
import java.net.URI;
import java.util.List;

@Immutable
@ThreadSafe
@SdkProtectedApi
public class ClientHandlerImpl extends ClientHandler {
   private final AWSCredentialsProvider awsCredentialsProvider;
   private final SignerProvider signerProvider;
   private final URI endpoint;
   private final List<RequestHandler2> requestHandler2s;
   private final RequestMetricCollector clientLevelMetricCollector;
   private final AmazonHttpClient client;

   public ClientHandlerImpl(ClientHandlerParams handlerParams) {
      this.signerProvider = handlerParams.getClientParams().getSignerProvider();
      this.endpoint = handlerParams.getClientParams().getEndpoint();
      this.awsCredentialsProvider = handlerParams.getClientParams().getCredentialsProvider();
      this.requestHandler2s = handlerParams.getClientParams().getRequestHandlers();
      this.clientLevelMetricCollector = handlerParams.getClientParams().getRequestMetricCollector();
      this.client = this.buildHttpClient(handlerParams);
   }

   private AmazonHttpClient buildHttpClient(ClientHandlerParams handlerParams) {
      AwsSyncClientParams clientParams = handlerParams.getClientParams();
      return AmazonHttpClient.builder()
         .clientConfiguration(clientParams.getClientConfiguration())
         .retryPolicy(clientParams.getRetryPolicy())
         .requestMetricCollector(clientParams.getRequestMetricCollector())
         .useBrowserCompatibleHostNameVerifier(handlerParams.isDisableStrictHostnameVerification())
         .build();
   }

   @Override
   public <Input, Output> Output execute(ClientExecutionParams<Input, Output> executionParams) {
      Input input = executionParams.getInput();
      ExecutionContext executionContext = this.createExecutionContext(executionParams.getRequestConfig());
      AWSRequestMetrics awsRequestMetrics = executionContext.getAwsRequestMetrics();
      awsRequestMetrics.startEvent(AWSRequestMetrics.Field.ClientExecuteTime);
      Request<Input> request = null;
      Response<Output> response = null;

      Object var7;
      try {
         awsRequestMetrics.startEvent(AWSRequestMetrics.Field.RequestMarshallTime);

         try {
            request = executionParams.getMarshaller().marshall(input);
            request.setAWSRequestMetrics(awsRequestMetrics);
         } finally {
            awsRequestMetrics.endEvent(AWSRequestMetrics.Field.RequestMarshallTime);
         }

         response = this.invoke(
            request, executionParams.getRequestConfig(), executionContext, executionParams.getResponseHandler(), executionParams.getErrorResponseHandler()
         );
         var7 = response.getAwsResponse();
      } finally {
         this.endClientExecution(awsRequestMetrics, executionParams.getRequestConfig(), request, response);
      }

      return (Output)var7;
   }

   @Override
   public void shutdown() {
      this.client.shutdown();
   }

   private ExecutionContext createExecutionContext(RequestConfig requestConfig) {
      boolean isMetricsEnabled = this.isRequestMetricsEnabled(requestConfig);
      return ExecutionContext.builder()
         .withRequestHandler2s(this.requestHandler2s)
         .withUseRequestMetrics(isMetricsEnabled)
         .withSignerProvider(this.signerProvider)
         .build();
   }

   private boolean isRequestMetricsEnabled(RequestConfig requestConfig) {
      return this.hasRequestMetricsCollector(requestConfig) || this.isRMCEnabledAtClientOrSdkLevel();
   }

   private boolean hasRequestMetricsCollector(RequestConfig requestConfig) {
      return requestConfig.getRequestMetricsCollector() != null && requestConfig.getRequestMetricsCollector().isEnabled();
   }

   private boolean isRMCEnabledAtClientOrSdkLevel() {
      RequestMetricCollector collector = this.requestMetricCollector();
      return collector != null && collector.isEnabled();
   }

   private RequestMetricCollector requestMetricCollector() {
      return this.clientLevelMetricCollector != null ? this.clientLevelMetricCollector : AwsSdkMetrics.getRequestMetricCollector();
   }

   protected final <T extends AmazonWebServiceRequest> T beforeMarshalling(T request) {
      T local = request;

      for(RequestHandler2 handler : this.requestHandler2s) {
         local = handler.beforeMarshalling(local);
      }

      return local;
   }

   private <Output, Input> Response<Output> invoke(
      Request<Input> request,
      RequestConfig requestConfig,
      ExecutionContext executionContext,
      HttpResponseHandler<Output> responseHandler,
      HttpResponseHandler<? extends SdkBaseException> errorResponseHandler
   ) {
      executionContext.setCredentialsProvider(CredentialUtils.getCredentialsProvider(requestConfig, this.awsCredentialsProvider));
      return this.doInvoke(request, requestConfig, executionContext, responseHandler, errorResponseHandler);
   }

   private <Output, Input> Response<Output> doInvoke(
      Request<Input> request,
      RequestConfig requestConfig,
      ExecutionContext executionContext,
      HttpResponseHandler<Output> responseHandler,
      HttpResponseHandler<? extends SdkBaseException> errorResponseHandler
   ) {
      request.setEndpoint(this.endpoint);
      return this.client
         .requestExecutionBuilder()
         .request(request)
         .requestConfig(requestConfig)
         .executionContext(executionContext)
         .errorResponseHandler(errorResponseHandler)
         .execute(responseHandler);
   }

   private void endClientExecution(AWSRequestMetrics awsRequestMetrics, RequestConfig requestConfig, Request<?> request, Response<?> response) {
      if (request != null) {
         awsRequestMetrics.endEvent(AWSRequestMetrics.Field.ClientExecuteTime);
         awsRequestMetrics.getTimingInfo().endTiming();
         RequestMetricCollector metricCollector = this.findRequestMetricCollector(requestConfig);
         metricCollector.collectMetrics(request, response);
         awsRequestMetrics.log();
      }
   }

   private RequestMetricCollector findRequestMetricCollector(RequestConfig requestConfig) {
      RequestMetricCollector reqLevelMetricsCollector = requestConfig.getRequestMetricsCollector();
      if (reqLevelMetricsCollector != null) {
         return reqLevelMetricsCollector;
      } else {
         return this.clientLevelMetricCollector != null ? this.clientLevelMetricCollector : AwsSdkMetrics.getRequestMetricCollector();
      }
   }
}
