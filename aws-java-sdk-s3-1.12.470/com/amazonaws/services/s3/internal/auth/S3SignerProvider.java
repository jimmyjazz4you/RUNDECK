package com.amazonaws.services.s3.internal.auth;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.Request;
import com.amazonaws.auth.RegionAwareSigner;
import com.amazonaws.auth.ServiceAwareSigner;
import com.amazonaws.auth.Signer;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.internal.auth.SignerProviderContext;
import com.amazonaws.regions.EndpointToRegion;
import com.amazonaws.services.s3.internal.ServiceUtils;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S3SignerProvider extends SignerProvider {
   private static Log log = LogFactory.getLog(S3SignerProvider.class);
   private final AmazonWebServiceClient awsClient;
   private Signer signer;

   public S3SignerProvider(AmazonWebServiceClient awsClient, Signer defaultSigner) {
      this.awsClient = awsClient;
      this.signer = defaultSigner;
   }

   public Signer getSigner(SignerProviderContext signerProviderContext) {
      URI uri = signerProviderContext.getUri();
      if (uri != null && !ServiceUtils.isS3AccelerateEndpoint(uri.getHost()) && !this.isSignerRegionOverrideSet()) {
         if (this.signer instanceof RegionAwareSigner && !this.isAccessPointUri(uri)) {
            RegionAwareSigner regionSigner = (RegionAwareSigner)this.signer;

            try {
               regionSigner.setRegionName(EndpointToRegion.guessRegionNameForEndpoint(uri.getHost(), "s3"));
            } catch (RuntimeException var5) {
               log.warn("Failed to parse the endpoint " + uri + ", and skip re-assigning the signer region", var5);
            }
         }

         Request<?> request = signerProviderContext.getRequest();
         if (!this.isSignerOverridden() && request != null && request.getHandlerContext(HandlerContextKey.SIGNING_NAME) != null) {
            String signingName = (String)request.getHandlerContext(HandlerContextKey.SIGNING_NAME);
            if (this.signer instanceof ServiceAwareSigner) {
               ((ServiceAwareSigner)this.signer).setServiceName(signingName);
            }
         }

         return this.signer;
      } else {
         return this.signer;
      }
   }

   private boolean isAccessPointUri(URI uri) {
      String str = uri.toASCIIString();
      return str.contains(".s3-accesspoint.") || str.contains(".s3-outposts.") || str.contains(".s3-object-lambda.");
   }

   private boolean isSignerRegionOverrideSet() {
      return this.awsClient != null && this.awsClient.getSignerRegionOverride() != null;
   }

   private boolean isSignerOverridden() {
      return this.awsClient.getSignerOverride() != null;
   }

   public void setSigner(Signer signer) {
      this.signer = signer;
   }
}
