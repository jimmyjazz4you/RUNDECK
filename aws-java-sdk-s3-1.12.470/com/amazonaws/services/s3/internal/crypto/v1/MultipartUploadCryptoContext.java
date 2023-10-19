package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.GuardedBy;
import com.amazonaws.services.s3.internal.crypto.CipherLite;

class MultipartUploadCryptoContext extends MultipartUploadContext {
   private final ContentCryptoMaterial cekMaterial;
   @GuardedBy("this")
   private int partNumber;
   private volatile boolean partUploadInProgress;

   MultipartUploadCryptoContext(String bucketName, String key, ContentCryptoMaterial cekMaterial) {
      super(bucketName, key);
      this.cekMaterial = cekMaterial;
   }

   CipherLite getCipherLite() {
      return this.cekMaterial.getCipherLite();
   }

   ContentCryptoMaterial getContentCryptoMaterial() {
      return this.cekMaterial;
   }

   void beginPartUpload(int nextPartNumber) throws SdkClientException {
      if (nextPartNumber < 1) {
         throw new IllegalArgumentException("part number must be at least 1");
      } else if (this.partUploadInProgress) {
         throw new SdkClientException("Parts are required to be uploaded in series");
      } else {
         synchronized(this) {
            if (nextPartNumber - this.partNumber <= 1) {
               this.partNumber = nextPartNumber;
               this.partUploadInProgress = true;
            } else {
               throw new SdkClientException(
                  "Parts are required to be uploaded in series (partNumber=" + this.partNumber + ", nextPartNumber=" + nextPartNumber + ")"
               );
            }
         }
      }
   }

   void endPartUpload() {
      this.partUploadInProgress = false;
   }
}
