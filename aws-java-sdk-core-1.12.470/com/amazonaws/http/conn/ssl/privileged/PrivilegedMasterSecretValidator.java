package com.amazonaws.http.conn.ssl.privileged;

import com.amazonaws.http.conn.ssl.MasterSecretValidators;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PrivilegedMasterSecretValidator implements MasterSecretValidators.MasterSecretValidator {
   private static final Log LOG = LogFactory.getLog(PrivilegedMasterSecretValidator.class);

   @Override
   public boolean isMasterSecretValid(final Socket socket) {
      return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
         public Boolean run() {
            return PrivilegedMasterSecretValidator.this.privilegedIsMasterSecretValid(socket);
         }
      });
   }

   private boolean privilegedIsMasterSecretValid(Socket socket) {
      if (socket instanceof SSLSocket) {
         SSLSession session = this.getSslSession(socket);
         if (session != null) {
            String className = session.getClass().getName();
            if ("sun.security.ssl.SSLSessionImpl".equals(className)) {
               try {
                  Object masterSecret = this.getMasterSecret(session, className);
                  if (masterSecret == null) {
                     session.invalidate();
                     if (LOG.isDebugEnabled()) {
                        LOG.debug("Invalidated session " + session);
                     }

                     return false;
                  }
               } catch (Exception var5) {
                  this.failedToVerifyMasterSecret(var5);
               }
            }
         }
      }

      return true;
   }

   private SSLSession getSslSession(Socket socket) {
      return ((SSLSocket)socket).getSession();
   }

   private Object getMasterSecret(SSLSession session, String className) throws Exception {
      Class<?> clazz = Class.forName(className);
      Method method = clazz.getDeclaredMethod("getMasterSecret");
      method.setAccessible(true);
      return method.invoke(session);
   }

   private void failedToVerifyMasterSecret(Throwable t) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("Failed to verify the SSL master secret", t);
      }
   }
}
