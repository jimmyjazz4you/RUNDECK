package com.amazonaws.services.s3;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface AmazonS3EncryptionV2 extends AmazonS3 {
   PutObjectResult putInstructionFile(PutInstructionFileRequest var1);

   CompleteMultipartUploadResult uploadObject(UploadObjectRequest var1) throws IOException, InterruptedException, ExecutionException;
}
