package com.amazonaws.http.conn.ssl;

import com.amazonaws.http.conn.ssl.privileged.PrivilegedMasterSecretValidator;
import com.amazonaws.util.JavaVersionParser;
import java.net.Socket;

public class MasterSecretValidators {
   private static final JavaVersionParser.JavaVersion FIXED_JAVA_6 = new JavaVersionParser.JavaVersion(1, 6, 0, 91);
   private static final JavaVersionParser.JavaVersion FIXED_JAVA_7 = new JavaVersionParser.JavaVersion(1, 7, 0, 51);
   private static final JavaVersionParser.JavaVersion FIXED_JAVA_8 = new JavaVersionParser.JavaVersion(1, 8, 0, 31);

   public static MasterSecretValidators.MasterSecretValidator getMasterSecretValidator() {
      return getMasterSecretValidator(JavaVersionParser.getCurrentJavaVersion());
   }

   public static MasterSecretValidators.MasterSecretValidator getMasterSecretValidator(JavaVersionParser.JavaVersion javaVersion) {
      switch(javaVersion.getKnownVersion()) {
         case JAVA_6:
            if (javaVersion.compareTo(FIXED_JAVA_6) < 0) {
               return new PrivilegedMasterSecretValidator();
            }
            break;
         case JAVA_7:
            if (javaVersion.compareTo(FIXED_JAVA_7) < 0) {
               return new PrivilegedMasterSecretValidator();
            }
            break;
         case JAVA_8:
            if (javaVersion.compareTo(FIXED_JAVA_8) < 0) {
               return new PrivilegedMasterSecretValidator();
            }
      }

      return new MasterSecretValidators.NoOpMasterSecretValidator();
   }

   public interface MasterSecretValidator {
      boolean isMasterSecretValid(Socket var1);
   }

   public static class NoOpMasterSecretValidator implements MasterSecretValidators.MasterSecretValidator {
      @Override
      public boolean isMasterSecretValid(Socket socket) {
         return true;
      }
   }
}
