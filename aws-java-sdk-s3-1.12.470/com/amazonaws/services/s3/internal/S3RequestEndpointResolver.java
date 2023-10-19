package com.amazonaws.services.s3.internal;

import com.amazonaws.Request;
import com.amazonaws.SdkClientException;
import com.amazonaws.internal.ServiceEndpointBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.util.SdkHttpUtils;
import java.net.URI;
import java.net.URISyntaxException;

public class S3RequestEndpointResolver {
   private final ServiceEndpointBuilder endpointBuilder;
   private final boolean isPathStyleAccess;
   private final String bucketName;
   private final String key;

   public S3RequestEndpointResolver(ServiceEndpointBuilder endpointBuilder, boolean isPathStyleAccess, String bucketName, String key) {
      this.endpointBuilder = endpointBuilder;
      this.isPathStyleAccess = isPathStyleAccess;
      this.bucketName = bucketName;
      this.key = key;
   }

   static boolean isValidIpV4Address(String ipAddr) {
      if (ipAddr == null) {
         return false;
      } else {
         String[] tokens = ipAddr.split("\\.");
         if (tokens.length != 4) {
            return false;
         } else {
            for(String token : tokens) {
               try {
                  int tokenInt = Integer.parseInt(token);
                  if (tokenInt < 0 || tokenInt > 255) {
                     return false;
                  }
               } catch (NumberFormatException var7) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private static URI convertToVirtualHostEndpoint(URI endpoint, String bucketName) {
      try {
         return new URI(String.format("%s://%s.%s", endpoint.getScheme(), bucketName, endpoint.getAuthority()));
      } catch (URISyntaxException var3) {
         throw new IllegalArgumentException("Invalid bucket name: " + bucketName, var3);
      }
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void resolveRequestEndpoint(Request<?> request) {
      this.resolveRequestEndpoint(request, null);
   }

   public void resolveRequestEndpoint(Request<?> request, String regionString) {
      if (regionString != null) {
         Region r = RegionUtils.getRegion(regionString);
         if (r == null) {
            throw new SdkClientException("Not able to determine region for " + regionString + ".Please upgrade to a newer version of the SDK");
         }

         this.endpointBuilder.withRegion(r);
      }

      URI endpoint = this.endpointBuilder.getServiceEndpoint();
      if (endpoint.getHost() == null) {
         throw new IllegalArgumentException("Endpoint does not contain a valid host name: " + request.getEndpoint());
      } else {
         if (this.shouldUseVirtualAddressing(endpoint)) {
            request.setEndpoint(convertToVirtualHostEndpoint(endpoint, this.bucketName));
            request.setResourcePath(SdkHttpUtils.urlEncode(this.getHostStyleResourcePath(), true));
         } else {
            request.setEndpoint(endpoint);
            request.setResourcePath(this.getPathStyleResourcePath());
         }
      }
   }

   private boolean shouldUseVirtualAddressing(URI endpoint) {
      return !this.isPathStyleAccess && BucketNameUtils.isDNSBucketName(this.bucketName) && !isValidIpV4Address(endpoint.getHost());
   }

   private String getHostStyleResourcePath() {
      return this.keyForBaseOfPath();
   }

   private String getPathStyleResourcePath() {
      if (this.bucketName == null) {
         return SdkHttpUtils.urlEncode(this.keyForBaseOfPath(), true);
      } else {
         String encodedBucketName = SdkHttpUtils.urlEncode(this.bucketName, false);
         return encodedBucketName + "/" + SdkHttpUtils.urlEncode(this.key == null ? "" : this.key, true);
      }
   }

   private String keyForBaseOfPath() {
      if (this.key == null) {
         return "";
      } else {
         return this.key.startsWith("/") ? "/" + this.key : this.key;
      }
   }
}
