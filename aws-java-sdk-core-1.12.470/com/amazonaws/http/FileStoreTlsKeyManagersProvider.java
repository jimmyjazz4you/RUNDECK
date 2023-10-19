package com.amazonaws.http;

import com.amazonaws.util.ValidationUtils;
import java.io.File;
import javax.net.ssl.KeyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileStoreTlsKeyManagersProvider extends AbstractFileTlsKeyManagersProvider {
   private static final Log log = LogFactory.getLog(FileStoreTlsKeyManagersProvider.class);
   private final File storePath;
   private final String storeType;
   private final char[] password;

   public FileStoreTlsKeyManagersProvider(File storePath, String storeType, String password) {
      this.storePath = ValidationUtils.assertNotNull(storePath, "storePath");
      this.storeType = ValidationUtils.assertStringNotEmpty(storeType, "storeType");
      this.password = password != null ? password.toCharArray() : null;
   }

   @Override
   public KeyManager[] getKeyManagers() {
      try {
         return this.createKeyManagers(this.storePath, this.storeType, this.password);
      } catch (Exception var2) {
         if (log.isWarnEnabled()) {
            log.warn(String.format("Unable to create KeyManagers from file %s", this.storePath.getAbsolutePath()), var2);
         }

         return null;
      }
   }
}
