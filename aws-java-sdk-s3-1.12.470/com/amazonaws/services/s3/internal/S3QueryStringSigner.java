package com.amazonaws.services.s3.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.SignableRequest;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AbstractAWSSigner;
import com.amazonaws.auth.SigningAlgorithm;
import java.util.Date;

public class S3QueryStringSigner extends AbstractAWSSigner {
   private final String httpVerb;
   private final String resourcePath;
   private final Date expiration;

   public S3QueryStringSigner(String httpVerb, String resourcePath, Date expiration) {
      this.httpVerb = httpVerb;
      this.resourcePath = resourcePath;
      this.expiration = expiration;
      if (resourcePath == null) {
         throw new IllegalArgumentException("Parameter resourcePath is empty");
      }
   }

   public void sign(SignableRequest<?> request, AWSCredentials credentials) throws SdkClientException {
      AWSCredentials sanitizedCredentials = this.sanitizeCredentials(credentials);
      if (sanitizedCredentials instanceof AWSSessionCredentials) {
         this.addSessionCredentials(request, (AWSSessionCredentials)sanitizedCredentials);
      }

      String expirationInSeconds = Long.toString(this.expiration.getTime() / 1000L);
      String canonicalString = RestUtils.makeS3CanonicalString(this.httpVerb, this.resourcePath, request, expirationInSeconds);
      String signature = super.signAndBase64Encode(canonicalString, sanitizedCredentials.getAWSSecretKey(), SigningAlgorithm.HmacSHA1);
      request.addParameter("AWSAccessKeyId", sanitizedCredentials.getAWSAccessKeyId());
      request.addParameter("Expires", expirationInSeconds);
      request.addParameter("Signature", signature);
   }

   protected void addSessionCredentials(SignableRequest<?> request, AWSSessionCredentials credentials) {
      request.addParameter("x-amz-security-token", credentials.getSessionToken());
   }
}
