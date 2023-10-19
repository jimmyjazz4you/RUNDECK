package com.amazonaws.services.s3.internal.crypto;

import java.security.Provider;
import java.security.Security;
import javax.crypto.Cipher;
import org.apache.commons.logging.LogFactory;

public class CryptoRuntime {
   public static final String BOUNCY_CASTLE_PROVIDER = "BC";
   private static final String BC_PROVIDER_FQCN = "org.bouncycastle.jce.provider.BouncyCastleProvider";

   public static boolean preferDefaultSecurityProvider() {
      String preferDefaultSecurityProvider = System.getProperty("com.amazonaws.services.s3.crypto.preferDefaultSecurityProvider");
      return preferDefaultSecurityProvider == null ? false : Boolean.parseBoolean(preferDefaultSecurityProvider);
   }

   public static synchronized boolean isBouncyCastleAvailable() {
      return Security.getProvider("BC") != null;
   }

   public static synchronized void enableBouncyCastle() {
      if (!isBouncyCastleAvailable()) {
         try {
            Class<Provider> c = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Provider provider = c.newInstance();
            Security.addProvider(provider);
         } catch (Exception var2) {
            LogFactory.getLog(CryptoRuntime.class).debug("Bouncy Castle not available", var2);
         }
      }
   }

   public static void recheck() {
      recheckAesGcmAvailablility();
      recheckRsaKeyWrapAvailablility();
   }

   public static boolean isAesGcmAvailable() {
      return CryptoRuntime.AesGcm.isAvailable;
   }

   public static void recheckAesGcmAvailablility() {
      CryptoRuntime.AesGcm.recheck();
   }

   public static boolean isRsaKeyWrapAvailable() {
      return CryptoRuntime.RsaEcbOaepWithSHA256AndMGF1Padding.isAvailable;
   }

   private static void recheckRsaKeyWrapAvailablility() {
      CryptoRuntime.RsaEcbOaepWithSHA256AndMGF1Padding.recheck();
   }

   private static final class AesGcm {
      static volatile boolean isAvailable = check();

      static boolean recheck() {
         return isAvailable = check();
      }

      private static boolean check() {
         try {
            Cipher.getInstance(ContentCryptoScheme.AES_GCM.getCipherAlgorithm(), "BC");
            return true;
         } catch (Exception var1) {
            return false;
         }
      }
   }

   private static final class RsaEcbOaepWithSHA256AndMGF1Padding {
      static volatile boolean isAvailable = check();

      static boolean recheck() {
         return isAvailable = check();
      }

      private static boolean check() {
         try {
            Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
            return true;
         } catch (Exception var1) {
            return false;
         }
      }
   }
}
