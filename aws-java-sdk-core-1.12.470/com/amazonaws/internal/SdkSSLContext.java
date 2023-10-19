package com.amazonaws.internal;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLInitializationException;

public class SdkSSLContext {
   public static final SSLContext getPreferredSSLContext(SecureRandom secureRandom) {
      return getPreferredSSLContext(null, secureRandom);
   }

   public static final SSLContext getPreferredSSLContext(KeyManager[] keyManagers, SecureRandom secureRandom) {
      try {
         SSLContext sslcontext = SSLContext.getInstance("TLS");
         sslcontext.init(keyManagers, null, secureRandom);
         return sslcontext;
      } catch (NoSuchAlgorithmException var3) {
         throw new SSLInitializationException(var3.getMessage(), var3);
      } catch (KeyManagementException var4) {
         throw new SSLInitializationException(var4.getMessage(), var4);
      }
   }
}
