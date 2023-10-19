package com.amazonaws.services.s3.internal;

import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import java.io.File;

public abstract class S3Direct implements S3DirectSpi {
   @Override
   public abstract PutObjectResult putObject(PutObjectRequest var1);

   @Override
   public abstract S3Object getObject(GetObjectRequest var1);

   @Override
   public abstract ObjectMetadata getObject(GetObjectRequest var1, File var2);

   public abstract ObjectMetadata getObjectMetadata(GetObjectMetadataRequest var1);

   @Override
   public abstract CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest var1);

   @Override
   public abstract InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest var1);

   @Override
   public abstract UploadPartResult uploadPart(UploadPartRequest var1);

   @Override
   public abstract CopyPartResult copyPart(CopyPartRequest var1);

   @Override
   public abstract void abortMultipartUpload(AbortMultipartUploadRequest var1);
}
