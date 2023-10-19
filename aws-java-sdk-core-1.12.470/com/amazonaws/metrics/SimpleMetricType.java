package com.amazonaws.metrics;

public abstract class SimpleMetricType implements MetricType {
   @Override
   public abstract String name();

   @Override
   public final int hashCode() {
      return this.name().hashCode();
   }

   @Override
   public final boolean equals(Object o) {
      if (!(o instanceof MetricType)) {
         return false;
      } else {
         MetricType that = (MetricType)o;
         return this.name().equals(that.name());
      }
   }

   @Override
   public final String toString() {
      return this.name();
   }
}
