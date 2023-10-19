package com.amazonaws.services.s3.internal;

public interface ServerSideEncryptionResult {
   String getSSEAlgorithm();

   void setSSEAlgorithm(String var1);

   String getSSECustomerAlgorithm();

   void setSSECustomerAlgorithm(String var1);

   String getSSECustomerKeyMd5();

   void setSSECustomerKeyMd5(String var1);

   Boolean getBucketKeyEnabled();

   void setBucketKeyEnabled(Boolean var1);
}
