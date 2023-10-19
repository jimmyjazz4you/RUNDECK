package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.HttpMethod;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GeneratePresignedUrlRequest extends AmazonWebServiceRequest implements SSECustomerKeyProvider, Serializable {
   private HttpMethod method;
   private String bucketName;
   private String key;
   private String versionId;
   private String contentType;
   private String contentMd5;
   private Date expiration;
   private boolean zeroByteContent;
   private Map<String, String> requestParameters = new HashMap<>();
   private ResponseHeaderOverrides responseHeaders;
   private SSECustomerKey sseCustomerKey;
   private String sseAlgorithm;
   private String kmsCmkId;

   public String getKmsCmkId() {
      return this.kmsCmkId;
   }

   public void setKmsCmkId(String kmsCmkId) {
      this.kmsCmkId = kmsCmkId;
   }

   public GeneratePresignedUrlRequest withKmsCmkId(String kmsCmkId) {
      this.setKmsCmkId(kmsCmkId);
      return this;
   }

   public String getSSEAlgorithm() {
      return this.sseAlgorithm;
   }

   public void setSSEAlgorithm(String sseAlgorithm) {
      this.sseAlgorithm = sseAlgorithm;
   }

   public GeneratePresignedUrlRequest withSSEAlgorithm(String sseAlgorithm) {
      this.setSSEAlgorithm(sseAlgorithm);
      return this;
   }

   public void setSSEAlgorithm(SSEAlgorithm sseAlgorithm) {
      this.sseAlgorithm = sseAlgorithm.getAlgorithm();
   }

   public GeneratePresignedUrlRequest withSSEAlgorithm(SSEAlgorithm sseAlgorithm) {
      this.setSSEAlgorithm(sseAlgorithm);
      return this;
   }

   public GeneratePresignedUrlRequest(String bucketName, String key) {
      this(bucketName, key, HttpMethod.GET);
   }

   public GeneratePresignedUrlRequest(String bucketName, String key, HttpMethod method) {
      this.bucketName = bucketName;
      this.key = key;
      this.method = method;
   }

   public HttpMethod getMethod() {
      return this.method;
   }

   public void setMethod(HttpMethod method) {
      this.method = method;
   }

   public GeneratePresignedUrlRequest withMethod(HttpMethod method) {
      this.setMethod(method);
      return this;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public GeneratePresignedUrlRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public GeneratePresignedUrlRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public GeneratePresignedUrlRequest withVersionId(String versionId) {
      this.setVersionId(versionId);
      return this;
   }

   public Date getExpiration() {
      return this.expiration;
   }

   public void setExpiration(Date expiration) {
      this.expiration = expiration;
   }

   public GeneratePresignedUrlRequest withExpiration(Date expiration) {
      this.setExpiration(expiration);
      return this;
   }

   public void addRequestParameter(String key, String value) {
      this.requestParameters.put(key, value);
   }

   public Map<String, String> getRequestParameters() {
      return this.requestParameters;
   }

   public ResponseHeaderOverrides getResponseHeaders() {
      return this.responseHeaders;
   }

   public void setResponseHeaders(ResponseHeaderOverrides responseHeaders) {
      this.responseHeaders = responseHeaders;
   }

   public GeneratePresignedUrlRequest withResponseHeaders(ResponseHeaderOverrides responseHeaders) {
      this.setResponseHeaders(responseHeaders);
      return this;
   }

   public String getContentType() {
      return this.contentType;
   }

   public void setContentType(String contentType) {
      this.contentType = contentType;
   }

   public GeneratePresignedUrlRequest withContentType(String contentType) {
      this.setContentType(contentType);
      return this;
   }

   public String getContentMd5() {
      return this.contentMd5;
   }

   public void setContentMd5(String contentMd5) {
      this.contentMd5 = contentMd5;
   }

   public GeneratePresignedUrlRequest withContentMd5(String contentMd5) {
      this.contentMd5 = contentMd5;
      return this;
   }

   @Override
   public SSECustomerKey getSSECustomerKey() {
      return this.sseCustomerKey;
   }

   public void setSSECustomerKey(SSECustomerKey sseCustomerKey) {
      this.sseCustomerKey = sseCustomerKey;
   }

   public GeneratePresignedUrlRequest withSSECustomerKey(SSECustomerKey sseKey) {
      this.setSSECustomerKey(sseKey);
      return this;
   }

   public void setSSECustomerKeyAlgorithm(SSEAlgorithm sseAlgorithm) {
      if (sseAlgorithm == null) {
         this.sseCustomerKey = null;
      } else {
         if (!sseAlgorithm.getAlgorithm().equals(SSEAlgorithm.AES256.getAlgorithm())) {
            throw new IllegalArgumentException("Currently the only supported Server Side Encryption algorithm is " + SSEAlgorithm.AES256);
         }

         this.sseCustomerKey = SSECustomerKey.generateSSECustomerKeyForPresignUrl(sseAlgorithm.getAlgorithm());
      }
   }

   public GeneratePresignedUrlRequest withSSECustomerKeyAlgorithm(SSEAlgorithm algorithm) {
      this.setSSECustomerKeyAlgorithm(algorithm);
      return this;
   }

   public boolean isZeroByteContent() {
      return this.zeroByteContent;
   }

   public void setZeroByteContent(boolean zeroByteContent) {
      this.zeroByteContent = zeroByteContent;
   }

   public GeneratePresignedUrlRequest withZeroByteContent(boolean zeroByteContent) {
      this.setZeroByteContent(zeroByteContent);
      return this;
   }

   public void rejectIllegalArguments() {
      if (this.bucketName == null) {
         throw new IllegalArgumentException("The bucket name parameter must be specified when generating a pre-signed URL");
      } else if (this.method == null) {
         throw new IllegalArgumentException("The HTTP method request parameter must be specified when generating a pre-signed URL");
      } else {
         if (this.sseCustomerKey != null) {
            if (this.sseAlgorithm != null) {
               throw new IllegalArgumentException("Either SSE or SSE-C can be specified but not both");
            }

            if (this.kmsCmkId != null) {
               throw new IllegalArgumentException("KMS CMK is not applicable for SSE-C");
            }
         } else if (this.kmsCmkId != null && !SSEAlgorithm.KMS.getAlgorithm().equals(this.sseAlgorithm)) {
            throw new IllegalArgumentException("For KMS server side encryption, the SSE algorithm must be set to " + SSEAlgorithm.KMS);
         }
      }
   }
}
