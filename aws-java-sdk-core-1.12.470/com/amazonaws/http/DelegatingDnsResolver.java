package com.amazonaws.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.http.conn.DnsResolver;

public class DelegatingDnsResolver implements DnsResolver {
   private final com.amazonaws.DnsResolver delegate;

   public DelegatingDnsResolver(com.amazonaws.DnsResolver delegate) {
      this.delegate = delegate;
   }

   public InetAddress[] resolve(String host) throws UnknownHostException {
      return this.delegate.resolve(host);
   }
}
