package com.amazonaws.util;

import com.amazonaws.Request;
import java.net.URI;
import java.net.URISyntaxException;

public final class UriResourcePathUtils {
   public static String addStaticQueryParamtersToRequest(Request<?> request, String uriResourcePath) {
      if (request != null && uriResourcePath != null) {
         String resourcePath = uriResourcePath;
         int index = uriResourcePath.indexOf("?");
         if (index != -1) {
            String queryString = uriResourcePath.substring(index + 1);
            resourcePath = uriResourcePath.substring(0, index);

            for(String s : queryString.split("[;&]")) {
               index = s.indexOf("=");
               if (index != -1) {
                  request.addParameter(s.substring(0, index), s.substring(index + 1));
               } else {
                  request.addParameter(s, (String)null);
               }
            }
         }

         return resourcePath;
      } else {
         return null;
      }
   }

   public static URI updateUriHost(URI uri, String newHostPrefix) {
      try {
         return new URI(uri.getScheme(), uri.getUserInfo(), newHostPrefix + uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
      } catch (URISyntaxException var3) {
         throw new RuntimeException(var3);
      }
   }
}
