package com.amazonaws.services.s3;

import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.amazonaws.internal.SdkFunction;
import com.amazonaws.regions.AwsRegionProvider;

@NotThreadSafe
public abstract class AmazonS3Builder<Subclass extends AmazonS3Builder, TypeToBuild extends AmazonS3> extends AwsSyncClientBuilder<Subclass, TypeToBuild> {
   private static final AmazonS3ClientConfigurationFactory CLIENT_CONFIG_FACTORY = new AmazonS3ClientConfigurationFactory();
   private static final SdkFunction<AmazonS3ClientParamsWrapper, AmazonS3> DEFAULT_CLIENT_FACTORY = new SdkFunction<AmazonS3ClientParamsWrapper, AmazonS3>() {
      public AmazonS3 apply(AmazonS3ClientParamsWrapper params) {
         return new AmazonS3Client(params);
      }
   };
   protected final SdkFunction<AmazonS3ClientParamsWrapper, AmazonS3> clientFactory;
   private Boolean pathStyleAccessEnabled;
   private Boolean chunkedEncodingDisabled;
   private Boolean accelerateModeEnabled;
   private Boolean payloadSigningEnabled;
   private Boolean dualstackEnabled;
   private Boolean forceGlobalBucketAccessEnabled;
   private Boolean useArnRegionEnabled;
   private Boolean regionalUsEast1EndpointEnabled;

   protected AmazonS3Builder() {
      super(CLIENT_CONFIG_FACTORY);
      this.clientFactory = DEFAULT_CLIENT_FACTORY;
   }

   @SdkTestInternalApi
   AmazonS3Builder(
      SdkFunction<AmazonS3ClientParamsWrapper, AmazonS3> clientFactory, ClientConfigurationFactory clientConfigFactory, AwsRegionProvider regionProvider
   ) {
      super(clientConfigFactory, regionProvider);
      this.clientFactory = clientFactory;
   }

   public Boolean isPathStyleAccessEnabled() {
      return this.pathStyleAccessEnabled;
   }

   public void setPathStyleAccessEnabled(Boolean pathStyleAccessEnabled) {
      this.pathStyleAccessEnabled = pathStyleAccessEnabled;
   }

   public Subclass withPathStyleAccessEnabled(Boolean pathStyleAccessEnabled) {
      this.setPathStyleAccessEnabled(pathStyleAccessEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enablePathStyleAccess() {
      this.setPathStyleAccessEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isChunkedEncodingDisabled() {
      return this.chunkedEncodingDisabled;
   }

   public void setChunkedEncodingDisabled(Boolean chunkedEncodingDisabled) {
      this.chunkedEncodingDisabled = chunkedEncodingDisabled;
   }

   public Subclass withChunkedEncodingDisabled(Boolean chunkedEncodingDisabled) {
      this.setChunkedEncodingDisabled(chunkedEncodingDisabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass disableChunkedEncoding() {
      this.setChunkedEncodingDisabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isAccelerateModeEnabled() {
      return this.accelerateModeEnabled;
   }

   public void setAccelerateModeEnabled(Boolean accelerateModeEnabled) {
      this.accelerateModeEnabled = accelerateModeEnabled;
   }

   public Subclass withAccelerateModeEnabled(Boolean accelerateModeEnabled) {
      this.setAccelerateModeEnabled(accelerateModeEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enableAccelerateMode() {
      this.setAccelerateModeEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isPayloadSigningEnabled() {
      return this.payloadSigningEnabled;
   }

   public void setPayloadSigningEnabled(Boolean payloadSigningEnabled) {
      this.payloadSigningEnabled = payloadSigningEnabled;
   }

   public Subclass withPayloadSigningEnabled(Boolean payloadSigningEnabled) {
      this.setPayloadSigningEnabled(payloadSigningEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enablePayloadSigning() {
      this.setPayloadSigningEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isDualstackEnabled() {
      return this.dualstackEnabled;
   }

   public void setDualstackEnabled(Boolean dualstackEnabled) {
      this.dualstackEnabled = dualstackEnabled;
   }

   public Subclass withDualstackEnabled(Boolean dualstackEnabled) {
      this.setDualstackEnabled(dualstackEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enableDualstack() {
      this.setDualstackEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isForceGlobalBucketAccessEnabled() {
      return this.forceGlobalBucketAccessEnabled;
   }

   public void setForceGlobalBucketAccessEnabled(Boolean forceGlobalBucketAccessEnabled) {
      this.forceGlobalBucketAccessEnabled = forceGlobalBucketAccessEnabled;
   }

   public Subclass withForceGlobalBucketAccessEnabled(Boolean forceGlobalBucketAccessEnabled) {
      this.setForceGlobalBucketAccessEnabled(forceGlobalBucketAccessEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enableForceGlobalBucketAccess() {
      this.setForceGlobalBucketAccessEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   public Boolean isUseArnRegionEnabled() {
      return this.useArnRegionEnabled;
   }

   public Subclass enableUseArnRegion() {
      this.useArnRegionEnabled = true;
      return (Subclass)this.getSubclass();
   }

   public Boolean isRegionalUsEast1EndpointEnabled() {
      return this.regionalUsEast1EndpointEnabled;
   }

   public void setRegionalUsEast1EndpointEnabled(Boolean regionalUsEast1EndpointEnabled) {
      this.regionalUsEast1EndpointEnabled = regionalUsEast1EndpointEnabled;
   }

   public Subclass withRegionalUsEast1EndpointEnabled(Boolean regionalUsEast1EndpointEnabled) {
      this.setRegionalUsEast1EndpointEnabled(regionalUsEast1EndpointEnabled);
      return (Subclass)this.getSubclass();
   }

   public Subclass enableRegionalUsEast1Endpoint() {
      this.setRegionalUsEast1EndpointEnabled(Boolean.TRUE);
      return (Subclass)this.getSubclass();
   }

   protected S3ClientOptions resolveS3ClientOptions() {
      S3ClientOptions.Builder builder = S3ClientOptions.builder();
      if (Boolean.TRUE.equals(this.chunkedEncodingDisabled)) {
         builder.disableChunkedEncoding();
      }

      if (this.payloadSigningEnabled != null) {
         builder.setPayloadSigningEnabled(this.payloadSigningEnabled);
      }

      if (this.accelerateModeEnabled != null) {
         builder.setAccelerateModeEnabled(this.accelerateModeEnabled);
      }

      if (this.pathStyleAccessEnabled != null) {
         builder.setPathStyleAccess(this.pathStyleAccessEnabled);
      }

      if (Boolean.TRUE.equals(this.dualstackEnabled)) {
         builder.enableDualstack();
      }

      if (Boolean.TRUE.equals(this.forceGlobalBucketAccessEnabled)) {
         builder.enableForceGlobalBucketAccess();
      }

      if (Boolean.TRUE.equals(this.useArnRegionEnabled)) {
         builder.enableUseArnRegion();
      }

      if (Boolean.TRUE.equals(this.regionalUsEast1EndpointEnabled)) {
         builder.enableRegionalUsEast1Endpoint();
      }

      return builder.build();
   }
}
