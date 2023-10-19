package com.amazonaws.protocol;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.http.HttpMethodName;

@SdkProtectedApi
public class OperationInfo {
   private final Protocol protocol;
   private final String requestUri;
   private final HttpMethodName httpMethodName;
   private final String operationIdentifier;
   private final String serviceName;
   private final boolean hasExplicitPayloadMember;
   private final boolean hasPayloadMembers;
   private final String serviceId;

   private OperationInfo(OperationInfo.Builder builder) {
      this.protocol = builder.protocol;
      this.requestUri = builder.requestUri;
      this.httpMethodName = builder.httpMethodName;
      this.operationIdentifier = builder.operationIdentifier;
      this.serviceName = builder.serviceName;
      this.hasExplicitPayloadMember = builder.hasExplicitPayloadMember;
      this.hasPayloadMembers = builder.hasPayloadMembers;
      this.serviceId = builder.serviceId;
   }

   public Protocol protocol() {
      return this.protocol;
   }

   public String requestUri() {
      return this.requestUri;
   }

   public HttpMethodName httpMethodName() {
      return this.httpMethodName;
   }

   public String operationIdentifier() {
      return this.operationIdentifier;
   }

   public String serviceName() {
      return this.serviceName;
   }

   public boolean hasExplicitPayloadMember() {
      return this.hasExplicitPayloadMember;
   }

   public boolean hasPayloadMembers() {
      return this.hasPayloadMembers;
   }

   public String serviceId() {
      return this.serviceId;
   }

   public static OperationInfo.Builder builder() {
      return new OperationInfo.Builder();
   }

   public static final class Builder {
      private Protocol protocol;
      private String requestUri;
      private HttpMethodName httpMethodName;
      private String operationIdentifier;
      private String serviceName;
      private boolean hasExplicitPayloadMember;
      private boolean hasPayloadMembers;
      private String serviceId;

      public OperationInfo.Builder protocol(Protocol protocol) {
         this.protocol = protocol;
         return this;
      }

      public OperationInfo.Builder requestUri(String requestUri) {
         this.requestUri = requestUri;
         return this;
      }

      public OperationInfo.Builder httpMethodName(HttpMethodName httpMethodName) {
         this.httpMethodName = httpMethodName;
         return this;
      }

      public OperationInfo.Builder operationIdentifier(String operationIdentifier) {
         this.operationIdentifier = operationIdentifier;
         return this;
      }

      public OperationInfo.Builder serviceName(String serviceName) {
         this.serviceName = serviceName;
         return this;
      }

      public OperationInfo.Builder hasExplicitPayloadMember(boolean hasExplicitPayloadMember) {
         this.hasExplicitPayloadMember = hasExplicitPayloadMember;
         return this;
      }

      public OperationInfo.Builder hasPayloadMembers(boolean hasPayloadMembers) {
         this.hasPayloadMembers = hasPayloadMembers;
         return this;
      }

      public OperationInfo.Builder serviceId(String serviceId) {
         this.serviceId = serviceId;
         return this;
      }

      private Builder() {
      }

      public OperationInfo build() {
         return new OperationInfo(this);
      }
   }
}
