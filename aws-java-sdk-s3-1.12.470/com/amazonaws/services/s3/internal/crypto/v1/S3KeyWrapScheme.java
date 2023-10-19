package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.services.s3.internal.crypto.CryptoRuntime;
import java.security.Key;

public class S3KeyWrapScheme {
   static final S3KeyWrapScheme NONE = new S3KeyWrapScheme() {
      @Override
      public String getKeyWrapAlgorithm(Key key) {
         return null;
      }

      @Override
      public String toString() {
         return "NONE";
      }
   };
   public static final String AESWrap = "AESWrap";
   public static final String RSA_ECB_OAEPWithSHA256AndMGF1Padding = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

   public String getKeyWrapAlgorithm(Key kek) {
      String algorithm = kek.getAlgorithm();
      if ("AES".equals(algorithm)) {
         return "AESWrap";
      } else {
         return "RSA".equals(algorithm) && CryptoRuntime.isRsaKeyWrapAvailable() ? "RSA/ECB/OAEPWithSHA-256AndMGF1Padding" : null;
      }
   }

   @Override
   public String toString() {
      return "S3KeyWrapScheme";
   }
}
