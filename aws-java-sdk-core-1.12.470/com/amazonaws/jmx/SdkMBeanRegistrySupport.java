package com.amazonaws.jmx;

import com.amazonaws.jmx.spi.SdkMBeanRegistry;
import com.amazonaws.metrics.MetricAdmin;
import org.apache.commons.logging.LogFactory;

public class SdkMBeanRegistrySupport implements SdkMBeanRegistry {
   @Override
   public boolean registerMetricAdminMBean(String objectName) {
      try {
         return MBeans.registerMBean(objectName, new MetricAdmin());
      } catch (Exception var3) {
         LogFactory.getLog(SdkMBeanRegistrySupport.class).warn("", var3);
         return false;
      }
   }

   @Override
   public boolean unregisterMBean(String objectName) {
      try {
         return MBeans.unregisterMBean(objectName);
      } catch (Exception var3) {
         LogFactory.getLog(SdkMBeanRegistrySupport.class).warn("", var3);
         return false;
      }
   }

   @Override
   public boolean isMBeanRegistered(String objectName) {
      return MBeans.isRegistered(objectName);
   }
}
