package com.amazonaws.services.s3.model;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.internal.crypto.CryptoRuntime;
import java.io.Serializable;
import java.security.Provider;
import java.security.SecureRandom;

@Deprecated
public class CryptoConfiguration implements Cloneable, Serializable {
   private static final long serialVersionUID = -8646831898339939580L;
   private static final SecureRandom SRAND = new SecureRandom();
   private CryptoMode cryptoMode;
   private CryptoStorageMode storageMode;
   private Provider cryptoProvider;
   private boolean alwaysUseCryptoProvider;
   private SecureRandom secureRandom;
   private boolean ignoreMissingInstructionFile = true;
   private transient com.amazonaws.regions.Region awskmsRegion;

   public CryptoConfiguration() {
      this(CryptoMode.EncryptionOnly);
   }

   public CryptoConfiguration(CryptoMode cryptoMode) {
      this.check(cryptoMode);
      this.storageMode = CryptoStorageMode.ObjectMetadata;
      this.cryptoProvider = null;
      this.secureRandom = SRAND;
      this.cryptoMode = cryptoMode;
   }

   public void setStorageMode(CryptoStorageMode storageMode) {
      this.storageMode = storageMode;
   }

   public CryptoConfiguration withStorageMode(CryptoStorageMode storageMode) {
      this.storageMode = storageMode;
      return this;
   }

   public CryptoStorageMode getStorageMode() {
      return this.storageMode;
   }

   public void setCryptoProvider(Provider cryptoProvider) {
      this.cryptoProvider = cryptoProvider;
   }

   public CryptoConfiguration withCryptoProvider(Provider cryptoProvider) {
      this.cryptoProvider = cryptoProvider;
      return this;
   }

   public Provider getCryptoProvider() {
      return this.cryptoProvider;
   }

   public void setAlwaysUseCryptoProvider(boolean value) {
      this.alwaysUseCryptoProvider = value;
   }

   public CryptoConfiguration withAlwaysUseCryptoProvider(boolean value) {
      this.alwaysUseCryptoProvider = value;
      return this;
   }

   public boolean getAlwaysUseCryptoProvider() {
      return this.alwaysUseCryptoProvider;
   }

   public SecureRandom getSecureRandom() {
      return this.secureRandom;
   }

   public void setSecureRandom(SecureRandom secureRandom) {
      this.secureRandom = secureRandom;
   }

   public CryptoConfiguration withSecureRandom(SecureRandom secureRandom) {
      this.secureRandom = secureRandom;
      return this;
   }

   public CryptoMode getCryptoMode() {
      return this.cryptoMode;
   }

   public void setCryptoMode(CryptoMode cryptoMode) throws UnsupportedOperationException {
      this.cryptoMode = cryptoMode;
      this.check(cryptoMode);
   }

   public CryptoConfiguration withCryptoMode(CryptoMode cryptoMode) throws UnsupportedOperationException {
      this.cryptoMode = cryptoMode;
      this.check(cryptoMode);
      return this;
   }

   public boolean isIgnoreMissingInstructionFile() {
      return this.ignoreMissingInstructionFile;
   }

   public void setIgnoreMissingInstructionFile(boolean ignoreMissingInstructionFile) {
      this.ignoreMissingInstructionFile = ignoreMissingInstructionFile;
   }

   public CryptoConfiguration withIgnoreMissingInstructionFile(boolean ignoreMissingInstructionFile) {
      this.ignoreMissingInstructionFile = ignoreMissingInstructionFile;
      return this;
   }

   private void check(CryptoMode cryptoMode) {
      boolean preferBC = cryptoMode == CryptoMode.AuthenticatedEncryption || cryptoMode == CryptoMode.StrictAuthenticatedEncryption;
      boolean haveOverride = this.cryptoProvider != null && this.alwaysUseCryptoProvider || CryptoRuntime.preferDefaultSecurityProvider();
      if (preferBC && !haveOverride) {
         if (!CryptoRuntime.isBouncyCastleAvailable()) {
            CryptoRuntime.enableBouncyCastle();
            if (!CryptoRuntime.isBouncyCastleAvailable()) {
               throw new UnsupportedOperationException("The Bouncy castle library jar is required on the classpath to enable authenticated encryption");
            }
         }

         if (!CryptoRuntime.isAesGcmAvailable()) {
            throw new UnsupportedOperationException("More recent version of the Bouncy castle library is required to enable authenticated encryption");
         }
      }
   }

   public boolean isReadOnly() {
      return false;
   }

   public CryptoConfiguration readOnly() {
      return this.isReadOnly() ? this : this.copyTo(new CryptoConfiguration.ReadOnly());
   }

   public CryptoConfiguration clone() {
      return this.copyTo(new CryptoConfiguration());
   }

   private CryptoConfiguration copyTo(CryptoConfiguration that) {
      that.cryptoMode = this.cryptoMode;
      that.storageMode = this.storageMode;
      that.cryptoProvider = this.cryptoProvider;
      that.alwaysUseCryptoProvider = this.alwaysUseCryptoProvider;
      that.secureRandom = this.secureRandom;
      that.ignoreMissingInstructionFile = this.ignoreMissingInstructionFile;
      that.awskmsRegion = this.awskmsRegion;
      return that;
   }

   @Deprecated
   public Regions getKmsRegion() {
      return this.awskmsRegion == null ? null : Regions.fromName(this.awskmsRegion.getName());
   }

   @Deprecated
   public void setKmsRegion(Regions kmsRegion) {
      if (kmsRegion != null) {
         this.setAwsKmsRegion(com.amazonaws.regions.Region.getRegion(kmsRegion));
      } else {
         this.setAwsKmsRegion(null);
      }
   }

   @Deprecated
   public CryptoConfiguration withKmsRegion(Regions kmsRegion) {
      this.setKmsRegion(kmsRegion);
      return this;
   }

   public com.amazonaws.regions.Region getAwsKmsRegion() {
      return this.awskmsRegion;
   }

   public void setAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
      this.awskmsRegion = awsKmsRegion;
   }

   public CryptoConfiguration withAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
      this.awskmsRegion = awsKmsRegion;
      return this;
   }

   private static final class ReadOnly extends CryptoConfiguration {
      private ReadOnly() {
      }

      @Override
      public boolean isReadOnly() {
         return true;
      }

      @Override
      public void setStorageMode(CryptoStorageMode storageMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withStorageMode(CryptoStorageMode storageMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setCryptoProvider(Provider cryptoProvider) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withCryptoProvider(Provider cryptoProvider) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setAlwaysUseCryptoProvider(boolean value) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withAlwaysUseCryptoProvider(boolean value) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setSecureRandom(SecureRandom random) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withSecureRandom(SecureRandom random) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setCryptoMode(CryptoMode cryptoMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withCryptoMode(CryptoMode cryptoMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setIgnoreMissingInstructionFile(boolean ignoreMissingInstructionFile) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withIgnoreMissingInstructionFile(boolean ignoreMissingInstructionFile) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setKmsRegion(Regions kmsRegion) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfiguration withKmsRegion(Regions kmsRegion) {
         throw new UnsupportedOperationException();
      }
   }
}
