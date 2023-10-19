package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonServiceException;
import java.io.Serializable;
import java.util.Map;

public class AmazonS3Exception extends AmazonServiceException implements Serializable {
   private static final long serialVersionUID = 7573680383273658477L;
   private String extendedRequestId;
   private String cloudFrontId;
   private Map<String, String> additionalDetails;
   private final String errorResponseXml;

   public AmazonS3Exception(String message) {
      super(message);
      this.errorResponseXml = null;
   }

   public AmazonS3Exception(String message, Exception cause) {
      super(message, cause);
      this.errorResponseXml = null;
   }

   public AmazonS3Exception(String message, String errorResponseXml) {
      super(message);
      if (errorResponseXml == null) {
         throw new IllegalArgumentException("Error Response XML cannot be null");
      } else {
         this.errorResponseXml = errorResponseXml;
      }
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

   @Override
   public String toString() {
      return super.toString() + ", S3 Extended Request ID: " + this.getExtendedRequestId();
   }

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
         + "; S3 Extended Request ID: "
         + this.getExtendedRequestId()
         + "; Proxy: "
         + this.getProxyHost()
         + ")";
   }

   public String getErrorResponseXml() {
      return this.errorResponseXml;
   }
}
