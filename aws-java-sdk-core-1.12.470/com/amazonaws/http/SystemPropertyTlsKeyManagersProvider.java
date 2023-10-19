package com.amazonaws.http;

import java.io.File;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SystemPropertyTlsKeyManagersProvider extends AbstractFileTlsKeyManagersProvider {
   private static final Log log = LogFactory.getLog(SystemPropertyTlsKeyManagersProvider.class);
   private static final String KEY_STORE_PROPERTY = "javax.net.ssl.keyStore";
   private static final String KEY_STORE_PASSWORD_PROPERTY = "javax.net.ssl.keyStorePassword";
   private static final String KEY_STORE_TYPE_PROPERTY = "javax.net.ssl.keyStoreType";

   @Override
   public KeyManager[] getKeyManagers() {
      String keyStorePath = getKeyStore();
      if (keyStorePath == null) {
         return null;
      } else {
         String type = getKeyStoreType();
         String password = getKeyStorePassword();
         char[] passwordChars = null;
         if (password != null) {
            passwordChars = password.toCharArray();
         }

         try {
            return this.createKeyManagers(new File(keyStorePath), type, passwordChars);
         } catch (Exception var6) {
            log.warn("Unable to load KeyManager from system properties", var6);
            return null;
         }
      }
   }

   private static String getKeyStore() {
      return System.getProperty("javax.net.ssl.keyStore");
   }

   private static String getKeyStoreType() {
      return System.getProperty("javax.net.ssl.keyStoreType", KeyStore.getDefaultType());
   }

   private static String getKeyStorePassword() {
      return System.getProperty("javax.net.ssl.keyStorePassword");
   }
}
