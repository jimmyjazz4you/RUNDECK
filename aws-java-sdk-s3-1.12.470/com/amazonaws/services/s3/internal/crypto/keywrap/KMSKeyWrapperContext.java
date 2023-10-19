package com.amazonaws.services.s3.internal.crypto.keywrap;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.kms.AWSKMS;
import java.util.Map;

public class KMSKeyWrapperContext {
   private final AWSKMS kms;
   private final AmazonWebServiceRequest originalRequest;
   private final Map<String, String> kmsMaterialsDescription;

   private KMSKeyWrapperContext(KMSKeyWrapperContext.Builder b) {
      this.kms = b.kms;
      this.originalRequest = b.originalRequest;
      this.kmsMaterialsDescription = b.kmsMaterialsDescription;
   }

   public static KMSKeyWrapperContext.Builder builder() {
      return new KMSKeyWrapperContext.Builder();
   }

   public AmazonWebServiceRequest originalRequest() {
      return this.originalRequest;
   }

   public Map<String, String> kmsMaterialsDescription() {
      return this.kmsMaterialsDescription;
   }

   public AWSKMS kms() {
      return this.kms;
   }

   public static class Builder {
      private AWSKMS kms;
      private AmazonWebServiceRequest originalRequest;
      private Map<String, String> kmsMaterialsDescription;

      public KMSKeyWrapperContext.Builder kms(AWSKMS kms) {
         this.kms = kms;
         return this;
      }

      public KMSKeyWrapperContext.Builder originalRequest(AmazonWebServiceRequest originalRequest) {
         this.originalRequest = originalRequest;
         return this;
      }

      public KMSKeyWrapperContext.Builder kmsMaterialsDescription(Map<String, String> kmsMaterialsDescription) {
         this.kmsMaterialsDescription = kmsMaterialsDescription;
         return this;
      }

      public KMSKeyWrapperContext build() {
         return new KMSKeyWrapperContext(this);
      }
   }
}
