package com.amazonaws.auth;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ReadLimitInfo;
import com.amazonaws.SDKGlobalTime;
import com.amazonaws.SdkClientException;
import com.amazonaws.SignableRequest;
import com.amazonaws.internal.SdkDigestInputStream;
import com.amazonaws.internal.SdkThreadLocalsRegistry;
import com.amazonaws.util.Base64;
import com.amazonaws.util.BinaryUtils;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractAWSSigner implements Signer {
   public static final String EMPTY_STRING_SHA256_HEX = BinaryUtils.toHex(doHash(""));
   private static final ThreadLocal<MessageDigest> SHA256_MESSAGE_DIGEST = SdkThreadLocalsRegistry.register(new ThreadLocal<MessageDigest>() {
      protected MessageDigest initialValue() {
         try {
            return MessageDigest.getInstance("SHA-256");
         } catch (NoSuchAlgorithmException var2) {
            throw new SdkClientException("Unable to get SHA256 Function" + var2.getMessage(), var2);
         }
      }
   });

   protected String signAndBase64Encode(String data, String key, SigningAlgorithm algorithm) throws SdkClientException {
      return this.signAndBase64Encode(data.getBytes(StringUtils.UTF8), key, algorithm);
   }

   protected String signAndBase64Encode(byte[] data, String key, SigningAlgorithm algorithm) throws SdkClientException {
      try {
         byte[] signature = this.sign(data, key.getBytes(StringUtils.UTF8), algorithm);
         return Base64.encodeAsString(signature);
      } catch (Exception var5) {
         throw new SdkClientException("Unable to calculate a request signature: " + var5.getMessage(), var5);
      }
   }

   public byte[] sign(String stringData, byte[] key, SigningAlgorithm algorithm) throws SdkClientException {
      try {
         byte[] data = stringData.getBytes(StringUtils.UTF8);
         return this.sign(data, key, algorithm);
      } catch (Exception var5) {
         throw new SdkClientException("Unable to calculate a request signature: " + var5.getMessage(), var5);
      }
   }

   public byte[] signWithMac(String stringData, Mac mac) {
      try {
         return mac.doFinal(stringData.getBytes(StringUtils.UTF8));
      } catch (Exception var4) {
         throw new SdkClientException("Unable to calculate a request signature: " + var4.getMessage(), var4);
      }
   }

   protected byte[] sign(byte[] data, byte[] key, SigningAlgorithm algorithm) throws SdkClientException {
      try {
         Mac mac = algorithm.getMac();
         mac.init(new SecretKeySpec(key, algorithm.toString()));
         return mac.doFinal(data);
      } catch (Exception var5) {
         throw new SdkClientException("Unable to calculate a request signature: " + var5.getMessage(), var5);
      }
   }

   public byte[] hash(String text) throws SdkClientException {
      return doHash(text);
   }

   private static byte[] doHash(String text) throws SdkClientException {
      try {
         MessageDigest md = getMessageDigestInstance();
         md.update(text.getBytes(StringUtils.UTF8));
         return md.digest();
      } catch (Exception var2) {
         throw new SdkClientException("Unable to compute hash while signing request: " + var2.getMessage(), var2);
      }
   }

   protected byte[] hash(InputStream input) throws SdkClientException {
      try {
         MessageDigest md = getMessageDigestInstance();
         DigestInputStream digestInputStream = new SdkDigestInputStream(input, md);
         byte[] buffer = new byte[1024];

         while(digestInputStream.read(buffer) > -1) {
         }

         return digestInputStream.getMessageDigest().digest();
      } catch (Exception var5) {
         throw new SdkClientException("Unable to compute hash while signing request: " + var5.getMessage(), var5);
      }
   }

   public byte[] hash(byte[] data) throws SdkClientException {
      try {
         MessageDigest md = getMessageDigestInstance();
         md.update(data);
         return md.digest();
      } catch (Exception var3) {
         throw new SdkClientException("Unable to compute hash while signing request: " + var3.getMessage(), var3);
      }
   }

   protected String getCanonicalizedQueryString(Map<String, List<String>> parameters) {
      SortedMap<String, List<String>> sorted = new TreeMap<>();

      for(Entry<String, List<String>> entry : parameters.entrySet()) {
         String encodedParamName = SdkHttpUtils.urlEncode(entry.getKey(), false);
         List<String> paramValues = entry.getValue();
         List<String> encodedValues = new ArrayList<>(paramValues.size());

         for(String value : paramValues) {
            encodedValues.add(SdkHttpUtils.urlEncode(value, false));
         }

         Collections.sort(encodedValues);
         sorted.put(encodedParamName, encodedValues);
      }

      StringBuilder result = new StringBuilder();

      for(Entry<String, List<String>> entry : sorted.entrySet()) {
         for(String value : entry.getValue()) {
            if (result.length() > 0) {
               result.append("&");
            }

            result.append(entry.getKey()).append("=").append(value);
         }
      }

      return result.toString();
   }

   protected String getCanonicalizedQueryString(SignableRequest<?> request) {
      return SdkHttpUtils.usePayloadForQueryParameters(request) ? "" : this.getCanonicalizedQueryString(request.getParameters());
   }

   protected byte[] getBinaryRequestPayload(SignableRequest<?> request) {
      if (SdkHttpUtils.usePayloadForQueryParameters(request)) {
         String encodedParameters = SdkHttpUtils.encodeParameters(request);
         return encodedParameters == null ? new byte[0] : encodedParameters.getBytes(StringUtils.UTF8);
      } else {
         return this.getBinaryRequestPayloadWithoutQueryParams(request);
      }
   }

   protected String getRequestPayload(SignableRequest<?> request) {
      return this.newString(this.getBinaryRequestPayload(request));
   }

   protected String getRequestPayloadWithoutQueryParams(SignableRequest<?> request) {
      return this.newString(this.getBinaryRequestPayloadWithoutQueryParams(request));
   }

   protected byte[] getBinaryRequestPayloadWithoutQueryParams(SignableRequest<?> request) {
      InputStream content = this.getBinaryRequestPayloadStreamWithoutQueryParams(request);

      try {
         ReadLimitInfo info = request.getReadLimitInfo();
         content.mark(info == null ? -1 : info.getReadLimit());
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         byte[] buffer = new byte[5120];

         while(true) {
            int bytesRead = content.read(buffer);
            if (bytesRead == -1) {
               byteArrayOutputStream.close();
               content.reset();
               return byteArrayOutputStream.toByteArray();
            }

            byteArrayOutputStream.write(buffer, 0, bytesRead);
         }
      } catch (Exception var7) {
         throw new SdkClientException("Unable to read request payload to sign request: " + var7.getMessage(), var7);
      }
   }

   protected InputStream getBinaryRequestPayloadStream(SignableRequest<?> request) {
      if (SdkHttpUtils.usePayloadForQueryParameters(request)) {
         String encodedParameters = SdkHttpUtils.encodeParameters(request);
         return encodedParameters == null ? new ByteArrayInputStream(new byte[0]) : new ByteArrayInputStream(encodedParameters.getBytes(StringUtils.UTF8));
      } else {
         return this.getBinaryRequestPayloadStreamWithoutQueryParams(request);
      }
   }

   protected InputStream getBinaryRequestPayloadStreamWithoutQueryParams(SignableRequest<?> request) {
      try {
         InputStream is = request.getContentUnwrapped();
         if (is == null) {
            return new ByteArrayInputStream(new byte[0]);
         } else if (!is.markSupported()) {
            throw new SdkClientException("Unable to read request payload to sign request.");
         } else {
            return is;
         }
      } catch (AmazonClientException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new SdkClientException("Unable to read request payload to sign request: " + var4.getMessage(), var4);
      }
   }

   protected String getCanonicalizedResourcePath(String resourcePath) {
      return this.getCanonicalizedResourcePath(resourcePath, true);
   }

   protected String getCanonicalizedResourcePath(String resourcePath, boolean urlEncode) {
      String value = resourcePath;
      if (urlEncode) {
         value = SdkHttpUtils.urlEncode(resourcePath, true);
         URI normalize = URI.create(value).normalize();
         value = normalize.getRawPath();
         if (!resourcePath.endsWith("/") && value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
         }
      }

      if (!value.startsWith("/")) {
         value = "/" + value;
      }

      return value;
   }

   protected String getCanonicalizedEndpoint(URI endpoint) {
      String endpointForStringToSign = StringUtils.lowerCase(endpoint.getHost());
      if (SdkHttpUtils.isUsingNonDefaultPort(endpoint)) {
         endpointForStringToSign = endpointForStringToSign + ":" + endpoint.getPort();
      }

      return endpointForStringToSign;
   }

   protected AWSCredentials sanitizeCredentials(AWSCredentials credentials) {
      String accessKeyId = null;
      String secretKey = null;
      String token = null;
      synchronized(credentials) {
         accessKeyId = credentials.getAWSAccessKeyId();
         secretKey = credentials.getAWSSecretKey();
         if (credentials instanceof AWSSessionCredentials) {
            token = ((AWSSessionCredentials)credentials).getSessionToken();
         }
      }

      if (secretKey != null) {
         secretKey = secretKey.trim();
      }

      if (accessKeyId != null) {
         accessKeyId = accessKeyId.trim();
      }

      if (token != null) {
         token = token.trim();
      }

      return (AWSCredentials)(credentials instanceof AWSSessionCredentials
         ? new BasicSessionCredentials(accessKeyId, secretKey, token)
         : new BasicAWSCredentials(accessKeyId, secretKey));
   }

   protected String newString(byte[] bytes) {
      return new String(bytes, StringUtils.UTF8);
   }

   protected Date getSignatureDate(int offsetInSeconds) {
      return new Date(System.currentTimeMillis() - (long)(offsetInSeconds * 1000));
   }

   @Deprecated
   protected int getTimeOffset(SignableRequest<?> request) {
      int globleOffset = SDKGlobalTime.getGlobalTimeOffset();
      return globleOffset == 0 ? request.getTimeOffset() : globleOffset;
   }

   protected abstract void addSessionCredentials(SignableRequest<?> var1, AWSSessionCredentials var2);

   private static MessageDigest getMessageDigestInstance() {
      MessageDigest messageDigest = SHA256_MESSAGE_DIGEST.get();
      messageDigest.reset();
      return messageDigest;
   }
}
