package com.amazonaws.log;

import java.util.logging.Logger;

public final class JulLogFactory extends InternalLogFactory {
   @Override
   protected InternalLogApi doGetLog(Class<?> clazz) {
      return new JulLog(Logger.getLogger(clazz.getName()));
   }

   @Override
   protected InternalLogApi doGetLog(String name) {
      return new JulLog(Logger.getLogger(name));
   }
}
