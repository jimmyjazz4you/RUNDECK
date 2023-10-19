package com.amazonaws.http.apache;

import com.amazonaws.Protocol;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.protocol.HttpContext;

@SdkInternalApi
public class SdkProxyRoutePlanner extends DefaultRoutePlanner {
   private HttpHost proxy;
   private String[] hostPatterns;

   public SdkProxyRoutePlanner(String proxyHost, int proxyPort, Protocol proxyProtocol, String nonProxyHosts) {
      super(DefaultSchemePortResolver.INSTANCE);
      this.proxy = new HttpHost(proxyHost, proxyPort, proxyProtocol.toString());
      this.parseNonProxyHosts(nonProxyHosts);
   }

   private void parseNonProxyHosts(String nonProxyHosts) {
      if (!StringUtils.isNullOrEmpty(nonProxyHosts)) {
         String[] hosts = nonProxyHosts.split("\\|");
         this.hostPatterns = new String[hosts.length];

         for(int i = 0; i < hosts.length; ++i) {
            this.hostPatterns[i] = hosts[i].toLowerCase().replace("*", ".*?");
         }
      }
   }

   boolean doesTargetMatchNonProxyHosts(HttpHost target) {
      if (this.hostPatterns == null) {
         return false;
      } else {
         String targetHost = target.getHostName().toLowerCase();

         for(String pattern : this.hostPatterns) {
            if (targetHost.matches(pattern)) {
               return true;
            }
         }

         return false;
      }
   }

   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      return this.doesTargetMatchNonProxyHosts(target) ? null : this.proxy;
   }
}
