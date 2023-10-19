package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class RestoreObjectRequest extends AmazonWebServiceRequest implements Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private int expirationInDays;
   private String bucketName;
   private String key;
   private String versionId;
   private boolean isRequesterPays;
   private GlacierJobParameters glacierJobParameters;
   private String type;
   private String tier;
   private String description;
   private SelectParameters selectParameters;
   private OutputLocation outputLocation;
   private String expectedBucketOwner;

   public RestoreObjectRequest(String bucketName, String key) {
      this(bucketName, key, -1);
   }

   public RestoreObjectRequest(String bucketName, String key, int expirationInDays) {
      this.bucketName = bucketName;
      this.key = key;
      this.expirationInDays = expirationInDays;
   }

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public RestoreObjectRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public RestoreObjectRequest withBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public RestoreObjectRequest withKey(String key) {
      this.key = key;
      return this;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public void setVersionId(String versionId) {
      this.versionId = versionId;
   }

   public RestoreObjectRequest withVersionId(String versionId) {
      this.versionId = versionId;
      return this;
   }

   public void setExpirationInDays(int expirationInDays) {
      this.expirationInDays = expirationInDays;
   }

   public int getExpirationInDays() {
      return this.expirationInDays;
   }

   public RestoreObjectRequest withExpirationInDays(int expirationInDays) {
      this.expirationInDays = expirationInDays;
      return this;
   }

   public boolean isRequesterPays() {
      return this.isRequesterPays;
   }

   public void setRequesterPays(boolean isRequesterPays) {
      this.isRequesterPays = isRequesterPays;
   }

   public RestoreObjectRequest withRequesterPays(boolean isRequesterPays) {
      this.setRequesterPays(isRequesterPays);
      return this;
   }

   public GlacierJobParameters getGlacierJobParameters() {
      return this.glacierJobParameters;
   }

   public void setGlacierJobParameters(GlacierJobParameters glacierJobParameters) {
      this.glacierJobParameters = glacierJobParameters;
   }

   public RestoreObjectRequest withGlacierJobParameters(GlacierJobParameters glacierJobParameters) {
      this.setGlacierJobParameters(glacierJobParameters);
      return this;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public RestoreObjectRequest withType(String restoreRequestType) {
      this.setType(restoreRequestType);
      return this;
   }

   public RestoreObjectRequest withType(RestoreRequestType restoreRequestType) {
      this.setType(restoreRequestType == null ? null : restoreRequestType.toString());
      return this;
   }

   public String getTier() {
      return this.tier;
   }

   public void setTier(String tier) {
      this.tier = tier;
   }

   public RestoreObjectRequest withTier(String tier) {
      this.tier = tier;
      return this;
   }

   public RestoreObjectRequest withTier(Tier tier) {
      this.tier = tier == null ? null : tier.toString();
      return this;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public RestoreObjectRequest withDescription(String description) {
      this.description = description;
      return this;
   }

   public SelectParameters getSelectParameters() {
      return this.selectParameters;
   }

   public void setSelectParameters(SelectParameters selectParameters) {
      this.selectParameters = selectParameters;
   }

   public RestoreObjectRequest withSelectParameters(SelectParameters selectParameters) {
      this.selectParameters = selectParameters;
      return this;
   }

   public OutputLocation getOutputLocation() {
      return this.outputLocation;
   }

   public void setOutputLocation(OutputLocation outputLocation) {
      this.outputLocation = outputLocation;
   }

   public RestoreObjectRequest withOutputLocation(OutputLocation outputLocation) {
      this.outputLocation = outputLocation;
      return this;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof RestoreObjectRequest) {
         RestoreObjectRequest other = (RestoreObjectRequest)obj;
         if (other.getExpirationInDays() != this.getExpirationInDays()) {
            return false;
         } else if (other.getBucketName() == null ^ this.getBucketName() == null) {
            return false;
         } else if (other.getBucketName() != null && !other.getBucketName().equals(this.getBucketName())) {
            return false;
         } else if (other.getKey() == null ^ this.getKey() == null) {
            return false;
         } else if (other.getKey() != null && !other.getKey().equals(this.getKey())) {
            return false;
         } else if (other.getVersionId() == null ^ this.getVersionId() == null) {
            return false;
         } else if (other.getVersionId() != null && !other.getVersionId().equals(this.getVersionId())) {
            return false;
         } else if (other.getGlacierJobParameters() == null ^ this.getGlacierJobParameters() == null) {
            return false;
         } else if (other.getGlacierJobParameters() != null && !other.getGlacierJobParameters().equals(this.getGlacierJobParameters())) {
            return false;
         } else if (other.getType() == null ^ this.getType() == null) {
            return false;
         } else if (other.getType() != null && !other.getType().equals(this.getType())) {
            return false;
         } else if (other.getTier() == null ^ this.getTier() == null) {
            return false;
         } else if (other.getTier() != null && !other.getTier().equals(this.getTier())) {
            return false;
         } else if (other.getDescription() == null ^ this.getDescription() == null) {
            return false;
         } else if (other.getDescription() != null && !other.getDescription().equals(this.getDescription())) {
            return false;
         } else if (other.getSelectParameters() == null ^ this.getSelectParameters() == null) {
            return false;
         } else if (other.getSelectParameters() != null && !other.getSelectParameters().equals(this.getSelectParameters())) {
            return false;
         } else if (other.getOutputLocation() == null ^ this.getOutputLocation() == null) {
            return false;
         } else if (other.getOutputLocation() != null && !other.getOutputLocation().equals(this.getOutputLocation())) {
            return false;
         } else {
            return other.isRequesterPays() == this.isRequesterPays();
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + (this.getBucketName() == null ? 0 : this.getBucketName().hashCode());
      hashCode = 31 * hashCode + (this.getKey() == null ? 0 : this.getKey().hashCode());
      hashCode = 31 * hashCode + (this.getVersionId() == null ? 0 : this.getVersionId().hashCode());
      hashCode = 31 * hashCode + (this.getGlacierJobParameters() == null ? 0 : this.getGlacierJobParameters().hashCode());
      hashCode = 31 * hashCode + (this.getType() == null ? 0 : this.getType().hashCode());
      hashCode = 31 * hashCode + (this.getTier() != null ? this.getTier().hashCode() : 0);
      hashCode = 31 * hashCode + (this.getDescription() != null ? this.getDescription().hashCode() : 0);
      hashCode = 31 * hashCode + (this.getSelectParameters() != null ? this.getSelectParameters().hashCode() : 0);
      return 31 * hashCode + (this.getOutputLocation() != null ? this.getOutputLocation().hashCode() : 0);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      sb.append("ExpirationInDays: ").append(this.expirationInDays).append(",");
      sb.append("IsRequesterPays").append(this.isRequesterPays()).append(",");
      if (this.getBucketName() != null) {
         sb.append("BucketName: ").append(this.getBucketName()).append(",");
      }

      if (this.getKey() != null) {
         sb.append("Key: ").append(this.getKey()).append(",");
      }

      if (this.getVersionId() != null) {
         sb.append("VersionId: ").append(this.getVersionId()).append(",");
      }

      if (this.getGlacierJobParameters() != null) {
         sb.append("GlacierJobParameters: ").append(this.getGlacierJobParameters()).append(",");
      }

      if (this.getType() != null) {
         sb.append("RestoreRequestType: ").append(this.getType()).append(",");
      }

      if (this.getTier() != null) {
         sb.append("Tier: ").append(this.getTier()).append(",");
      }

      if (this.getDescription() != null) {
         sb.append("Description: ").append(this.getDescription()).append(",");
      }

      if (this.getSelectParameters() != null) {
         sb.append("SelectParameters: ").append(this.getSelectParameters()).append(",");
      }

      if (this.getOutputLocation() != null) {
         sb.append("OutputLocation").append(this.getOutputLocation());
      }

      sb.append("}");
      return sb.toString();
   }

   public RestoreObjectRequest clone() {
      return (RestoreObjectRequest)super.clone();
   }
}
