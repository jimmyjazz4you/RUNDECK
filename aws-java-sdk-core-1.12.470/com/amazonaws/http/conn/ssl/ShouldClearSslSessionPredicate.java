package com.amazonaws.http.conn.ssl;

import com.amazonaws.internal.SdkPredicate;
import com.amazonaws.util.JavaVersionParser;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLException;

public class ShouldClearSslSessionPredicate extends SdkPredicate<SSLException> {
   public static final JavaVersionParser.JavaVersion FIXED_JAVA_7 = new JavaVersionParser.JavaVersion(1, 7, 0, 85);
   public static final JavaVersionParser.JavaVersion FIXED_JAVA_8 = new JavaVersionParser.JavaVersion(1, 8, 0, 60);
   private static List<String> EXCEPTION_MESSAGE_WHITELIST = Arrays.asList("server certificate change is restricted", "peer not authenticated");
   private final JavaVersionParser.JavaVersion javaVersion;

   public ShouldClearSslSessionPredicate(JavaVersionParser.JavaVersion javaVersion) {
      this.javaVersion = javaVersion;
   }

   public boolean test(SSLException sslEx) {
      return this.isExceptionAffected(sslEx.getMessage()) && this.isJvmAffected();
   }

   private boolean isJvmAffected() {
      switch(this.javaVersion.getKnownVersion()) {
         case JAVA_6:
            return true;
         case JAVA_7:
            return this.javaVersion.compareTo(FIXED_JAVA_7) < 0;
         case JAVA_8:
            return this.javaVersion.compareTo(FIXED_JAVA_8) < 0;
         case JAVA_9:
            return false;
         case UNKNOWN:
            return true;
         default:
            return true;
      }
   }

   private boolean isExceptionAffected(String exceptionMessage) {
      if (exceptionMessage != null) {
         for(String affectedMessage : EXCEPTION_MESSAGE_WHITELIST) {
            if (exceptionMessage.contains(affectedMessage)) {
               return true;
            }
         }
      }

      return false;
   }
}
