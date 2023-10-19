package com.amazonaws.services.s3.event;

import com.amazonaws.internal.DateTimeJsonSerializer;
import com.amazonaws.services.s3.model.S3Event;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.joda.time.DateTime;

public class S3EventNotification {
   private final List<S3EventNotification.S3EventNotificationRecord> records;

   @JsonCreator
   public S3EventNotification(@JsonProperty("Records") List<S3EventNotification.S3EventNotificationRecord> records) {
      this.records = records;
   }

   public static S3EventNotification parseJson(String json) {
      return (S3EventNotification)Jackson.fromJsonString(json, S3EventNotification.class);
   }

   @JsonProperty("Records")
   public List<S3EventNotification.S3EventNotificationRecord> getRecords() {
      return this.records;
   }

   public String toJson() {
      return Jackson.toJsonString(this);
   }

   public static class GlacierEventDataEntity {
      private final S3EventNotification.RestoreEventDataEntity restoreEventData;

      @JsonCreator
      public GlacierEventDataEntity(@JsonProperty("restoreEventData") S3EventNotification.RestoreEventDataEntity restoreEventData) {
         this.restoreEventData = restoreEventData;
      }

      public S3EventNotification.RestoreEventDataEntity getRestoreEventData() {
         return this.restoreEventData;
      }
   }

   public static class IntelligentTieringEventDataEntity {
      private final String destinationAccessTier;

      @JsonCreator
      public IntelligentTieringEventDataEntity(@JsonProperty("destinationAccessTier") String destinationAccessTier) {
         this.destinationAccessTier = destinationAccessTier;
      }

      @JsonProperty("destinationAccessTier")
      public String getDestinationAccessTier() {
         return this.destinationAccessTier;
      }
   }

   public static class LifecycleEventDataEntity {
      private final S3EventNotification.TransitionEventDataEntity transitionEventData;

      @JsonCreator
      public LifecycleEventDataEntity(@JsonProperty("transitionEventData") S3EventNotification.TransitionEventDataEntity transitionEventData) {
         this.transitionEventData = transitionEventData;
      }

      public S3EventNotification.TransitionEventDataEntity getTransitionEventData() {
         return this.transitionEventData;
      }
   }

   public static class ReplicationEventDataEntity {
      private final String replicationRuleId;
      private final String destinationBucket;
      private final String s3Operation;
      private final String requestTime;
      private final String failureReason;
      private final String threshold;
      private final String replicationTime;

      @JsonCreator
      public ReplicationEventDataEntity(
         @JsonProperty("replicationRuleId") String replicationRuleId,
         @JsonProperty("destinationBucket") String destinationBucket,
         @JsonProperty("s3Operation") String s3Operation,
         @JsonProperty("requestTime") String requestTime,
         @JsonProperty("failureReason") String failureReason,
         @JsonProperty("threshold") String threshold,
         @JsonProperty("replicationTime") String replicationTime
      ) {
         this.replicationRuleId = replicationRuleId;
         this.destinationBucket = destinationBucket;
         this.s3Operation = s3Operation;
         this.requestTime = requestTime;
         this.failureReason = failureReason;
         this.threshold = threshold;
         this.replicationTime = replicationTime;
      }

      @JsonProperty("replicationRuleId")
      public String getReplicationRuleId() {
         return this.replicationRuleId;
      }

      @JsonProperty("destinationBucket")
      public String getDestinationBucket() {
         return this.destinationBucket;
      }

      @JsonProperty("s3Operation")
      public String getS3Operation() {
         return this.s3Operation;
      }

      @JsonProperty("requestTime")
      public String getRequestTime() {
         return this.requestTime;
      }

      @JsonProperty("failureReason")
      public String getFailureReason() {
         return this.failureReason;
      }

      @JsonProperty("threshold")
      public String getThreshold() {
         return this.threshold;
      }

      @JsonProperty("replicationTime")
      public String getReplicationTime() {
         return this.replicationTime;
      }
   }

   public static class RequestParametersEntity {
      private final String sourceIPAddress;

      @JsonCreator
      public RequestParametersEntity(@JsonProperty("sourceIPAddress") String sourceIPAddress) {
         this.sourceIPAddress = sourceIPAddress;
      }

      public String getSourceIPAddress() {
         return this.sourceIPAddress;
      }
   }

   public static class ResponseElementsEntity {
      private final String xAmzId2;
      private final String xAmzRequestId;

      @JsonCreator
      public ResponseElementsEntity(@JsonProperty("x-amz-id-2") String xAmzId2, @JsonProperty("x-amz-request-id") String xAmzRequestId) {
         this.xAmzId2 = xAmzId2;
         this.xAmzRequestId = xAmzRequestId;
      }

      @JsonProperty("x-amz-id-2")
      public String getxAmzId2() {
         return this.xAmzId2;
      }

      @JsonProperty("x-amz-request-id")
      public String getxAmzRequestId() {
         return this.xAmzRequestId;
      }
   }

   public static class RestoreEventDataEntity {
      private DateTime lifecycleRestorationExpiryTime;
      private final String lifecycleRestoreStorageClass;

      @JsonCreator
      public RestoreEventDataEntity(
         @JsonProperty("lifecycleRestorationExpiryTime") String lifecycleRestorationExpiryTime,
         @JsonProperty("lifecycleRestoreStorageClass") String lifecycleRestoreStorageClass
      ) {
         if (lifecycleRestorationExpiryTime != null) {
            this.lifecycleRestorationExpiryTime = DateTime.parse(lifecycleRestorationExpiryTime);
         }

         this.lifecycleRestoreStorageClass = lifecycleRestoreStorageClass;
      }

      @JsonSerialize(
         using = DateTimeJsonSerializer.class
      )
      public DateTime getLifecycleRestorationExpiryTime() {
         return this.lifecycleRestorationExpiryTime;
      }

      public String getLifecycleRestoreStorageClass() {
         return this.lifecycleRestoreStorageClass;
      }
   }

   public static class S3BucketEntity {
      private final String name;
      private final S3EventNotification.UserIdentityEntity ownerIdentity;
      private final String arn;

      @JsonCreator
      public S3BucketEntity(
         @JsonProperty("name") String name,
         @JsonProperty("ownerIdentity") S3EventNotification.UserIdentityEntity ownerIdentity,
         @JsonProperty("arn") String arn
      ) {
         this.name = name;
         this.ownerIdentity = ownerIdentity;
         this.arn = arn;
      }

      public String getName() {
         return this.name;
      }

      public S3EventNotification.UserIdentityEntity getOwnerIdentity() {
         return this.ownerIdentity;
      }

      public String getArn() {
         return this.arn;
      }
   }

   public static class S3Entity {
      private final String configurationId;
      private final S3EventNotification.S3BucketEntity bucket;
      private final S3EventNotification.S3ObjectEntity object;
      private final String s3SchemaVersion;

      @JsonCreator
      public S3Entity(
         @JsonProperty("configurationId") String configurationId,
         @JsonProperty("bucket") S3EventNotification.S3BucketEntity bucket,
         @JsonProperty("object") S3EventNotification.S3ObjectEntity object,
         @JsonProperty("s3SchemaVersion") String s3SchemaVersion
      ) {
         this.configurationId = configurationId;
         this.bucket = bucket;
         this.object = object;
         this.s3SchemaVersion = s3SchemaVersion;
      }

      public String getConfigurationId() {
         return this.configurationId;
      }

      public S3EventNotification.S3BucketEntity getBucket() {
         return this.bucket;
      }

      public S3EventNotification.S3ObjectEntity getObject() {
         return this.object;
      }

      public String getS3SchemaVersion() {
         return this.s3SchemaVersion;
      }
   }

   public static class S3EventNotificationRecord {
      private final String awsRegion;
      private final String eventName;
      private final String eventSource;
      private DateTime eventTime;
      private final String eventVersion;
      private final S3EventNotification.RequestParametersEntity requestParameters;
      private final S3EventNotification.ResponseElementsEntity responseElements;
      private final S3EventNotification.S3Entity s3;
      private final S3EventNotification.UserIdentityEntity userIdentity;
      private final S3EventNotification.GlacierEventDataEntity glacierEventData;
      private final S3EventNotification.LifecycleEventDataEntity lifecycleEventData;
      private final S3EventNotification.IntelligentTieringEventDataEntity intelligentTieringEventData;
      private final S3EventNotification.ReplicationEventDataEntity replicationEventDataEntity;

      @Deprecated
      public S3EventNotificationRecord(
         String awsRegion,
         String eventName,
         String eventSource,
         String eventTime,
         String eventVersion,
         S3EventNotification.RequestParametersEntity requestParameters,
         S3EventNotification.ResponseElementsEntity responseElements,
         S3EventNotification.S3Entity s3,
         S3EventNotification.UserIdentityEntity userIdentity
      ) {
         this(awsRegion, eventName, eventSource, eventTime, eventVersion, requestParameters, responseElements, s3, userIdentity, null, null, null, null);
      }

      @Deprecated
      public S3EventNotificationRecord(
         String awsRegion,
         String eventName,
         String eventSource,
         String eventTime,
         String eventVersion,
         S3EventNotification.RequestParametersEntity requestParameters,
         S3EventNotification.ResponseElementsEntity responseElements,
         S3EventNotification.S3Entity s3,
         S3EventNotification.UserIdentityEntity userIdentity,
         S3EventNotification.GlacierEventDataEntity glacierEventData
      ) {
         this(
            awsRegion,
            eventName,
            eventSource,
            eventTime,
            eventVersion,
            requestParameters,
            responseElements,
            s3,
            userIdentity,
            glacierEventData,
            null,
            null,
            null
         );
      }

      @JsonCreator
      public S3EventNotificationRecord(
         @JsonProperty("awsRegion") String awsRegion,
         @JsonProperty("eventName") String eventName,
         @JsonProperty("eventSource") String eventSource,
         @JsonProperty("eventTime") String eventTime,
         @JsonProperty("eventVersion") String eventVersion,
         @JsonProperty("requestParameters") S3EventNotification.RequestParametersEntity requestParameters,
         @JsonProperty("responseElements") S3EventNotification.ResponseElementsEntity responseElements,
         @JsonProperty("s3") S3EventNotification.S3Entity s3,
         @JsonProperty("userIdentity") S3EventNotification.UserIdentityEntity userIdentity,
         @JsonProperty("glacierEventData") S3EventNotification.GlacierEventDataEntity glacierEventData,
         @JsonProperty("lifecycleEventData") S3EventNotification.LifecycleEventDataEntity lifecycleEventData,
         @JsonProperty("intelligentTieringEventData") S3EventNotification.IntelligentTieringEventDataEntity intelligentTieringEventData,
         @JsonProperty("replicationEventData") S3EventNotification.ReplicationEventDataEntity replicationEventData
      ) {
         this.awsRegion = awsRegion;
         this.eventName = eventName;
         this.eventSource = eventSource;
         if (eventTime != null) {
            this.eventTime = DateTime.parse(eventTime);
         }

         this.eventVersion = eventVersion;
         this.requestParameters = requestParameters;
         this.responseElements = responseElements;
         this.s3 = s3;
         this.userIdentity = userIdentity;
         this.glacierEventData = glacierEventData;
         this.lifecycleEventData = lifecycleEventData;
         this.intelligentTieringEventData = intelligentTieringEventData;
         this.replicationEventDataEntity = replicationEventData;
      }

      public String getAwsRegion() {
         return this.awsRegion;
      }

      public String getEventName() {
         return this.eventName;
      }

      @JsonIgnore
      public S3Event getEventNameAsEnum() {
         return S3Event.fromValue(this.eventName);
      }

      public String getEventSource() {
         return this.eventSource;
      }

      @JsonSerialize(
         using = DateTimeJsonSerializer.class
      )
      public DateTime getEventTime() {
         return this.eventTime;
      }

      public String getEventVersion() {
         return this.eventVersion;
      }

      public S3EventNotification.RequestParametersEntity getRequestParameters() {
         return this.requestParameters;
      }

      public S3EventNotification.ResponseElementsEntity getResponseElements() {
         return this.responseElements;
      }

      public S3EventNotification.S3Entity getS3() {
         return this.s3;
      }

      public S3EventNotification.UserIdentityEntity getUserIdentity() {
         return this.userIdentity;
      }

      public S3EventNotification.GlacierEventDataEntity getGlacierEventData() {
         return this.glacierEventData;
      }

      public S3EventNotification.LifecycleEventDataEntity getLifecycleEventData() {
         return this.lifecycleEventData;
      }

      public S3EventNotification.IntelligentTieringEventDataEntity getIntelligentTieringEventData() {
         return this.intelligentTieringEventData;
      }

      public S3EventNotification.ReplicationEventDataEntity getReplicationEventDataEntity() {
         return this.replicationEventDataEntity;
      }
   }

   public static class S3ObjectEntity {
      private final String key;
      private final Long size;
      private final String eTag;
      private final String versionId;
      private final String sequencer;

      @Deprecated
      public S3ObjectEntity(String key, Integer size, String eTag, String versionId) {
         this.key = key;
         this.size = size == null ? null : size.longValue();
         this.eTag = eTag;
         this.versionId = versionId;
         this.sequencer = null;
      }

      @Deprecated
      public S3ObjectEntity(String key, Long size, String eTag, String versionId) {
         this(key, size, eTag, versionId, null);
      }

      @JsonCreator
      public S3ObjectEntity(
         @JsonProperty("key") String key,
         @JsonProperty("size") Long size,
         @JsonProperty("eTag") String eTag,
         @JsonProperty("versionId") String versionId,
         @JsonProperty("sequencer") String sequencer
      ) {
         this.key = key;
         this.size = size;
         this.eTag = eTag;
         this.versionId = versionId;
         this.sequencer = sequencer;
      }

      public String getKey() {
         return this.key;
      }

      public String getUrlDecodedKey() {
         return SdkHttpUtils.urlDecode(this.getKey());
      }

      @Deprecated
      @JsonIgnore
      public Integer getSize() {
         return this.size == null ? null : this.size.intValue();
      }

      @JsonProperty("size")
      public Long getSizeAsLong() {
         return this.size;
      }

      public String geteTag() {
         return this.eTag;
      }

      public String getVersionId() {
         return this.versionId;
      }

      public String getSequencer() {
         return this.sequencer;
      }
   }

   public static class TransitionEventDataEntity {
      private final String destinationStorageClass;

      @JsonCreator
      public TransitionEventDataEntity(@JsonProperty("destinationStorageClass") String destinationStorageClass) {
         this.destinationStorageClass = destinationStorageClass;
      }

      public String getDestinationStorageClass() {
         return this.destinationStorageClass;
      }
   }

   public static class UserIdentityEntity {
      private final String principalId;

      @JsonCreator
      public UserIdentityEntity(@JsonProperty("principalId") String principalId) {
         this.principalId = principalId;
      }

      public String getPrincipalId() {
         return this.principalId;
      }
   }
}
