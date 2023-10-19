package com.amazonaws;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface DnsResolver {
   InetAddress[] resolve(String var1) throws UnknownHostException;
}
