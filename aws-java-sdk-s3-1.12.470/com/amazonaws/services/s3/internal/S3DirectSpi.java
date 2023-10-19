package com.amazonaws.services.s3.internal;

import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
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

public interface S3DirectSpi {
   PutObjectResult putObject(PutObjectRequest var1);

   S3Object getObject(GetObjectRequest var1);

   ObjectMetadata getObject(GetObjectRequest var1, File var2);

   CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest var1);

   InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest var1);

   UploadPartResult uploadPart(UploadPartRequest var1);

   CopyPartResult copyPart(CopyPartRequest var1);

   void abortMultipartUpload(AbortMultipartUploadRequest var1);
}
