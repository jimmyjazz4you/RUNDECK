package com.amazonaws;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemDefaultDnsResolver implements DnsResolver {
   @Override
   public InetAddress[] resolve(String host) throws UnknownHostException {
      return InetAddress.getAllByName(host);
   }
}
