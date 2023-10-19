package com.amazonaws.auth;

import com.amazonaws.SignableRequest;
import com.amazonaws.annotation.SdkTestInternalApi;

public class AWS4UnsignedPayloadSigner extends AWS4Signer {
   public AWS4UnsignedPayloadSigner() {
   }

   @SdkTestInternalApi
   public AWS4UnsignedPayloadSigner(SdkClock clock) {
      super(clock);
   }

   @Override
   public void sign(SignableRequest<?> request, AWSCredentials credentials) {
      request.getHeaders().put("x-amz-content-sha256", "required");
      super.sign(request, credentials);
   }

   @Override
   protected String calculateContentHash(SignableRequest<?> request) {
      return "https".equals(request.getEndpoint().getScheme()) ? "UNSIGNED-PAYLOAD" : super.calculateContentHash(request);
   }
}
