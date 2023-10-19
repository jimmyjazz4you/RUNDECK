package com.amazonaws.monitoring.internal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.Response;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.handlers.HandlerAfterAttemptContext;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.timers.client.ClientExecutionTimeoutException;
import com.amazonaws.monitoring.ApiCallAttemptMonitoringEvent;
import com.amazonaws.monitoring.ApiCallMonitoringEvent;
import com.amazonaws.monitoring.MonitoringEvent;
import com.amazonaws.monitoring.MonitoringListener;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.AwsClientSideMonitoringMetrics;
import com.amazonaws.util.CollectionUtils;
import com.amazonaws.util.ImmutableMapParameter;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.Throwables;
import com.amazonaws.util.TimingInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public final class ClientSideMonitoringRequestHandler extends RequestHandler2 {
   private static final Log LOG = LogFactory.getLog(ClientSideMonitoringRequestHandler.class);
   private static final String X_AMZN_REQUEST_ID_HEADER_KEY = "x-amzn-RequestId";
   private static final String X_AMZ_REQUEST_ID_HEADER_KEY = "x-amz-request-id";
   private static final String X_AMZ_REQUEST_ID_2_HEADER_KEY = "x-amz-id-2";
   private static final String EXCEPTION_MESSAGE_KEY = "ExceptionMessage";
   private static final String EXCEPTION_KEY = "Exception";
   private static final String CLIENT_ID_KEY = "ClientId";
   private static final String USER_AGENT_KEY = "UserAgent";
   private static final HandlerContextKey<ApiCallAttemptMonitoringEvent> LAST_CALL_ATTEMPT = new HandlerContextKey<>("LastCallAttemptMonitoringEvent");
   private static final Integer VERSION = 1;
   private static final List<String> SECURITY_TOKENS = Arrays.asList("x-amz-security-token", "X-Amz-Security-Token");
   private static final Map<String, Integer> ENTRY_TO_MAX_SIZE = new ImmutableMapParameter.Builder<String, Integer>()
      .put("ClientId", 255)
      .put("UserAgent", 256)
      .put("ExceptionMessage", 512)
      .put("Exception", 128)
      .build();
   private final String clientId;
   private final Collection<MonitoringListener> monitoringListeners;

   public ClientSideMonitoringRequestHandler(String clientId, Collection<MonitoringListener> monitoringListeners) {
      this.clientId = this.trimValueIfExceedsMaxLength("ClientId", clientId);
      this.monitoringListeners = monitoringListeners;
   }

   @Override
   public void afterAttempt(HandlerAfterAttemptContext context) {
      ApiCallAttemptMonitoringEvent event = this.generateApiCallAttemptMonitoringEvent(context);
      context.getRequest().addHandlerContext(LAST_CALL_ATTEMPT, event);
      this.handToMonitoringListeners(event);
   }

   @Override
   public void afterResponse(Request<?> request, Response<?> response) {
      ApiCallMonitoringEvent event = this.generateApiCallMonitoringEvent(request);
      this.handToMonitoringListeners(event);
   }

   @Override
   public void afterError(Request<?> request, Response<?> response, Exception e) {
      ApiCallMonitoringEvent event = this.generateApiCallMonitoringEvent(request, e);
      this.handToMonitoringListeners(event);
   }

   private ApiCallAttemptMonitoringEvent generateApiCallAttemptMonitoringEvent(HandlerAfterAttemptContext context) {
      Request<?> request = context.getRequest();
      AWSRequestMetrics metrics = context.getRequest().getAWSRequestMetrics();
      String apiName = request.getHandlerContext(HandlerContextKey.OPERATION_NAME);
      String serviceId = request.getHandlerContext(HandlerContextKey.SERVICE_ID);
      String sessionToken = this.getSessionToken(request.getHeaders());
      String region = request.getHandlerContext(HandlerContextKey.SIGNING_REGION);
      String accessKey = null;
      if (request.getHandlerContext(HandlerContextKey.AWS_CREDENTIALS) != null) {
         accessKey = request.getHandlerContext(HandlerContextKey.AWS_CREDENTIALS).getAWSAccessKeyId();
      }

      String fqdn = this.extractFqdn(request.getEndpoint());
      TimingInfo timingInfo = metrics == null ? null : metrics.getTimingInfo();
      Long timestamp = null;
      Long attemptLatency = null;
      if (timingInfo != null && timingInfo.getLastSubMeasurement(AWSRequestMetrics.Field.HttpRequestTime.name()) != null) {
         TimingInfo httpRequestTime = timingInfo.getLastSubMeasurement(AWSRequestMetrics.Field.HttpRequestTime.name());
         timestamp = httpRequestTime.getStartEpochTimeMilliIfKnown();
         attemptLatency = this.convertToLongIfNotNull(httpRequestTime.getTimeTakenMillisIfKnown());
      }

      ApiCallAttemptMonitoringEvent event = new ApiCallAttemptMonitoringEvent()
         .withFqdn(fqdn)
         .withVersion(VERSION)
         .withService(serviceId)
         .withClientId(this.clientId)
         .withRegion(region)
         .withAccessKey(accessKey)
         .withUserAgent(this.trimValueIfExceedsMaxLength("UserAgent", this.getDefaultUserAgent(request)))
         .withTimestamp(timestamp)
         .withAttemptLatency(attemptLatency)
         .withSessionToken(sessionToken)
         .withApi(apiName);
      this.addConditionalFieldsToAttemptEvent(metrics, context, event);
      return event;
   }

   private ApiCallMonitoringEvent generateApiCallMonitoringEvent(Request<?> request) {
      String apiName = request.getHandlerContext(HandlerContextKey.OPERATION_NAME);
      String serviceId = request.getHandlerContext(HandlerContextKey.SERVICE_ID);
      String region = request.getHandlerContext(HandlerContextKey.SIGNING_REGION);
      ApiCallAttemptMonitoringEvent lastApiCallAttempt = request.getHandlerContext(LAST_CALL_ATTEMPT);
      Long timestamp = null;
      Long latency = null;
      Integer requestCount = 0;
      AWSRequestMetrics metrics = request.getAWSRequestMetrics();
      if (metrics != null) {
         TimingInfo timingInfo = metrics.getTimingInfo();
         requestCount = timingInfo.getCounter(AWSRequestMetrics.Field.RequestCount.name()) == null
            ? 0
            : timingInfo.getCounter(AWSRequestMetrics.Field.RequestCount.name()).intValue();
         TimingInfo latencyTimingInfo = timingInfo.getSubMeasurement(AwsClientSideMonitoringMetrics.ApiCallLatency.name());
         if (latencyTimingInfo != null) {
            latency = this.convertToLongIfNotNull(latencyTimingInfo.getTimeTakenMillisIfKnown());
            timestamp = latencyTimingInfo.getStartEpochTimeMilliIfKnown();
         }
      }

      ApiCallMonitoringEvent event = new ApiCallMonitoringEvent()
         .withApi(apiName)
         .withVersion(VERSION)
         .withRegion(region)
         .withService(serviceId)
         .withClientId(this.clientId)
         .withAttemptCount(requestCount)
         .withLatency(latency)
         .withUserAgent(this.trimValueIfExceedsMaxLength("UserAgent", this.getDefaultUserAgent(request)))
         .withTimestamp(timestamp);
      if (lastApiCallAttempt != null) {
         event.withFinalAwsException(lastApiCallAttempt.getAwsException())
            .withFinalAwsExceptionMessage(lastApiCallAttempt.getAwsExceptionMessage())
            .withFinalSdkException(lastApiCallAttempt.getSdkException())
            .withFinalSdkExceptionMessage(lastApiCallAttempt.getSdkExceptionMessage())
            .withFinalHttpStatusCode(lastApiCallAttempt.getHttpStatusCode());
      }

      return event;
   }

   private ApiCallMonitoringEvent generateApiCallMonitoringEvent(Request<?> request, Exception e) {
      ApiCallMonitoringEvent event = this.generateApiCallMonitoringEvent(request);
      AWSRequestMetrics metrics = request.getAWSRequestMetrics();
      if (e instanceof ClientExecutionTimeoutException) {
         event.withApiCallTimeout(1);
      }

      if (metrics != null && !CollectionUtils.isNullOrEmpty(metrics.getProperty(AwsClientSideMonitoringMetrics.MaxRetriesExceeded))) {
         boolean maxRetriesExceeded = metrics.getProperty(AwsClientSideMonitoringMetrics.MaxRetriesExceeded).get(0);
         event.withMaxRetriesExceeded(maxRetriesExceeded ? 1 : 0);
      }

      return event;
   }

   private void addConditionalFieldsToAttemptEvent(AWSRequestMetrics metrics, HandlerAfterAttemptContext context, ApiCallAttemptMonitoringEvent event) {
      TimingInfo timingInfo = metrics == null ? null : metrics.getTimingInfo();
      Response<?> response = context.getResponse();
      Integer statusCode = null;
      String xAmznRequestId = null;
      String xAmzRequestId = null;
      String xAmzId2 = null;
      Long requestLatency = null;
      Map<String, String> responseHeaders = null;
      if (response != null && response.getHttpResponse() != null) {
         responseHeaders = response.getHttpResponse().getHeaders();
         statusCode = response.getHttpResponse().getStatusCode();
         requestLatency = this.calculateRequestLatency(timingInfo);
      } else if (context.getException() instanceof AmazonServiceException) {
         responseHeaders = ((AmazonServiceException)context.getException()).getHttpHeaders();
         statusCode = this.extractHttpStatusCode((AmazonServiceException)context.getException());
         requestLatency = this.calculateRequestLatency(timingInfo);
      }

      if (responseHeaders != null) {
         xAmznRequestId = responseHeaders.get("x-amzn-RequestId");
         xAmzRequestId = responseHeaders.get("x-amz-request-id");
         xAmzId2 = responseHeaders.get("x-amz-id-2");
      }

      event.withXAmznRequestId(xAmznRequestId)
         .withXAmzRequestId(xAmzRequestId)
         .withXAmzId2(xAmzId2)
         .withHttpStatusCode(statusCode)
         .withRequestLatency(requestLatency);
      this.addException(context.getException(), event);
   }

   private void handToMonitoringListeners(MonitoringEvent event) {
      for(MonitoringListener monitoringListener : this.monitoringListeners) {
         try {
            monitoringListener.handleEvent(event);
         } catch (Exception var5) {
            if (LOG.isDebugEnabled()) {
               LOG.debug(String.format("MonitoringListener: %s failed to handle event", monitoringListener.toString()), var5);
            }
         }
      }
   }

   private String extractFqdn(URI endpoint) {
      return endpoint == null ? null : endpoint.getHost();
   }

   private String trimValueIfExceedsMaxLength(String entry, String value) {
      if (value == null) {
         return null;
      } else {
         String result = value;
         Integer maximumSize = ENTRY_TO_MAX_SIZE.get(entry);
         if (maximumSize != null && value.length() > maximumSize) {
            result = value.substring(0, maximumSize);
         }

         return result;
      }
   }

   private String getDefaultUserAgent(Request<?> request) {
      String userAgentMarker = request.getOriginalRequest().getRequestClientOptions().getClientMarker(RequestClientOptions.Marker.USER_AGENT);
      String userAgent = ClientConfiguration.DEFAULT_USER_AGENT;
      if (StringUtils.hasValue(userAgentMarker)) {
         userAgent = userAgent + " " + userAgentMarker;
      }

      return userAgent;
   }

   private String getSessionToken(Map<String, String> requestHeaders) {
      for(String header : SECURITY_TOKENS) {
         if (requestHeaders.get(header) != null) {
            return requestHeaders.get(header);
         }
      }

      return null;
   }

   private Long convertToLongIfNotNull(Double number) {
      return number == null ? null : number.longValue();
   }

   private Long calculateRequestLatency(TimingInfo timingInfo) {
      if (timingInfo == null) {
         return null;
      } else {
         TimingInfo httpClientSendRequestTime = timingInfo.getLastSubMeasurement(AWSRequestMetrics.Field.HttpClientSendRequestTime.name());
         TimingInfo httpClientReceiveResponseTime = timingInfo.getLastSubMeasurement(AWSRequestMetrics.Field.HttpClientReceiveResponseTime.name());
         return httpClientSendRequestTime != null
               && httpClientSendRequestTime.getTimeTakenMillisIfKnown() != null
               && httpClientReceiveResponseTime != null
               && httpClientReceiveResponseTime.getTimeTakenMillisIfKnown() != null
            ? httpClientSendRequestTime.getTimeTakenMillisIfKnown().longValue() + httpClientReceiveResponseTime.getTimeTakenMillisIfKnown().longValue()
            : null;
      }
   }

   private void addException(Exception exception, ApiCallAttemptMonitoringEvent event) {
      if (exception != null) {
         if (exception instanceof AmazonServiceException) {
            String errorCode = ((AmazonServiceException)exception).getErrorCode();
            String errorMessage = ((AmazonServiceException)exception).getErrorMessage();
            event.withAwsException(this.trimValueIfExceedsMaxLength("Exception", errorCode));
            event.withAwsExceptionMessage(this.trimValueIfExceedsMaxLength("ExceptionMessage", errorMessage));
         } else {
            String exceptionClassName = exception.getClass().getName();
            String exceptionMessage = this.getRootCauseMessage(exception);
            event.withSdkException(this.trimValueIfExceedsMaxLength("Exception", exceptionClassName));
            event.withSdkExceptionMessage(this.trimValueIfExceedsMaxLength("ExceptionMessage", exceptionMessage));
         }
      }
   }

   private String getRootCauseMessage(Exception exception) {
      return exception.getMessage() != null ? exception.getMessage() : Throwables.getRootCause(exception).getMessage();
   }

   private int extractHttpStatusCode(AmazonServiceException exception) {
      return exception.getStatusCode();
   }
}
