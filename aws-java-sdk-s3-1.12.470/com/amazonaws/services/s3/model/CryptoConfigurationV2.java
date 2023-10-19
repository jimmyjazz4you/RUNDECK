package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.internal.crypto.CryptoRuntime;
import java.io.Serializable;
import java.security.Provider;
import java.security.SecureRandom;

public class CryptoConfigurationV2 implements Cloneable, Serializable {
   private static final long serialVersionUID = -8646831898339939580L;
   private static final SecureRandom SRAND = new SecureRandom();
   private CryptoMode cryptoMode;
   private CryptoStorageMode storageMode;
   private Provider cryptoProvider;
   private boolean alwaysUseCryptoProvider;
   private SecureRandom secureRandom;
   private boolean unsafeUndecryptableObjectPassthrough = false;
   private transient com.amazonaws.regions.Region awsKmsRegion;
   private CryptoRangeGetMode rangeGetMode = CryptoRangeGetMode.DISABLED;

   public CryptoConfigurationV2() {
      this(CryptoMode.StrictAuthenticatedEncryption);
   }

   public CryptoConfigurationV2(CryptoMode cryptoMode) {
      this.checkCryptoMode(cryptoMode);
      this.storageMode = CryptoStorageMode.ObjectMetadata;
      this.cryptoProvider = null;
      this.secureRandom = SRAND;
      this.cryptoMode = cryptoMode;
   }

   public void setStorageMode(CryptoStorageMode storageMode) {
      this.storageMode = storageMode;
   }

   public CryptoConfigurationV2 withStorageMode(CryptoStorageMode storageMode) {
      this.storageMode = storageMode;
      return this;
   }

   public CryptoStorageMode getStorageMode() {
      return this.storageMode;
   }

   public void setCryptoProvider(Provider cryptoProvider) {
      this.cryptoProvider = cryptoProvider;
   }

   public CryptoConfigurationV2 withCryptoProvider(Provider cryptoProvider) {
      this.cryptoProvider = cryptoProvider;
      return this;
   }

   public Provider getCryptoProvider() {
      return this.cryptoProvider;
   }

   public void setAlwaysUseCryptoProvider(boolean value) {
      this.alwaysUseCryptoProvider = value;
   }

   public CryptoConfigurationV2 withAlwaysUseCryptoProvider(boolean value) {
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

   public CryptoConfigurationV2 withSecureRandom(SecureRandom secureRandom) {
      this.secureRandom = secureRandom;
      return this;
   }

   public CryptoMode getCryptoMode() {
      return this.cryptoMode;
   }

   public void setCryptoMode(CryptoMode cryptoMode) throws UnsupportedOperationException {
      this.checkCryptoMode(cryptoMode);
      this.cryptoMode = cryptoMode;
   }

   public CryptoConfigurationV2 withCryptoMode(CryptoMode cryptoMode) throws UnsupportedOperationException {
      this.checkCryptoMode(cryptoMode);
      this.cryptoMode = cryptoMode;
      return this;
   }

   public boolean isUnsafeUndecryptableObjectPassthrough() {
      return this.unsafeUndecryptableObjectPassthrough;
   }

   public void setUnsafeUndecryptableObjectPassthrough(boolean unsafeUndecryptableObjectPassthrough) {
      this.unsafeUndecryptableObjectPassthrough = unsafeUndecryptableObjectPassthrough;
   }

   public CryptoConfigurationV2 withUnsafeUndecryptableObjectPassthrough(boolean unsafeUndecryptableObjectPassthrough) {
      this.unsafeUndecryptableObjectPassthrough = unsafeUndecryptableObjectPassthrough;
      return this;
   }

   public com.amazonaws.regions.Region getAwsKmsRegion() {
      return this.awsKmsRegion;
   }

   public void setAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
      this.awsKmsRegion = awsKmsRegion;
   }

   public CryptoConfigurationV2 withAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
      this.awsKmsRegion = awsKmsRegion;
      return this;
   }

   public CryptoConfigurationV2 withRangeGetMode(CryptoRangeGetMode rangeGetMode) {
      if (rangeGetMode == null) {
         rangeGetMode = CryptoRangeGetMode.DISABLED;
      }

      this.rangeGetMode = rangeGetMode;
      return this;
   }

   public void setRangeGetMode(CryptoRangeGetMode rangeGetMode) {
      this.withRangeGetMode(rangeGetMode);
   }

   public CryptoRangeGetMode getRangeGetMode() {
      return this.rangeGetMode;
   }

   private void checkCryptoMode(CryptoMode cryptoMode) {
      if (cryptoMode == CryptoMode.EncryptionOnly) {
         throw new UnsupportedOperationException("CryptoMode.EncryptionOnly is not allowed in this configuration");
      } else {
         boolean preferBC = cryptoMode == CryptoMode.AuthenticatedEncryption || cryptoMode == CryptoMode.StrictAuthenticatedEncryption;
         boolean haveOverride = this.cryptoProvider != null && this.alwaysUseCryptoProvider || CryptoRuntime.preferDefaultSecurityProvider();
         if (preferBC && !haveOverride) {
            this.checkBountyCastle();
         }
      }
   }

   private void checkBountyCastle() {
      if (!CryptoRuntime.isBouncyCastleAvailable()) {
         CryptoRuntime.enableBouncyCastle();
         if (!CryptoRuntime.isBouncyCastleAvailable()) {
            throw new UnsupportedOperationException("The Bouncy castle library jar is required on the classpath to enable authenticated encryption");
         }
      }

      if (!CryptoRuntime.isAesGcmAvailable()) {
         throw new UnsupportedOperationException("A more recent version of Bouncy castle is required for authenticated encryption.");
      }
   }

   public boolean isReadOnly() {
      return false;
   }

   public CryptoConfigurationV2 readOnly() {
      return this.isReadOnly() ? this : this.copyTo(new CryptoConfigurationV2.ReadOnly());
   }

   public CryptoConfigurationV2 clone() {
      return this.copyTo(new CryptoConfigurationV2());
   }

   private CryptoConfigurationV2 copyTo(CryptoConfigurationV2 that) {
      that.cryptoMode = this.cryptoMode;
      that.storageMode = this.storageMode;
      that.cryptoProvider = this.cryptoProvider;
      that.alwaysUseCryptoProvider = this.alwaysUseCryptoProvider;
      that.secureRandom = this.secureRandom;
      that.unsafeUndecryptableObjectPassthrough = this.unsafeUndecryptableObjectPassthrough;
      that.awsKmsRegion = this.awsKmsRegion;
      that.rangeGetMode = this.rangeGetMode;
      return that;
   }

   private static final class ReadOnly extends CryptoConfigurationV2 {
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
      public CryptoConfigurationV2 withStorageMode(CryptoStorageMode storageMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setCryptoProvider(Provider cryptoProvider) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withCryptoProvider(Provider cryptoProvider) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setAlwaysUseCryptoProvider(boolean value) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withAlwaysUseCryptoProvider(boolean value) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setSecureRandom(SecureRandom random) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withSecureRandom(SecureRandom random) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setCryptoMode(CryptoMode cryptoMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withCryptoMode(CryptoMode cryptoMode) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setUnsafeUndecryptableObjectPassthrough(boolean unsafeUndecryptableObjectPassthrough) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withUnsafeUndecryptableObjectPassthrough(boolean unsafeUndecryptableObjectPassthrough) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
         throw new UnsupportedOperationException();
      }

      @Override
      public CryptoConfigurationV2 withAwsKmsRegion(com.amazonaws.regions.Region awsKmsRegion) {
         throw new UnsupportedOperationException();
      }
   }
}
