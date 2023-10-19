package com.amazonaws.auth.internal;

import com.amazonaws.SignableRequest;
import com.amazonaws.auth.SdkClock;
import com.amazonaws.util.endpoint.DefaultRegionFromEndpointResolver;
import com.amazonaws.util.endpoint.RegionFromEndpointResolver;
import java.util.Date;

public final class AWS4SignerRequestParams {
   private final SignableRequest<?> request;
   private final long signingDateTimeMilli;
   private final String scope;
   private final String regionName;
   private final String serviceName;
   private final String formattedSigningDateTime;
   private final String formattedSigningDate;
   private final String signingAlgorithm;

   public AWS4SignerRequestParams(SignableRequest<?> request, Date signingDateOverride, String regionNameOverride, String serviceName, String signingAlgorithm) {
      this(request, signingDateOverride, regionNameOverride, serviceName, signingAlgorithm, null);
   }

   public AWS4SignerRequestParams(
      SignableRequest<?> request, Date signingDateOverride, String regionNameOverride, String serviceName, String signingAlgorithm, String endpointPrefix
   ) {
      this(request, signingDateOverride, regionNameOverride, serviceName, signingAlgorithm, endpointPrefix, null);
   }

   public AWS4SignerRequestParams(
      SignableRequest<?> request,
      Date signingDateOverride,
      String regionNameOverride,
      String serviceName,
      String signingAlgorithm,
      String endpointPrefix,
      RegionFromEndpointResolver regionFromEndpointResolver
   ) {
      if (request == null) {
         throw new IllegalArgumentException("Request cannot be null");
      } else if (signingAlgorithm == null) {
         throw new IllegalArgumentException("Signing Algorithm cannot be null");
      } else {
         this.request = request;
         this.signingDateTimeMilli = signingDateOverride != null ? signingDateOverride.getTime() : this.getSigningDate(request);
         this.formattedSigningDate = AWS4SignerUtils.formatDateStamp(this.signingDateTimeMilli);
         this.serviceName = serviceName;
         this.regionName = regionNameOverride != null ? regionNameOverride : this.resolveRegion(regionFromEndpointResolver, endpointPrefix, this.serviceName);
         this.scope = this.generateScope(request, this.formattedSigningDate, this.serviceName, this.regionName);
         this.formattedSigningDateTime = AWS4SignerUtils.formatTimestamp(this.signingDateTimeMilli);
         this.signingAlgorithm = signingAlgorithm;
      }
   }

   private String resolveRegion(RegionFromEndpointResolver resolver, String endpointPrefix, String serviceSigningName) {
      if (resolver == null) {
         resolver = new DefaultRegionFromEndpointResolver();
      }

      String host = this.request.getEndpoint().getHost();
      String region = resolver.guessRegionFromEndpoint(host, endpointPrefix != null ? endpointPrefix : serviceSigningName);
      return region != null ? region : "us-east-1";
   }

   private final long getSigningDate(SignableRequest<?> request) {
      return SdkClock.Instance.get().currentTimeMillis() - (long)request.getTimeOffset() * 1000L;
   }

   private String generateScope(SignableRequest<?> request, String dateStamp, String serviceName, String regionName) {
      StringBuilder scopeBuilder = new StringBuilder();
      return scopeBuilder.append(dateStamp).append("/").append(regionName).append("/").append(serviceName).append("/").append("aws4_request").toString();
   }

   public SignableRequest<?> getRequest() {
      return this.request;
   }

   public String getScope() {
      return this.scope;
   }

   public String getFormattedSigningDateTime() {
      return this.formattedSigningDateTime;
   }

   public long getSigningDateTimeMilli() {
      return this.signingDateTimeMilli;
   }

   public String getRegionName() {
      return this.regionName;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public String getFormattedSigningDate() {
      return this.formattedSigningDate;
   }

   public String getSigningAlgorithm() {
      return this.signingAlgorithm;
   }
}
