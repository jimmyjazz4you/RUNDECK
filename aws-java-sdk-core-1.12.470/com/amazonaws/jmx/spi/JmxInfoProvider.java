package com.amazonaws.jmx.spi;

import org.apache.commons.logging.LogFactory;

public interface JmxInfoProvider {
   JmxInfoProvider NONE = new JmxInfoProvider() {
      @Override
      public long[] getFileDecriptorInfo() {
         return null;
      }

      @Override
      public int getThreadCount() {
         return 0;
      }

      @Override
      public int getDaemonThreadCount() {
         return 0;
      }

      @Override
      public int getPeakThreadCount() {
         return 0;
      }

      @Override
      public long getTotalStartedThreadCount() {
         return 0L;
      }

      @Override
      public long[] findDeadlockedThreads() {
         return null;
      }

      @Override
      public boolean isEnabled() {
         return false;
      }
   };

   long[] getFileDecriptorInfo();

   int getThreadCount();

   int getDaemonThreadCount();

   int getPeakThreadCount();

   long getTotalStartedThreadCount();

   long[] findDeadlockedThreads();

   boolean isEnabled();

   public static class Factory {
      private static final JmxInfoProvider provider;

      public static JmxInfoProvider getJmxInfoProvider() {
         return provider;
      }

      static {
         JmxInfoProvider p;
         try {
            Class<?> c = Class.forName("com.amazonaws.jmx.JmxInfoProviderSupport");
            p = (JmxInfoProvider)c.newInstance();
         } catch (Exception var2) {
            LogFactory.getLog(JmxInfoProvider.class).debug("Failed to load the JMX implementation module - JMX is disabled", var2);
            p = JmxInfoProvider.NONE;
         }

         provider = p;
      }
   }
}
