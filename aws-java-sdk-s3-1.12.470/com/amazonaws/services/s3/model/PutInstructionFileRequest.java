package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PutInstructionFileRequest extends AmazonWebServiceRequest implements MaterialsDescriptionProvider, EncryptionMaterialsFactory {
   private final S3ObjectId s3ObjectId;
   private final EncryptionMaterials encryptionMaterials;
   private final Map<String, String> matDesc;
   private final String suffix;
   private CannedAccessControlList cannedAcl;
   private AccessControlList accessControlList;
   private String redirectLocation;
   private String storageClass;

   public PutInstructionFileRequest(S3ObjectId s3ObjectId, Map<String, String> matDesc, String suffix) {
      if (s3ObjectId == null || s3ObjectId instanceof InstructionFileId) {
         throw new IllegalArgumentException("Invalid s3 object id");
      } else if (suffix != null && !suffix.trim().isEmpty()) {
         this.s3ObjectId = s3ObjectId;
         Map<String, String> md = matDesc == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(new HashMap<>(matDesc));
         this.matDesc = md;
         this.suffix = suffix;
         this.encryptionMaterials = null;
      } else {
         throw new IllegalArgumentException("suffix must be specified");
      }
   }

   public PutInstructionFileRequest(S3ObjectId s3ObjectId, EncryptionMaterials encryptionMaterials, String suffix) {
      if (s3ObjectId == null || s3ObjectId instanceof InstructionFileId) {
         throw new IllegalArgumentException("Invalid s3 object id");
      } else if (suffix == null || suffix.trim().isEmpty()) {
         throw new IllegalArgumentException("suffix must be specified");
      } else if (encryptionMaterials == null) {
         throw new IllegalArgumentException("encryption materials must be specified");
      } else {
         this.s3ObjectId = s3ObjectId;
         this.suffix = suffix;
         this.encryptionMaterials = encryptionMaterials;
         this.matDesc = null;
      }
   }

   public S3ObjectId getS3ObjectId() {
      return this.s3ObjectId;
   }

   @Override
   public Map<String, String> getMaterialsDescription() {
      return this.matDesc == null ? this.encryptionMaterials.getMaterialsDescription() : this.matDesc;
   }

   @Override
   public EncryptionMaterials getEncryptionMaterials() {
      return this.encryptionMaterials;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public CannedAccessControlList getCannedAcl() {
      return this.cannedAcl;
   }

   public void setCannedAcl(CannedAccessControlList cannedAcl) {
      this.cannedAcl = cannedAcl;
   }

   public PutInstructionFileRequest withCannedAcl(CannedAccessControlList cannedAcl) {
      this.setCannedAcl(cannedAcl);
      return this;
   }

   public AccessControlList getAccessControlList() {
      return this.accessControlList;
   }

   public void setAccessControlList(AccessControlList accessControlList) {
      this.accessControlList = accessControlList;
   }

   public PutInstructionFileRequest withAccessControlList(AccessControlList accessControlList) {
      this.setAccessControlList(accessControlList);
      return this;
   }

   public String getRedirectLocation() {
      return this.redirectLocation;
   }

   public void setRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
   }

   public PutInstructionFileRequest withRedirectLocation(String redirectLocation) {
      this.redirectLocation = redirectLocation;
      return this;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public PutInstructionFileRequest withStorageClass(String storageClass) {
      this.setStorageClass(storageClass);
      return this;
   }

   public void setStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass.toString();
   }

   public PutInstructionFileRequest withStorageClass(StorageClass storageClass) {
      this.setStorageClass(storageClass);
      return this;
   }

   @Deprecated
   public PutObjectRequest createPutObjectRequest(S3Object s3Object) {
      if (s3Object.getBucketName().equals(this.s3ObjectId.getBucket()) && s3Object.getKey().equals(this.s3ObjectId.getKey())) {
         InstructionFileId ifid = this.s3ObjectId.instructionFileId(this.suffix);
         return (PutObjectRequest)new PutObjectRequest(ifid.getBucket(), ifid.getKey(), this.redirectLocation)
            .withAccessControlList(this.accessControlList)
            .withCannedAcl(this.cannedAcl)
            .withStorageClass(this.storageClass)
            .withGeneralProgressListener(this.getGeneralProgressListener())
            .withRequestMetricCollector(this.getRequestMetricCollector());
      } else {
         throw new IllegalArgumentException("s3Object passed inconsistent with the instruction file being created");
      }
   }

   public PutObjectRequest createPutObjectRequest(S3ObjectId objectId) {
      if (objectId.getBucket().equals(this.s3ObjectId.getBucket()) && objectId.getKey().equals(this.s3ObjectId.getKey())) {
         InstructionFileId ifid = this.s3ObjectId.instructionFileId(this.suffix);
         return (PutObjectRequest)new PutObjectRequest(ifid.getBucket(), ifid.getKey(), this.redirectLocation)
            .withAccessControlList(this.accessControlList)
            .withCannedAcl(this.cannedAcl)
            .withStorageClass(this.storageClass)
            .withGeneralProgressListener(this.getGeneralProgressListener())
            .withRequestMetricCollector(this.getRequestMetricCollector());
      } else {
         throw new IllegalArgumentException("s3Object passed inconsistent with the instruction file being created");
      }
   }
}
