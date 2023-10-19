package com.amazonaws.services.s3.model;

import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.InputStream;
import org.apache.commons.logging.Log;

public interface S3DataSource {
   File getFile();

   void setFile(File var1);

   InputStream getInputStream();

   void setInputStream(InputStream var1);

   public static enum Utils {
      public static void cleanupDataSource(S3DataSource req, File fileOrig, InputStream inputStreamOrig, InputStream inputStreamCurr, Log log) {
         if (fileOrig != null) {
            IOUtils.release(inputStreamCurr, log);
         }

         req.setInputStream(inputStreamOrig);
         req.setFile(fileOrig);
      }
   }
}
