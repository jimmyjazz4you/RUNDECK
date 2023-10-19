package com.amazonaws.services.s3.internal.crypto.v1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class MultipartUploadContext {
   private final String bucketName;
   private final String key;
   private boolean hasFinalPartBeenSeen;
   private Map<String, String> materialsDescription;

   protected MultipartUploadContext(String bucketName, String key) {
      this.bucketName = bucketName;
      this.key = key;
   }

   public final String getBucketName() {
      return this.bucketName;
   }

   public final String getKey() {
      return this.key;
   }

   public final boolean hasFinalPartBeenSeen() {
      return this.hasFinalPartBeenSeen;
   }

   public final void setHasFinalPartBeenSeen(boolean hasFinalPartBeenSeen) {
      this.hasFinalPartBeenSeen = hasFinalPartBeenSeen;
   }

   public final Map<String, String> getMaterialsDescription() {
      return this.materialsDescription;
   }

   public final void setMaterialsDescription(Map<String, String> materialsDescription) {
      this.materialsDescription = materialsDescription == null ? null : Collections.unmodifiableMap(new HashMap<>(materialsDescription));
   }
}
