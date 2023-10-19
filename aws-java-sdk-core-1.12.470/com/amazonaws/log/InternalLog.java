package com.amazonaws.log;

class InternalLog implements InternalLogApi {
   private final String name;

   InternalLog(String name) {
      this.name = name;
   }

   private InternalLogApi logger() {
      return InternalLogFactory.getFactory().doGetLog(this.name);
   }

   @Override
   public void debug(Object message) {
      this.logger().debug(message);
   }

   @Override
   public void debug(Object message, Throwable t) {
      this.logger().debug(message, t);
   }

   @Override
   public void error(Object message) {
      this.logger().error(message);
   }

   @Override
   public void error(Object message, Throwable t) {
      this.logger().error(message, t);
   }

   @Override
   public void fatal(Object message) {
      this.logger().fatal(message);
   }

   @Override
   public void fatal(Object message, Throwable t) {
      this.logger().fatal(message, t);
   }

   @Override
   public void info(Object message) {
      this.logger().info(message);
   }

   @Override
   public void info(Object message, Throwable t) {
      this.logger().info(message, t);
   }

   @Override
   public boolean isDebugEnabled() {
      return this.logger().isDebugEnabled();
   }

   @Override
   public boolean isErrorEnabled() {
      return this.logger().isErrorEnabled();
   }

   @Override
   public boolean isFatalEnabled() {
      return this.logger().isFatalEnabled();
   }

   @Override
   public boolean isInfoEnabled() {
      return this.logger().isInfoEnabled();
   }

   @Override
   public boolean isTraceEnabled() {
      return this.logger().isTraceEnabled();
   }

   @Override
   public boolean isWarnEnabled() {
      return this.logger().isWarnEnabled();
   }

   @Override
   public void trace(Object message) {
      this.logger().trace(message);
   }

   @Override
   public void trace(Object message, Throwable t) {
      this.logger().trace(message, t);
   }

   @Override
   public void warn(Object message) {
      this.logger().warn(message);
   }

   @Override
   public void warn(Object message, Throwable t) {
      this.logger().warn(message, t);
   }
}
