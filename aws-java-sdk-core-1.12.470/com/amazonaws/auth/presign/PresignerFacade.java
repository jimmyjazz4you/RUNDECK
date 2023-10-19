package com.amazonaws.auth.presign;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.Presigner;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.internal.auth.SignerProviderContext;
import com.amazonaws.util.CredentialUtils;
import com.amazonaws.util.RuntimeHttpUtils;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Immutable
@SdkProtectedApi
public final class PresignerFacade {
   private final AWSCredentialsProvider credentialsProvider;
   private final SignerProvider signerProvider;

   public PresignerFacade(PresignerParams presignerParams) {
      this.credentialsProvider = presignerParams.credentialsProvider();
      this.signerProvider = presignerParams.signerProvider();
   }

   public URL presign(Request<?> request, Date expirationDate) {
      Presigner presigner = (Presigner)this.signerProvider
         .getSigner(SignerProviderContext.builder().withIsRedirect(false).withRequest(request).withUri(request.getEndpoint()).build());
      if (request.getOriginalRequest() != null) {
         this.addCustomQueryParams(request);
         this.addCustomHeaders(request);
      }

      AWSCredentialsProvider credentialsProvider = this.resolveCredentials(request);
      presigner.presignRequest(request, credentialsProvider.getCredentials(), expirationDate);
      return RuntimeHttpUtils.convertRequestToUrl(request, true, false);
   }

   private void addCustomQueryParams(Request<?> request) {
      Map<String, List<String>> queryParameters = request.getOriginalRequest().getCustomQueryParameters();
      if (queryParameters != null && !queryParameters.isEmpty()) {
         for(Entry<String, List<String>> param : queryParameters.entrySet()) {
            request.addParameters(param.getKey(), param.getValue());
         }
      }
   }

   private void addCustomHeaders(Request<?> request) {
      Map<String, String> headers = request.getOriginalRequest().getCustomRequestHeaders();
      if (headers != null && !headers.isEmpty()) {
         for(Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
         }
      }
   }

   private AWSCredentialsProvider resolveCredentials(Request<?> request) {
      return CredentialUtils.getCredentialsProvider(request.getOriginalRequest(), this.credentialsProvider);
   }

   public static class PresigningRequest extends AmazonWebServiceRequest {
      public PresignerFacade.PresigningRequest withRequestCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
         this.setRequestCredentialsProvider(credentialsProvider);
         return this;
      }
   }
}
