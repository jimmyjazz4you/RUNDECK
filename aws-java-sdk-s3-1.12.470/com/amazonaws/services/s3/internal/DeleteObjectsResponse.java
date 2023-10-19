package com.amazonaws.services.s3.internal;

import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import java.util.ArrayList;
import java.util.List;

public class DeleteObjectsResponse implements S3RequesterChargedResult {
   private List<DeleteObjectsResult.DeletedObject> deletedObjects;
   private List<MultiObjectDeleteException.DeleteError> errors;
   private boolean isRequesterCharged;

   public DeleteObjectsResponse() {
      this(new ArrayList<>(), new ArrayList<>());
   }

   public DeleteObjectsResponse(List<DeleteObjectsResult.DeletedObject> deletedObjects, List<MultiObjectDeleteException.DeleteError> errors) {
      this.deletedObjects = deletedObjects;
      this.errors = errors;
   }

   public List<DeleteObjectsResult.DeletedObject> getDeletedObjects() {
      return this.deletedObjects;
   }

   public void setDeletedObjects(List<DeleteObjectsResult.DeletedObject> deletedObjects) {
      this.deletedObjects = deletedObjects;
   }

   public List<MultiObjectDeleteException.DeleteError> getErrors() {
      return this.errors;
   }

   public void setErrors(List<MultiObjectDeleteException.DeleteError> errors) {
      this.errors = errors;
   }

   @Override
   public boolean isRequesterCharged() {
      return this.isRequesterCharged;
   }

   @Override
   public void setRequesterCharged(boolean isRequesterCharged) {
      this.isRequesterCharged = isRequesterCharged;
   }
}
