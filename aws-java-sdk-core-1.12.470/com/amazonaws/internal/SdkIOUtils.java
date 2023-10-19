package com.amazonaws.internal;

import com.amazonaws.log.InternalLogApi;
import com.amazonaws.log.InternalLogFactory;
import java.io.Closeable;
import java.io.IOException;

enum SdkIOUtils {
   private static final InternalLogApi defaultLog = InternalLogFactory.getLog(SdkIOUtils.class);

   static void closeQuietly(Closeable is) {
      closeQuietly(is, null);
   }

   static void closeQuietly(Closeable is, InternalLogApi log) {
      if (is != null) {
         try {
            is.close();
         } catch (IOException var4) {
            InternalLogApi logger = log == null ? defaultLog : log;
            if (logger.isDebugEnabled()) {
               logger.debug("Ignore failure in closing the Closeable", var4);
            }
         }
      }
   }
}
