package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.SignableRequest;
import com.amazonaws.log.InternalLogApi;
import com.amazonaws.log.InternalLogFactory;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.StringUtils;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

public class AWS3Signer extends AbstractAWSSigner {
   private static final String AUTHORIZATION_HEADER = "X-Amzn-Authorization";
   private static final String NONCE_HEADER = "x-amz-nonce";
   private static final String HTTP_SCHEME = "AWS3";
   private static final String HTTPS_SCHEME = "AWS3-HTTPS";
   private String overriddenDate;
   @Deprecated
   protected static final DateUtils dateUtils = new DateUtils();
   private static final InternalLogApi log = InternalLogFactory.getLog(AWS3Signer.class);

   @Override
   public void sign(SignableRequest<?> request, AWSCredentials credentials) throws SdkClientException {
      if (!(credentials instanceof AnonymousAWSCredentials)) {
         AWSCredentials sanitizedCredentials = this.sanitizeCredentials(credentials);
         SigningAlgorithm algorithm = SigningAlgorithm.HmacSHA256;
         String nonce = UUID.randomUUID().toString();
         int timeOffset = request.getTimeOffset();
         Date dateValue = this.getSignatureDate(timeOffset);
         String date = DateUtils.formatRFC822Date(dateValue);
         boolean isHttps = false;
         if (this.overriddenDate != null) {
            date = this.overriddenDate;
         }

         request.addHeader("Date", date);
         request.addHeader("X-Amz-Date", date);
         String hostHeader = request.getEndpoint().getHost();
         if (SdkHttpUtils.isUsingNonDefaultPort(request.getEndpoint())) {
            hostHeader = hostHeader + ":" + request.getEndpoint().getPort();
         }

         request.addHeader("Host", hostHeader);
         if (sanitizedCredentials instanceof AWSSessionCredentials) {
            this.addSessionCredentials(request, (AWSSessionCredentials)sanitizedCredentials);
         }

         byte[] bytesToSign;
         String stringToSign;
         if (isHttps) {
            request.addHeader("x-amz-nonce", nonce);
            stringToSign = date + nonce;
            bytesToSign = stringToSign.getBytes(StringUtils.UTF8);
         } else {
            String path = SdkHttpUtils.appendUri(request.getEndpoint().getPath(), request.getResourcePath());
            stringToSign = request.getHttpMethod().toString()
               + "\n"
               + this.getCanonicalizedResourcePath(path)
               + "\n"
               + this.getCanonicalizedQueryString(request.getParameters())
               + "\n"
               + this.getCanonicalizedHeadersForStringToSign(request)
               + "\n"
               + this.getRequestPayloadWithoutQueryParams(request);
            bytesToSign = this.hash(stringToSign);
         }

         if (log.isDebugEnabled()) {
            log.debug("Calculated StringToSign: " + stringToSign);
         }

         String signature = this.signAndBase64Encode(bytesToSign, sanitizedCredentials.getAWSSecretKey(), algorithm);
         StringBuilder builder = new StringBuilder();
         builder.append(isHttps ? "AWS3-HTTPS" : "AWS3").append(" ");
         builder.append("AWSAccessKeyId=" + sanitizedCredentials.getAWSAccessKeyId() + ",");
         builder.append("Algorithm=" + algorithm.toString() + ",");
         if (!isHttps) {
            builder.append(this.getSignedHeadersComponent(request) + ",");
         }

         builder.append("Signature=" + signature);
         request.addHeader("X-Amzn-Authorization", builder.toString());
      }
   }

   private String getSignedHeadersComponent(SignableRequest<?> request) {
      StringBuilder builder = new StringBuilder();
      builder.append("SignedHeaders=");
      boolean first = true;

      for(String header : this.getHeadersForStringToSign(request)) {
         if (!first) {
            builder.append(";");
         }

         builder.append(header);
         first = false;
      }

      return builder.toString();
   }

   protected List<String> getHeadersForStringToSign(SignableRequest<?> request) {
      List<String> headersToSign = new ArrayList<>();

      for(Entry<String, String> entry : request.getHeaders().entrySet()) {
         String key = entry.getKey();
         String lowerCaseKey = StringUtils.lowerCase(key);
         if (lowerCaseKey.startsWith("x-amz") || lowerCaseKey.equals("host")) {
            headersToSign.add(key);
         }
      }

      Collections.sort(headersToSign);
      return headersToSign;
   }

   void overrideDate(String date) {
      this.overriddenDate = date;
   }

   protected String getCanonicalizedHeadersForStringToSign(SignableRequest<?> request) {
      List<String> headersToSign = this.getHeadersForStringToSign(request);

      for(int i = 0; i < headersToSign.size(); ++i) {
         headersToSign.set(i, StringUtils.lowerCase(headersToSign.get(i)));
      }

      SortedMap<String, String> sortedHeaderMap = new TreeMap<>();

      for(Entry<String, String> entry : request.getHeaders().entrySet()) {
         if (headersToSign.contains(StringUtils.lowerCase(entry.getKey()))) {
            sortedHeaderMap.put(StringUtils.lowerCase(entry.getKey()), entry.getValue());
         }
      }

      StringBuilder builder = new StringBuilder();

      for(Entry<String, String> entry : sortedHeaderMap.entrySet()) {
         builder.append(StringUtils.lowerCase(entry.getKey())).append(":").append(entry.getValue()).append("\n");
      }

      return builder.toString();
   }

   protected boolean shouldUseHttpsScheme(SignableRequest<?> request) throws SdkClientException {
      try {
         String protocol = StringUtils.lowerCase(request.getEndpoint().toURL().getProtocol());
         if (protocol.equals("http")) {
            return false;
         } else if (protocol.equals("https")) {
            return true;
         } else {
            throw new SdkClientException("Unknown request endpoint protocol encountered while signing request: " + protocol);
         }
      } catch (MalformedURLException var3) {
         throw new SdkClientException("Unable to parse request endpoint during signing", var3);
      }
   }

   @Override
   protected void addSessionCredentials(SignableRequest<?> request, AWSSessionCredentials credentials) {
      request.addHeader("x-amz-security-token", credentials.getSessionToken());
   }
}
