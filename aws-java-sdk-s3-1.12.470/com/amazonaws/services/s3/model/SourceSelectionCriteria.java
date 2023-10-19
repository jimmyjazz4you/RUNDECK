package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class SourceSelectionCriteria implements Serializable, Cloneable {
   private SseKmsEncryptedObjects sseKmsEncryptedObjects;
   private ReplicaModifications replicaModifications;

   public SseKmsEncryptedObjects getSseKmsEncryptedObjects() {
      return this.sseKmsEncryptedObjects;
   }

   public void setSseKmsEncryptedObjects(SseKmsEncryptedObjects sseKmsEncryptedObjects) {
      this.sseKmsEncryptedObjects = sseKmsEncryptedObjects;
   }

   public SourceSelectionCriteria withSseKmsEncryptedObjects(SseKmsEncryptedObjects sseKmsEncryptedObjects) {
      this.setSseKmsEncryptedObjects(sseKmsEncryptedObjects);
      return this;
   }

   public ReplicaModifications getReplicaModifications() {
      return this.replicaModifications;
   }

   public void setReplicaModifications(ReplicaModifications replicaModifications) {
      this.replicaModifications = replicaModifications;
   }

   public SourceSelectionCriteria withReplicaModifications(ReplicaModifications replicaModifications) {
      this.setReplicaModifications(replicaModifications);
      return this;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getSseKmsEncryptedObjects() != null) {
         sb.append("SseKmsEncryptedObjects: ").append(this.getSseKmsEncryptedObjects()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof SourceSelectionCriteria)) {
         return false;
      } else {
         SourceSelectionCriteria other = (SourceSelectionCriteria)obj;
         if (other.getSseKmsEncryptedObjects() == null ^ this.getSseKmsEncryptedObjects() == null) {
            return false;
         } else {
            return other.getSseKmsEncryptedObjects() == null || other.getSseKmsEncryptedObjects().equals(this.getSseKmsEncryptedObjects());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getSseKmsEncryptedObjects() == null ? 0 : this.getSseKmsEncryptedObjects().hashCode());
   }

   public SourceSelectionCriteria clone() {
      try {
         return (SourceSelectionCriteria)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
