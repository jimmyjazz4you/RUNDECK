package com.amazonaws.metrics;

public abstract class ServiceMetricCollector {
   public static final ServiceMetricCollector NONE = new ServiceMetricCollector() {
      @Override
      public void collectByteThroughput(ByteThroughputProvider provider) {
      }

      @Override
      public void collectLatency(ServiceLatencyProvider provider) {
      }

      @Override
      public boolean isEnabled() {
         return false;
      }
   };

   public abstract void collectByteThroughput(ByteThroughputProvider var1);

   public abstract void collectLatency(ServiceLatencyProvider var1);

   public boolean isEnabled() {
      return true;
   }

   public interface Factory {
      ServiceMetricCollector getServiceMetricCollector();
   }
}
