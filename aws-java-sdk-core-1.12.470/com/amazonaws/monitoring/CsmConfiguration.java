package com.amazonaws.monitoring;

public final class CsmConfiguration {
   private final boolean enabled;
   private final String host;
   private final int port;
   private final String clientId;

   public CsmConfiguration(boolean enabled, int port, String clientId) {
      this.enabled = enabled;
      this.host = null;
      this.port = port;
      this.clientId = clientId;
   }

   public static CsmConfiguration.Builder builder() {
      return new CsmConfiguration.Builder();
   }

   private CsmConfiguration(CsmConfiguration.Builder builder) {
      this.enabled = builder.enabled == null ? false : builder.enabled;
      this.host = builder.host == null ? "127.0.0.1" : builder.host;
      this.port = builder.port == null ? 31000 : builder.port;
      this.clientId = builder.clientId == null ? "" : builder.clientId;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getClientId() {
      return this.clientId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CsmConfiguration that = (CsmConfiguration)o;
         if (this.enabled != that.enabled) {
            return false;
         } else if (this.port != that.port) {
            return false;
         } else if (this.host != null ? this.host.equals(that.host) : that.host == null) {
            return this.clientId != null ? this.clientId.equals(that.clientId) : that.clientId == null;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.enabled ? 1 : 0;
      result = 31 * result + (this.host != null ? this.host.hashCode() : 0);
      result = 31 * result + this.port;
      return 31 * result + (this.clientId != null ? this.clientId.hashCode() : 0);
   }

   public static class Builder {
      private Boolean enabled;
      private String host;
      private Integer port;
      private String clientId;

      private Builder() {
      }

      public CsmConfiguration.Builder withEnabled(Boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      public CsmConfiguration.Builder withClientId(String clientId) {
         this.clientId = clientId;
         return this;
      }

      public CsmConfiguration.Builder withHost(String host) {
         this.host = host;
         return this;
      }

      public CsmConfiguration.Builder withPort(Integer port) {
         this.port = port;
         return this;
      }

      public CsmConfiguration build() {
         return new CsmConfiguration(this);
      }
   }
}
