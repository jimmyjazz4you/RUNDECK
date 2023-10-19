package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.UploadObjectObserver;
import com.amazonaws.services.s3.internal.MultiFileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class UploadObjectRequest extends AbstractPutObjectRequest implements MaterialsDescriptionProvider, Serializable {
   private static final long serialVersionUID = 1L;
   static final int MIN_PART_SIZE = 5242880;
   private ObjectMetadata uploadPartMetadata;
   private Map<String, String> materialsDescription;
   private long partSize = 5242880L;
   private transient ExecutorService executorService;
   private transient MultiFileOutputStream multiFileOutputStream;
   private transient UploadObjectObserver uploadObjectObserver;
   private long diskLimit = Long.MAX_VALUE;

   public UploadObjectRequest(String bucketName, String key, File file) {
      super(bucketName, key, file);
   }

   public UploadObjectRequest(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
      super(bucketName, key, input, metadata);
   }

   public long getPartSize() {
      return this.partSize;
   }

   public UploadObjectRequest withPartSize(long partSize) {
      if (partSize < 5242880L) {
         throw new IllegalArgumentException("partSize must be at least 5242880");
      } else {
         this.partSize = partSize;
         return this;
      }
   }

   public long getDiskLimit() {
      return this.diskLimit;
   }

   public UploadObjectRequest withDiskLimit(long diskLimit) {
      this.diskLimit = diskLimit;
      return this;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public UploadObjectRequest withExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
   }

   public MultiFileOutputStream getMultiFileOutputStream() {
      return this.multiFileOutputStream;
   }

   public UploadObjectRequest withMultiFileOutputStream(MultiFileOutputStream multiFileOutputStream) {
      this.multiFileOutputStream = multiFileOutputStream;
      return this;
   }

   public UploadObjectObserver getUploadObjectObserver() {
      return this.uploadObjectObserver;
   }

   public UploadObjectRequest withUploadObjectObserver(UploadObjectObserver uploadObjectObserver) {
      this.uploadObjectObserver = uploadObjectObserver;
      return this;
   }

   @Override
   public Map<String, String> getMaterialsDescription() {
      return this.materialsDescription;
   }

   public void setMaterialsDescription(Map<String, String> materialsDescription) {
      this.materialsDescription = materialsDescription == null ? null : Collections.unmodifiableMap(new HashMap<>(materialsDescription));
   }

   public UploadObjectRequest withMaterialsDescription(Map<String, String> materialsDescription) {
      this.setMaterialsDescription(materialsDescription);
      return this;
   }

   public ObjectMetadata getUploadPartMetadata() {
      return this.uploadPartMetadata;
   }

   public void setUploadPartMetadata(ObjectMetadata partUploadMetadata) {
      this.uploadPartMetadata = partUploadMetadata;
   }

   public <T extends UploadObjectRequest> T withUploadPartMetadata(ObjectMetadata partUploadMetadata) {
      this.setUploadPartMetadata(partUploadMetadata);
      return (T)this;
   }

   public UploadObjectRequest clone() {
      UploadObjectRequest cloned = (UploadObjectRequest)super.clone();
      super.copyPutObjectBaseTo(cloned);
      Map<String, String> materialsDescription = this.getMaterialsDescription();
      ObjectMetadata uploadPartMetadata = this.getUploadPartMetadata();
      return cloned.withMaterialsDescription(materialsDescription == null ? null : new HashMap<>(materialsDescription))
         .withDiskLimit(this.getDiskLimit())
         .withExecutorService(this.getExecutorService())
         .withMultiFileOutputStream(this.getMultiFileOutputStream())
         .withPartSize(this.getPartSize())
         .withUploadObjectObserver(this.getUploadObjectObserver())
         .withUploadPartMetadata(uploadPartMetadata == null ? null : uploadPartMetadata.clone());
   }
}
