package com.amazonaws.metrics;

public interface ServiceMetricType extends MetricType {
   String UPLOAD_THROUGHPUT_NAME_SUFFIX = "UploadThroughput";
   String UPLOAD_BYTE_COUNT_NAME_SUFFIX = "UploadByteCount";
   String DOWNLOAD_THROUGHPUT_NAME_SUFFIX = "DownloadThroughput";
   String DOWNLOAD_BYTE_COUNT_NAME_SUFFIX = "DownloadByteCount";

   String getServiceName();
}
