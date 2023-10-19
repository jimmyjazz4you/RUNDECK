package com.amazonaws.util;

import com.amazonaws.metrics.RequestMetricType;

public enum AwsClientSideMonitoringMetrics implements RequestMetricType {
   ApiCallLatency,
   MaxRetriesExceeded;
}
