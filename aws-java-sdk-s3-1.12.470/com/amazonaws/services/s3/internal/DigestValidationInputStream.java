package com.amazonaws.services.s3.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.SdkDigestInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

public class DigestValidationInputStream extends SdkDigestInputStream {
   private byte[] expectedHash;
   private boolean digestValidated = false;

   public DigestValidationInputStream(InputStream in, MessageDigest digest, byte[] serverSideHash) {
      super(in, digest);
      this.expectedHash = serverSideHash;
   }

   public int read() throws IOException {
      int ch = super.read();
      if (ch == -1) {
         this.validateMD5Digest();
      }

      return ch;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      int result = super.read(b, off, len);
      if (result == -1) {
         this.validateMD5Digest();
      }

      return result;
   }

   public byte[] getMD5Checksum() {
      return this.digest.digest();
   }

   private void validateMD5Digest() {
      if (this.expectedHash != null && !this.digestValidated) {
         this.digestValidated = true;
         if (!Arrays.equals(this.digest.digest(), this.expectedHash)) {
            throw new SdkClientException(
               "Unable to verify integrity of data download.  Client calculated content hash didn't match hash calculated by Amazon S3.  The data may be corrupt."
            );
         }
      }
   }
}
