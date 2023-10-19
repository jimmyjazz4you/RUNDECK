package com.amazonaws.services.s3.model;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.internal.ObjectExpirationResult;
import com.amazonaws.services.s3.internal.ObjectRestoreResult;
import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.ServerSideEncryptionResult;
import com.amazonaws.util.DateUtils;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ObjectMetadata
   implements ServerSideEncryptionResult,
   S3RequesterChargedResult,
   ObjectExpirationResult,
   ObjectRestoreResult,
   Cloneable,
   Serializable {
   private Map<String, String> userMetadata = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   private Map<String, Object> metadata = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   public static final String AES_256_SERVER_SIDE_ENCRYPTION = SSEAlgorithm.AES256.getAlgorithm();
   private Date httpExpiresDate;
   private Date expirationTime;
   private String expirationTimeRuleId;
   private Boolean ongoingRestore;
   private Date restoreExpirationTime;
   private Boolean bucketKeyEnabled;

   public ObjectMetadata() {
   }

   private ObjectMetadata(ObjectMetadata from) {
      this.userMetadata = from.userMetadata == null ? null : new TreeMap<>(from.userMetadata);
      this.metadata = from.metadata == null ? null : new TreeMap<>(from.metadata);
      this.expirationTime = DateUtils.cloneDate(from.expirationTime);
      this.expirationTimeRuleId = from.expirationTimeRuleId;
      this.httpExpiresDate = DateUtils.cloneDate(from.httpExpiresDate);
      this.ongoingRestore = from.ongoingRestore;
      this.restoreExpirationTime = DateUtils.cloneDate(from.restoreExpirationTime);
      this.bucketKeyEnabled = from.bucketKeyEnabled;
   }

   public Map<String, String> getUserMetadata() {
      return this.userMetadata;
   }

   public void setUserMetadata(Map<String, String> userMetadata) {
      this.userMetadata = userMetadata;
   }

   public void setHeader(String key, Object value) {
      this.metadata.put(key, value);
   }

   public void addUserMetadata(String key, String value) {
      this.userMetadata.put(key, value);
   }

   public Map<String, Object> getRawMetadata() {
      Map<String, Object> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      copy.putAll(this.metadata);
      return Collections.unmodifiableMap(copy);
   }

   public Object getRawMetadataValue(String key) {
      return this.metadata.get(key);
   }

   public Date getLastModified() {
      return DateUtils.cloneDate((Date)this.metadata.get("Last-Modified"));
   }

   public void setLastModified(Date lastModified) {
      this.metadata.put("Last-Modified", lastModified);
   }

   public long getContentLength() {
      Long contentLength = (Long)this.metadata.get("Content-Length");
      return contentLength == null ? 0L : contentLength;
   }

   public long getInstanceLength() {
      String contentRange = (String)this.metadata.get("Content-Range");
      if (contentRange != null) {
         int pos = contentRange.lastIndexOf("/");
         if (pos >= 0) {
            return Long.parseLong(contentRange.substring(pos + 1));
         }
      }

      return this.getContentLength();
   }

   public void setContentLength(long contentLength) {
      this.metadata.put("Content-Length", contentLength);
   }

   public String getContentType() {
      return (String)this.metadata.get("Content-Type");
   }

   public void setContentType(String contentType) {
      this.metadata.put("Content-Type", contentType);
   }

   public String getContentLanguage() {
      return (String)this.metadata.get("Content-Language");
   }

   public void setContentLanguage(String contentLanguage) {
      this.metadata.put("Content-Language", contentLanguage);
   }

   public String getContentEncoding() {
      return (String)this.metadata.get("Content-Encoding");
   }

   public void setContentEncoding(String encoding) {
      this.metadata.put("Content-Encoding", encoding);
   }

   public String getCacheControl() {
      return (String)this.metadata.get("Cache-Control");
   }

   public void setCacheControl(String cacheControl) {
      this.metadata.put("Cache-Control", cacheControl);
   }

   public void setContentMD5(String md5Base64) {
      if (md5Base64 == null) {
         this.metadata.remove("Content-MD5");
      } else {
         this.metadata.put("Content-MD5", md5Base64);
      }
   }

   public String getContentMD5() {
      return (String)this.metadata.get("Content-MD5");
   }

   public void setContentDisposition(String disposition) {
      this.metadata.put("Content-Disposition", disposition);
   }

   public String getContentDisposition() {
      return (String)this.metadata.get("Content-Disposition");
   }

   public String getETag() {
      return (String)this.metadata.get("ETag");
   }

   public String getVersionId() {
      return (String)this.metadata.get("x-amz-version-id");
   }

   @Override
   public String getSSEAlgorithm() {
      return (String)this.metadata.get("x-amz-server-side-encryption");
   }

   @Deprecated
   public String getServerSideEncryption() {
      return (String)this.metadata.get("x-amz-server-side-encryption");
   }

   @Override
   public void setSSEAlgorithm(String algorithm) {
      this.metadata.put("x-amz-server-side-encryption", algorithm);
   }

   @Deprecated
   public void setServerSideEncryption(String algorithm) {
      this.metadata.put("x-amz-server-side-encryption", algorithm);
   }

   @Override
   public String getSSECustomerAlgorithm() {
      return (String)this.metadata.get("x-amz-server-side-encryption-customer-algorithm");
   }

   @Override
   public void setSSECustomerAlgorithm(String algorithm) {
      this.metadata.put("x-amz-server-side-encryption-customer-algorithm", algorithm);
   }

   @Override
   public String getSSECustomerKeyMd5() {
      return (String)this.metadata.get("x-amz-server-side-encryption-customer-key-MD5");
   }

   @Override
   public void setSSECustomerKeyMd5(String md5Digest) {
      this.metadata.put("x-amz-server-side-encryption-customer-key-MD5", md5Digest);
   }

   @Override
   public Date getExpirationTime() {
      return DateUtils.cloneDate(this.expirationTime);
   }

   @Override
   public void setExpirationTime(Date expirationTime) {
      this.expirationTime = expirationTime;
   }

   @Override
   public String getExpirationTimeRuleId() {
      return this.expirationTimeRuleId;
   }

   @Override
   public void setExpirationTimeRuleId(String expirationTimeRuleId) {
      this.expirationTimeRuleId = expirationTimeRuleId;
   }

   @Override
   public Date getRestoreExpirationTime() {
      return DateUtils.cloneDate(this.restoreExpirationTime);
   }

   @Override
   public void setRestoreExpirationTime(Date restoreExpirationTime) {
      this.restoreExpirationTime = restoreExpirationTime;
   }

   @Override
   public void setOngoingRestore(boolean ongoingRestore) {
      this.ongoingRestore = ongoingRestore;
   }

   @Override
   public Boolean getOngoingRestore() {
      return this.ongoingRestore;
   }

   public void setHttpExpiresDate(Date httpExpiresDate) {
      this.httpExpiresDate = httpExpiresDate;
   }

   public Date getHttpExpiresDate() {
      return DateUtils.cloneDate(this.httpExpiresDate);
   }

   public String getStorageClass() {
      Object storageClass = this.metadata.get("x-amz-storage-class");
      return storageClass == null ? null : storageClass.toString();
   }

   public String getArchiveStatus() {
      Object archiveStatus = this.metadata.get("x-amz-archive-status");
      return archiveStatus == null ? null : archiveStatus.toString();
   }

   public String getUserMetaDataOf(String key) {
      return this.userMetadata == null ? null : this.userMetadata.get(key);
   }

   public ObjectMetadata clone() {
      return new ObjectMetadata(this);
   }

   public String getSSEAwsKmsKeyId() {
      return (String)this.metadata.get("x-amz-server-side-encryption-aws-kms-key-id");
   }

   public String getSSEAwsKmsEncryptionContext() {
      return (String)this.metadata.get("x-amz-server-side-encryption-context");
   }

   @Override
   public boolean isRequesterCharged() {
      return this.metadata.get("x-amz-request-charged") != null;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      if (isRequesterCharged) {
         this.metadata.put("x-amz-request-charged", "requester");
      }
   }

   public Integer getPartCount() {
      return (Integer)this.metadata.get("x-amz-mp-parts-count");
   }

   public Long[] getContentRange() {
      String contentRange = (String)this.metadata.get("Content-Range");
      Long[] range = null;
      if (contentRange != null) {
         String[] tokens = contentRange.split("[ -/]+");

         try {
            range = new Long[]{Long.parseLong(tokens[1]), Long.parseLong(tokens[2])};
         } catch (NumberFormatException var5) {
            throw new SdkClientException("Unable to parse content range. Header 'Content-Range' has corrupted data" + var5.getMessage(), var5);
         }
      }

      return range;
   }

   public String getReplicationStatus() {
      return (String)this.metadata.get("x-amz-replication-status");
   }

   public String getObjectLockMode() {
      return (String)this.metadata.get("x-amz-object-lock-mode");
   }

   public Date getObjectLockRetainUntilDate() {
      String dateStr = (String)this.metadata.get("x-amz-object-lock-retain-until-date");
      return dateStr != null ? DateUtils.parseISO8601Date(dateStr) : null;
   }

   public String getObjectLockLegalHoldStatus() {
      return (String)this.metadata.get("x-amz-object-lock-legal-hold");
   }

   @Override
   public Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   @Override
   public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
   }
}
