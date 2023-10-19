package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class KMSEncryptionMaterialsProvider extends StaticEncryptionMaterialsProvider implements Serializable {
   public KMSEncryptionMaterialsProvider(String defaultCustomerMasterKeyId) {
      this(new KMSEncryptionMaterials(defaultCustomerMasterKeyId));
   }

   public KMSEncryptionMaterialsProvider(KMSEncryptionMaterials materials) {
      super(materials);
   }
}
