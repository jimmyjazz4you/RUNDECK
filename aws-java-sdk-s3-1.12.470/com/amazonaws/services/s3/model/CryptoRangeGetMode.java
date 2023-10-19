package com.amazonaws.services.s3.model;

public enum CryptoRangeGetMode {
   DISABLED(new CryptoRangeGetMode.Predicate() {
      @Override
      public boolean isPermitted(CryptoMode cryptoMode, String algorithm) {
         return false;
      }
   }),
   @Deprecated
   ALL(new CryptoRangeGetMode.Predicate() {
      @Override
      public boolean isPermitted(CryptoMode cryptoMode, String algorithm) {
         switch(cryptoMode) {
            case AuthenticatedEncryption:
               return "AES/CTR/NoPadding".equals(algorithm) || "AES/CBC/PKCS5Padding".equals(algorithm) || "AES/CBC/PKCS7Padding".equals(algorithm);
            case StrictAuthenticatedEncryption:
               return "AES/CTR/NoPadding".equals(algorithm);
            default:
               return false;
         }
      }
   });

   private static final String AES_CTR = "AES/CTR/NoPadding";
   private static final String AES_CBC_PKCS7 = "AES/CBC/PKCS7Padding";
   private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
   private final CryptoRangeGetMode.Predicate predicate;

   public boolean permitsCipherAlgorithm(CryptoMode cryptoMode, String algorithm) {
      return this.predicate.isPermitted(cryptoMode, algorithm);
   }

   private CryptoRangeGetMode(CryptoRangeGetMode.Predicate predicate) {
      this.predicate = predicate;
   }

   private interface Predicate {
      boolean isPermitted(CryptoMode var1, String var2);
   }
}
