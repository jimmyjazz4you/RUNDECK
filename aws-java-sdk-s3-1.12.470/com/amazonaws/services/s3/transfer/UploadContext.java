package com.amazonaws.services.s3.transfer;

import com.amazonaws.annotation.SdkInternalApi;
import java.io.File;

public final class UploadContext {
   private final File file;
   private final String bucket;
   private final String key;

   @SdkInternalApi
   UploadContext(File file, String bucket, String key) {
      this.file = file;
      this.bucket = bucket;
      this.key = key;
   }

   public File getFile() {
      return this.file;
   }

   public String getBucket() {
      return this.bucket;
   }

   public String getKey() {
      return this.key;
   }
}
