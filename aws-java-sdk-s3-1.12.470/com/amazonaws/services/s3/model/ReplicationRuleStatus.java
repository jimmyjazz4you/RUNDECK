package com.amazonaws.services.s3.model;

public enum ReplicationRuleStatus {
   Enabled("Enabled"),
   Disabled("Disabled");

   private final String status;

   private ReplicationRuleStatus(String status) {
      this.status = status;
   }

   public String getStatus() {
      return this.status;
   }
}
