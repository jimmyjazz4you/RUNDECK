package com.amazonaws.auth.policy;

public class Principal {
   public static final Principal AllUsers = new Principal("AWS", "*");
   public static final Principal AllServices = new Principal("Service", "*");
   public static final Principal AllWebProviders = new Principal("Federated", "*");
   public static final Principal All = new Principal("*", "*");
   private final String id;
   private final String provider;

   public Principal(Principal.Services service) {
      if (service == null) {
         throw new IllegalArgumentException("Null AWS service name specified");
      } else {
         this.id = service.getServiceId();
         this.provider = "Service";
      }
   }

   public Principal(String accountId) {
      this("AWS", accountId);
      if (accountId == null) {
         throw new IllegalArgumentException("Null AWS account ID specified");
      }
   }

   public Principal(String provider, String id) {
      this(provider, id, provider.equals("AWS"));
   }

   public Principal(String provider, String id, boolean stripHyphen) {
      this.provider = provider;
      this.id = stripHyphen ? id.replace("-", "") : id;
   }

   public Principal(Principal.WebIdentityProviders webIdentityProvider) {
      if (webIdentityProvider == null) {
         throw new IllegalArgumentException("Null web identity provider specified");
      } else {
         this.id = webIdentityProvider.getWebIdentityProvider();
         this.provider = "Federated";
      }
   }

   public String getProvider() {
      return this.provider;
   }

   public String getId() {
      return this.id;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      hashCode = 31 * hashCode + this.provider.hashCode();
      return 31 * hashCode + this.id.hashCode();
   }

   @Override
   public boolean equals(Object principal) {
      if (this == principal) {
         return true;
      } else if (principal == null) {
         return false;
      } else if (!(principal instanceof Principal)) {
         return false;
      } else {
         Principal other = (Principal)principal;
         return this.getProvider().equals(other.getProvider()) && this.getId().equals(other.getId());
      }
   }

   public static enum Services {
      AmazonApiGateway("apigateway.amazonaws.com"),
      AWSDataPipeline("datapipeline.amazonaws.com"),
      AmazonElasticTranscoder("elastictranscoder.amazonaws.com"),
      AmazonEC2("ec2.amazonaws.com"),
      AWSOpsWorks("opsworks.amazonaws.com"),
      AWSCloudHSM("cloudhsm.amazonaws.com"),
      AllServices("*");

      private String serviceId;

      private Services(String serviceId) {
         this.serviceId = serviceId;
      }

      public String getServiceId() {
         return this.serviceId;
      }

      public static Principal.Services fromString(String serviceId) {
         if (serviceId != null) {
            for(Principal.Services s : values()) {
               if (s.getServiceId().equalsIgnoreCase(serviceId)) {
                  return s;
               }
            }
         }

         return null;
      }
   }

   public static enum WebIdentityProviders {
      Facebook("graph.facebook.com"),
      Google("accounts.google.com"),
      Amazon("www.amazon.com"),
      AllProviders("*");

      private String webIdentityProvider;

      private WebIdentityProviders(String webIdentityProvider) {
         this.webIdentityProvider = webIdentityProvider;
      }

      public String getWebIdentityProvider() {
         return this.webIdentityProvider;
      }

      public static Principal.WebIdentityProviders fromString(String webIdentityProvider) {
         if (webIdentityProvider != null) {
            for(Principal.WebIdentityProviders provider : values()) {
               if (provider.getWebIdentityProvider().equalsIgnoreCase(webIdentityProvider)) {
                  return provider;
               }
            }
         }

         return null;
      }
   }
}
