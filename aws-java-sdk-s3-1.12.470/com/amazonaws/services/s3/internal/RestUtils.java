package com.amazonaws.services.s3.internal;

import com.amazonaws.SignableRequest;
import com.amazonaws.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class RestUtils {
   private static final List<String> SIGNED_PARAMETERS = Arrays.asList(
      "acl",
      "torrent",
      "logging",
      "location",
      "policy",
      "requestPayment",
      "versioning",
      "versions",
      "versionId",
      "notification",
      "uploadId",
      "uploads",
      "partNumber",
      "website",
      "delete",
      "lifecycle",
      "tagging",
      "cors",
      "restore",
      "replication",
      "accelerate",
      "inventory",
      "analytics",
      "metrics",
      "response-cache-control",
      "response-content-disposition",
      "response-content-encoding",
      "response-content-language",
      "response-content-type",
      "response-expires"
   );

   public static <T> String makeS3CanonicalString(String method, String resource, SignableRequest<T> request, String expires) {
      return makeS3CanonicalString(method, resource, request, expires, null);
   }

   public static <T> String makeS3CanonicalString(
      String method, String resource, SignableRequest<T> request, String expires, Collection<String> additionalQueryParamsToSign
   ) {
      StringBuilder buf = new StringBuilder();
      buf.append(method + "\n");
      Map<String, String> headersMap = request.getHeaders();
      SortedMap<String, String> interestingHeaders = new TreeMap<>();
      if (headersMap != null && headersMap.size() > 0) {
         for(Entry<String, String> entry : headersMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
               String lk = StringUtils.lowerCase(key);
               if (lk.equals("content-type") || lk.equals("content-md5") || lk.equals("date") || lk.startsWith("x-amz-")) {
                  interestingHeaders.put(lk, value);
               }
            }
         }
      }

      if (interestingHeaders.containsKey("x-amz-date")) {
         interestingHeaders.put("date", "");
      }

      if (expires != null) {
         interestingHeaders.put("date", expires);
      }

      if (!interestingHeaders.containsKey("content-type")) {
         interestingHeaders.put("content-type", "");
      }

      if (!interestingHeaders.containsKey("content-md5")) {
         interestingHeaders.put("content-md5", "");
      }

      Map<String, List<String>> requestParameters = request.getParameters();

      for(Entry<String, List<String>> parameter : requestParameters.entrySet()) {
         if (parameter.getKey().startsWith("x-amz-")) {
            StringBuilder parameterValueBuilder = new StringBuilder();

            for(String value : parameter.getValue()) {
               if (parameterValueBuilder.length() > 0) {
                  parameterValueBuilder.append(",");
               }

               parameterValueBuilder.append(value);
            }

            interestingHeaders.put(parameter.getKey(), parameterValueBuilder.toString());
         }
      }

      for(Entry<String, String> entry : interestingHeaders.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         if (key.startsWith("x-amz-")) {
            buf.append(key).append(':');
            if (value != null) {
               buf.append(value);
            }
         } else if (value != null) {
            buf.append(value);
         }

         buf.append("\n");
      }

      buf.append(resource);
      String[] parameterNames = requestParameters.keySet().toArray(new String[request.getParameters().size()]);
      Arrays.sort((Object[])parameterNames);
      StringBuilder queryParams = new StringBuilder();

      for(String parameterName : parameterNames) {
         if (SIGNED_PARAMETERS.contains(parameterName) || additionalQueryParamsToSign != null && additionalQueryParamsToSign.contains(parameterName)) {
            for(String value : requestParameters.get(parameterName)) {
               queryParams = queryParams.length() > 0 ? queryParams.append("&") : queryParams.append("?");
               queryParams.append(parameterName);
               if (value != null) {
                  queryParams.append("=").append(value);
               }
            }
         }
      }

      buf.append(queryParams.toString());
      return buf.toString();
   }
}
