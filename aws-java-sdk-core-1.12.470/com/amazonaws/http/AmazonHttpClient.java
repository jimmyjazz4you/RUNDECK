package com.amazonaws.http;

import com.amazonaws.AbortedException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.RequestConfig;
import com.amazonaws.ResetException;
import com.amazonaws.Response;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.SDKGlobalTime;
import com.amazonaws.SdkBaseException;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CanHandleNullCredentials;
import com.amazonaws.auth.Signer;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressInputStream;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.handlers.CredentialsRequestHandler;
import com.amazonaws.handlers.HandlerAfterAttemptContext;
import com.amazonaws.handlers.HandlerBeforeAttemptContext;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.apache.client.impl.ApacheHttpClientFactory;
import com.amazonaws.http.apache.client.impl.ConnectionManagerAwareHttpClient;
import com.amazonaws.http.apache.request.impl.ApacheHttpRequestFactory;
import com.amazonaws.http.apache.utils.ApacheUtils;
import com.amazonaws.http.client.HttpClientFactory;
import com.amazonaws.http.exception.HttpRequestTimeoutException;
import com.amazonaws.http.request.HttpRequestFactory;
import com.amazonaws.http.response.AwsResponseHandlerAdapter;
import com.amazonaws.http.settings.HttpClientSettings;
import com.amazonaws.http.timers.client.ClientExecutionAbortTrackerTask;
import com.amazonaws.http.timers.client.ClientExecutionTimeoutException;
import com.amazonaws.http.timers.client.ClientExecutionTimer;
import com.amazonaws.http.timers.client.SdkInterruptedException;
import com.amazonaws.http.timers.request.HttpRequestAbortTaskTracker;
import com.amazonaws.http.timers.request.HttpRequestTimer;
import com.amazonaws.internal.AmazonWebServiceRequestAdapter;
import com.amazonaws.internal.CRC32MismatchException;
import com.amazonaws.internal.ReleasableInputStream;
import com.amazonaws.internal.ResettableInputStream;
import com.amazonaws.internal.SdkBufferedInputStream;
import com.amazonaws.internal.SdkRequestRetryHeaderProvider;
import com.amazonaws.internal.TokenBucket;
import com.amazonaws.internal.auth.SignerProviderContext;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.monitoring.internal.ClientSideMonitoringRequestHandler;
import com.amazonaws.retry.ClockSkewAdjuster;
import com.amazonaws.retry.RetryMode;
import com.amazonaws.retry.RetryPolicyAdapter;
import com.amazonaws.retry.RetryUtils;
import com.amazonaws.retry.internal.AuthErrorRetryStrategy;
import com.amazonaws.retry.internal.AuthRetryParameters;
import com.amazonaws.retry.v2.RetryPolicy;
import com.amazonaws.retry.v2.RetryPolicyContext;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.AwsClientSideMonitoringMetrics;
import com.amazonaws.util.CapacityManager;
import com.amazonaws.util.CollectionUtils;
import com.amazonaws.util.CountingInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.ImmutableMapParameter;
import com.amazonaws.util.MetadataCache;
import com.amazonaws.util.NullResponseMetadataCache;
import com.amazonaws.util.ResponseMetadataCache;
import com.amazonaws.util.RuntimeHttpUtils;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.UnreliableFilterInputStream;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class AmazonHttpClient {
   public static final String HEADER_USER_AGENT = "User-Agent";
   public static final String HEADER_SDK_TRANSACTION_ID = "amz-sdk-invocation-id";
   public static final String HEADER_SDK_RETRY_INFO = "amz-sdk-retry";
   private static final String TRACE_ID_HEADER = "X-Amzn-Trace-Id";
   static final Log log = LogFactory.getLog(AmazonHttpClient.class);
   @SdkInternalApi
   public static final Log requestLog = LogFactory.getLog("com.amazonaws.request");
   private static final HttpClientFactory<ConnectionManagerAwareHttpClient> httpClientFactory = new ApacheHttpClientFactory();
   private static UnreliableTestConfig unreliableTestConfig;
   private static final int THROTTLED_RETRY_COST = 5;
   private static final int TIMEOUT_RETRY_COST = 10;
   private final ClockSkewAdjuster clockSkewAdjuster = new ClockSkewAdjuster();
   private final HttpRequestFactory<HttpRequestBase> httpRequestFactory = new ApacheHttpRequestFactory();
   private ConnectionManagerAwareHttpClient httpClient;
   private final ClientConfiguration config;
   private final RetryPolicy retryPolicy;
   private final HttpClientSettings httpClientSettings;
   private final MetadataCache responseMetadataCache;
   private final HttpRequestTimer httpRequestTimer;
   private final CapacityManager retryCapacity;
   private TokenBucket tokenBucket;
   private final ClientExecutionTimer clientExecutionTimer;
   private final RequestMetricCollector requestMetricCollector;
   private final Random random = new Random();
   private volatile int timeOffset = SDKGlobalTime.getGlobalTimeOffset();
   private final RetryMode retryMode;
   private final SdkRequestRetryHeaderProvider sdkRequestHeaderProvider;

   public AmazonHttpClient(ClientConfiguration config) {
      this(config, null);
   }

   public AmazonHttpClient(ClientConfiguration config, RequestMetricCollector requestMetricCollector) {
      this(config, requestMetricCollector, false);
   }

   public AmazonHttpClient(ClientConfiguration config, RequestMetricCollector requestMetricCollector, boolean useBrowserCompatibleHostNameVerifier) {
      this(config, requestMetricCollector, useBrowserCompatibleHostNameVerifier, false);
   }

   public AmazonHttpClient(
      ClientConfiguration config,
      RequestMetricCollector requestMetricCollector,
      boolean useBrowserCompatibleHostNameVerifier,
      boolean calculateCRC32FromCompressedData
   ) {
      this(config, null, requestMetricCollector, useBrowserCompatibleHostNameVerifier, calculateCRC32FromCompressedData);
   }

   private AmazonHttpClient(
      ClientConfiguration config,
      RetryPolicy retryPolicy,
      RequestMetricCollector requestMetricCollector,
      boolean useBrowserCompatibleHostNameVerifier,
      boolean calculateCRC32FromCompressedData
   ) {
      this(
         config, retryPolicy, requestMetricCollector, HttpClientSettings.adapt(config, useBrowserCompatibleHostNameVerifier, calculateCRC32FromCompressedData)
      );
      this.httpClient = httpClientFactory.create(this.httpClientSettings);
   }

   @SdkTestInternalApi
   public AmazonHttpClient(
      ClientConfiguration clientConfig, ConnectionManagerAwareHttpClient httpClient, RequestMetricCollector requestMetricCollector, TokenBucket tokenBucket
   ) {
      this(clientConfig, null, requestMetricCollector, HttpClientSettings.adapt(clientConfig, false));
      this.httpClient = httpClient;
      this.tokenBucket = tokenBucket;
   }

   private AmazonHttpClient(
      ClientConfiguration clientConfig, RetryPolicy retryPolicy, RequestMetricCollector requestMetricCollector, HttpClientSettings httpClientSettings
   ) {
      this.config = clientConfig;
      this.retryPolicy = (RetryPolicy)(retryPolicy == null ? new RetryPolicyAdapter(clientConfig.getRetryPolicy(), clientConfig) : retryPolicy);
      this.retryMode = clientConfig.getRetryMode() == null ? clientConfig.getRetryPolicy().getRetryMode() : clientConfig.getRetryMode();
      this.httpClientSettings = httpClientSettings;
      this.requestMetricCollector = requestMetricCollector;
      this.responseMetadataCache = (MetadataCache)(clientConfig.getCacheResponseMetadata()
         ? new ResponseMetadataCache(clientConfig.getResponseMetadataCacheSize())
         : new NullResponseMetadataCache());
      this.httpRequestTimer = new HttpRequestTimer();
      this.clientExecutionTimer = new ClientExecutionTimer();
      int throttledRetryMaxCapacity = clientConfig.useThrottledRetries() ? 5 * this.config.getMaxConsecutiveRetriesBeforeThrottling() : -1;
      this.retryCapacity = new CapacityManager(throttledRetryMaxCapacity);
      this.tokenBucket = new TokenBucket();
      this.sdkRequestHeaderProvider = new SdkRequestRetryHeaderProvider(this.config, this.retryPolicy, this.clockSkewAdjuster);
   }

   public static AmazonHttpClient.Builder builder() {
      return new AmazonHttpClient.Builder();
   }

   private static boolean isTemporaryRedirect(org.apache.http.HttpResponse response) {
      int status = response.getStatusLine().getStatusCode();
      return status == 307 && response.getHeaders("Location") != null && response.getHeaders("Location").length > 0;
   }

   @Override
   protected void finalize() throws Throwable {
      this.shutdown();
      super.finalize();
   }

   public void shutdown() {
      this.clientExecutionTimer.shutdown();
      this.httpRequestTimer.shutdown();
      IdleConnectionReaper.removeConnectionManager(this.httpClient.getHttpClientConnectionManager());
      this.httpClient.getHttpClientConnectionManager().shutdown();
   }

   static void configUnreliableTestConditions(UnreliableTestConfig config) {
      unreliableTestConfig = config;
   }

   @SdkTestInternalApi
   public HttpRequestTimer getHttpRequestTimer() {
      return this.httpRequestTimer;
   }

   @SdkTestInternalApi
   public ClientExecutionTimer getClientExecutionTimer() {
      return this.clientExecutionTimer;
   }

   public ResponseMetadata getResponseMetadataForRequest(AmazonWebServiceRequest request) {
      return this.responseMetadataCache.get(request);
   }

   public RequestMetricCollector getRequestMetricCollector() {
      return this.requestMetricCollector;
   }

   public int getTimeOffset() {
      return this.timeOffset;
   }

   @Deprecated
   public <T> Response<T> execute(
      Request<?> request,
      HttpResponseHandler<AmazonWebServiceResponse<T>> responseHandler,
      HttpResponseHandler<AmazonServiceException> errorResponseHandler,
      ExecutionContext executionContext
   ) {
      return this.execute(request, responseHandler, errorResponseHandler, executionContext, new AmazonWebServiceRequestAdapter(request.getOriginalRequest()));
   }

   @SdkInternalApi
   public <T> Response<T> execute(
      Request<?> request,
      HttpResponseHandler<AmazonWebServiceResponse<T>> responseHandler,
      HttpResponseHandler<AmazonServiceException> errorResponseHandler,
      ExecutionContext executionContext,
      RequestConfig requestConfig
   ) {
      HttpResponseHandler<T> adaptedRespHandler = new AwsResponseHandlerAdapter<>(
         this.getNonNullResponseHandler(responseHandler), request, executionContext.getAwsRequestMetrics(), this.responseMetadataCache
      );
      return this.requestExecutionBuilder()
         .request(request)
         .requestConfig(requestConfig)
         .errorResponseHandler(new AwsErrorResponseHandler(errorResponseHandler, executionContext.getAwsRequestMetrics(), this.config))
         .executionContext(executionContext)
         .execute(adaptedRespHandler);
   }

   private <T> HttpResponseHandler<T> getNonNullResponseHandler(HttpResponseHandler<T> responseHandler) {
      return responseHandler != null ? responseHandler : new HttpResponseHandler<T>() {
         @Override
         public T handle(HttpResponse response) throws Exception {
            return null;
         }

         @Override
         public boolean needsConnectionLeftOpen() {
            return false;
         }
      };
   }

   public AmazonHttpClient.RequestExecutionBuilder requestExecutionBuilder() {
      return new AmazonHttpClient.RequestExecutionBuilderImpl();
   }

   static {
      List<String> problematicJvmVersions = Arrays.asList("1.6.0_06", "1.6.0_13", "1.6.0_17", "1.6.0_65", "1.7.0_45");
      String jvmVersion = System.getProperty("java.version");
      if (problematicJvmVersions.contains(jvmVersion)) {
         log.warn(
            "Detected a possible problem with the current JVM version ("
               + jvmVersion
               + ").  If you experience XML parsing problems using the SDK, try upgrading to a more recent JVM update."
         );
      }
   }

   public static class Builder {
      private ClientConfiguration clientConfig;
      private RetryPolicy retryPolicy;
      private RequestMetricCollector requestMetricCollector;
      private boolean useBrowserCompatibleHostNameVerifier;
      private boolean calculateCRC32FromCompressedData;

      private Builder() {
      }

      public AmazonHttpClient.Builder clientConfiguration(ClientConfiguration clientConfig) {
         this.clientConfig = clientConfig;
         return this;
      }

      public AmazonHttpClient.Builder retryPolicy(RetryPolicy retryPolicy) {
         this.retryPolicy = retryPolicy;
         return this;
      }

      public AmazonHttpClient.Builder requestMetricCollector(RequestMetricCollector requestMetricCollector) {
         this.requestMetricCollector = requestMetricCollector;
         return this;
      }

      public AmazonHttpClient.Builder useBrowserCompatibleHostNameVerifier(boolean useBrowserCompatibleHostNameVerifier) {
         this.useBrowserCompatibleHostNameVerifier = useBrowserCompatibleHostNameVerifier;
         return this;
      }

      public AmazonHttpClient.Builder calculateCRC32FromCompressedData(boolean calculateCRC32FromCompressedData) {
         this.calculateCRC32FromCompressedData = calculateCRC32FromCompressedData;
         return this;
      }

      public AmazonHttpClient build() {
         return new AmazonHttpClient(
            this.clientConfig, this.retryPolicy, this.requestMetricCollector, this.useBrowserCompatibleHostNameVerifier, this.calculateCRC32FromCompressedData
         );
      }
   }

   public interface RequestExecutionBuilder {
      AmazonHttpClient.RequestExecutionBuilder request(Request<?> var1);

      AmazonHttpClient.RequestExecutionBuilder errorResponseHandler(HttpResponseHandler<? extends SdkBaseException> var1);

      AmazonHttpClient.RequestExecutionBuilder executionContext(ExecutionContext var1);

      AmazonHttpClient.RequestExecutionBuilder requestConfig(RequestConfig var1);

      <Output> Response<Output> execute(HttpResponseHandler<Output> var1);

      Response<Void> execute();
   }

   private class RequestExecutionBuilderImpl implements AmazonHttpClient.RequestExecutionBuilder {
      private Request<?> request;
      private RequestConfig requestConfig;
      private HttpResponseHandler<? extends SdkBaseException> errorResponseHandler;
      private ExecutionContext executionContext = new ExecutionContext();

      private RequestExecutionBuilderImpl() {
      }

      @Override
      public AmazonHttpClient.RequestExecutionBuilder request(Request<?> request) {
         this.request = request;
         return this;
      }

      @Override
      public AmazonHttpClient.RequestExecutionBuilder errorResponseHandler(HttpResponseHandler<? extends SdkBaseException> errorResponseHandler) {
         this.errorResponseHandler = errorResponseHandler;
         return this;
      }

      @Override
      public AmazonHttpClient.RequestExecutionBuilder executionContext(ExecutionContext executionContext) {
         this.executionContext = executionContext;
         return this;
      }

      @Override
      public AmazonHttpClient.RequestExecutionBuilder requestConfig(RequestConfig requestConfig) {
         this.requestConfig = requestConfig;
         return this;
      }

      @Override
      public <Output> Response<Output> execute(HttpResponseHandler<Output> responseHandler) {
         RequestConfig config = (RequestConfig)(this.requestConfig != null
            ? this.requestConfig
            : new AmazonWebServiceRequestAdapter(this.request.getOriginalRequest()));
         return AmazonHttpClient.this.new RequestExecutor<Output>(
               this.request,
               config,
               AmazonHttpClient.this.getNonNullResponseHandler(this.errorResponseHandler),
               AmazonHttpClient.this.getNonNullResponseHandler(responseHandler),
               this.executionContext,
               this.getRequestHandlers()
            )
            .execute();
      }

      @Override
      public Response<Void> execute() {
         return this.execute(null);
      }

      private List<RequestHandler2> getRequestHandlers() {
         List<RequestHandler2> requestHandler2s = this.executionContext.getRequestHandler2s();
         return requestHandler2s == null ? Collections.emptyList() : requestHandler2s;
      }
   }

   private class RequestExecutor<Output> {
      private final Request<?> request;
      private final RequestConfig requestConfig;
      private final HttpResponseHandler<? extends SdkBaseException> errorResponseHandler;
      private final HttpResponseHandler<Output> responseHandler;
      private final ExecutionContext executionContext;
      private final List<RequestHandler2> requestHandler2s;
      private final AWSRequestMetrics awsRequestMetrics;
      private RequestHandler2 csmRequestHandler;

      private RequestExecutor(
         Request<?> request,
         RequestConfig requestConfig,
         HttpResponseHandler<? extends SdkBaseException> errorResponseHandler,
         HttpResponseHandler<Output> responseHandler,
         ExecutionContext executionContext,
         List<RequestHandler2> requestHandler2s
      ) {
         this.request = request;
         this.requestConfig = requestConfig;
         this.errorResponseHandler = errorResponseHandler;
         this.responseHandler = responseHandler;
         this.executionContext = executionContext;
         this.requestHandler2s = requestHandler2s;
         this.awsRequestMetrics = executionContext.getAwsRequestMetrics();

         for(RequestHandler2 requestHandler2 : requestHandler2s) {
            if (requestHandler2 instanceof ClientSideMonitoringRequestHandler) {
               this.csmRequestHandler = requestHandler2;
               break;
            }
         }
      }

      private Response<Output> execute() {
         if (this.executionContext == null) {
            throw new SdkClientException("Internal SDK Error: No execution context parameter specified.");
         } else {
            Response ae;
            try {
               ae = this.executeWithTimer();
            } catch (InterruptedException var6) {
               throw this.handleInterruptedException(var6);
            } catch (AbortedException var7) {
               throw this.handleAbortedException(var7);
            } finally {
               if (this.executionContext.getClientExecutionTrackerTask().hasTimeoutExpired()) {
                  Thread.interrupted();
               }
            }

            return ae;
         }
      }

      private Response<Output> executeWithTimer() throws InterruptedException {
         ClientExecutionAbortTrackerTask clientExecutionTrackerTask = AmazonHttpClient.this.clientExecutionTimer
            .startTimer(this.getClientExecutionTimeout(this.requestConfig));

         Response<Output> outputResponse;
         try {
            this.executionContext.setClientExecutionTrackerTask(clientExecutionTrackerTask);
            outputResponse = this.doExecute();
         } finally {
            this.executionContext.getClientExecutionTrackerTask().cancelTask();
         }

         return outputResponse;
      }

      private Response<Output> doExecute() throws InterruptedException {
         this.runBeforeRequestHandlers();
         this.setSdkTransactionId(this.request);
         this.setUserAgent(this.request);
         this.setTraceId(this.request);
         ProgressListener listener = this.requestConfig.getProgressListener();
         this.request.getHeaders().putAll(AmazonHttpClient.this.config.getHeaders());
         this.request.getHeaders().putAll(this.requestConfig.getCustomRequestHeaders());
         this.mergeQueryParameters(this.requestConfig.getCustomQueryParameters());
         Response<Output> response = null;
         InputStream origContent = this.request.getContent();
         InputStream toBeClosed = this.beforeRequest();
         InputStream notCloseable = toBeClosed == null ? null : ReleasableInputStream.wrap(toBeClosed).disableClose();
         this.request.setContent(notCloseable);

         Response e;
         try {
            SDKProgressPublisher.publishProgress(listener, ProgressEventType.CLIENT_REQUEST_STARTED_EVENT);
            response = this.executeHelper();
            SDKProgressPublisher.publishProgress(listener, ProgressEventType.CLIENT_REQUEST_SUCCESS_EVENT);
            this.awsRequestMetrics.endEvent(AwsClientSideMonitoringMetrics.ApiCallLatency);
            this.awsRequestMetrics.getTimingInfo().endTiming();
            this.afterResponse(response);
            e = response;
         } catch (AmazonClientException var10) {
            SDKProgressPublisher.publishProgress(listener, ProgressEventType.CLIENT_REQUEST_FAILED_EVENT);
            this.awsRequestMetrics.endEvent(AwsClientSideMonitoringMetrics.ApiCallLatency);
            this.afterError(response, var10);
            throw var10;
         } finally {
            this.closeQuietlyForRuntimeExceptions(toBeClosed, AmazonHttpClient.log);
            this.request.setContent(origContent);
         }

         return e;
      }

      private void closeQuietlyForRuntimeExceptions(Closeable c, Log log) {
         try {
            IOUtils.closeQuietly(c, log);
         } catch (RuntimeException var4) {
            if (log.isDebugEnabled()) {
               log.debug("Unable to close closeable", var4);
            }
         }
      }

      private void runBeforeRequestHandlers() {
         AWSCredentials credentials = this.getCredentialsFromContext();
         this.request.addHandlerContext(HandlerContextKey.AWS_CREDENTIALS, credentials);

         for(RequestHandler2 requestHandler2 : this.requestHandler2s) {
            if (requestHandler2 instanceof CredentialsRequestHandler) {
               ((CredentialsRequestHandler)requestHandler2).setCredentials(credentials);
            }

            requestHandler2.beforeRequest(this.request);
         }
      }

      private RuntimeException handleInterruptedException(InterruptedException e) {
         if (e instanceof SdkInterruptedException && ((SdkInterruptedException)e).getResponse() != null) {
            ((SdkInterruptedException)e).getResponse().getHttpResponse().getHttpRequest().abort();
         }

         if (this.executionContext.getClientExecutionTrackerTask().hasTimeoutExpired()) {
            Thread.interrupted();
            ClientExecutionTimeoutException exception = new ClientExecutionTimeoutException();
            this.reportClientExecutionTimeout(exception);
            return exception;
         } else {
            Thread.currentThread().interrupt();
            return new AbortedException(e);
         }
      }

      private RuntimeException handleAbortedException(AbortedException ae) {
         if (this.executionContext.getClientExecutionTrackerTask().hasTimeoutExpired()) {
            Thread.interrupted();
            ClientExecutionTimeoutException exception = new ClientExecutionTimeoutException();
            this.reportClientExecutionTimeout(exception);
            return exception;
         } else {
            Thread.currentThread().interrupt();
            return ae;
         }
      }

      private void reportClientExecutionTimeout(ClientExecutionTimeoutException exception) {
         if (this.csmRequestHandler != null) {
            this.csmRequestHandler.afterError(this.request, null, exception);
         }
      }

      private void checkInterrupted() throws InterruptedException {
         this.checkInterrupted(null);
      }

      private void checkInterrupted(Response<?> response) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new SdkInterruptedException(response);
         }
      }

      private void mergeQueryParameters(Map<String, List<String>> params) {
         Map<String, List<String>> existingParams = this.request.getParameters();

         for(Entry<String, List<String>> param : params.entrySet()) {
            String pName = param.getKey();
            List<String> pValues = param.getValue();
            existingParams.put(pName, CollectionUtils.mergeLists(existingParams.get(pName), pValues));
         }
      }

      private InputStream beforeRequest() {
         ProgressListener listener = this.requestConfig.getProgressListener();
         this.reportContentLength(listener);
         if (this.request.getContent() == null) {
            return null;
         } else {
            InputStream content = this.monitorStreamProgress(listener, this.buffer(this.makeResettable(this.request.getContent())));
            return AmazonHttpClient.unreliableTestConfig == null ? content : this.wrapWithUnreliableStream(content);
         }
      }

      private void reportContentLength(ProgressListener listener) {
         Map<String, String> headers = this.request.getHeaders();
         String contentLengthStr = headers.get("Content-Length");
         if (contentLengthStr != null) {
            try {
               long contentLength = Long.parseLong(contentLengthStr);
               SDKProgressPublisher.publishRequestContentLength(listener, contentLength);
            } catch (NumberFormatException var6) {
               AmazonHttpClient.log.warn("Cannot parse the Content-Length header of the request.");
            }
         }
      }

      private InputStream makeResettable(InputStream content) {
         if (!content.markSupported() && content instanceof FileInputStream) {
            try {
               return new ResettableInputStream((FileInputStream)content);
            } catch (IOException var3) {
               if (AmazonHttpClient.log.isDebugEnabled()) {
                  AmazonHttpClient.log.debug("For the record; ignore otherwise", var3);
               }
            }
         }

         return content;
      }

      private InputStream buffer(InputStream content) {
         if (!content.markSupported()) {
            content = new SdkBufferedInputStream(content);
         }

         return content;
      }

      private InputStream monitorStreamProgress(ProgressListener listener, InputStream content) {
         return ProgressInputStream.inputStreamForRequest(content, listener);
      }

      private InputStream wrapWithUnreliableStream(InputStream content) {
         return new UnreliableFilterInputStream(content, AmazonHttpClient.unreliableTestConfig.isFakeIOException())
            .withBytesReadBeforeException(AmazonHttpClient.unreliableTestConfig.getBytesReadBeforeException())
            .withMaxNumErrors(AmazonHttpClient.unreliableTestConfig.getMaxNumErrors())
            .withResetIntervalBeforeException(AmazonHttpClient.unreliableTestConfig.getResetIntervalBeforeException());
      }

      private void afterError(Response<?> response, AmazonClientException e) throws InterruptedException {
         for(RequestHandler2 handler2 : this.requestHandler2s) {
            handler2.afterError(this.request, response, e);
            this.checkInterrupted(response);
         }
      }

      private <T> void afterResponse(Response<T> response) throws InterruptedException {
         for(RequestHandler2 handler2 : this.requestHandler2s) {
            handler2.afterResponse(this.request, response);
            this.checkInterrupted(response);
         }
      }

      private <T> void beforeAttempt(HandlerBeforeAttemptContext context) throws InterruptedException {
         for(RequestHandler2 handler2 : this.requestHandler2s) {
            handler2.beforeAttempt(context);
            this.checkInterrupted();
         }
      }

      private <T> void afterAttempt(HandlerAfterAttemptContext context) throws InterruptedException {
         for(RequestHandler2 handler2 : this.requestHandler2s) {
            handler2.afterAttempt(context);
            this.checkInterrupted(context.getResponse());
         }
      }

      private Response<Output> executeHelper() throws InterruptedException {
         this.awsRequestMetrics
            .addPropertyWith(AWSRequestMetrics.Field.RequestType, this.requestConfig.getRequestType())
            .addPropertyWith(AWSRequestMetrics.Field.ServiceName, this.request.getServiceName())
            .addPropertyWith(AWSRequestMetrics.Field.ServiceEndpoint, this.request.getEndpoint());
         Map<String, List<String>> originalParameters = new LinkedHashMap<>(this.request.getParameters());
         Map<String, String> originalHeaders = new HashMap<>(this.request.getHeaders());
         AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams = new AmazonHttpClient.RequestExecutor.ExecOneRequestParams();
         InputStream originalContent = this.request.getContent();
         if (originalContent != null && originalContent.markSupported() && !(originalContent instanceof BufferedInputStream)) {
            int readLimit = this.requestConfig.getRequestClientOptions().getReadLimit();
            originalContent.mark(readLimit);
         }

         this.awsRequestMetrics.startEvent(AwsClientSideMonitoringMetrics.ApiCallLatency);

         while(true) {
            this.checkInterrupted();
            if (originalContent instanceof BufferedInputStream && originalContent.markSupported()) {
               int readLimit = this.requestConfig.getRequestClientOptions().getReadLimit();
               originalContent.mark(readLimit);
            }

            execOneParams.initPerRetry();
            URI redirectedURI = execOneParams.redirectedURI;
            if (redirectedURI != null) {
               String scheme = redirectedURI.getScheme();
               String beforeAuthority = scheme == null ? "" : scheme + "://";
               String authority = redirectedURI.getAuthority();
               String path = redirectedURI.getPath();
               this.request.setEndpoint(URI.create(beforeAuthority + authority));
               this.request.setResourcePath(SdkHttpUtils.urlEncode(path, true));
               this.awsRequestMetrics.addPropertyWith(AWSRequestMetrics.Field.RedirectLocation, redirectedURI.toString());
            }

            if (execOneParams.authRetryParam != null) {
               this.request.setEndpoint(execOneParams.authRetryParam.getEndpointForRetry());
            }

            this.awsRequestMetrics.setCounter(AWSRequestMetrics.Field.RequestCount, (long)execOneParams.requestCount);
            if (execOneParams.isRetry()) {
               this.request.setParameters(originalParameters);
               this.request.setHeaders(originalHeaders);
               this.request.setContent(originalContent);
            }

            Response<Output> response = null;
            Exception savedException = null;
            boolean thrown = false;

            try {
               HandlerBeforeAttemptContext beforeAttemptContext = HandlerBeforeAttemptContext.builder().withRequest(this.request).build();
               this.beforeAttempt(beforeAttemptContext);
               response = this.executeOneRequest(execOneParams);
               savedException = execOneParams.retriedException;
               if (response != null) {
                  return response;
               }
            } catch (IOException var26) {
               savedException = var26;
               this.handleRetryableException(execOneParams, var26);
            } catch (InterruptedException var27) {
               savedException = var27;
               thrown = true;
               throw var27;
            } catch (RuntimeException var28) {
               savedException = var28;
               thrown = true;
               throw (RuntimeException)this.lastReset(this.captureExceptionMetrics(var28));
            } catch (Error var29) {
               thrown = true;
               throw (Error)this.lastReset(this.captureExceptionMetrics(var29));
            } finally {
               if ((!execOneParams.leaveHttpConnectionOpen || thrown) && execOneParams.apacheResponse != null) {
                  HttpEntity entity = execOneParams.apacheResponse.getEntity();
                  if (entity != null) {
                     try {
                        IOUtils.closeQuietly(entity.getContent(), AmazonHttpClient.log);
                     } catch (IOException var25) {
                        AmazonHttpClient.log.warn("Cannot close the response content.", var25);
                     }
                  }
               }

               HandlerAfterAttemptContext afterAttemptContext = HandlerAfterAttemptContext.builder()
                  .withRequest(this.request)
                  .withResponse(response)
                  .withException(savedException)
                  .build();
               this.afterAttempt(afterAttemptContext);
            }
         }
      }

      private void handleRetryableException(AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams, Exception e) {
         this.captureExceptionMetrics(e);
         this.awsRequestMetrics.addProperty(AWSRequestMetrics.Field.AWSRequestID, null);
         SdkClientException sdkClientException;
         if (!(e instanceof SdkClientException)) {
            sdkClientException = new SdkClientException("Unable to execute HTTP request: " + e.getMessage(), e);
         } else {
            sdkClientException = (SdkClientException)e;
         }

         boolean willRetry = this.shouldRetry(execOneParams, sdkClientException);
         if (AmazonHttpClient.log.isTraceEnabled()) {
            AmazonHttpClient.log.trace(sdkClientException.getMessage() + (willRetry ? " Request will be retried." : ""), e);
         } else if (AmazonHttpClient.log.isDebugEnabled()) {
            AmazonHttpClient.log.debug(sdkClientException.getMessage() + (willRetry ? " Request will be retried." : ""));
         }

         if (!willRetry) {
            throw (SdkClientException)this.lastReset(sdkClientException);
         } else {
            execOneParams.retriedException = sdkClientException;
         }
      }

      private <T extends Throwable> T lastReset(T t) {
         try {
            InputStream content = this.request.getContent();
            if (content != null && content.markSupported()) {
               content.reset();
            }
         } catch (Exception var3) {
            AmazonHttpClient.log.debug("FYI: failed to reset content inputstream before throwing up", var3);
         }

         return t;
      }

      private AWSCredentials getCredentialsFromContext() {
         AWSCredentialsProvider credentialsProvider = this.executionContext.getCredentialsProvider();
         AWSCredentials credentials = null;
         if (credentialsProvider != null) {
            this.awsRequestMetrics.startEvent(AWSRequestMetrics.Field.CredentialsRequestTime);

            try {
               credentials = credentialsProvider.getCredentials();
            } finally {
               this.awsRequestMetrics.endEvent(AWSRequestMetrics.Field.CredentialsRequestTime);
            }
         }

         return credentials;
      }

      private Response<Output> executeOneRequest(AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams) throws IOException, InterruptedException {
         if (execOneParams.isRetry()) {
            this.resetRequestInputStream(this.request, execOneParams.retriedException);
         }

         this.checkInterrupted();
         if (AmazonHttpClient.requestLog.isDebugEnabled()) {
            AmazonHttpClient.requestLog.debug((execOneParams.isRetry() ? "Retrying " : "Sending ") + "Request: " + this.request);
         }

         AWSCredentials credentials = this.getCredentialsFromContext();
         ProgressListener listener = this.requestConfig.getProgressListener();
         this.getSendToken();
         if (execOneParams.isRetry()) {
            this.pauseBeforeRetry(execOneParams, listener);
         }

         this.updateRetryHeaderInfo(this.request, execOneParams);
         AmazonHttpClient.this.sdkRequestHeaderProvider.addSdkRequestRetryHeader(this.request, execOneParams.requestCount);
         execOneParams.newSigner(this.request, this.executionContext);
         if (execOneParams.signer != null && (credentials != null || execOneParams.signer instanceof CanHandleNullCredentials)) {
            this.awsRequestMetrics.startEvent(AWSRequestMetrics.Field.RequestSigningTime);

            try {
               if (AmazonHttpClient.this.timeOffset != 0) {
                  this.request.setTimeOffset(AmazonHttpClient.this.timeOffset);
               }

               execOneParams.signer.sign(this.request, credentials);
            } finally {
               this.awsRequestMetrics.endEvent(AWSRequestMetrics.Field.RequestSigningTime);
            }
         }

         this.checkInterrupted();
         execOneParams.newApacheRequest(AmazonHttpClient.this.httpRequestFactory, this.request, AmazonHttpClient.this.httpClientSettings);
         this.captureConnectionPoolMetrics();
         HttpClientContext localRequestContext = ApacheUtils.newClientContext(
            AmazonHttpClient.this.httpClientSettings, ImmutableMapParameter.of(AWSRequestMetrics.SIMPLE_NAME, this.awsRequestMetrics)
         );
         execOneParams.resetBeforeHttpRequest();
         SDKProgressPublisher.publishProgress(listener, ProgressEventType.HTTP_REQUEST_STARTED_EVENT);
         this.awsRequestMetrics.startEvent(AWSRequestMetrics.Field.HttpRequestTime);
         this.awsRequestMetrics.setCounter(AWSRequestMetrics.Field.RetryCapacityConsumed, (long)AmazonHttpClient.this.retryCapacity.consumedCapacity());
         this.executionContext.getClientExecutionTrackerTask().setCurrentHttpRequest(execOneParams.apacheRequest);
         HttpRequestAbortTaskTracker requestAbortTaskTracker = AmazonHttpClient.this.httpRequestTimer
            .startTimer(execOneParams.apacheRequest, this.getRequestTimeout(this.requestConfig));

         try {
            execOneParams.apacheResponse = AmazonHttpClient.this.httpClient.execute(execOneParams.apacheRequest, localRequestContext);
            if (this.shouldBufferHttpEntity(this.responseHandler.needsConnectionLeftOpen(), this.executionContext, execOneParams, requestAbortTaskTracker)) {
               execOneParams.apacheResponse.setEntity(new BufferedHttpEntity(execOneParams.apacheResponse.getEntity()));
            }
         } catch (IOException var15) {
            if (this.executionContext.getClientExecutionTrackerTask().hasTimeoutExpired()) {
               throw new InterruptedException();
            }

            if (requestAbortTaskTracker.httpRequestAborted()) {
               if (var15 instanceof RequestAbortedException) {
                  Thread.interrupted();
               }

               throw new HttpRequestTimeoutException(var15);
            }

            throw var15;
         } finally {
            requestAbortTaskTracker.cancelTask();
            this.awsRequestMetrics.endEvent(AWSRequestMetrics.Field.HttpRequestTime);
         }

         SDKProgressPublisher.publishProgress(listener, ProgressEventType.HTTP_REQUEST_COMPLETED_EVENT);
         StatusLine statusLine = execOneParams.apacheResponse.getStatusLine();
         int statusCode = statusLine == null ? -1 : statusLine.getStatusCode();
         AmazonHttpClient.this.clockSkewAdjuster
            .updateEstimatedSkew(new ClockSkewAdjuster.AdjustmentRequest().clientRequest(this.request).serviceResponse(execOneParams.apacheResponse));
         return ApacheUtils.isRequestSuccessful(execOneParams.apacheResponse)
            ? this.handleSuccessResponse(execOneParams, localRequestContext, statusCode)
            : this.handleServiceErrorResponse(execOneParams, localRequestContext, statusCode);
      }

      private boolean isSignerOverridden() {
         return AmazonHttpClient.this.config != null && AmazonHttpClient.this.config.getSignerOverride() != null;
      }

      private Response<Output> handleServiceErrorResponse(
         AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams, HttpClientContext localRequestContext, int statusCode
      ) throws IOException, InterruptedException {
         if (AmazonHttpClient.isTemporaryRedirect(execOneParams.apacheResponse)) {
            Header[] locationHeaders = execOneParams.apacheResponse.getHeaders("location");
            String redirectedLocation = locationHeaders[0].getValue();
            if (AmazonHttpClient.log.isDebugEnabled()) {
               AmazonHttpClient.log.debug("Redirecting to: " + redirectedLocation);
            }

            execOneParams.redirectedURI = URI.create(redirectedLocation);
            this.awsRequestMetrics.addPropertyWith(AWSRequestMetrics.Field.StatusCode, statusCode).addPropertyWith(AWSRequestMetrics.Field.AWSRequestID, null);
            return null;
         } else {
            execOneParams.leaveHttpConnectionOpen = this.errorResponseHandler.needsConnectionLeftOpen();
            SdkBaseException exception = this.handleErrorResponse(execOneParams.apacheRequest, execOneParams.apacheResponse, localRequestContext);
            ClockSkewAdjuster.ClockSkewAdjustment clockSkewAdjustment = AmazonHttpClient.this.clockSkewAdjuster
               .getAdjustment(
                  new ClockSkewAdjuster.AdjustmentRequest().exception(exception).clientRequest(this.request).serviceResponse(execOneParams.apacheResponse)
               );
            if (clockSkewAdjustment.shouldAdjustForSkew()) {
               AmazonHttpClient.this.timeOffset = clockSkewAdjustment.inSeconds();
               this.request.setTimeOffset(AmazonHttpClient.this.timeOffset);
               SDKGlobalTime.setGlobalTimeOffset(AmazonHttpClient.this.timeOffset);
            }

            if (RetryUtils.isThrottlingException(exception)) {
               AmazonHttpClient.this.tokenBucket.updateClientSendingRate(true);
            }

            execOneParams.authRetryParam = null;
            AuthErrorRetryStrategy authRetry = this.executionContext.getAuthErrorRetryStrategy();
            if (authRetry != null && exception instanceof AmazonServiceException) {
               HttpResponse httpResponse = ApacheUtils.createResponse(
                  this.request, execOneParams.apacheRequest, execOneParams.apacheResponse, localRequestContext
               );
               execOneParams.authRetryParam = authRetry.shouldRetryWithAuthParam(this.request, httpResponse, (AmazonServiceException)exception);
            }

            if (execOneParams.authRetryParam == null && !this.shouldRetry(execOneParams, exception)) {
               throw exception;
            } else {
               if (RetryUtils.isThrottlingException(exception)) {
                  this.awsRequestMetrics
                     .incrementCounterWith(AWSRequestMetrics.Field.ThrottleException)
                     .addProperty(AWSRequestMetrics.Field.ThrottleException, exception);
               }

               execOneParams.retriedException = exception;
               return null;
            }
         }
      }

      private Response<Output> handleSuccessResponse(
         AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams, HttpClientContext localRequestContext, int statusCode
      ) throws IOException, InterruptedException {
         this.awsRequestMetrics.addProperty(AWSRequestMetrics.Field.StatusCode, statusCode);
         execOneParams.leaveHttpConnectionOpen = this.responseHandler.needsConnectionLeftOpen();
         HttpResponse httpResponse = ApacheUtils.createResponse(this.request, execOneParams.apacheRequest, execOneParams.apacheResponse, localRequestContext);
         Output response = this.handleResponse(httpResponse);
         if (execOneParams.isRetry() && this.executionContext.retryCapacityConsumed()) {
            AmazonHttpClient.this.retryCapacity.release(execOneParams.lastConsumedRetryCapacity);
         } else {
            AmazonHttpClient.this.retryCapacity.release();
         }

         AmazonHttpClient.this.tokenBucket.updateClientSendingRate(false);
         return new Response<>(response, httpResponse);
      }

      private void resetRequestInputStream(Request<?> request, SdkBaseException retriedException) throws ResetException {
         InputStream requestInputStream = request.getContent();
         if (requestInputStream != null && requestInputStream.markSupported()) {
            try {
               requestInputStream.reset();
            } catch (IOException var6) {
               ResetException resetException = new ResetException(
                  "The request to the service failed with a retryable reason, but resetting the request input stream has failed. See exception.getExtraInfo or debug-level logging for the original failure that caused this retry.",
                  var6
               );
               resetException.setExtraInfo(retriedException.getMessage());
               throw resetException;
            }
         }
      }

      private boolean shouldBufferHttpEntity(
         boolean needsConnectionLeftOpen,
         ExecutionContext execContext,
         AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execParams,
         HttpRequestAbortTaskTracker requestAbortTaskTracker
      ) {
         return (execContext.getClientExecutionTrackerTask().isEnabled() || requestAbortTaskTracker.isEnabled())
            && !needsConnectionLeftOpen
            && execParams.apacheResponse.getEntity() != null;
      }

      private void captureConnectionPoolMetrics() {
         if (this.awsRequestMetrics.isEnabled() && AmazonHttpClient.this.httpClient.getHttpClientConnectionManager() instanceof ConnPoolControl) {
            PoolStats stats = ((ConnPoolControl)AmazonHttpClient.this.httpClient.getHttpClientConnectionManager()).getTotalStats();
            this.awsRequestMetrics
               .withCounter(AWSRequestMetrics.Field.HttpClientPoolAvailableCount, (long)stats.getAvailable())
               .withCounter(AWSRequestMetrics.Field.HttpClientPoolLeasedCount, (long)stats.getLeased())
               .withCounter(AWSRequestMetrics.Field.HttpClientPoolPendingCount, (long)stats.getPending());
         }
      }

      private <T extends Throwable> T captureExceptionMetrics(T t) {
         this.awsRequestMetrics.incrementCounterWith(AWSRequestMetrics.Field.Exception).addProperty(AWSRequestMetrics.Field.Exception, t);
         if (t instanceof AmazonServiceException) {
            AmazonServiceException ase = (AmazonServiceException)t;
            if (RetryUtils.isThrottlingException(ase)) {
               this.awsRequestMetrics
                  .incrementCounterWith(AWSRequestMetrics.Field.ThrottleException)
                  .addProperty(AWSRequestMetrics.Field.ThrottleException, ase);
            }
         }

         return t;
      }

      private void setSdkTransactionId(Request<?> request) {
         request.addHeader("amz-sdk-invocation-id", new UUID(AmazonHttpClient.this.random.nextLong(), AmazonHttpClient.this.random.nextLong()).toString());
      }

      private void setUserAgent(Request<?> request) {
         RequestClientOptions opts = this.requestConfig.getRequestClientOptions();
         if (opts != null) {
            request.addHeader(
               "User-Agent", RuntimeHttpUtils.getUserAgent(AmazonHttpClient.this.config, opts.getClientMarker(RequestClientOptions.Marker.USER_AGENT))
            );
         } else {
            request.addHeader("User-Agent", RuntimeHttpUtils.getUserAgent(AmazonHttpClient.this.config, null));
         }
      }

      private void setTraceId(Request<?> request) {
         String traceIdHeader = request.getHeaders().get("X-Amzn-Trace-Id");
         if (StringUtils.isNullOrEmpty(traceIdHeader)) {
            String traceId = RuntimeHttpUtils.getLambdaEnvironmentTraceId();
            if (!StringUtils.isNullOrEmpty(traceId)) {
               request.addHeader("X-Amzn-Trace-Id", traceId);
            }
         }
      }

      private void updateRetryHeaderInfo(Request<?> request, AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneRequestParams) {
         int availableRetryCapacity = AmazonHttpClient.this.retryCapacity.availableCapacity();
         String headerValue = String.format(
            "%s/%s/%s", execOneRequestParams.requestCount - 1, execOneRequestParams.lastBackoffDelay, availableRetryCapacity >= 0 ? availableRetryCapacity : ""
         );
         request.addHeader("amz-sdk-retry", headerValue);
      }

      private boolean shouldRetry(AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams params, SdkBaseException exception) {
         int retriesAttempted = params.requestCount - 1;
         HttpRequestBase method = params.apacheRequest;
         if (method instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest)method).getEntity();
            if (entity != null && !entity.isRepeatable()) {
               if (AmazonHttpClient.log.isDebugEnabled()) {
                  AmazonHttpClient.log.debug("Entity not repeatable");
               }

               return false;
            }
         }

         RetryPolicyContext context = RetryPolicyContext.builder()
            .request(this.request)
            .originalRequest(this.requestConfig.getOriginalRequest())
            .exception(exception)
            .retriesAttempted(retriesAttempted)
            .httpStatusCode(params.getStatusCode())
            .build();
         if (!this.acquireRetryCapacity(context, params)) {
            return false;
         } else if (!AmazonHttpClient.this.retryPolicy.shouldRetry(context)) {
            if (this.executionContext.retryCapacityConsumed()) {
               AmazonHttpClient.this.retryCapacity.release(5);
            }

            this.reportMaxRetriesExceededIfRetryable(context);
            return false;
         } else {
            return true;
         }
      }

      private void getSendToken() {
         if (AmazonHttpClient.this.retryMode == RetryMode.ADAPTIVE) {
            if (!AmazonHttpClient.this.tokenBucket.acquire(1.0, this.fastFailRateLimiting())) {
               throw new SdkClientException("Unable to acquire enough send tokens to execute request.");
            }
         }
      }

      private boolean fastFailRateLimiting() {
         return AmazonHttpClient.this.config.getRetryPolicy().isFastFailRateLimiting();
      }

      private boolean acquireRetryCapacity(RetryPolicyContext context, AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams params) {
         switch(AmazonHttpClient.this.retryMode) {
            case LEGACY:
               return this.legacyAcquireRetryCapacity(context, params);
            case ADAPTIVE:
            case STANDARD:
               return this.standardAcquireRetryCapacity(context, params);
            default:
               throw new IllegalStateException("Unsupported retry mode: " + AmazonHttpClient.this.retryMode);
         }
      }

      private boolean standardAcquireRetryCapacity(RetryPolicyContext context, AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams params) {
         SdkBaseException exception = context.exception();
         return this.isTimeoutError(exception) ? this.doAcquireCapacity(context, 10, params) : this.doAcquireCapacity(context, 5, params);
      }

      private boolean isTimeoutError(SdkBaseException exception) {
         Throwable cause = exception.getCause();
         return cause instanceof ConnectTimeoutException || cause instanceof SocketTimeoutException;
      }

      private boolean legacyAcquireRetryCapacity(RetryPolicyContext context, AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams params) {
         return !RetryUtils.isThrottlingException(context.exception()) ? this.doAcquireCapacity(context, 5, params) : true;
      }

      private boolean doAcquireCapacity(RetryPolicyContext context, int retryCost, AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams params) {
         if (!AmazonHttpClient.this.retryCapacity.acquire(retryCost)) {
            this.awsRequestMetrics.incrementCounter(AWSRequestMetrics.Field.ThrottledRetryCount);
            this.reportMaxRetriesExceededIfRetryable(context);
            return false;
         } else {
            params.lastConsumedRetryCapacity = retryCost;
            this.executionContext.markRetryCapacityConsumed();
            return true;
         }
      }

      private void reportMaxRetriesExceededIfRetryable(RetryPolicyContext context) {
         if (AmazonHttpClient.this.retryPolicy instanceof RetryPolicyAdapter && ((RetryPolicyAdapter)AmazonHttpClient.this.retryPolicy).isRetryable(context)) {
            this.awsRequestMetrics.addPropertyWith(AwsClientSideMonitoringMetrics.MaxRetriesExceeded, true);
         }
      }

      private Output handleResponse(HttpResponse httpResponse) throws IOException, InterruptedException {
         ProgressListener listener = this.requestConfig.getProgressListener();

         try {
            CountingInputStream countingInputStream = null;
            InputStream is = httpResponse.getContent();
            if (is != null) {
               if (System.getProperty("com.amazonaws.sdk.enableRuntimeProfiling") != null) {
                  is = countingInputStream = new CountingInputStream(is);
                  httpResponse.setContent(is);
               }

               httpResponse.setContent(ProgressInputStream.inputStreamForResponse(is, listener));
            }

            Map<String, String> headers = httpResponse.getHeaders();
            String s = headers.get("Content-Length");
            if (s != null) {
               try {
                  long contentLength = Long.parseLong(s);
                  SDKProgressPublisher.publishResponseContentLength(listener, contentLength);
               } catch (NumberFormatException var18) {
                  AmazonHttpClient.log.warn("Cannot parse the Content-Length header of the response.");
               }
            }

            this.awsRequestMetrics.startEvent(AWSRequestMetrics.Field.ResponseProcessingTime);
            SDKProgressPublisher.publishProgress(listener, ProgressEventType.HTTP_RESPONSE_STARTED_EVENT);

            Output awsResponse;
            try {
               awsResponse = this.responseHandler.handle(this.beforeUnmarshalling(httpResponse));
            } finally {
               this.awsRequestMetrics.endEvent(AWSRequestMetrics.Field.ResponseProcessingTime);
            }

            SDKProgressPublisher.publishProgress(listener, ProgressEventType.HTTP_RESPONSE_COMPLETED_EVENT);
            if (countingInputStream != null) {
               this.awsRequestMetrics.setCounter(AWSRequestMetrics.Field.BytesProcessed, countingInputStream.getByteCount());
            }

            return awsResponse;
         } catch (CRC32MismatchException var19) {
            throw var19;
         } catch (IOException var20) {
            throw var20;
         } catch (AmazonClientException var21) {
            throw var21;
         } catch (InterruptedException var22) {
            throw var22;
         } catch (Exception var23) {
            String errorMessage = "Unable to unmarshall response ("
               + var23.getMessage()
               + "). Response Code: "
               + httpResponse.getStatusCode()
               + ", Response Text: "
               + httpResponse.getStatusText();
            throw new SdkClientException(errorMessage, var23);
         }
      }

      private HttpResponse beforeUnmarshalling(HttpResponse origHttpResponse) {
         HttpResponse toReturn = origHttpResponse;

         for(RequestHandler2 requestHandler : this.requestHandler2s) {
            toReturn = requestHandler.beforeUnmarshalling(this.request, toReturn);
         }

         return toReturn;
      }

      private SdkBaseException handleErrorResponse(HttpRequestBase method, org.apache.http.HttpResponse apacheHttpResponse, HttpContext context) throws IOException, InterruptedException {
         StatusLine statusLine = apacheHttpResponse.getStatusLine();
         int statusCode;
         String reasonPhrase;
         if (statusLine == null) {
            statusCode = -1;
            reasonPhrase = null;
         } else {
            statusCode = statusLine.getStatusCode();
            reasonPhrase = statusLine.getReasonPhrase();
         }

         HttpResponse response = ApacheUtils.createResponse(this.request, method, apacheHttpResponse, context);

         SdkBaseException exception;
         try {
            exception = this.errorResponseHandler.handle(response);
            if (AmazonHttpClient.requestLog.isDebugEnabled()) {
               AmazonHttpClient.requestLog.debug("Received error response: " + exception);
            }
         } catch (InterruptedException var11) {
            throw var11;
         } catch (Exception var12) {
            if (var12 instanceof IOException) {
               throw (IOException)var12;
            }

            String errorMessage = "Unable to unmarshall error response ("
               + var12.getMessage()
               + "). Response Code: "
               + (statusLine == null ? "None" : statusCode)
               + ", Response Text: "
               + reasonPhrase;
            throw new SdkClientException(errorMessage, var12);
         }

         exception.fillInStackTrace();
         return exception;
      }

      private void pauseBeforeRetry(AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams, ProgressListener listener) throws InterruptedException {
         SDKProgressPublisher.publishProgress(listener, ProgressEventType.CLIENT_REQUEST_RETRY_EVENT);
         this.awsRequestMetrics.startEvent(AWSRequestMetrics.Field.RetryPauseTime);

         try {
            this.doPauseBeforeRetry(execOneParams);
         } finally {
            this.awsRequestMetrics.endEvent(AWSRequestMetrics.Field.RetryPauseTime);
         }
      }

      private void doPauseBeforeRetry(AmazonHttpClient.RequestExecutor<Output>.ExecOneRequestParams execOneParams) throws InterruptedException {
         int retriesAttempted = execOneParams.requestCount - 2;
         RetryPolicyContext context = RetryPolicyContext.builder()
            .request(this.request)
            .originalRequest(this.requestConfig.getOriginalRequest())
            .retriesAttempted(retriesAttempted)
            .exception(execOneParams.retriedException)
            .build();
         if (context.exception() != null) {
            long delay = AmazonHttpClient.this.retryPolicy.computeDelayBeforeNextRetry(context);
            execOneParams.lastBackoffDelay = delay;
            if (AmazonHttpClient.log.isDebugEnabled()) {
               AmazonHttpClient.log.debug("Retriable error detected, will retry in " + delay + "ms, attempt number: " + retriesAttempted);
            }

            Thread.sleep(delay);
         }
      }

      private int getRequestTimeout(RequestConfig requestConfig) {
         return requestConfig.getRequestTimeout() != null ? requestConfig.getRequestTimeout() : AmazonHttpClient.this.config.getRequestTimeout();
      }

      private int getClientExecutionTimeout(RequestConfig requestConfig) {
         return requestConfig.getClientExecutionTimeout() != null
            ? requestConfig.getClientExecutionTimeout()
            : AmazonHttpClient.this.config.getClientExecutionTimeout();
      }

      private class ExecOneRequestParams {
         int requestCount;
         long lastBackoffDelay = 0L;
         SdkBaseException retriedException;
         HttpRequestBase apacheRequest;
         org.apache.http.HttpResponse apacheResponse;
         URI redirectedURI;
         AuthRetryParameters authRetryParam;
         int lastConsumedRetryCapacity;
         boolean leaveHttpConnectionOpen;
         private Signer signer;
         private URI signerURI;

         private ExecOneRequestParams() {
         }

         boolean isRetry() {
            return this.requestCount > 1 || this.redirectedURI != null || this.authRetryParam != null;
         }

         void initPerRetry() {
            ++this.requestCount;
            this.apacheRequest = null;
            this.apacheResponse = null;
            this.leaveHttpConnectionOpen = false;
         }

         void newSigner(Request<?> request, ExecutionContext execContext) {
            SignerProviderContext.Builder signerProviderContext = SignerProviderContext.builder()
               .withRequest(request)
               .withRequestConfig(RequestExecutor.this.requestConfig);
            if (this.authRetryParam != null) {
               this.signerURI = this.authRetryParam.getEndpointForRetry();
               this.signer = this.authRetryParam.getSignerForRetry();
               execContext.setSigner(this.signer);
            } else if (this.redirectedURI != null && !this.redirectedURI.equals(this.signerURI)) {
               this.signerURI = this.redirectedURI;
               this.signer = execContext.getSigner(signerProviderContext.withUri(this.signerURI).withIsRedirect(true).build());
               if (this.signer instanceof AWS4Signer) {
                  String regionName = ((AWS4Signer)this.signer).getRegionName();
                  if (regionName != null) {
                     request.addHandlerContext(HandlerContextKey.SIGNING_REGION, regionName);
                  }
               }
            } else if (this.signer == null) {
               this.signerURI = request.getEndpoint();
               this.signer = execContext.getSigner(signerProviderContext.withUri(this.signerURI).build());
            }
         }

         HttpRequestBase newApacheRequest(HttpRequestFactory<HttpRequestBase> httpRequestFactory, Request<?> request, HttpClientSettings options) throws IOException {
            this.apacheRequest = (HttpRequestBase)httpRequestFactory.create(request, options);
            if (this.redirectedURI != null) {
               this.apacheRequest.setURI(this.redirectedURI);
            }

            return this.apacheRequest;
         }

         void resetBeforeHttpRequest() {
            this.retriedException = null;
            this.authRetryParam = null;
            this.redirectedURI = null;
         }

         private Integer getStatusCode() {
            return this.apacheResponse != null && this.apacheResponse.getStatusLine() != null ? this.apacheResponse.getStatusLine().getStatusCode() : null;
         }
      }
   }
}
