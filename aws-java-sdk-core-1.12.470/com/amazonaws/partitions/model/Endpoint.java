package com.amazonaws.partitions.model;

import com.amazonaws.Protocol;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public class Endpoint {
   private String hostName;
   private CredentialScope credentialScope;
   private Set<String> protocols;
   private Set<String> signatureVersions;
   private String sslCommonName;

   public static Endpoint merge(Endpoint defaults, Endpoint override) {
      if (defaults == null) {
         defaults = new Endpoint();
      }

      if (override == null) {
         override = new Endpoint();
      }

      Endpoint merged = new Endpoint();
      merged.setCredentialScope(override.getCredentialScope() != null ? override.getCredentialScope() : defaults.getCredentialScope());
      merged.setHostName(override.getHostName() != null ? override.getHostName() : defaults.getHostName());
      merged.setSslCommonName(override.getSslCommonName() != null ? override.getSslCommonName() : defaults.getSslCommonName());
      merged.setProtocols(override.getProtocols() != null ? override.getProtocols() : defaults.getProtocols());
      merged.setSignatureVersions(override.getSignatureVersions() != null ? override.getSignatureVersions() : defaults.getSignatureVersions());
      return merged;
   }

   public String getHostName() {
      return this.hostName;
   }

   @JsonProperty("hostname")
   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   public CredentialScope getCredentialScope() {
      return this.credentialScope;
   }

   @JsonProperty("credentialScope")
   public void setCredentialScope(CredentialScope credentialScope) {
      this.credentialScope = credentialScope;
   }

   public Set<String> getProtocols() {
      return this.protocols;
   }

   @JsonProperty("protocols")
   public void setProtocols(Set<String> protocols) {
      this.protocols = protocols;
   }

   public Set<String> getSignatureVersions() {
      return this.signatureVersions;
   }

   @JsonProperty("signatureVersions")
   public void setSignatureVersions(Set<String> signatureVersions) {
      this.signatureVersions = signatureVersions;
   }

   public String getSslCommonName() {
      return this.sslCommonName;
   }

   @JsonProperty("sslCommonName")
   public void setSslCommonName(String sslCommonName) {
      this.sslCommonName = sslCommonName;
   }

   public boolean hasHttpsSupport() {
      return this.isProtocolSupported(Protocol.HTTPS);
   }

   public boolean hasHttpSupport() {
      return this.isProtocolSupported(Protocol.HTTP);
   }

   private boolean isProtocolSupported(Protocol protocol) {
      return this.protocols != null && this.protocols.contains(protocol.toString());
   }
}
