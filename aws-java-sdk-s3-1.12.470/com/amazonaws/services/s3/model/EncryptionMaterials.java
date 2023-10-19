package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

public class EncryptionMaterials implements Serializable {
   private final KeyPair keyPair;
   private final SecretKey symmetricKey;
   private final Map<String, String> desc = new HashMap<>();

   public EncryptionMaterials(KeyPair keyPair) {
      this(keyPair, null);
   }

   public EncryptionMaterials(SecretKey symmetricKey) {
      this(null, symmetricKey);
   }

   protected EncryptionMaterials(KeyPair keyPair, SecretKey symmetricKey) {
      this.keyPair = keyPair;
      this.symmetricKey = symmetricKey;
   }

   public KeyPair getKeyPair() {
      return this.keyPair;
   }

   public SecretKey getSymmetricKey() {
      return this.symmetricKey;
   }

   public Map<String, String> getMaterialsDescription() {
      return new HashMap<>(this.desc);
   }

   public EncryptionMaterialsAccessor getAccessor() {
      return null;
   }

   public EncryptionMaterials addDescription(String name, String value) {
      this.desc.put(name, value);
      return this;
   }

   public EncryptionMaterials addDescriptions(Map<String, String> descriptions) {
      this.desc.putAll(descriptions);
      return this;
   }

   public boolean isKMSEnabled() {
      return false;
   }

   public String getCustomerMasterKeyId() {
      throw new UnsupportedOperationException();
   }

   protected String getDescription(String name) {
      return this.desc.get(name);
   }
}
