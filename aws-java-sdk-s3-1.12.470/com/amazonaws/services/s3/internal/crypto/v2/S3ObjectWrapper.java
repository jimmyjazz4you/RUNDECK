package com.amazonaws.services.s3.internal.crypto.v2;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.StringUtils;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

class S3ObjectWrapper implements Closeable {
   private final S3Object s3obj;
   private final S3ObjectId id;

   S3ObjectWrapper(S3Object s3obj, S3ObjectId id) {
      if (s3obj == null) {
         throw new IllegalArgumentException();
      } else {
         this.s3obj = s3obj;
         this.id = id;
      }
   }

   public S3ObjectId getS3ObjectId() {
      return this.id;
   }

   ObjectMetadata getObjectMetadata() {
      return this.s3obj.getObjectMetadata();
   }

   void setObjectMetadata(ObjectMetadata metadata) {
      this.s3obj.setObjectMetadata(metadata);
   }

   S3ObjectInputStream getObjectContent() {
      return this.s3obj.getObjectContent();
   }

   void setObjectContent(S3ObjectInputStream objectContent) {
      this.s3obj.setObjectContent(objectContent);
   }

   void setObjectContent(InputStream objectContent) {
      this.s3obj.setObjectContent(objectContent);
   }

   String getBucketName() {
      return this.s3obj.getBucketName();
   }

   void setBucketName(String bucketName) {
      this.s3obj.setBucketName(bucketName);
   }

   String getKey() {
      return this.s3obj.getKey();
   }

   void setKey(String key) {
      this.s3obj.setKey(key);
   }

   String getRedirectLocation() {
      return this.s3obj.getRedirectLocation();
   }

   void setRedirectLocation(String redirectLocation) {
      this.s3obj.setRedirectLocation(redirectLocation);
   }

   @Override
   public String toString() {
      return this.s3obj.toString();
   }

   final boolean hasEncryptionInfo() {
      ObjectMetadata metadata = this.s3obj.getObjectMetadata();
      Map<String, String> userMeta = metadata.getUserMetadata();
      return userMeta != null && userMeta.containsKey("x-amz-iv") && (userMeta.containsKey("x-amz-key-v2") || userMeta.containsKey("x-amz-key"));
   }

   String toJsonString() {
      try {
         return from(this.s3obj.getObjectContent());
      } catch (Exception var2) {
         throw new SdkClientException("Error parsing JSON: " + var2.getMessage());
      }
   }

   private static String from(InputStream is) throws IOException {
      if (is == null) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StringUtils.UTF8));

            String line;
            while((line = reader.readLine()) != null) {
               sb.append(line);
            }
         } finally {
            is.close();
         }

         return sb.toString();
      }
   }

   @Override
   public void close() throws IOException {
      this.s3obj.close();
   }

   S3Object getS3Object() {
      return this.s3obj;
   }

   ContentCryptoScheme encryptionSchemeOf(Map<String, String> instructionFile) {
      if (instructionFile != null) {
         String cekAlgo = instructionFile.get("x-amz-cek-alg");
         return ContentCryptoScheme.fromCEKAlgo(cekAlgo);
      } else {
         ObjectMetadata meta = this.s3obj.getObjectMetadata();
         Map<String, String> userMeta = meta.getUserMetadata();
         String cekAlgo = userMeta.get("x-amz-cek-alg");
         return ContentCryptoScheme.fromCEKAlgo(cekAlgo);
      }
   }
}
