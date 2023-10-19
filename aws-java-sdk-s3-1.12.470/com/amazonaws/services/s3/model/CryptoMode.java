package com.amazonaws.services.s3.model;

public enum CryptoMode {
   @Deprecated
   EncryptionOnly,
   AuthenticatedEncryption,
   StrictAuthenticatedEncryption;
}
