package com.amazonaws.services.s3.internal;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.SdkClientException;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.services.s3.S3ResponseMetadata;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.StringUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractS3ResponseHandler<T> implements HttpResponseHandler<AmazonWebServiceResponse<T>> {
   private static final Log log = LogFactory.getLog(S3MetadataResponseHandler.class);
   private static final Set<String> ignoredHeaders = new HashSet<>();

   public boolean needsConnectionLeftOpen() {
      return false;
   }

   protected AmazonWebServiceResponse<T> parseResponseMetadata(HttpResponse response) {
      AmazonWebServiceResponse<T> awsResponse = new AmazonWebServiceResponse();
      String awsRequestId = (String)response.getHeaders().get("x-amz-request-id");
      String hostId = (String)response.getHeaders().get("x-amz-id-2");
      String cloudFrontId = (String)response.getHeaders().get("X-Amz-Cf-Id");
      Map<String, String> metadataMap = new HashMap<>();
      metadataMap.put("AWS_REQUEST_ID", awsRequestId);
      metadataMap.put("HOST_ID", hostId);
      metadataMap.put("CLOUD_FRONT_ID", cloudFrontId);
      awsResponse.setResponseMetadata(new S3ResponseMetadata(metadataMap));
      return awsResponse;
   }

   protected void populateObjectMetadata(HttpResponse response, ObjectMetadata metadata) {
      for(Entry<String, String> header : response.getHeaders().entrySet()) {
         String key = header.getKey();
         if (StringUtils.beginsWithIgnoreCase(key, "x-amz-meta-")) {
            key = key.substring("x-amz-meta-".length());
            metadata.addUserMetadata(key, header.getValue());
         } else if (!ignoredHeaders.contains(key)) {
            if (key.equalsIgnoreCase("Last-Modified")) {
               try {
                  metadata.setHeader(key, ServiceUtils.parseRfc822Date(header.getValue()));
               } catch (Exception var10) {
                  log.warn("Unable to parse last modified date: " + (String)header.getValue(), var10);
               }
            } else if (key.equalsIgnoreCase("Content-Length")) {
               try {
                  metadata.setHeader(key, Long.parseLong(header.getValue()));
               } catch (NumberFormatException var9) {
                  throw new SdkClientException("Unable to parse content length. Header 'Content-Length' has corrupted data" + var9.getMessage(), var9);
               }
            } else if (key.equalsIgnoreCase("ETag")) {
               metadata.setHeader(key, ServiceUtils.removeQuotes(header.getValue()));
            } else if (key.equalsIgnoreCase("Expires")) {
               metadata.setHeader("Expires", header.getValue());

               try {
                  metadata.setHttpExpiresDate(DateUtils.parseRFC822Date(header.getValue()));
               } catch (Exception var8) {
                  log.warn("Unable to parse http expiration date: " + (String)header.getValue(), var8);
               }
            } else if (key.equalsIgnoreCase("x-amz-expiration")) {
               new ObjectExpirationHeaderHandler<ObjectMetadata>().handle(metadata, response);
            } else if (key.equalsIgnoreCase("x-amz-restore")) {
               new ObjectRestoreHeaderHandler<ObjectMetadata>().handle(metadata, response);
            } else if (key.equalsIgnoreCase("x-amz-request-charged")) {
               new S3RequesterChargedHeaderHandler<ObjectMetadata>().handle(metadata, response);
            } else if (key.equalsIgnoreCase("x-amz-mp-parts-count")) {
               try {
                  metadata.setHeader(key, Integer.parseInt(header.getValue()));
               } catch (NumberFormatException var7) {
                  throw new SdkClientException("Unable to parse part count. Header x-amz-mp-parts-count has corrupted data" + var7.getMessage(), var7);
               }
            } else if (key.equalsIgnoreCase("x-amz-server-side-encryption-bucket-key-enabled")) {
               metadata.setBucketKeyEnabled("true".equals(header.getValue()));
            } else {
               metadata.setHeader(key, header.getValue());
            }
         }
      }
   }

   static {
      ignoredHeaders.add("Date");
      ignoredHeaders.add("Server");
      ignoredHeaders.add("x-amz-request-id");
      ignoredHeaders.add("x-amz-id-2");
      ignoredHeaders.add("X-Amz-Cf-Id");
      ignoredHeaders.add("Connection");
   }
}
