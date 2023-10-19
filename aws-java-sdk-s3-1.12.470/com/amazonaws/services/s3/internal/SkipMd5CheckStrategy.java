package com.amazonaws.services.s3.internal;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.PresignedUrlUploadRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class SkipMd5CheckStrategy {
   public static final String DISABLE_GET_OBJECT_MD5_VALIDATION_PROPERTY = "com.amazonaws.services.s3.disableGetObjectMD5Validation";
   public static final String DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY = "com.amazonaws.services.s3.disablePutObjectMD5Validation";
   public static final SkipMd5CheckStrategy INSTANCE = new SkipMd5CheckStrategy();

   private SkipMd5CheckStrategy() {
   }

   public boolean skipClientSideValidationPerGetResponse(ObjectMetadata metadata) {
      return this.isGetObjectMd5ValidationDisabledByProperty() ? true : this.skipClientSideValidationPerResponse(metadata);
   }

   public boolean skipClientSideValidationPerPutResponse(ObjectMetadata metadata) {
      return this.isPutObjectMd5ValidationDisabledByProperty() ? true : this.skipClientSideValidationPerResponse(metadata);
   }

   public boolean skipClientSideValidationPerUploadPartResponse(ObjectMetadata metadata) {
      return this.skipClientSideValidationPerPutResponse(metadata);
   }

   public boolean skipClientSideValidation(GetObjectRequest request, ObjectMetadata returnedMetadata) {
      return this.skipClientSideValidationPerRequest(request) || this.skipClientSideValidationPerGetResponse(returnedMetadata);
   }

   public boolean skipClientSideValidation(PresignedUrlDownloadRequest request, ObjectMetadata returnedMetadata) {
      return this.skipClientSideValidationPerRequest(request) || this.skipClientSideValidationPerGetResponse(returnedMetadata);
   }

   public boolean skipClientSideValidationPerRequest(PutObjectRequest request) {
      if (this.isPutObjectMd5ValidationDisabledByProperty()) {
         return true;
      } else {
         return this.putRequestInvolvesSse(request) || this.metadataInvolvesSse(request.getMetadata());
      }
   }

   public boolean skipClientSideValidationPerRequest(UploadPartRequest request) {
      if (this.isPutObjectMd5ValidationDisabledByProperty()) {
         return true;
      } else {
         return request.getSSECustomerKey() != null;
      }
   }

   public boolean skipServerSideValidation(PutObjectRequest request) {
      return this.isPutObjectMd5ValidationDisabledByProperty();
   }

   public boolean skipServerSideValidation(UploadPartRequest request) {
      return this.isPutObjectMd5ValidationDisabledByProperty();
   }

   public boolean skipClientSideValidationPerRequest(GetObjectRequest request) {
      if (this.isGetObjectMd5ValidationDisabledByProperty()) {
         return true;
      } else if (request.getRange() != null) {
         return true;
      } else {
         return request.getSSECustomerKey() != null;
      }
   }

   public boolean skipClientSideValidationPerRequest(PresignedUrlDownloadRequest request) {
      if (this.isGetObjectMd5ValidationDisabledByProperty()) {
         return true;
      } else {
         return request.getRange() != null;
      }
   }

   public boolean skipClientSideValidationPerRequest(PresignedUrlUploadRequest request) {
      return this.isPutObjectMd5ValidationDisabledByProperty() ? true : this.metadataInvolvesSse(request.getMetadata());
   }

   private boolean skipClientSideValidationPerResponse(ObjectMetadata metadata) {
      if (metadata == null) {
         return true;
      } else {
         return metadata.getETag() != null && !isMultipartUploadETag(metadata.getETag()) ? this.metadataInvolvesSse(metadata) : true;
      }
   }

   private boolean isGetObjectMd5ValidationDisabledByProperty() {
      return System.getProperty("com.amazonaws.services.s3.disableGetObjectMD5Validation") != null;
   }

   private boolean isPutObjectMd5ValidationDisabledByProperty() {
      return System.getProperty("com.amazonaws.services.s3.disablePutObjectMD5Validation") != null;
   }

   private boolean metadataInvolvesSse(ObjectMetadata metadata) {
      return metadata == null ? false : containsNonNull(metadata.getSSECustomerAlgorithm(), metadata.getSSECustomerKeyMd5(), metadata.getSSEAwsKmsKeyId());
   }

   private boolean putRequestInvolvesSse(PutObjectRequest request) {
      return containsNonNull(request.getSSECustomerKey(), request.getSSEAwsKeyManagementParams());
   }

   private static boolean isMultipartUploadETag(String eTag) {
      return eTag.contains("-");
   }

   private static boolean containsNonNull(Object... items) {
      for(Object item : items) {
         if (item != null) {
            return true;
         }
      }

      return false;
   }
}
