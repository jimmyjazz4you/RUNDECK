package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EncryptedInitiateMultipartUploadRequest extends InitiateMultipartUploadRequest implements MaterialsDescriptionProvider, Serializable {
   private Map<String, String> materialsDescription;
   private boolean createEncryptionMaterial = true;

   public EncryptedInitiateMultipartUploadRequest(String bucketName, String key) {
      super(bucketName, key);
   }

   public EncryptedInitiateMultipartUploadRequest(String bucketName, String key, ObjectMetadata objectMetadata) {
      super(bucketName, key, objectMetadata);
   }

   @Override
   public Map<String, String> getMaterialsDescription() {
      return this.materialsDescription;
   }

   public void setMaterialsDescription(Map<String, String> materialsDescription) {
      this.materialsDescription = materialsDescription == null ? null : Collections.unmodifiableMap(new HashMap<>(materialsDescription));
   }

   public EncryptedInitiateMultipartUploadRequest withMaterialsDescription(Map<String, String> materialsDescription) {
      this.setMaterialsDescription(materialsDescription);
      return this;
   }

   public boolean isCreateEncryptionMaterial() {
      return this.createEncryptionMaterial;
   }

   public void setCreateEncryptionMaterial(boolean createEncryptionMaterial) {
      this.createEncryptionMaterial = createEncryptionMaterial;
   }

   public EncryptedInitiateMultipartUploadRequest withCreateEncryptionMaterial(boolean createEncryptionMaterial) {
      this.createEncryptionMaterial = createEncryptionMaterial;
      return this;
   }
}
