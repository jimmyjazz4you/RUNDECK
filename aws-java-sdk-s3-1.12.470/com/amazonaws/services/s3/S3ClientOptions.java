package com.amazonaws.services.s3;

import com.amazonaws.SdkClientException;

public class S3ClientOptions {
   public static final boolean DEFAULT_PATH_STYLE_ACCESS = false;
   public static final boolean DEFAULT_CHUNKED_ENCODING_DISABLED = false;
   public static final boolean DEFAULT_PAYLOAD_SIGNING_ENABLED = false;
   public static final boolean DEFAULT_ACCELERATE_MODE_ENABLED = false;
   public static final boolean DEFAULT_DUALSTACK_ENABLED = false;
   public static final boolean DEFAULT_FORCE_GLOBAL_BUCKET_ACCESS_ENABLED = false;
   public static final boolean DEFAULT_USE_ARN_REGION = false;
   public static final boolean DEFAULT_US_EAST_1_REGION_ENDPOINT_ENABLED = false;
   private boolean pathStyleAccess;
   private boolean chunkedEncodingDisabled;
   private final boolean accelerateModeEnabled;
   private final boolean payloadSigningEnabled;
   private final boolean dualstackEnabled;
   private final boolean forceGlobalBucketAccessEnabled;
   private final boolean useArnRegion;
   private final boolean regionalUsEast1EndpointEnabled;

   public static S3ClientOptions.Builder builder() {
      return new S3ClientOptions.Builder();
   }

   @Deprecated
   public S3ClientOptions() {
      this.pathStyleAccess = false;
      this.chunkedEncodingDisabled = false;
      this.accelerateModeEnabled = false;
      this.payloadSigningEnabled = false;
      this.dualstackEnabled = false;
      this.forceGlobalBucketAccessEnabled = false;
      this.useArnRegion = false;
      this.regionalUsEast1EndpointEnabled = false;
   }

   @Deprecated
   public S3ClientOptions(S3ClientOptions other) {
      this.pathStyleAccess = other.pathStyleAccess;
      this.chunkedEncodingDisabled = other.chunkedEncodingDisabled;
      this.accelerateModeEnabled = other.accelerateModeEnabled;
      this.payloadSigningEnabled = other.payloadSigningEnabled;
      this.dualstackEnabled = other.dualstackEnabled;
      this.forceGlobalBucketAccessEnabled = other.forceGlobalBucketAccessEnabled;
      this.useArnRegion = other.useArnRegion;
      this.regionalUsEast1EndpointEnabled = other.regionalUsEast1EndpointEnabled;
   }

   private S3ClientOptions(S3ClientOptions.Builder b) {
      this.pathStyleAccess = b.pathStyleAccess;
      this.chunkedEncodingDisabled = b.chunkedEncodingDisabled;
      this.accelerateModeEnabled = b.accelerateModeEnabled;
      this.payloadSigningEnabled = b.payloadSigningEnabled;
      this.dualstackEnabled = b.dualstackEnabled;
      this.forceGlobalBucketAccessEnabled = b.forceGlobalBucketAccessEnabled;
      this.useArnRegion = Boolean.TRUE.equals(b.useArnRegion);
      this.regionalUsEast1EndpointEnabled = b.regionalUsEast1EndpointEnabled;
   }

   public boolean isPathStyleAccess() {
      return this.pathStyleAccess;
   }

   public boolean isChunkedEncodingDisabled() {
      return this.chunkedEncodingDisabled;
   }

   public boolean isAccelerateModeEnabled() {
      return this.accelerateModeEnabled;
   }

   public boolean isPayloadSigningEnabled() {
      return this.payloadSigningEnabled;
   }

   public boolean isDualstackEnabled() {
      return this.dualstackEnabled;
   }

   public boolean isForceGlobalBucketAccessEnabled() {
      return this.forceGlobalBucketAccessEnabled;
   }

   public boolean isUseArnRegion() {
      return this.useArnRegion;
   }

   public boolean isRegionalUsEast1EndpointEnabled() {
      return this.regionalUsEast1EndpointEnabled;
   }

   @Deprecated
   public void setPathStyleAccess(boolean pathStyleAccess) {
      this.pathStyleAccess = pathStyleAccess;
   }

   @Deprecated
   public S3ClientOptions withPathStyleAccess(boolean pathStyleAccess) {
      this.setPathStyleAccess(pathStyleAccess);
      return this;
   }

   @Deprecated
   public void setChunkedEncodingDisabled(boolean chunkedEncodingDisabled) {
      this.chunkedEncodingDisabled = chunkedEncodingDisabled;
   }

   @Deprecated
   public S3ClientOptions withChunkedEncodingDisabled(boolean chunkedEncodingDisabled) {
      this.setChunkedEncodingDisabled(chunkedEncodingDisabled);
      return this;
   }

   @Deprecated
   public S3ClientOptions disableChunkedEncoding() {
      return this.withChunkedEncodingDisabled(true);
   }

   public static class Builder {
      private boolean pathStyleAccess = false;
      private boolean chunkedEncodingDisabled = false;
      private boolean accelerateModeEnabled = false;
      private boolean payloadSigningEnabled = false;
      private boolean dualstackEnabled = false;
      private boolean forceGlobalBucketAccessEnabled = false;
      private Boolean useArnRegion = null;
      private boolean regionalUsEast1EndpointEnabled = false;

      private Builder() {
      }

      public S3ClientOptions build() {
         if (this.pathStyleAccess && this.accelerateModeEnabled) {
            throw new SdkClientException(
               "Both accelerate mode and path style access are being enabled either through S3ClientOptions or AmazonS3ClientBuilder. These options are mutually exclusive and cannot be enabled together. Please disable one of them"
            );
         } else {
            return new S3ClientOptions(this);
         }
      }

      public S3ClientOptions.Builder setPathStyleAccess(boolean pathStyleAccess) {
         this.pathStyleAccess = pathStyleAccess;
         return this;
      }

      public S3ClientOptions.Builder setAccelerateModeEnabled(boolean accelerateModeEnabled) {
         this.accelerateModeEnabled = accelerateModeEnabled;
         return this;
      }

      public S3ClientOptions.Builder setPayloadSigningEnabled(boolean payloadSigningEnabled) {
         this.payloadSigningEnabled = payloadSigningEnabled;
         return this;
      }

      public S3ClientOptions.Builder disableChunkedEncoding() {
         this.chunkedEncodingDisabled = true;
         return this;
      }

      public S3ClientOptions.Builder enableDualstack() {
         this.dualstackEnabled = true;
         return this;
      }

      public S3ClientOptions.Builder enableForceGlobalBucketAccess() {
         this.forceGlobalBucketAccessEnabled = true;
         return this;
      }

      public S3ClientOptions.Builder enableUseArnRegion() {
         this.useArnRegion = true;
         return this;
      }

      public S3ClientOptions.Builder enableRegionalUsEast1Endpoint() {
         this.regionalUsEast1EndpointEnabled = true;
         return this;
      }
   }
}
