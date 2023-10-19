package com.amazonaws.http;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
abstract class AbstractFileTlsKeyManagersProvider implements TlsKeyManagersProvider {
   private static final Log log = LogFactory.getLog(AbstractFileTlsKeyManagersProvider.class);

   protected final KeyManager[] createKeyManagers(File storePath, String storeType, char[] password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
      KeyStore ks = this.createKeyStore(storePath, storeType, password);
      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(ks, password);
      return kmf.getKeyManagers();
   }

   private KeyStore createKeyStore(File storePath, String storeType, char[] password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
      KeyStore ks = KeyStore.getInstance(storeType);
      InputStream storeIs = null;

      KeyStore var6;
      try {
         storeIs = new FileInputStream(storePath);
         ks.load(storeIs, password);
         var6 = ks;
      } finally {
         if (storeIs != null) {
            IOUtils.closeQuietly(storeIs, log);
         }
      }

      return var6;
   }
}
