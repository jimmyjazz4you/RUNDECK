package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiObjectDeleteException extends AmazonS3Exception implements Serializable {
   private static final long serialVersionUID = -2004213552302446866L;
   private final List<MultiObjectDeleteException.DeleteError> errors = new ArrayList<>();
   private final List<DeleteObjectsResult.DeletedObject> deletedObjects = new ArrayList<>();

   public MultiObjectDeleteException(Collection<MultiObjectDeleteException.DeleteError> errors, Collection<DeleteObjectsResult.DeletedObject> deletedObjects) {
      super("One or more objects could not be deleted");
      this.deletedObjects.addAll(deletedObjects);
      this.errors.addAll(errors);
   }

   public String getErrorCode() {
      return super.getErrorCode();
   }

   public List<DeleteObjectsResult.DeletedObject> getDeletedObjects() {
      return this.deletedObjects;
   }

   public List<MultiObjectDeleteException.DeleteError> getErrors() {
      return this.errors;
   }

   public static class DeleteError implements Serializable {
      private String key;
      private String versionId;
      private String code;
      private String message;

      public String getKey() {
         return this.key;
      }

      public void setKey(String key) {
         this.key = key;
      }

      public String getVersionId() {
         return this.versionId;
      }

      public void setVersionId(String versionId) {
         this.versionId = versionId;
      }

      public String getCode() {
         return this.code;
      }

      public void setCode(String code) {
         this.code = code;
      }

      public String getMessage() {
         return this.message;
      }

      public void setMessage(String message) {
         this.message = message;
      }
   }
}
