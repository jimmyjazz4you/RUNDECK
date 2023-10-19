package com.amazonaws.services.s3.internal;

import com.amazonaws.internal.SdkFilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5DigestCalculatingInputStream extends SdkFilterInputStream {
   private static Log log = LogFactory.getLog(MD5DigestCalculatingInputStream.class);
   private MessageDigest digest;
   private boolean digestCanBeCloned;
   private MessageDigest digestLastMarked;

   public MD5DigestCalculatingInputStream(InputStream in) {
      super(in);
      this.resetDigest();
      if (in.markSupported() && !this.digestCanBeCloned) {
         log.debug(
            "Mark-and-reset disabled on MD5 calculation because the digest implementation does not support cloning. This will limit the SDK's ability to retry requests that failed. Consider pre-calculating the MD5 checksum for the request or switching to a security provider that supports message digest cloning."
         );
      }
   }

   private void resetDigest() {
      try {
         this.digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException("No message digest support for MD5 was found.", var2);
      }

      this.digestCanBeCloned = this.canBeCloned(this.digest);
   }

   private boolean canBeCloned(MessageDigest digest) {
      try {
         digest.clone();
         return true;
      } catch (CloneNotSupportedException var3) {
         return false;
      }
   }

   private MessageDigest cloneFrom(MessageDigest from) {
      try {
         return (MessageDigest)from.clone();
      } catch (CloneNotSupportedException var3) {
         throw new IllegalStateException("Message digest implementation does not support cloning.", var3);
      }
   }

   public boolean markSupported() {
      return super.markSupported() && this.digestCanBeCloned;
   }

   public byte[] getMd5Digest() {
      return this.digest.digest();
   }

   public void mark(int readlimit) {
      if (this.markSupported()) {
         super.mark(readlimit);
         this.digestLastMarked = this.cloneFrom(this.digest);
      }
   }

   public void reset() throws IOException {
      if (this.markSupported()) {
         super.reset();
         if (this.digestLastMarked == null) {
            this.resetDigest();
         } else {
            this.digest = this.cloneFrom(this.digestLastMarked);
         }
      } else {
         throw new IOException("mark/reset not supported");
      }
   }

   public int read() throws IOException {
      int ch = super.read();
      if (ch != -1) {
         this.digest.update((byte)ch);
      }

      return ch;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      int result = super.read(b, off, len);
      if (result != -1) {
         this.digest.update(b, off, result);
      }

      return result;
   }
}
