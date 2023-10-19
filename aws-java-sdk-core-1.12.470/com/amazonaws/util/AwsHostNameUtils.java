package com.amazonaws.util;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.internal.config.HostRegexToRegionMapping;
import com.amazonaws.internal.config.InternalConfig;
import com.amazonaws.log.InternalLogFactory;
import java.net.InetAddress;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwsHostNameUtils {
   private static final Pattern S3_ENDPOINT_PATTERN = Pattern.compile("^(?:.+\\.)?s3[.-]([a-z0-9-]+)$");
   private static final Pattern STANDARD_CLOUDSEARCH_ENDPOINT_PATTERN = Pattern.compile("^(?:.+\\.)?([a-z0-9-]+)\\.cloudsearch$");
   private static final Pattern EXTENDED_CLOUDSEARCH_ENDPOINT_PATTERN = Pattern.compile("^(?:.+\\.)?([a-z0-9-]+)\\.cloudsearch\\..+");

   @Deprecated
   public static String parseRegionName(URI endpoint) {
      return parseRegionName(endpoint.getHost(), null);
   }

   @Deprecated
   public static String parseRegionName(String host, String serviceHint) {
      String region = parseRegion(host, serviceHint);
      return region == null ? "us-east-1" : region;
   }

   public static String parseRegion(String host, String serviceHint) {
      String result = parseRegionFromInternalConfig(host);
      if (result != null) {
         return result;
      } else {
         result = parseRegionFromAwsPartitionPattern(host);
         if (result != null) {
            return result;
         } else {
            result = parseRegionUsingServiceHint(host, serviceHint);
            return result != null ? result : parseRegionFromAfterServiceName(host, serviceHint);
         }
      }
   }

   @SdkProtectedApi
   public static String parseRegionFromInternalConfig(String host) {
      validateHostname(host);
      InternalConfig internConfig = InternalConfig.Factory.getInternalConfig();

      for(HostRegexToRegionMapping mapping : internConfig.getHostRegexToRegionMappings()) {
         if (mapping.isHostNameMatching(host)) {
            return mapping.getRegionName();
         }
      }

      return null;
   }

   @SdkProtectedApi
   public static String parseRegionFromAwsPartitionPattern(String host) {
      validateHostname(host);
      if (host.endsWith(".amazonaws.com")) {
         int index = host.length() - ".amazonaws.com".length();
         return parseStandardRegionName(host.substring(0, index));
      } else {
         return null;
      }
   }

   @SdkProtectedApi
   public static String parseRegionUsingServiceHint(String host, String serviceHint) {
      validateHostname(host);
      if (serviceHint != null && serviceHint.equals("cloudsearch") && !host.startsWith("cloudsearch.")) {
         Matcher matcher = EXTENDED_CLOUDSEARCH_ENDPOINT_PATTERN.matcher(host);
         if (matcher.matches()) {
            return matcher.group(1);
         }
      }

      return null;
   }

   public static String parseRegionFromAfterServiceName(String host, String serviceHint) {
      validateHostname(host);
      if (serviceHint != null) {
         Pattern pattern = Pattern.compile("^(?:.+\\.)?" + Pattern.quote(serviceHint) + "[.-]([a-z0-9-]+)\\.");
         Matcher matcher = pattern.matcher(host);
         if (matcher.find()) {
            return matcher.group(1);
         }
      }

      return null;
   }

   private static void validateHostname(String hostname) {
      if (hostname == null) {
         throw new IllegalArgumentException("hostname cannot be null");
      }
   }

   private static String parseStandardRegionName(String fragment) {
      Matcher matcher = S3_ENDPOINT_PATTERN.matcher(fragment);
      if (matcher.matches()) {
         return matcher.group(1);
      } else {
         matcher = STANDARD_CLOUDSEARCH_ENDPOINT_PATTERN.matcher(fragment);
         if (matcher.matches()) {
            return matcher.group(1);
         } else {
            int index = fragment.lastIndexOf(46);
            if (index == -1) {
               return "us-east-1";
            } else {
               String region = fragment.substring(index + 1);
               if ("us-gov".equals(region)) {
                  region = "us-gov-west-1";
               }

               return region;
            }
         }
      }
   }

   @Deprecated
   public static String parseServiceName(URI endpoint) {
      String host = endpoint.getHost();
      if (!host.endsWith(".amazonaws.com")) {
         throw new IllegalArgumentException("Cannot parse a service name from an unrecognized endpoint (" + host + ").");
      } else {
         String serviceAndRegion = host.substring(0, host.indexOf(".amazonaws.com"));
         if (!serviceAndRegion.endsWith(".s3") && !S3_ENDPOINT_PATTERN.matcher(serviceAndRegion).matches()) {
            char separator = '.';
            return serviceAndRegion.indexOf(separator) == -1 ? serviceAndRegion : serviceAndRegion.substring(0, serviceAndRegion.indexOf(separator));
         } else {
            return "s3";
         }
      }
   }

   public static String localHostName() {
      try {
         InetAddress localhost = InetAddress.getLocalHost();
         return localhost.getHostName();
      } catch (Exception var1) {
         InternalLogFactory.getLog(AwsHostNameUtils.class).debug("Failed to determine the local hostname; fall back to use \"localhost\".", var1);
         return "localhost";
      }
   }
}
