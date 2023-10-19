package com.amazonaws.services.s3.model;

public enum S3Event {
   ReducedRedundancyLostObject("s3:ReducedRedundancyLostObject"),
   ObjectCreated("s3:ObjectCreated:*"),
   ObjectCreatedByPut("s3:ObjectCreated:Put"),
   ObjectCreatedByPost("s3:ObjectCreated:Post"),
   ObjectCreatedByCopy("s3:ObjectCreated:Copy"),
   ObjectCreatedByCompleteMultipartUpload("s3:ObjectCreated:CompleteMultipartUpload"),
   ObjectRemoved("s3:ObjectRemoved:*"),
   ObjectRemovedDelete("s3:ObjectRemoved:Delete"),
   ObjectRemovedDeleteMarkerCreated("s3:ObjectRemoved:DeleteMarkerCreated"),
   ObjectRestorePost("s3:ObjectRestore:Post"),
   ObjectRestoreCompleted("s3:ObjectRestore:Completed"),
   Replication("s3:Replication:*"),
   ReplicationOperationFailed("s3:Replication:OperationFailedReplication"),
   ReplicationOperationNotTracked("s3:Replication:OperationNotTracked"),
   ReplicationOperationMissedThreshold("s3:Replication:OperationMissedThreshold"),
   ReplicationOperationReplicatedAfterThreshold("s3:Replication:OperationReplicatedAfterThreshold"),
   ObjectRestoreDelete("s3:ObjectRestore:Delete"),
   LifecycleTransition("s3:LifecycleTransition"),
   IntelligentTiering("s3:IntelligentTiering"),
   ObjectAclPut("s3:ObjectAcl:Put"),
   LifecycleExpiration("s3:LifecycleExpiration:*"),
   LifecycleExpirationDelete("s3:LifecycleExpiration:Delete"),
   LifecycleExpirationDeleteMarkerCreated("s3:LifecycleExpiration:DeleteMarkerCreated"),
   ObjectTagging("s3:ObjectTagging:*"),
   ObjectTaggingPut("s3:ObjectTagging:Put"),
   ObjectTaggingDelete("s3:ObjectTagging:Delete");

   private final String event;
   private static final String S3_PREFIX = "s3:";

   private S3Event(String event) {
      this.event = event;
   }

   @Override
   public String toString() {
      return this.event;
   }

   public static S3Event fromValue(String value) {
      if (value != null && !"".equals(value)) {
         for(S3Event enumEntry : values()) {
            if (enumEntry.toString().equals(value) || enumEntry.toString().equals("s3:".concat(value))) {
               return enumEntry;
            }
         }

         throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
      } else {
         throw new IllegalArgumentException("Value cannot be null or empty!");
      }
   }
}
