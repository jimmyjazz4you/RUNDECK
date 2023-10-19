package com.amazonaws.services.s3.internal.crypto.v2;

import com.amazonaws.services.s3.internal.crypto.keywrap.InternalKeyWrapAlgorithm;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class SecuredCEK {
   private final byte[] encrypted;
   private final InternalKeyWrapAlgorithm keyWrapAlgorithm;
   private final Map<String, String> matdesc;

   SecuredCEK(byte[] encryptedKey, InternalKeyWrapAlgorithm keyWrapAlgorithm, Map<String, String> matdesc) {
      this.encrypted = encryptedKey;
      this.keyWrapAlgorithm = keyWrapAlgorithm;
      this.matdesc = Collections.unmodifiableMap(new TreeMap<>(matdesc));
   }

   byte[] getEncrypted() {
      return this.encrypted;
   }

   InternalKeyWrapAlgorithm getKeyWrapAlgorithm() {
      return this.keyWrapAlgorithm;
   }

   Map<String, String> getMaterialDescription() {
      return this.matdesc;
   }
}
