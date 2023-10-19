package com.amazonaws.services.s3;

import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.internal.SdkFunction;
import com.amazonaws.regions.AwsRegionProvider;

@NotThreadSafe
public final class AmazonS3ClientBuilder extends AmazonS3Builder<AmazonS3ClientBuilder, AmazonS3> {
   private AmazonS3ClientBuilder() {
   }

   @SdkTestInternalApi
   AmazonS3ClientBuilder(
      SdkFunction<AmazonS3ClientParamsWrapper, AmazonS3> clientFactory, ClientConfigurationFactory clientConfigFactory, AwsRegionProvider regionProvider
   ) {
      super(clientFactory, clientConfigFactory, regionProvider);
   }

   public static AmazonS3ClientBuilder standard() {
      return (AmazonS3ClientBuilder)new AmazonS3ClientBuilder().withCredentials(new S3CredentialsProviderChain());
   }

   public static AmazonS3 defaultClient() {
      return (AmazonS3)standard().build();
   }

   protected AmazonS3 build(AwsSyncClientParams clientParams) {
      return (AmazonS3)this.clientFactory.apply(new AmazonS3ClientParamsWrapper(clientParams, this.resolveS3ClientOptions()));
   }
}
