package com.amazonaws.services.s3;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.UploadObjectRequest;

public abstract class AbstractAmazonS3EncryptionV2 extends AbstractAmazonS3 implements AmazonS3EncryptionV2 {
   @Override
   public PutObjectResult putInstructionFile(PutInstructionFileRequest req) {
      throw new UnsupportedOperationException("Extend AbstractAmazonS3 to provide an implementation");
   }

   @Override
   public CompleteMultipartUploadResult uploadObject(UploadObjectRequest req) {
      throw new UnsupportedOperationException("Extend AbstractAmazonS3 to provide an implementation");
   }
}
