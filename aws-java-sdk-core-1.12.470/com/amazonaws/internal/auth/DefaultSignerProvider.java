package com.amazonaws.internal.auth;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.auth.ServiceAwareSigner;
import com.amazonaws.auth.Signer;
import com.amazonaws.auth.SignerFactory;
import com.amazonaws.auth.SignerParams;
import com.amazonaws.auth.SignerTypeAware;
import com.amazonaws.handlers.HandlerContextKey;
import com.amazonaws.regions.EndpointToRegion;
import java.net.URI;

public class DefaultSignerProvider extends SignerProvider {
   private final AmazonWebServiceClient awsClient;
   private final Signer defaultSigner;

   public DefaultSignerProvider(AmazonWebServiceClient awsClient, Signer defaultSigner) {
      this.awsClient = awsClient;
      this.defaultSigner = defaultSigner;
   }

   @Override
   public Signer getSigner(SignerProviderContext context) {
      Request<?> request = context.getRequest();
      if (request != null && !this.shouldUseDefaultSigner(request.getOriginalRequest())) {
         SignerTypeAware signerTypeAware = (SignerTypeAware)request.getOriginalRequest();
         SignerParams params = new SignerParams(this.awsClient.getServiceName(), this.getSigningRegionForRequestURI(request.getEndpoint()));
         return SignerFactory.createSigner(signerTypeAware.getSignerType(), params);
      } else if (context.isRedirect()) {
         return this.awsClient.getSignerByURI(context.getUri());
      } else {
         if (request != null && request.getHandlerContext(HandlerContextKey.SIGNING_NAME) != null) {
            String signingName = request.getHandlerContext(HandlerContextKey.SIGNING_NAME);
            Signer newSigner = this.awsClient.getSignerByURI(context.getUri());
            if (newSigner instanceof ServiceAwareSigner && !this.isSignerOverridden()) {
               ((ServiceAwareSigner)newSigner).setServiceName(signingName);
               return newSigner;
            }
         }

         return this.defaultSigner;
      }
   }

   private boolean shouldUseDefaultSigner(AmazonWebServiceRequest originalRequest) {
      return !(originalRequest instanceof SignerTypeAware) || this.isSignerOverridden();
   }

   private boolean isSignerOverridden() {
      return this.awsClient.getSignerOverride() != null;
   }

   private String getSigningRegionForRequestURI(URI uri) {
      String regionName = this.awsClient.getSignerRegionOverride();
      if (regionName == null) {
         regionName = EndpointToRegion.guessRegionNameForEndpoint(uri.getHost(), this.awsClient.getEndpointPrefix());
      }

      return regionName;
   }
}
