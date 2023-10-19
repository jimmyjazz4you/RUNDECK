package com.amazonaws.jmx;

import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.commons.logging.LogFactory;

public enum MBeans {
   public static <T> boolean registerMBean(String objectName, T mbean) throws MBeanRegistrationException {
      MBeanServer server = getMBeanServer();

      try {
         server.registerMBean(mbean, new ObjectName(objectName));
         return true;
      } catch (MalformedObjectNameException var4) {
         throw new IllegalArgumentException(var4);
      } catch (NotCompliantMBeanException var5) {
         throw new IllegalArgumentException(var5);
      } catch (InstanceAlreadyExistsException var6) {
         LogFactory.getLog(MBeans.class).debug("Failed to register mbean " + objectName, var6);
         return false;
      }
   }

   public static <T> boolean unregisterMBean(String objectName) throws MBeanRegistrationException {
      MBeanServer server = getMBeanServer();

      try {
         server.unregisterMBean(new ObjectName(objectName));
         return true;
      } catch (MalformedObjectNameException var3) {
         throw new IllegalArgumentException(var3);
      } catch (InstanceNotFoundException var4) {
         LogFactory.getLog(MBeans.class).debug("Failed to unregister mbean " + objectName, var4);
         return false;
      }
   }

   public static boolean isRegistered(String objectName) {
      MBeanServer server = getMBeanServer();

      try {
         return server.isRegistered(new ObjectName(objectName));
      } catch (MalformedObjectNameException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public static MBeanServer getMBeanServer() {
      List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
      return servers.size() > 0 ? servers.get(0) : ManagementFactory.getPlatformMBeanServer();
   }
}
