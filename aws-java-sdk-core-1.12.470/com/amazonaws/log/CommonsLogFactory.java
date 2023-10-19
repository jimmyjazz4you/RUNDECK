package com.amazonaws.log;

import org.apache.commons.logging.LogFactory;

public final class CommonsLogFactory extends InternalLogFactory {
   @Override
   protected InternalLogApi doGetLog(Class<?> clazz) {
      return new CommonsLog(LogFactory.getLog(clazz));
   }

   @Override
   protected InternalLogApi doGetLog(String name) {
      return new CommonsLog(LogFactory.getLog(name));
   }
}
