package com.amazonaws.services.s3.model.ownership;

public enum ObjectOwnership {
   BucketOwnerPreferred("BucketOwnerPreferred"),
   ObjectWriter("ObjectWriter"),
   BucketOwnerEnforced("BucketOwnerEnforced");

   private final String objectOwnershipId;

   public static ObjectOwnership fromValue(String s3OwnershipString) throws IllegalArgumentException {
      for(ObjectOwnership objectOwnership : values()) {
         if (objectOwnership.toString().equals(s3OwnershipString)) {
            return objectOwnership;
         }
      }

      throw new IllegalArgumentException("Cannot create enum from " + s3OwnershipString + " value!");
   }

   private ObjectOwnership(String id) {
      this.objectOwnershipId = id;
   }

   @Override
   public String toString() {
      return this.objectOwnershipId;
   }
}
