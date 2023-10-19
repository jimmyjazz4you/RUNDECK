package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.replication.ReplicationFilter;
import com.amazonaws.util.json.Jackson;
import java.io.Serializable;

public class ReplicationRule implements Serializable {
   private String prefix;
   private String status;
   private ReplicationDestinationConfig destinationConfig;
   private SourceSelectionCriteria sourceSelectionCriteria;
   private ReplicationFilter filter;
   private Integer priority;
   private ExistingObjectReplication existingObjectReplication;
   private DeleteMarkerReplication deleteMarkerReplication;

   public Integer getPriority() {
      return this.priority;
   }

   public void setPriority(Integer priority) {
      if (priority < 0) {
         throw new IllegalArgumentException("Priority has to be a positive number");
      } else {
         this.priority = priority;
      }
   }

   public ReplicationRule withPriority(Integer priority) {
      this.setPriority(priority);
      return this;
   }

   public ExistingObjectReplication getExistingObjectReplication() {
      return this.existingObjectReplication;
   }

   public void setExistingObjectReplication(ExistingObjectReplication existingObjectReplication) {
      this.existingObjectReplication = existingObjectReplication;
   }

   public ReplicationRule withExistingObjectReplication(ExistingObjectReplication existingObjectReplication) {
      this.setExistingObjectReplication(existingObjectReplication);
      return this;
   }

   public DeleteMarkerReplication getDeleteMarkerReplication() {
      return this.deleteMarkerReplication;
   }

   public void setDeleteMarkerReplication(DeleteMarkerReplication deleteMarkerReplication) {
      this.deleteMarkerReplication = deleteMarkerReplication;
   }

   public ReplicationRule withDeleteMarkerReplication(DeleteMarkerReplication deleteMarkerReplication) {
      this.setDeleteMarkerReplication(deleteMarkerReplication);
      return this;
   }

   @Deprecated
   public String getPrefix() {
      return this.prefix;
   }

   @Deprecated
   public void setPrefix(String prefix) {
      if (prefix == null) {
         throw new IllegalArgumentException("Prefix cannot be null for a replication rule");
      } else if (this.filter != null) {
         throw new IllegalArgumentException("You cannot use both prefix and filter at the same time in a replication rule");
      } else {
         this.prefix = prefix;
      }
   }

   @Deprecated
   public ReplicationRule withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public ReplicationFilter getFilter() {
      return this.filter;
   }

   public void setFilter(ReplicationFilter filter) {
      if (filter == null) {
         throw new IllegalArgumentException("Filter cannot be null for a replication rule");
      } else if (this.prefix != null) {
         throw new IllegalArgumentException("You cannot use both prefix and filter at the same time in a replication rule");
      } else {
         this.filter = filter;
      }
   }

   public ReplicationRule withFilter(ReplicationFilter filter) {
      this.setFilter(filter);
      return this;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public ReplicationRule withStatus(String status) {
      this.setStatus(status);
      return this;
   }

   public void setStatus(ReplicationRuleStatus status) {
      this.setStatus(status.getStatus());
   }

   public ReplicationRule withStatus(ReplicationRuleStatus status) {
      this.setStatus(status.getStatus());
      return this;
   }

   public ReplicationDestinationConfig getDestinationConfig() {
      return this.destinationConfig;
   }

   public void setDestinationConfig(ReplicationDestinationConfig destinationConfig) {
      if (destinationConfig == null) {
         throw new IllegalArgumentException("Destination cannot be null in the replication rule");
      } else {
         this.destinationConfig = destinationConfig;
      }
   }

   public ReplicationRule withDestinationConfig(ReplicationDestinationConfig destinationConfig) {
      this.setDestinationConfig(destinationConfig);
      return this;
   }

   public SourceSelectionCriteria getSourceSelectionCriteria() {
      return this.sourceSelectionCriteria;
   }

   public void setSourceSelectionCriteria(SourceSelectionCriteria sourceSelectionCriteria) {
      this.sourceSelectionCriteria = sourceSelectionCriteria;
   }

   public ReplicationRule withSourceSelectionCriteria(SourceSelectionCriteria sourceSelectionCriteria) {
      this.setSourceSelectionCriteria(sourceSelectionCriteria);
      return this;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this);
   }
}
