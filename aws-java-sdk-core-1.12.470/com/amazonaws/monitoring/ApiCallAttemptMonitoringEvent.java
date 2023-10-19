package com.amazonaws.monitoring;

public class ApiCallAttemptMonitoringEvent extends ApiMonitoringEvent {
   public static final String API_CALL_ATTEMPT_MONITORING_EVENT_TYPE = "ApiCallAttempt";
   private String fqdn;
   private String accessKey;
   private String sessionToken;
   private Integer httpStatusCode;
   private String xAmznRequestId;
   private String xAmzRequestId;
   private String xAmzId2;
   private String awsException;
   private String awsExceptionMessage;
   private String sdkException;
   private String sdkExceptionMessage;
   private Long attemptLatency;
   private Long requestLatency;

   public ApiCallAttemptMonitoringEvent withApi(String api) {
      this.api = api;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withVersion(Integer version) {
      this.version = version;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withUserAgent(String userAgent) {
      this.userAgent = userAgent;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withService(String service) {
      this.service = service;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withClientId(String clientId) {
      this.clientId = clientId;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
   }

   public String getFqdn() {
      return this.fqdn;
   }

   public ApiCallAttemptMonitoringEvent withFqdn(String fqdn) {
      this.fqdn = fqdn;
      return this;
   }

   public ApiCallAttemptMonitoringEvent withRegion(String region) {
      this.region = region;
      return this;
   }

   public String getAccessKey() {
      return this.accessKey;
   }

   public ApiCallAttemptMonitoringEvent withAccessKey(String accessKey) {
      this.accessKey = accessKey;
      return this;
   }

   public String getSessionToken() {
      return this.sessionToken;
   }

   public ApiCallAttemptMonitoringEvent withSessionToken(String sessionToken) {
      this.sessionToken = sessionToken;
      return this;
   }

   public Integer getHttpStatusCode() {
      return this.httpStatusCode;
   }

   public ApiCallAttemptMonitoringEvent withHttpStatusCode(Integer httpStatusCode) {
      this.httpStatusCode = httpStatusCode;
      return this;
   }

   public String getxAmznRequestId() {
      return this.xAmznRequestId;
   }

   public ApiCallAttemptMonitoringEvent withXAmznRequestId(String xAmznRequestId) {
      this.xAmznRequestId = xAmznRequestId;
      return this;
   }

   public String getxAmzRequestId() {
      return this.xAmzRequestId;
   }

   public ApiCallAttemptMonitoringEvent withXAmzRequestId(String xAmzRequestId) {
      this.xAmzRequestId = xAmzRequestId;
      return this;
   }

   public String getxAmzId2() {
      return this.xAmzId2;
   }

   public ApiCallAttemptMonitoringEvent withXAmzId2(String xAmzId2) {
      this.xAmzId2 = xAmzId2;
      return this;
   }

   public String getAwsException() {
      return this.awsException;
   }

   public ApiCallAttemptMonitoringEvent withAwsException(String awsException) {
      this.awsException = awsException;
      return this;
   }

   public String getAwsExceptionMessage() {
      return this.awsExceptionMessage;
   }

   public ApiCallAttemptMonitoringEvent withAwsExceptionMessage(String awsExceptionMessage) {
      this.awsExceptionMessage = awsExceptionMessage;
      return this;
   }

   public String getSdkException() {
      return this.sdkException;
   }

   public ApiCallAttemptMonitoringEvent withSdkException(String sdkException) {
      this.sdkException = sdkException;
      return this;
   }

   public String getSdkExceptionMessage() {
      return this.sdkExceptionMessage;
   }

   public ApiCallAttemptMonitoringEvent withSdkExceptionMessage(String sdkExceptionMessage) {
      this.sdkExceptionMessage = sdkExceptionMessage;
      return this;
   }

   public Long getAttemptLatency() {
      return this.attemptLatency;
   }

   public ApiCallAttemptMonitoringEvent withAttemptLatency(Long attemptLatency) {
      this.attemptLatency = attemptLatency;
      return this;
   }

   public Long getRequestLatency() {
      return this.requestLatency;
   }

   public ApiCallAttemptMonitoringEvent withRequestLatency(Long requestLatency) {
      this.requestLatency = requestLatency;
      return this;
   }

   @Override
   public String getType() {
      return "ApiCallAttempt";
   }
}
