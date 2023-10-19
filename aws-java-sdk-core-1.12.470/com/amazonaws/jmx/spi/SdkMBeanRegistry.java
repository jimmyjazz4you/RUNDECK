package com.amazonaws.jmx.spi;

import org.apache.commons.logging.LogFactory;

public interface SdkMBeanRegistry {
   SdkMBeanRegistry NONE = new SdkMBeanRegistry() {
      @Override
      public boolean registerMetricAdminMBean(String objectName) {
         return false;
      }

      @Override
      public boolean unregisterMBean(String objectName) {
         return false;
      }

      @Override
      public boolean isMBeanRegistered(String objectName) {
         return false;
      }
   };

   boolean registerMetricAdminMBean(String var1);

   boolean unregisterMBean(String var1);

   boolean isMBeanRegistered(String var1);

   public static class Factory {
      private static final SdkMBeanRegistry registry;

      public static SdkMBeanRegistry getMBeanRegistry() {
         return registry;
      }

      static {
         SdkMBeanRegistry rego;
         try {
            Class<?> c = Class.forName("com.amazonaws.jmx.SdkMBeanRegistrySupport");
            rego = (SdkMBeanRegistry)c.newInstance();
         } catch (Exception var2) {
            LogFactory.getLog(SdkMBeanRegistry.class).debug("Failed to load the JMX implementation module - JMX is disabled", var2);
            rego = SdkMBeanRegistry.NONE;
         }

         registry = rego;
      }
   }
}
