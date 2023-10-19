package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.util.Throwables;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;

public final class AesGcmKeyWrapper implements KeyWrapper {
   private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
   private static final int IV_LENGTH_IN_BYTES = 12;
   private static final int TAG_LENGTH_IN_BYTES = 16;
   private static final int TAG_LENGTH_IN_BITS = 128;
   private final CipherProvider cipherProvider;
   private final SecureRandom secureRandom;
   private final String cekAlgorithm;

   private AesGcmKeyWrapper(AesGcmKeyWrapper.Builder b) {
      this.cipherProvider = this.validateNotNull(b.cipherProvider, "cipherProvider");
      this.secureRandom = b.secureRandom;
      this.cekAlgorithm = this.validateNotNull(b.cekAlgorithm, "cekAlgorithm");
   }

   public static AesGcmKeyWrapper.Builder builder() {
      return new AesGcmKeyWrapper.Builder();
   }

   public static String cipherAlgorithm() {
      return "AES/GCM/NoPadding";
   }

   public CipherProvider cipherProvider() {
      return this.cipherProvider;
   }

   public SecureRandom secureRandom() {
      return this.secureRandom;
   }

   public String cekAlgorithm() {
      return this.cekAlgorithm;
   }

   @Override
   public byte[] unwrapCek(byte[] encryptedCek, Key key) {
      ByteBuffer encryptedCekBuff = ByteBuffer.wrap(encryptedCek);
      byte[] iv = new byte[12];
      byte[] taggedCek = new byte[encryptedCek.length - 12];
      encryptedCekBuff.get(iv);
      encryptedCekBuff.get(taggedCek);
      Cipher cipher = this.cipherProvider.createCipher();
      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

      try {
         cipher.init(2, key, gcmParameterSpec);
         cipher.updateAAD(this.cekAlgorithm.getBytes(StandardCharsets.UTF_8));
         return cipher.doFinal(taggedCek);
      } catch (Exception var9) {
         throw Throwables.failure(var9, "An exception was thrown when attempting to decrypt the Content Encryption Key");
      }
   }

   @Override
   public byte[] wrapCek(byte[] plaintextCek, Key key) {
      if (this.secureRandom == null) {
         throw new NullPointerException("Error initializing AesGcmKeyWrapper for wrapping: 'secureRandom' cannot be null");
      } else {
         Cipher cipher = this.cipherProvider.createCipher();
         byte[] iv = new byte[12];
         this.secureRandom.nextBytes(iv);
         GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

         try {
            cipher.init(1, key, gcmParameterSpec, this.secureRandom);
            cipher.updateAAD(this.cekAlgorithm.getBytes(StandardCharsets.UTF_8));
            return concat(cipher.getIV(), cipher.doFinal(plaintextCek));
         } catch (Exception var7) {
            throw Throwables.failure(var7, "An exception was thrown when attempting to encrypt the Content Encryption Key");
         }
      }
   }

   private <T> T validateNotNull(T obj, String propertyName) {
      if (obj == null) {
         throw new NullPointerException("Error initializing AesGcmKeyWrapper: '" + propertyName + "' cannot be null");
      } else {
         return obj;
      }
   }

   private static byte[] concat(byte[] a, byte[] b) {
      byte[] c = new byte[a.length + b.length];
      System.arraycopy(a, 0, c, 0, a.length);
      System.arraycopy(b, 0, c, a.length, b.length);
      return c;
   }

   public static final class Builder {
      private CipherProvider cipherProvider;
      private SecureRandom secureRandom;
      private String cekAlgorithm;

      private Builder() {
      }

      public AesGcmKeyWrapper.Builder cipherProvider(CipherProvider cipherProvider) {
         this.cipherProvider = cipherProvider;
         return this;
      }

      public AesGcmKeyWrapper.Builder secureRandom(SecureRandom secureRandom) {
         this.secureRandom = secureRandom;
         return this;
      }

      public AesGcmKeyWrapper.Builder cekAlgorithm(String cekAlgorithm) {
         this.cekAlgorithm = cekAlgorithm;
         return this;
      }

      public AesGcmKeyWrapper build() {
         return new AesGcmKeyWrapper(this);
      }
   }
}
