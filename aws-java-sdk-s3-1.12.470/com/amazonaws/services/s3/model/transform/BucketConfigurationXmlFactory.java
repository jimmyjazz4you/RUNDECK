package com.amazonaws.services.s3.model.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.AccessControlTranslation;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.BucketTaggingConfiguration;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.CloudFunctionConfiguration;
import com.amazonaws.services.s3.model.DeleteMarkerReplication;
import com.amazonaws.services.s3.model.EventBridgeConfiguration;
import com.amazonaws.services.s3.model.ExistingObjectReplication;
import com.amazonaws.services.s3.model.Filter;
import com.amazonaws.services.s3.model.FilterRule;
import com.amazonaws.services.s3.model.LambdaConfiguration;
import com.amazonaws.services.s3.model.Metrics;
import com.amazonaws.services.s3.model.NotificationConfiguration;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.services.s3.model.QueueConfiguration;
import com.amazonaws.services.s3.model.RedirectRule;
import com.amazonaws.services.s3.model.ReplicaModifications;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationTime;
import com.amazonaws.services.s3.model.ReplicationTimeValue;
import com.amazonaws.services.s3.model.RoutingRule;
import com.amazonaws.services.s3.model.RoutingRuleCondition;
import com.amazonaws.services.s3.model.S3KeyFilter;
import com.amazonaws.services.s3.model.ServerSideEncryptionByDefault;
import com.amazonaws.services.s3.model.ServerSideEncryptionConfiguration;
import com.amazonaws.services.s3.model.ServerSideEncryptionRule;
import com.amazonaws.services.s3.model.SourceSelectionCriteria;
import com.amazonaws.services.s3.model.SseKmsEncryptedObjects;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.TagSet;
import com.amazonaws.services.s3.model.TopicConfiguration;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import com.amazonaws.services.s3.model.analytics.AnalyticsExportDestination;
import com.amazonaws.services.s3.model.analytics.AnalyticsFilter;
import com.amazonaws.services.s3.model.analytics.AnalyticsFilterPredicate;
import com.amazonaws.services.s3.model.analytics.AnalyticsS3BucketDestination;
import com.amazonaws.services.s3.model.analytics.StorageClassAnalysis;
import com.amazonaws.services.s3.model.analytics.StorageClassAnalysisDataExport;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringFilter;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringFilterPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.Tiering;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import com.amazonaws.services.s3.model.inventory.InventoryDestination;
import com.amazonaws.services.s3.model.inventory.InventoryEncryption;
import com.amazonaws.services.s3.model.inventory.InventoryFilter;
import com.amazonaws.services.s3.model.inventory.InventoryFilterPredicate;
import com.amazonaws.services.s3.model.inventory.InventoryPrefixPredicate;
import com.amazonaws.services.s3.model.inventory.InventoryS3BucketDestination;
import com.amazonaws.services.s3.model.inventory.InventorySchedule;
import com.amazonaws.services.s3.model.inventory.ServerSideEncryptionKMS;
import com.amazonaws.services.s3.model.inventory.ServerSideEncryptionS3;
import com.amazonaws.services.s3.model.lifecycle.LifecycleAndOperator;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilterPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeGreaterThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeLessThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePredicateVisitor;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePrefixPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsAccessPointArnPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsAndOperator;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import com.amazonaws.services.s3.model.metrics.MetricsFilter;
import com.amazonaws.services.s3.model.metrics.MetricsFilterPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsPredicateVisitor;
import com.amazonaws.services.s3.model.metrics.MetricsPrefixPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsTagPredicate;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import com.amazonaws.services.s3.model.ownership.OwnershipControlsRule;
import com.amazonaws.services.s3.model.replication.ReplicationFilter;
import com.amazonaws.services.s3.model.replication.ReplicationFilterPredicate;
import com.amazonaws.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BucketConfigurationXmlFactory {
   public byte[] convertToXmlByteArray(BucketVersioningConfiguration versioningConfiguration) {
      XmlWriter xml = new XmlWriter();
      xml.start("VersioningConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      xml.start("Status").value(versioningConfiguration.getStatus()).end();
      Boolean mfaDeleteEnabled = versioningConfiguration.isMfaDeleteEnabled();
      if (mfaDeleteEnabled != null) {
         if (mfaDeleteEnabled) {
            xml.start("MfaDelete").value("Enabled").end();
         } else {
            xml.start("MfaDelete").value("Disabled").end();
         }
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketAccelerateConfiguration accelerateConfiguration) {
      XmlWriter xml = new XmlWriter();
      xml.start("AccelerateConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      xml.start("Status").value(accelerateConfiguration.getStatus()).end();
      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketLoggingConfiguration loggingConfiguration) {
      String logFilePrefix = loggingConfiguration.getLogFilePrefix();
      if (logFilePrefix == null) {
         logFilePrefix = "";
      }

      XmlWriter xml = new XmlWriter();
      xml.start("BucketLoggingStatus", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      if (loggingConfiguration.isLoggingEnabled()) {
         xml.start("LoggingEnabled");
         xml.start("TargetBucket").value(loggingConfiguration.getDestinationBucketName()).end();
         xml.start("TargetPrefix").value(loggingConfiguration.getLogFilePrefix()).end();
         xml.end();
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketNotificationConfiguration notificationConfiguration) {
      XmlWriter xml = new XmlWriter();
      xml.start("NotificationConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      Map<String, NotificationConfiguration> configurations = notificationConfiguration.getConfigurations();

      for(Entry<String, NotificationConfiguration> entry : configurations.entrySet()) {
         String configName = entry.getKey();
         NotificationConfiguration config = entry.getValue();
         if (config instanceof TopicConfiguration) {
            xml.start("TopicConfiguration");
            xml.start("Id").value(configName).end();
            xml.start("Topic").value(((TopicConfiguration)config).getTopicARN()).end();
            this.addEventsAndFilterCriteria(xml, config);
            xml.end();
         } else if (config instanceof QueueConfiguration) {
            xml.start("QueueConfiguration");
            xml.start("Id").value(configName).end();
            xml.start("Queue").value(((QueueConfiguration)config).getQueueARN()).end();
            this.addEventsAndFilterCriteria(xml, config);
            xml.end();
         } else if (config instanceof CloudFunctionConfiguration) {
            xml.start("CloudFunctionConfiguration");
            xml.start("Id").value(configName).end();
            xml.start("InvocationRole").value(((CloudFunctionConfiguration)config).getInvocationRoleARN()).end();
            xml.start("CloudFunction").value(((CloudFunctionConfiguration)config).getCloudFunctionARN()).end();
            this.addEventsAndFilterCriteria(xml, config);
            xml.end();
         } else if (config instanceof LambdaConfiguration) {
            xml.start("CloudFunctionConfiguration");
            xml.start("Id").value(configName).end();
            xml.start("CloudFunction").value(((LambdaConfiguration)config).getFunctionARN()).end();
            this.addEventsAndFilterCriteria(xml, config);
            xml.end();
         }
      }

      EventBridgeConfiguration eventBridgeConfiguration = notificationConfiguration.getEventBridgeConfiguration();
      if (eventBridgeConfiguration != null) {
         xml.start("EventBridgeConfiguration");
         xml.end();
      }

      xml.end();
      return xml.getBytes();
   }

   private void addEventsAndFilterCriteria(XmlWriter xml, NotificationConfiguration config) {
      for(String event : config.getEvents()) {
         xml.start("Event").value(event).end();
      }

      Filter filter = config.getFilter();
      if (filter != null) {
         this.validateFilter(filter);
         xml.start("Filter");
         if (filter.getS3KeyFilter() != null) {
            this.validateS3KeyFilter(filter.getS3KeyFilter());
            xml.start("S3Key");

            for(FilterRule filterRule : filter.getS3KeyFilter().getFilterRules()) {
               xml.start("FilterRule");
               xml.start("Name").value(filterRule.getName()).end();
               xml.start("Value").value(filterRule.getValue()).end();
               xml.end();
            }

            xml.end();
         }

         xml.end();
      }
   }

   private void validateFilter(Filter filter) {
      if (filter.getS3KeyFilter() == null) {
         throw new SdkClientException("Cannot have a Filter without any criteria");
      }
   }

   private void validateS3KeyFilter(S3KeyFilter s3KeyFilter) {
      if (CollectionUtils.isNullOrEmpty(s3KeyFilter.getFilterRules())) {
         throw new SdkClientException("Cannot have an S3KeyFilter without any filter rules");
      }
   }

   private void writeReplicationPrefix(XmlWriter xml, ReplicationRule rule) {
      if (rule.getFilter() == null) {
         xml.start("Prefix").value(rule.getPrefix() == null ? "" : rule.getPrefix()).end();
      } else if (rule.getPrefix() != null) {
         throw new IllegalArgumentException("Prefix cannot be used with Filter. Use ReplicationPrefixPredicate to create a ReplicationFilter");
      }
   }

   public byte[] convertToXmlByteArray(BucketReplicationConfiguration replicationConfiguration) {
      XmlWriter xml = new XmlWriter();
      xml.start("ReplicationConfiguration");
      Map<String, ReplicationRule> rules = replicationConfiguration.getRules();
      String role = replicationConfiguration.getRoleARN();
      xml.start("Role").value(role).end();

      for(Entry<String, ReplicationRule> entry : rules.entrySet()) {
         String ruleId = entry.getKey();
         ReplicationRule rule = entry.getValue();
         xml.start("Rule");
         xml.start("ID").value(ruleId).end();
         Integer priority = rule.getPriority();
         if (priority != null) {
            xml.start("Priority").value(Integer.toString(priority)).end();
         }

         xml.start("Status").value(rule.getStatus()).end();
         ExistingObjectReplication existingObjectReplication = rule.getExistingObjectReplication();
         if (existingObjectReplication != null) {
            xml.start("ExistingObjectReplication").start("Status").value(existingObjectReplication.getStatus()).end().end();
         }

         DeleteMarkerReplication deleteMarkerReplication = rule.getDeleteMarkerReplication();
         if (deleteMarkerReplication != null) {
            xml.start("DeleteMarkerReplication").start("Status").value(deleteMarkerReplication.getStatus()).end().end();
         }

         this.writeReplicationPrefix(xml, rule);
         this.writeReplicationFilter(xml, rule.getFilter());
         SourceSelectionCriteria sourceSelectionCriteria = rule.getSourceSelectionCriteria();
         if (sourceSelectionCriteria != null) {
            xml.start("SourceSelectionCriteria");
            SseKmsEncryptedObjects sseKmsEncryptedObjects = sourceSelectionCriteria.getSseKmsEncryptedObjects();
            if (sseKmsEncryptedObjects != null) {
               xml.start("SseKmsEncryptedObjects");
               BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Status", sseKmsEncryptedObjects.getStatus());
               xml.end();
            }

            ReplicaModifications replicaModifications = sourceSelectionCriteria.getReplicaModifications();
            if (replicaModifications != null) {
               xml.start("ReplicaModifications");
               BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Status", replicaModifications.getStatus());
               xml.end();
            }

            xml.end();
         }

         ReplicationDestinationConfig config = rule.getDestinationConfig();
         xml.start("Destination");
         xml.start("Bucket").value(config.getBucketARN()).end();
         BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Account", config.getAccount());
         if (config.getStorageClass() != null) {
            xml.start("StorageClass").value(config.getStorageClass()).end();
         }

         AccessControlTranslation accessControlTranslation = config.getAccessControlTranslation();
         if (accessControlTranslation != null) {
            xml.start("AccessControlTranslation");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Owner", accessControlTranslation.getOwner());
            xml.end();
         }

         if (config.getEncryptionConfiguration() != null) {
            xml.start("EncryptionConfiguration");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "ReplicaKmsKeyID", config.getEncryptionConfiguration().getReplicaKmsKeyID());
            xml.end();
         }

         ReplicationTime replicationTime = config.getReplicationTime();
         if (replicationTime != null) {
            xml.start("ReplicationTime");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Status", replicationTime.getStatus());
            if (replicationTime.getTime() != null) {
               xml.start("Time");
               ReplicationTimeValue time = replicationTime.getTime();
               if (time.getMinutes() != null) {
                  xml.start("Minutes").value(time.getMinutes().toString()).end();
               }

               xml.end();
            }

            xml.end();
         }

         Metrics metrics = config.getMetrics();
         if (metrics != null) {
            xml.start("Metrics");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Status", metrics.getStatus());
            if (metrics.getEventThreshold() != null) {
               xml.start("EventThreshold");
               ReplicationTimeValue eventThreshold = metrics.getEventThreshold();
               if (eventThreshold.getMinutes() != null) {
                  xml.start("Minutes").value(eventThreshold.getMinutes().toString()).end();
               }

               xml.end();
            }

            xml.end();
         }

         xml.end();
         xml.end();
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketWebsiteConfiguration websiteConfiguration) {
      XmlWriter xml = new XmlWriter();
      xml.start("WebsiteConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      if (websiteConfiguration.getIndexDocumentSuffix() != null) {
         XmlWriter indexDocumentElement = xml.start("IndexDocument");
         indexDocumentElement.start("Suffix").value(websiteConfiguration.getIndexDocumentSuffix()).end();
         indexDocumentElement.end();
      }

      if (websiteConfiguration.getErrorDocument() != null) {
         XmlWriter errorDocumentElement = xml.start("ErrorDocument");
         errorDocumentElement.start("Key").value(websiteConfiguration.getErrorDocument()).end();
         errorDocumentElement.end();
      }

      RedirectRule redirectAllRequestsTo = websiteConfiguration.getRedirectAllRequestsTo();
      if (redirectAllRequestsTo != null) {
         XmlWriter redirectAllRequestsElement = xml.start("RedirectAllRequestsTo");
         if (redirectAllRequestsTo.getprotocol() != null) {
            xml.start("Protocol").value(redirectAllRequestsTo.getprotocol()).end();
         }

         if (redirectAllRequestsTo.getHostName() != null) {
            xml.start("HostName").value(redirectAllRequestsTo.getHostName()).end();
         }

         if (redirectAllRequestsTo.getReplaceKeyPrefixWith() != null) {
            xml.start("ReplaceKeyPrefixWith").value(redirectAllRequestsTo.getReplaceKeyPrefixWith()).end();
         }

         if (redirectAllRequestsTo.getReplaceKeyWith() != null) {
            xml.start("ReplaceKeyWith").value(redirectAllRequestsTo.getReplaceKeyWith()).end();
         }

         redirectAllRequestsElement.end();
      }

      if (websiteConfiguration.getRoutingRules() != null && websiteConfiguration.getRoutingRules().size() > 0) {
         XmlWriter routingRules = xml.start("RoutingRules");

         for(RoutingRule rule : websiteConfiguration.getRoutingRules()) {
            this.writeRule(routingRules, rule);
         }

         routingRules.end();
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketLifecycleConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("LifecycleConfiguration");

      for(BucketLifecycleConfiguration.Rule rule : config.getRules()) {
         this.writeRule(xml, rule);
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(BucketCrossOriginConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("CORSConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");

      for(CORSRule rule : config.getRules()) {
         this.writeRule(xml, rule);
      }

      xml.end();
      return xml.getBytes();
   }

   private void writeLifecyclePrefix(XmlWriter xml, BucketLifecycleConfiguration.Rule rule) {
      if (rule.getFilter() == null) {
         xml.start("Prefix").value(rule.getPrefix() == null ? "" : rule.getPrefix()).end();
      } else if (rule.getPrefix() != null) {
         throw new IllegalArgumentException("Prefix cannot be used with Filter. Use LifecyclePrefixPredicate to create a LifecycleFilter");
      }
   }

   private void writeRule(XmlWriter xml, BucketLifecycleConfiguration.Rule rule) {
      xml.start("Rule");
      if (rule.getId() != null) {
         xml.start("ID").value(rule.getId()).end();
      }

      this.writeLifecyclePrefix(xml, rule);
      xml.start("Status").value(rule.getStatus()).end();
      this.writeLifecycleFilter(xml, rule.getFilter());
      this.addTransitions(xml, rule.getTransitions());
      this.addNoncurrentTransitions(xml, rule.getNoncurrentVersionTransitions());
      this.addNoncurrentExpiration(xml, rule.getNoncurrentVersionExpiration());
      if (this.hasCurrentExpirationPolicy(rule)) {
         xml.start("Expiration");
         if (rule.getExpirationInDays() != -1) {
            xml.start("Days").value("" + rule.getExpirationInDays()).end();
         }

         if (rule.getExpirationDate() != null) {
            xml.start("Date").value(ServiceUtils.formatIso8601Date(rule.getExpirationDate())).end();
         }

         if (rule.isExpiredObjectDeleteMarker()) {
            xml.start("ExpiredObjectDeleteMarker").value("true").end();
         }

         xml.end();
      }

      if (rule.getAbortIncompleteMultipartUpload() != null) {
         xml.start("AbortIncompleteMultipartUpload");
         xml.start("DaysAfterInitiation").value(Integer.toString(rule.getAbortIncompleteMultipartUpload().getDaysAfterInitiation())).end();
         xml.end();
      }

      xml.end();
   }

   private void addTransitions(XmlWriter xml, List<BucketLifecycleConfiguration.Transition> transitions) {
      if (transitions != null && !transitions.isEmpty()) {
         for(BucketLifecycleConfiguration.Transition t : transitions) {
            if (t != null) {
               xml.start("Transition");
               if (t.getDate() != null) {
                  xml.start("Date");
                  xml.value(ServiceUtils.formatIso8601Date(t.getDate()));
                  xml.end();
               }

               if (t.getDays() != -1) {
                  xml.start("Days");
                  xml.value(Integer.toString(t.getDays()));
                  xml.end();
               }

               xml.start("StorageClass");
               xml.value(t.getStorageClassAsString());
               xml.end();
               xml.end();
            }
         }
      }
   }

   private void addNoncurrentTransitions(XmlWriter xml, List<BucketLifecycleConfiguration.NoncurrentVersionTransition> transitions) {
      if (transitions != null && !transitions.isEmpty()) {
         for(BucketLifecycleConfiguration.NoncurrentVersionTransition t : transitions) {
            if (t != null) {
               xml.start("NoncurrentVersionTransition");
               if (t.getDays() != -1) {
                  xml.start("NoncurrentDays");
                  xml.value(Integer.toString(t.getDays()));
                  xml.end();
               }

               if (t.getNewerNoncurrentVersions() != -1) {
                  xml.start("NewerNoncurrentVersions");
                  xml.value(Integer.toString(t.getNewerNoncurrentVersions()));
                  xml.end();
               }

               xml.start("StorageClass");
               xml.value(t.getStorageClassAsString());
               xml.end();
               xml.end();
            }
         }
      }
   }

   private void addNoncurrentExpiration(XmlWriter xml, BucketLifecycleConfiguration.NoncurrentVersionExpiration expiration) {
      if (expiration != null) {
         xml.start("NoncurrentVersionExpiration");
         if (expiration.getDays() != -1) {
            xml.start("NoncurrentDays");
            xml.value(Integer.toString(expiration.getDays()));
            xml.end();
         }

         if (expiration.getNewerNoncurrentVersions() != -1) {
            xml.start("NewerNoncurrentVersions");
            xml.value(Integer.toString(expiration.getNewerNoncurrentVersions()));
            xml.end();
         }

         xml.end();
      }
   }

   private void writeLifecycleFilter(XmlWriter xml, LifecycleFilter filter) {
      if (filter != null) {
         xml.start("Filter");
         this.writeLifecycleFilterPredicate(xml, filter.getPredicate());
         xml.end();
      }
   }

   private void writeLifecycleFilterPredicate(XmlWriter xml, LifecycleFilterPredicate predicate) {
      if (predicate != null) {
         predicate.accept(new BucketConfigurationXmlFactory.LifecyclePredicateVisitorImpl(xml));
      }
   }

   private void writeReplicationFilter(XmlWriter xml, ReplicationFilter filter) {
      if (filter != null) {
         xml.start("Filter");
         this.writeReplicationPredicate(xml, filter.getPredicate());
         xml.end();
      }
   }

   private void writeReplicationPredicate(XmlWriter xml, ReplicationFilterPredicate predicate) {
      if (predicate != null) {
         predicate.accept(new ReplicationPredicateVisitorImpl(xml));
      }
   }

   public byte[] convertToXmlByteArray(ServerSideEncryptionConfiguration sseConfig) {
      XmlWriter xml = new XmlWriter();
      xml.start("ServerSideEncryptionConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");

      for(ServerSideEncryptionRule rule : sseConfig.getRules()) {
         xml.start("Rule");
         this.addBooleanParameterIfNotNull(xml, "BucketKeyEnabled", rule.getBucketKeyEnabled());
         this.writeServerSideEncryptionByDefault(xml, rule.getApplyServerSideEncryptionByDefault());
         xml.end();
      }

      xml.end();
      return xml.getBytes();
   }

   private void writeServerSideEncryptionByDefault(XmlWriter xml, ServerSideEncryptionByDefault sseByDefault) {
      if (sseByDefault != null) {
         xml.start("ApplyServerSideEncryptionByDefault");
         BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "SSEAlgorithm", sseByDefault.getSSEAlgorithm());
         BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "KMSMasterKeyID", sseByDefault.getKMSMasterKeyID());
         xml.end();
      }
   }

   public byte[] convertToXmlByteArray(PublicAccessBlockConfiguration config) {
      XmlWriter xml = new XmlWriter();
      xml.start("PublicAccessBlockConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      this.addBooleanParameterIfNotNull(xml, "BlockPublicAcls", config.getBlockPublicAcls());
      this.addBooleanParameterIfNotNull(xml, "IgnorePublicAcls", config.getIgnorePublicAcls());
      this.addBooleanParameterIfNotNull(xml, "BlockPublicPolicy", config.getBlockPublicPolicy());
      this.addBooleanParameterIfNotNull(xml, "RestrictPublicBuckets", config.getRestrictPublicBuckets());
      xml.end();
      return xml.getBytes();
   }

   private boolean hasCurrentExpirationPolicy(BucketLifecycleConfiguration.Rule rule) {
      return rule.getExpirationInDays() != -1 || rule.getExpirationDate() != null || rule.isExpiredObjectDeleteMarker();
   }

   private void writeRule(XmlWriter xml, CORSRule rule) {
      xml.start("CORSRule");
      if (rule.getId() != null) {
         xml.start("ID").value(rule.getId()).end();
      }

      if (rule.getAllowedOrigins() != null) {
         for(String origin : rule.getAllowedOrigins()) {
            xml.start("AllowedOrigin").value(origin).end();
         }
      }

      if (rule.getAllowedMethods() != null) {
         for(CORSRule.AllowedMethods method : rule.getAllowedMethods()) {
            xml.start("AllowedMethod").value(method.toString()).end();
         }
      }

      if (rule.getMaxAgeSeconds() != 0) {
         xml.start("MaxAgeSeconds").value(Integer.toString(rule.getMaxAgeSeconds())).end();
      }

      if (rule.getExposedHeaders() != null) {
         for(String header : rule.getExposedHeaders()) {
            xml.start("ExposeHeader").value(header).end();
         }
      }

      if (rule.getAllowedHeaders() != null) {
         for(String header : rule.getAllowedHeaders()) {
            xml.start("AllowedHeader").value(header).end();
         }
      }

      xml.end();
   }

   private void writeRule(XmlWriter xml, RoutingRule rule) {
      xml.start("RoutingRule");
      RoutingRuleCondition condition = rule.getCondition();
      if (condition != null) {
         xml.start("Condition");
         xml.start("KeyPrefixEquals");
         if (condition.getKeyPrefixEquals() != null) {
            xml.value(condition.getKeyPrefixEquals());
         }

         xml.end();
         if (condition.getHttpErrorCodeReturnedEquals() != null) {
            xml.start("HttpErrorCodeReturnedEquals ").value(condition.getHttpErrorCodeReturnedEquals()).end();
         }

         xml.end();
      }

      xml.start("Redirect");
      RedirectRule redirect = rule.getRedirect();
      if (redirect != null) {
         if (redirect.getprotocol() != null) {
            xml.start("Protocol").value(redirect.getprotocol()).end();
         }

         if (redirect.getHostName() != null) {
            xml.start("HostName").value(redirect.getHostName()).end();
         }

         if (redirect.getReplaceKeyPrefixWith() != null) {
            xml.start("ReplaceKeyPrefixWith").value(redirect.getReplaceKeyPrefixWith()).end();
         }

         if (redirect.getReplaceKeyWith() != null) {
            xml.start("ReplaceKeyWith").value(redirect.getReplaceKeyWith()).end();
         }

         if (redirect.getHttpRedirectCode() != null) {
            xml.start("HttpRedirectCode").value(redirect.getHttpRedirectCode()).end();
         }
      }

      xml.end();
      xml.end();
   }

   public byte[] convertToXmlByteArray(BucketTaggingConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("Tagging");

      for(TagSet tagset : config.getAllTagSets()) {
         this.writeRule(xml, tagset);
      }

      xml.end();
      return xml.getBytes();
   }

   public byte[] convertToXmlByteArray(InventoryConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("InventoryConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      xml.start("Id").value(config.getId()).end();
      xml.start("IsEnabled").value(String.valueOf(config.isEnabled())).end();
      xml.start("IncludedObjectVersions").value(config.getIncludedObjectVersions()).end();
      this.writeInventoryDestination(xml, config.getDestination());
      this.writeInventoryFilter(xml, config.getInventoryFilter());
      this.addInventorySchedule(xml, config.getSchedule());
      this.addInventoryOptionalFields(xml, config.getOptionalFields());
      xml.end();
      return xml.getBytes();
   }

   private void writeInventoryDestination(XmlWriter xml, InventoryDestination destination) {
      if (destination != null) {
         xml.start("Destination");
         InventoryS3BucketDestination s3BucketDestination = destination.getS3BucketDestination();
         if (s3BucketDestination != null) {
            xml.start("S3BucketDestination");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "AccountId", s3BucketDestination.getAccountId());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Bucket", s3BucketDestination.getBucketArn());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Prefix", s3BucketDestination.getPrefix());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Format", s3BucketDestination.getFormat());
            this.writeInventoryEncryption(xml, s3BucketDestination.getEncryption());
            xml.end();
         }

         xml.end();
      }
   }

   private void writeInventoryEncryption(XmlWriter xml, InventoryEncryption encryption) {
      if (encryption != null) {
         xml.start("Encryption");
         if (encryption instanceof ServerSideEncryptionS3) {
            xml.start("SSE-S3").end();
         } else if (encryption instanceof ServerSideEncryptionKMS) {
            xml.start("SSE-KMS");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "KeyId", ((ServerSideEncryptionKMS)encryption).getKeyId());
            xml.end();
         }

         xml.end();
      }
   }

   private void writeInventoryFilter(XmlWriter xml, InventoryFilter inventoryFilter) {
      if (inventoryFilter != null) {
         xml.start("Filter");
         this.writeInventoryFilterPredicate(xml, inventoryFilter.getPredicate());
         xml.end();
      }
   }

   private void writeInventoryFilterPredicate(XmlWriter xml, InventoryFilterPredicate predicate) {
      if (predicate != null) {
         if (predicate instanceof InventoryPrefixPredicate) {
            BucketConfigurationXmlFactoryFunctions.writePrefix(xml, ((InventoryPrefixPredicate)predicate).getPrefix());
         }
      }
   }

   private void addInventorySchedule(XmlWriter xml, InventorySchedule schedule) {
      if (schedule != null) {
         xml.start("Schedule");
         BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Frequency", schedule.getFrequency());
         xml.end();
      }
   }

   private void addInventoryOptionalFields(XmlWriter xml, List<String> optionalFields) {
      if (!CollectionUtils.isNullOrEmpty(optionalFields)) {
         xml.start("OptionalFields");

         for(String field : optionalFields) {
            xml.start("Field").value(field).end();
         }

         xml.end();
      }
   }

   private void writeRule(XmlWriter xml, TagSet tagset) {
      xml.start("TagSet");

      for(String key : tagset.getAllTags().keySet()) {
         xml.start("Tag");
         xml.start("Key").value(key).end();
         xml.start("Value").value(tagset.getTag(key)).end();
         xml.end();
      }

      xml.end();
   }

   private boolean hasTags(TagSet tagSet) {
      return tagSet != null && tagSet.getAllTags() != null && tagSet.getAllTags().size() > 0;
   }

   public byte[] convertToXmlByteArray(AnalyticsConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("AnalyticsConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Id", config.getId());
      this.writeAnalyticsFilter(xml, config.getFilter());
      this.writeStorageClassAnalysis(xml, config.getStorageClassAnalysis());
      xml.end();
      return xml.getBytes();
   }

   private void writeAnalyticsFilter(XmlWriter xml, AnalyticsFilter filter) {
      if (filter != null) {
         xml.start("Filter");
         this.writeAnalyticsFilterPredicate(xml, filter.getPredicate());
         xml.end();
      }
   }

   private void writeAnalyticsFilterPredicate(XmlWriter xml, AnalyticsFilterPredicate predicate) {
      if (predicate != null) {
         predicate.accept(new AnalyticsPredicateVisitorImpl(xml));
      }
   }

   private void writeStorageClassAnalysis(XmlWriter xml, StorageClassAnalysis storageClassAnalysis) {
      if (storageClassAnalysis != null) {
         xml.start("StorageClassAnalysis");
         if (storageClassAnalysis.getDataExport() != null) {
            StorageClassAnalysisDataExport dataExport = storageClassAnalysis.getDataExport();
            xml.start("DataExport");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "OutputSchemaVersion", dataExport.getOutputSchemaVersion());
            this.writeAnalyticsExportDestination(xml, dataExport.getDestination());
            xml.end();
         }

         xml.end();
      }
   }

   private void writeAnalyticsExportDestination(XmlWriter xml, AnalyticsExportDestination destination) {
      if (destination != null) {
         xml.start("Destination");
         if (destination.getS3BucketDestination() != null) {
            xml.start("S3BucketDestination");
            AnalyticsS3BucketDestination s3BucketDestination = destination.getS3BucketDestination();
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Format", s3BucketDestination.getFormat());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "BucketAccountId", s3BucketDestination.getBucketAccountId());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Bucket", s3BucketDestination.getBucketArn());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Prefix", s3BucketDestination.getPrefix());
            xml.end();
         }

         xml.end();
      }
   }

   public byte[] convertToXmlByteArray(IntelligentTieringConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("IntelligentTieringConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Id", config.getId());
      this.writeIntelligentTieringFilter(xml, config.getFilter());
      BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Status", config.getStatus().name());
      this.writeIntelligentTierings(xml, config.getTierings());
      xml.end();
      return xml.getBytes();
   }

   private void writeIntelligentTieringFilter(XmlWriter xml, IntelligentTieringFilter filter) {
      if (filter != null) {
         xml.start("Filter");
         this.writeIntelligentTieringFilterPredicate(xml, filter.getPredicate());
         xml.end();
      }
   }

   private void writeIntelligentTieringFilterPredicate(XmlWriter xml, IntelligentTieringFilterPredicate predicate) {
      if (predicate != null) {
         predicate.accept(new XmlIntelligentTieringPredicateVisitor(xml));
      }
   }

   private void writeIntelligentTierings(XmlWriter xml, List<Tiering> tierings) {
      if (tierings != null) {
         for(Tiering tiering : tierings) {
            xml.start("Tiering");
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "AccessTier", tiering.getAccessTier().name());
            BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Days", Integer.toString(tiering.getDays()));
            xml.end();
         }
      }
   }

   public byte[] convertToXmlByteArray(MetricsConfiguration config) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("MetricsConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "Id", config.getId());
      this.writeMetricsFilter(xml, config.getFilter());
      xml.end();
      return xml.getBytes();
   }

   private void writeMetricsFilter(XmlWriter xml, MetricsFilter filter) {
      if (filter != null) {
         xml.start("Filter");
         this.writeMetricsFilterPredicate(xml, filter.getPredicate());
         xml.end();
      }
   }

   private void writeMetricsFilterPredicate(XmlWriter xml, MetricsFilterPredicate predicate) {
      if (predicate != null) {
         predicate.accept(new BucketConfigurationXmlFactory.MetricsPredicateVisitorImpl(xml));
      }
   }

   public byte[] convertToXmlByteArray(OwnershipControls controls) throws SdkClientException {
      XmlWriter xml = new XmlWriter();
      xml.start("OwnershipControls", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      this.writeOwnershipControlsRule(xml, controls.getRules());
      xml.end();
      return xml.getBytes();
   }

   private void writeOwnershipControlsRule(XmlWriter xml, List<OwnershipControlsRule> rules) {
      if (rules != null) {
         for(OwnershipControlsRule rule : rules) {
            if (rule == null) {
               throw new IllegalArgumentException("Ownership control rules must not be null.");
            }

            xml.start("Rule");
            if (rule.getOwnership() != null) {
               xml.start("ObjectOwnership").value(rule.getOwnership()).end();
            }

            xml.end();
         }
      }
   }

   private void addBooleanParameterIfNotNull(XmlWriter xml, String xmlTagName, Boolean value) {
      if (value != null) {
         xml.start(xmlTagName).value(value.toString()).end();
      }
   }

   private void writeTag(XmlWriter xml, Tag tag) {
      if (tag != null) {
         xml.start("Tag");
         xml.start("Key").value(tag.getKey()).end();
         xml.start("Value").value(tag.getValue()).end();
         xml.end();
      }
   }

   static void writeAccessPointArn(XmlWriter xml, String accessPointArn) {
      BucketConfigurationXmlFactoryFunctions.addParameterIfNotNull(xml, "AccessPointArn", accessPointArn);
   }

   private class LifecyclePredicateVisitorImpl implements LifecyclePredicateVisitor {
      private final XmlWriter xml;

      public LifecyclePredicateVisitorImpl(XmlWriter xml) {
         this.xml = xml;
      }

      @Override
      public void visit(LifecyclePrefixPredicate lifecyclePrefixPredicate) {
         BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, lifecyclePrefixPredicate.getPrefix());
      }

      @Override
      public void visit(LifecycleTagPredicate lifecycleTagPredicate) {
         BucketConfigurationXmlFactory.this.writeTag(this.xml, lifecycleTagPredicate.getTag());
      }

      @Override
      public void visit(LifecycleObjectSizeGreaterThanPredicate lifecycleObjectSizeGreaterThanPredicate) {
         BucketConfigurationXmlFactoryFunctions.writeObjectSizeGreaterThan(this.xml, lifecycleObjectSizeGreaterThanPredicate.getObjectSizeGreaterThan());
      }

      @Override
      public void visit(LifecycleObjectSizeLessThanPredicate lifecycleObjectSizeLessThanPredicate) {
         BucketConfigurationXmlFactoryFunctions.writeObjectSizeLessThan(this.xml, lifecycleObjectSizeLessThanPredicate.getObjectSizeLessThan());
      }

      @Override
      public void visit(LifecycleAndOperator lifecycleAndOperator) {
         this.xml.start("And");

         for(LifecycleFilterPredicate predicate : lifecycleAndOperator.getOperands()) {
            predicate.accept(this);
         }

         this.xml.end();
      }
   }

   private class MetricsPredicateVisitorImpl implements MetricsPredicateVisitor {
      private final XmlWriter xml;

      public MetricsPredicateVisitorImpl(XmlWriter xml) {
         this.xml = xml;
      }

      @Override
      public void visit(MetricsPrefixPredicate metricsPrefixPredicate) {
         BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, metricsPrefixPredicate.getPrefix());
      }

      @Override
      public void visit(MetricsTagPredicate metricsTagPredicate) {
         BucketConfigurationXmlFactory.this.writeTag(this.xml, metricsTagPredicate.getTag());
      }

      @Override
      public void visit(MetricsAndOperator metricsAndOperator) {
         this.xml.start("And");

         for(MetricsFilterPredicate predicate : metricsAndOperator.getOperands()) {
            predicate.accept(this);
         }

         this.xml.end();
      }

      @Override
      public void visit(MetricsAccessPointArnPredicate metricsAccessPointArnPredicate) {
         BucketConfigurationXmlFactory.writeAccessPointArn(this.xml, metricsAccessPointArnPredicate.getAccessPointArn());
      }
   }
}
