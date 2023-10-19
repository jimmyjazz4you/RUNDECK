package com.amazonaws.monitoring;

public abstract class ApiMonitoringEvent implements MonitoringEvent {
   protected String api;
   protected String service;
   protected String clientId;
   protected Long timestamp;
   protected Integer version;
   protected String region;
   protected String userAgent;

   public String getApi() {
      return this.api;
   }

   public String getService() {
      return this.service;
   }

   public abstract ApiMonitoringEvent withService(String var1);

   public String getClientId() {
      return this.clientId;
   }

   public abstract ApiMonitoringEvent withClientId(String var1);

   public Long getTimestamp() {
      return this.timestamp;
   }

   public abstract ApiMonitoringEvent withTimestamp(Long var1);

   public abstract ApiMonitoringEvent withApi(String var1);

   public Integer getVersion() {
      return this.version;
   }

   public abstract ApiMonitoringEvent withVersion(Integer var1);

   public String getRegion() {
      return this.region;
   }

   public abstract ApiMonitoringEvent withRegion(String var1);

   public String getUserAgent() {
      return this.userAgent;
   }

   public abstract ApiMonitoringEvent withUserAgent(String var1);

   public abstract String getType();
}
