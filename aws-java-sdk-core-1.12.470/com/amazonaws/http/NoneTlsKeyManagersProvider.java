package com.amazonaws.http;

import javax.net.ssl.KeyManager;

public class NoneTlsKeyManagersProvider implements TlsKeyManagersProvider {
   private static final NoneTlsKeyManagersProvider INSTANCE = new NoneTlsKeyManagersProvider();

   private NoneTlsKeyManagersProvider() {
   }

   @Override
   public KeyManager[] getKeyManagers() {
      return null;
   }

   public static NoneTlsKeyManagersProvider getInstance() {
      return INSTANCE;
   }
}
