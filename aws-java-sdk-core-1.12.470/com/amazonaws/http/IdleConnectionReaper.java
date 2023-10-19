package com.amazonaws.http;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.HttpClientConnectionManager;

@SdkInternalApi
public final class IdleConnectionReaper extends Thread {
   private static final Log LOG = LogFactory.getLog(IdleConnectionReaper.class);
   private static final int PERIOD_MILLISECONDS = 60000;
   @Deprecated
   private static final int DEFAULT_MAX_IDLE_MILLIS = 60000;
   private static final Map<HttpClientConnectionManager, Long> connectionManagers = new ConcurrentHashMap<>();
   private static volatile IdleConnectionReaper instance;
   private volatile boolean shuttingDown;

   private IdleConnectionReaper() {
      super("java-sdk-http-connection-reaper");
      this.setDaemon(true);
   }

   @Deprecated
   public static boolean registerConnectionManager(HttpClientConnectionManager connectionManager) {
      return registerConnectionManager(connectionManager, 60000L);
   }

   public static boolean registerConnectionManager(HttpClientConnectionManager connectionManager, long maxIdleInMs) {
      if (instance == null) {
         synchronized(IdleConnectionReaper.class) {
            if (instance == null) {
               instance = new IdleConnectionReaper();
               instance.start();
            }
         }
      }

      return connectionManagers.put(connectionManager, maxIdleInMs) == null;
   }

   public static boolean removeConnectionManager(HttpClientConnectionManager connectionManager) {
      boolean wasRemoved = connectionManagers.remove(connectionManager) != null;
      if (connectionManagers.isEmpty()) {
         shutdown();
      }

      return wasRemoved;
   }

   @SdkTestInternalApi
   public static List<HttpClientConnectionManager> getRegisteredConnectionManagers() {
      return new ArrayList(connectionManagers.keySet());
   }

   public static synchronized boolean shutdown() {
      if (instance != null) {
         instance.markShuttingDown();
         instance.interrupt();
         connectionManagers.clear();
         instance = null;
         return true;
      } else {
         return false;
      }
   }

   static int size() {
      return connectionManagers.size();
   }

   private void markShuttingDown() {
      this.shuttingDown = true;
   }

   @Override
   public void run() {
      while(!this.shuttingDown) {
         try {
            for(Entry<HttpClientConnectionManager, Long> entry : connectionManagers.entrySet()) {
               try {
                  ((HttpClientConnectionManager)entry.getKey()).closeIdleConnections(entry.getValue(), TimeUnit.MILLISECONDS);
               } catch (Exception var4) {
                  LOG.warn("Unable to close idle connections", var4);
               }
            }

            Thread.sleep(60000L);
         } catch (Throwable var5) {
            LOG.debug("Reaper thread: ", var5);
         }
      }

      LOG.debug("Shutting down reaper thread.");
   }
}
