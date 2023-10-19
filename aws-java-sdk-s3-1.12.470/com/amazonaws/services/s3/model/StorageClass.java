package com.amazonaws.services.s3.model;

public enum StorageClass {
   Standard("STANDARD"),
   ReducedRedundancy("REDUCED_REDUNDANCY"),
   Glacier("GLACIER"),
   StandardInfrequentAccess("STANDARD_IA"),
   OneZoneInfrequentAccess("ONEZONE_IA"),
   IntelligentTiering("INTELLIGENT_TIERING"),
   DeepArchive("DEEP_ARCHIVE"),
   Outposts("OUTPOSTS"),
   GlacierInstantRetrieval("GLACIER_IR"),
   Snow("SNOW");

   private final String storageClassId;

   public static StorageClass fromValue(String s3StorageClassString) throws IllegalArgumentException {
      for(StorageClass storageClass : values()) {
         if (storageClass.toString().equals(s3StorageClassString)) {
            return storageClass;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + s3StorageClassString + " value!");
   }

   private StorageClass(String id) {
      this.storageClassId = id;
   }

   @Override
   public String toString() {
      return this.storageClassId;
   }
}
