package com.amazonaws.services.s3.internal.crypto.v1;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class SecuredCEK {
   private final byte[] encrypted;
   private final String keyWrapAlgorithm;
   private final Map<String, String> matdesc;

   SecuredCEK(byte[] encryptedKey, String keyWrapAlgorithm, Map<String, String> matdesc) {
      this.encrypted = encryptedKey;
      this.keyWrapAlgorithm = keyWrapAlgorithm;
      this.matdesc = Collections.unmodifiableMap(new TreeMap<>(matdesc));
   }

   byte[] getEncrypted() {
      return this.encrypted;
   }

   String getKeyWrapAlgorithm() {
      return this.keyWrapAlgorithm;
   }

   Map<String, String> getMaterialDescription() {
      return this.matdesc;
   }
}
