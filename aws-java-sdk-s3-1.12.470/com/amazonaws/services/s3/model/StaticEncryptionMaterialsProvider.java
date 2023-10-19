package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Map;

public class StaticEncryptionMaterialsProvider implements EncryptionMaterialsProvider, Serializable {
   private final EncryptionMaterials materials;

   public StaticEncryptionMaterialsProvider(EncryptionMaterials materials) {
      this.materials = materials;
   }

   @Override
   public EncryptionMaterials getEncryptionMaterials() {
      return this.materials;
   }

   @Override
   public void refresh() {
   }

   @Override
   public EncryptionMaterials getEncryptionMaterials(Map<String, String> materialDescIn) {
      if (this.materials == null) {
         return null;
      } else {
         Map<String, String> materialDesc = this.materials.getMaterialsDescription();
         if (materialDescIn != null && materialDescIn.equals(materialDesc)) {
            return this.materials;
         } else {
            EncryptionMaterialsAccessor accessor = this.materials.getAccessor();
            if (accessor != null) {
               EncryptionMaterials accessorMaterials = accessor.getEncryptionMaterials(materialDescIn);
               if (accessorMaterials != null) {
                  return accessorMaterials;
               }
            }

            boolean noMaterialDescIn = materialDescIn == null || materialDescIn.size() == 0;
            boolean noMaterialDesc = materialDesc == null || materialDesc.size() == 0;
            return noMaterialDescIn && noMaterialDesc ? this.materials : null;
         }
      }
   }
}
