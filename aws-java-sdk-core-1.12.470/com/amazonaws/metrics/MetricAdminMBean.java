package com.amazonaws.metrics;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MetricAdminMBean {
   boolean isMetricsEnabled();

   String getRequestMetricCollector();

   String getServiceMetricCollector();

   boolean enableDefaultMetrics();

   void disableMetrics();

   boolean isMachineMetricsExcluded();

   void setMachineMetricsExcluded(boolean var1);

   boolean isPerHostMetricsIncluded();

   void setPerHostMetricsIncluded(boolean var1);

   String getRegion();

   void setRegion(String var1);

   String getCredentialFile();

   void setCredentialFile(String var1) throws FileNotFoundException, IOException;

   Integer getMetricQueueSize();

   void setMetricQueueSize(Integer var1);

   Integer getQueuePollTimeoutMilli();

   void setQueuePollTimeoutMilli(Integer var1);

   String getMetricNameSpace();

   void setMetricNameSpace(String var1);

   String getJvmMetricName();

   void setJvmMetricName(String var1);

   String getHostMetricName();

   void setHostMetricName(String var1);

   boolean isSingleMetricNamespace();

   void setSingleMetricNamespace(boolean var1);
}
