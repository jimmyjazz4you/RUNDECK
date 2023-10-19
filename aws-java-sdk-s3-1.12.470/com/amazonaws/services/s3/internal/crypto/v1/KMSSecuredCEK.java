package com.amazonaws.services.s3.internal.crypto.v1;

import java.util.Map;

final class KMSSecuredCEK extends SecuredCEK {
   static final String KEY_PROTECTION_MECHANISM_V1 = "kms";
   static final String KEY_PROTECTION_MECHANISM_V2 = "kms+context";

   KMSSecuredCEK(byte[] encryptedKeyBlob, Map<String, String> matdesc) {
      super(encryptedKeyBlob, "kms", matdesc);
   }

   public static boolean isKMSKeyWrapped(String keyWrapAlgo) {
      return isKMSV1KeyWrapped(keyWrapAlgo) || isKMSV2KeyWrapped(keyWrapAlgo);
   }

   public static boolean isKMSV1KeyWrapped(String keyWrapAlgo) {
      return "kms".equals(keyWrapAlgo);
   }

   public static boolean isKMSV2KeyWrapped(String keyWrapAlgo) {
      return "kms+context".equals(keyWrapAlgo);
   }
}
