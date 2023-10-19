package com.amazonaws.log;

import java.util.logging.Level;
import java.util.logging.Logger;

final class JulLog implements InternalLogApi {
   private final Logger log;

   JulLog(Logger logger) {
      this.log = logger;
   }

   @Override
   public void debug(Object message) {
      this.log.log(Level.FINE, String.valueOf(message));
   }

   @Override
   public void debug(Object message, Throwable t) {
      this.log.log(Level.FINE, String.valueOf(message), t);
   }

   @Override
   public void error(Object message) {
      this.log.log(Level.SEVERE, String.valueOf(message));
   }

   @Override
   public void error(Object message, Throwable t) {
      this.log.log(Level.SEVERE, String.valueOf(message), t);
   }

   @Override
   public void fatal(Object message) {
      this.log.log(Level.SEVERE, String.valueOf(message));
   }

   @Override
   public void fatal(Object message, Throwable t) {
      this.log.log(Level.SEVERE, String.valueOf(message), t);
   }

   @Override
   public void info(Object message) {
      this.log.log(Level.INFO, String.valueOf(message));
   }

   @Override
   public void info(Object message, Throwable t) {
      this.log.log(Level.INFO, String.valueOf(message), t);
   }

   @Override
   public boolean isDebugEnabled() {
      return this.log.isLoggable(Level.FINE);
   }

   @Override
   public boolean isErrorEnabled() {
      return this.log.isLoggable(Level.SEVERE);
   }

   @Override
   public boolean isFatalEnabled() {
      return this.log.isLoggable(Level.SEVERE);
   }

   @Override
   public boolean isInfoEnabled() {
      return this.log.isLoggable(Level.INFO);
   }

   @Override
   public boolean isTraceEnabled() {
      return this.log.isLoggable(Level.FINER);
   }

   @Override
   public boolean isWarnEnabled() {
      return this.log.isLoggable(Level.WARNING);
   }

   @Override
   public void trace(Object message) {
      this.log.log(Level.FINER, String.valueOf(message));
   }

   @Override
   public void trace(Object message, Throwable t) {
      this.log.log(Level.FINER, String.valueOf(message), t);
   }

   @Override
   public void warn(Object message) {
      this.log.log(Level.WARNING, String.valueOf(message));
   }

   @Override
   public void warn(Object message, Throwable t) {
      this.log.log(Level.WARNING, String.valueOf(message), t);
   }
}
