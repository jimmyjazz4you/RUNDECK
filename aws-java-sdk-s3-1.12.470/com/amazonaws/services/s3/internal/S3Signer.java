package com.amazonaws.services.s3.internal;

import com.amazonaws.SignableRequest;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AbstractAWSSigner;
import com.amazonaws.auth.SigningAlgorithm;
import com.amazonaws.util.SdkHttpUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S3Signer extends AbstractAWSSigner {
   private static final Log log = LogFactory.getLog(S3Signer.class);
   private final String httpVerb;
   private final String resourcePath;
   private final Set<String> additionalQueryParamsToSign;

   public S3Signer() {
      this.httpVerb = null;
      this.resourcePath = null;
      this.additionalQueryParamsToSign = null;
   }

   public S3Signer(String httpVerb, String resourcePath) {
      this(httpVerb, resourcePath, null);
   }

   public S3Signer(String httpVerb, String resourcePath, Collection<String> additionalQueryParamsToSign) {
      if (resourcePath == null) {
         throw new IllegalArgumentException("Parameter resourcePath is empty");
      } else {
         this.httpVerb = httpVerb;
         this.resourcePath = resourcePath;
         this.additionalQueryParamsToSign = additionalQueryParamsToSign == null
            ? null
            : Collections.unmodifiableSet(new HashSet<>(additionalQueryParamsToSign));
      }
   }

   public void sign(SignableRequest<?> request, AWSCredentials credentials) {
      if (this.resourcePath == null) {
         throw new UnsupportedOperationException("Cannot sign a request using a dummy S3Signer instance with no resource path");
      } else if (credentials != null && credentials.getAWSSecretKey() != null) {
         AWSCredentials sanitizedCredentials = this.sanitizeCredentials(credentials);
         if (sanitizedCredentials instanceof AWSSessionCredentials) {
            this.addSessionCredentials(request, (AWSSessionCredentials)sanitizedCredentials);
         }

         String encodedResourcePath = SdkHttpUtils.appendUri(request.getEndpoint().getPath(), SdkHttpUtils.urlEncode(this.resourcePath, true), true);
         int timeOffset = request.getTimeOffset();
         Date date = this.getSignatureDate(timeOffset);
         request.addHeader("Date", ServiceUtils.formatRfc822Date(date));
         String canonicalString = RestUtils.makeS3CanonicalString(this.httpVerb, encodedResourcePath, request, null, this.additionalQueryParamsToSign);
         log.debug("Calculated string to sign:\n\"" + canonicalString + "\"");
         String signature = super.signAndBase64Encode(canonicalString, sanitizedCredentials.getAWSSecretKey(), SigningAlgorithm.HmacSHA1);
         request.addHeader("Authorization", "AWS " + sanitizedCredentials.getAWSAccessKeyId() + ":" + signature);
      } else {
         log.debug("Canonical string will not be signed, as no AWS Secret Key was provided");
      }
   }

   protected void addSessionCredentials(SignableRequest<?> request, AWSSessionCredentials credentials) {
      request.addHeader("x-amz-security-token", credentials.getSessionToken());
   }
}
