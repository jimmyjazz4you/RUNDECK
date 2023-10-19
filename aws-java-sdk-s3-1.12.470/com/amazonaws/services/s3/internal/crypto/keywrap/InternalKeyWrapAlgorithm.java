package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.services.s3.model.CryptoKeyWrapAlgorithm;
import com.amazonaws.util.ValidationUtils;
import java.io.Serializable;

public enum InternalKeyWrapAlgorithm {
   AES_GCM_NoPadding("AES/GCM", InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(false).isAsymmetric(false).isKms(false).build()),
   AESWrap("AESWrap", InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(true).isAsymmetric(false).isKms(false).build()),
   RSA_OAEP_SHA1("RSA-OAEP-SHA1", InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(false).isAsymmetric(true).isKms(false).build()),
   RSA_ECB_OAEPWithSHA256AndMGF1Padding(
      "RSA/ECB/OAEPWithSHA-256AndMGF1Padding",
      InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(true).isAsymmetric(true).isKms(false).build()
   ),
   KMS("kms+context", InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(false).isAsymmetric(false).isKms(true).build()),
   KMS_V1("kms", InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.builder().isV1Algorithm(true).isAsymmetric(false).isKms(true).build());

   private final String algorithmName;
   private final InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs keyWrapAlgorithmSpecs;

   private InternalKeyWrapAlgorithm(String algorithmName, InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs keyWrapAlgorithmSpecs) {
      this.algorithmName = algorithmName;
      this.keyWrapAlgorithmSpecs = keyWrapAlgorithmSpecs;
   }

   public String algorithmName() {
      return this.algorithmName;
   }

   public static InternalKeyWrapAlgorithm fromExternal(CryptoKeyWrapAlgorithm external) {
      switch(external) {
         case RSA_OAEP_SHA1:
            return RSA_OAEP_SHA1;
         case AES_GCM_NoPadding:
            return AES_GCM_NoPadding;
         case KMS:
            return KMS;
         default:
            throw new SecurityException("Unknown key-wrapping algorithm: " + external.algorithmName());
      }
   }

   public static InternalKeyWrapAlgorithm fromAlgorithmName(String algorithmName) {
      for(InternalKeyWrapAlgorithm value : values()) {
         if (value.algorithmName.equals(algorithmName)) {
            return value;
         }
      }

      return null;
   }

   public boolean isV1Algorithm() {
      return this.keyWrapAlgorithmSpecs.isV1Algorithm();
   }

   public boolean isAsymmetric() {
      return this.keyWrapAlgorithmSpecs.isAsymmetric();
   }

   public boolean isSymmetric() {
      return !this.keyWrapAlgorithmSpecs.isAsymmetric();
   }

   public boolean isKMS() {
      return this.keyWrapAlgorithmSpecs.isKms();
   }

   private static final class KeyWrapAlgorithmSpecs implements Serializable {
      private final boolean isV1Algorithm;
      private final boolean isAsymmetric;
      private final boolean isKms;

      private KeyWrapAlgorithmSpecs(InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder b) {
         this.isV1Algorithm = ValidationUtils.assertNotNull(b.isV1Algorithm, "isV1Algorithm");
         this.isAsymmetric = ValidationUtils.assertNotNull(b.isAsymmetric, "isAsymmetric");
         this.isKms = ValidationUtils.assertNotNull(b.isKms, "isKms");
      }

      private static InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder builder() {
         return new InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder();
      }

      public boolean isV1Algorithm() {
         return this.isV1Algorithm;
      }

      public boolean isAsymmetric() {
         return this.isAsymmetric;
      }

      public boolean isKms() {
         return this.isKms;
      }

      private static final class Builder {
         private Boolean isV1Algorithm;
         private Boolean isAsymmetric;
         private Boolean isKms;

         private Builder() {
         }

         public InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder isV1Algorithm(boolean v1Algorithm) {
            this.isV1Algorithm = v1Algorithm;
            return this;
         }

         public InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder isAsymmetric(boolean asymmetric) {
            this.isAsymmetric = asymmetric;
            return this;
         }

         public InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs.Builder isKms(boolean kms) {
            this.isKms = kms;
            return this;
         }

         public InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs build() {
            return new InternalKeyWrapAlgorithm.KeyWrapAlgorithmSpecs(this);
         }
      }
   }
}
