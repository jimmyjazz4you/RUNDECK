package com.amazonaws.services.s3.model;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EncryptedPutObjectRequest extends PutObjectRequest implements MaterialsDescriptionProvider, Serializable {
   private Map<String, String> materialsDescription;

   public EncryptedPutObjectRequest(String bucketName, String key, File file) {
      super(bucketName, key, file);
   }

   public EncryptedPutObjectRequest(String bucketName, String key, String redirectLocation) {
      super(bucketName, key, redirectLocation);
   }

   public EncryptedPutObjectRequest(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
      super(bucketName, key, input, metadata);
   }

   @Override
   public Map<String, String> getMaterialsDescription() {
      return this.materialsDescription;
   }

   public void setMaterialsDescription(Map<String, String> materialsDescription) {
      this.materialsDescription = materialsDescription == null ? null : Collections.unmodifiableMap(new HashMap<>(materialsDescription));
   }

   public EncryptedPutObjectRequest withMaterialsDescription(Map<String, String> materialsDescription) {
      this.setMaterialsDescription(materialsDescription);
      return this;
   }

   public EncryptedPutObjectRequest clone() {
      EncryptedPutObjectRequest cloned = (EncryptedPutObjectRequest)super.clone();
      Map<String, String> materialsDescription = this.getMaterialsDescription();
      cloned.withMaterialsDescription(materialsDescription == null ? null : new HashMap<>(materialsDescription));
      return cloned;
   }
}
