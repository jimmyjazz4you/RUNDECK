package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public abstract class S3CryptoModule<T extends MultipartUploadContext> {
   public abstract PutObjectResult putObjectSecurely(PutObjectRequest var1);

   public abstract S3Object getObjectSecurely(GetObjectRequest var1);

   public abstract ObjectMetadata getObjectSecurely(GetObjectRequest var1, File var2);

   public abstract CompleteMultipartUploadResult completeMultipartUploadSecurely(CompleteMultipartUploadRequest var1);

   public abstract InitiateMultipartUploadResult initiateMultipartUploadSecurely(InitiateMultipartUploadRequest var1);

   public abstract UploadPartResult uploadPartSecurely(UploadPartRequest var1);

   public abstract CopyPartResult copyPartSecurely(CopyPartRequest var1);

   public abstract void abortMultipartUploadSecurely(AbortMultipartUploadRequest var1);

   public abstract PutObjectResult putInstructionFileSecurely(PutInstructionFileRequest var1);

   public abstract void putLocalObjectSecurely(UploadObjectRequest var1, String var2, OutputStream var3) throws IOException;
}
