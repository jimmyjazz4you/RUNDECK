package com.amazonaws.log;

import com.amazonaws.annotation.ThreadSafe;

@ThreadSafe
public abstract class InternalLogFactory {
   private static volatile InternalLogFactory factory = new JulLogFactory();
   private static volatile boolean factoryConfigured;

   public static InternalLogApi getLog(Class<?> clazz) {
      return (InternalLogApi)(factoryConfigured ? factory.doGetLog(clazz) : new InternalLog(clazz.getName()));
   }

   public static InternalLogApi getLog(String name) {
      return (InternalLogApi)(factoryConfigured ? factory.doGetLog(name) : new InternalLog(name));
   }

   protected abstract InternalLogApi doGetLog(Class<?> var1);

   protected abstract InternalLogApi doGetLog(String var1);

   public static InternalLogFactory getFactory() {
      return factory;
   }

   public static synchronized boolean configureFactory(InternalLogFactory factory) {
      if (factory == null) {
         throw new IllegalArgumentException();
      } else if (factoryConfigured) {
         return false;
      } else {
         InternalLogFactory.factory = factory;
         factoryConfigured = true;
         return true;
      }
   }
}
