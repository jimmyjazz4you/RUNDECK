package com.amazonaws.services.s3.internal;

import com.amazonaws.AmazonServiceException.ErrorType;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.util.HashMap;
import java.util.Map;

public class AmazonS3ExceptionBuilder {
   private String requestId;
   private String errorCode;
   private String errorMessage;
   private int statusCode;
   private String extendedRequestId;
   private String cloudFrontId;
   private Map<String, String> additionalDetails;
   private String errorResponseXml;
   private String proxyHost;

   public String getRequestId() {
      return this.requestId;
   }

   public void setRequestId(String requestId) {
      this.requestId = requestId;
   }

   public void setErrorCode(String errorCode) {
      this.errorCode = errorCode;
   }

   public String getErrorCode() {
      return this.errorCode;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public String getExtendedRequestId() {
      return this.extendedRequestId;
   }

   public void setExtendedRequestId(String extendedRequestId) {
      this.extendedRequestId = extendedRequestId;
   }

   public String getCloudFrontId() {
      return this.cloudFrontId;
   }

   public void setCloudFrontId(String cloudFrontId) {
      this.cloudFrontId = cloudFrontId;
   }

   public Map<String, String> getAdditionalDetails() {
      return this.additionalDetails;
   }

   public void setAdditionalDetails(Map<String, String> additionalDetails) {
      this.additionalDetails = additionalDetails;
   }

   public void addAdditionalDetail(String key, String detail) {
      if (detail != null && !detail.trim().isEmpty()) {
         if (this.additionalDetails == null) {
            this.additionalDetails = new HashMap<>();
         }

         String additionalContent = this.additionalDetails.get(key);
         if (additionalContent != null && !additionalContent.trim().isEmpty()) {
            detail = additionalContent + "-" + detail;
         }

         if (!detail.isEmpty()) {
            this.additionalDetails.put(key, detail);
         }
      }
   }

   public String getErrorResponseXml() {
      return this.errorResponseXml;
   }

   public void setErrorResponseXml(String errorResponseXml) {
      this.errorResponseXml = errorResponseXml;
   }

   public void setProxyHost(String proxyHost) {
      this.proxyHost = proxyHost;
   }

   public AmazonS3Exception build() {
      AmazonS3Exception s3Exception = this.errorResponseXml == null
         ? new AmazonS3Exception(this.errorMessage)
         : new AmazonS3Exception(this.errorMessage, this.errorResponseXml);
      s3Exception.setErrorCode(this.errorCode);
      s3Exception.setExtendedRequestId(this.extendedRequestId);
      s3Exception.setStatusCode(this.statusCode);
      s3Exception.setRequestId(this.requestId);
      s3Exception.setCloudFrontId(this.cloudFrontId);
      s3Exception.setAdditionalDetails(this.additionalDetails);
      s3Exception.setErrorType(this.errorTypeOf(this.statusCode));
      s3Exception.setProxyHost(this.proxyHost);
      return s3Exception;
   }

   private ErrorType errorTypeOf(int statusCode) {
      return statusCode >= 500 ? ErrorType.Service : ErrorType.Client;
   }
}
