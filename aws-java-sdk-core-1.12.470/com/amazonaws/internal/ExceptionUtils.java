package com.amazonaws.internal;

import com.amazonaws.annotation.SdkInternalApi;
import java.io.PrintWriter;
import java.io.StringWriter;

@SdkInternalApi
public final class ExceptionUtils {
   private ExceptionUtils() {
   }

   public static String exceptionStackTrace(Throwable t) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);

      String var3;
      try {
         t.printStackTrace(pw);
         var3 = sw.toString();
      } finally {
         pw.close();
      }

      return var3;
   }
}
