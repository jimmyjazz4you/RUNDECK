package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.SdkThreadLocalsRegistry;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.util.BinaryUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

public class UploadPartCallable implements Callable<PartETag> {
   private static final int MAX_SKIPS = 100;
   private static final ThreadLocal<MessageDigest> MD5_DIGEST = SdkThreadLocalsRegistry.register(new ThreadLocal<MessageDigest>() {
      protected MessageDigest initialValue() {
         try {
            return MessageDigest.getInstance("MD5");
         } catch (NoSuchAlgorithmException var2) {
            throw new SdkClientException("Unable to get a digest instance for MD5!", var2);
         }
      }
   });
   private final AmazonS3 s3;
   private final UploadPartRequest request;
   private final boolean calculateMd5;

   public UploadPartCallable(AmazonS3 s3, UploadPartRequest request) {
      this(s3, request, false);
   }

   public UploadPartCallable(AmazonS3 s3, UploadPartRequest request, boolean calculateMd5) {
      this.s3 = s3;
      this.request = request;
      this.calculateMd5 = calculateMd5;
   }

   public PartETag call() throws Exception {
      if (this.calculateMd5) {
         this.request.withMD5Digest(this.computedMd5());
      }

      return this.s3.uploadPart(this.request).getPartETag();
   }

   private String computedMd5() {
      FileInputStream fileStream = null;

      String e;
      try {
         fileStream = new FileInputStream(this.request.getFile());
         this.skipBytes(fileStream, this.request.getFileOffset());
         e = BinaryUtils.toBase64(computeMd5Bytes(fileStream, this.request.getPartSize()));
      } catch (IOException var11) {
         throw new SdkClientException(var11);
      } finally {
         if (fileStream != null) {
            try {
               fileStream.close();
            } catch (IOException var10) {
            }
         }
      }

      return e;
   }

   private static byte[] computeMd5Bytes(InputStream is, long remaining) throws IOException {
      byte[] readBuff = new byte[4096];
      MessageDigest digest = MD5_DIGEST.get();
      digest.reset();

      int read;
      while(remaining > 0L && (read = is.read(readBuff)) != -1) {
         int updateLen = (int)Math.min(remaining, (long)read);
         digest.update(readBuff, 0, updateLen);
         remaining -= (long)updateLen;
      }

      return digest.digest();
   }

   private void skipBytes(FileInputStream fs, long n) throws IOException {
      long skippedSoFar = 0L;

      for(int skips = 0; skips < 100 && skippedSoFar < n; ++skips) {
         skippedSoFar += fs.skip(n - skippedSoFar);
      }

      if (skippedSoFar != n) {
         throw new SdkClientException(
            String.format("Unable to skip to offset %d in file %s after %d attempts", n, this.request.getFile().getAbsolutePath(), 100)
         );
      }
   }
}
