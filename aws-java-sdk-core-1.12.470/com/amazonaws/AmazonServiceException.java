package com.amazonaws;

import com.amazonaws.util.StringUtils;
import java.util.Map;

public class AmazonServiceException extends SdkClientException {
   private static final long serialVersionUID = 1L;
   private String requestId;
   private String errorCode;
   private AmazonServiceException.ErrorType errorType = AmazonServiceException.ErrorType.Unknown;
   private String errorMessage;
   private int statusCode;
   private String serviceName;
   private Map<String, String> httpHeaders;
   private byte[] rawResponse;
   private String proxyHost;

   public AmazonServiceException(String errorMessage) {
      super((String)null);
      this.errorMessage = errorMessage;
   }

   public AmazonServiceException(String errorMessage, Exception cause) {
      super(null, cause);
      this.errorMessage = errorMessage;
   }

   public void setRequestId(String requestId) {
      this.requestId = requestId;
   }

   public String getRequestId() {
      return this.requestId;
   }

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public void setErrorCode(String errorCode) {
      this.errorCode = errorCode;
   }

   public String getErrorCode() {
      return this.errorCode;
   }

   public void setErrorType(AmazonServiceException.ErrorType errorType) {
      this.errorType = errorType;
   }

   public AmazonServiceException.ErrorType getErrorType() {
      return this.errorType;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public void setErrorMessage(String value) {
      this.errorMessage = value;
   }

   public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   @Override
   public String getMessage() {
      return this.getErrorMessage()
         + " (Service: "
         + this.getServiceName()
         + "; Status Code: "
         + this.getStatusCode()
         + "; Error Code: "
         + this.getErrorCode()
         + "; Request ID: "
         + this.getRequestId()
         + "; Proxy: "
         + this.getProxyHost()
         + ")";
   }

   public String getRawResponseContent() {
      return this.rawResponse == null ? null : new String(this.rawResponse, StringUtils.UTF8);
   }

   public void setRawResponseContent(String rawResponseContent) {
      this.rawResponse = rawResponseContent == null ? null : rawResponseContent.getBytes(StringUtils.UTF8);
   }

   public byte[] getRawResponse() {
      return this.rawResponse == null ? null : (byte[])this.rawResponse.clone();
   }

   public void setRawResponse(byte[] rawResponse) {
      this.rawResponse = rawResponse == null ? null : (byte[])rawResponse.clone();
   }

   public Map<String, String> getHttpHeaders() {
      return this.httpHeaders;
   }

   public void setHttpHeaders(Map<String, String> httpHeaders) {
      this.httpHeaders = httpHeaders;
   }

   public String getProxyHost() {
      return this.proxyHost;
   }

   public void setProxyHost(String proxyHost) {
      this.proxyHost = proxyHost;
   }

   public static enum ErrorType {
      Client,
      Service,
      Unknown;
   }
}
