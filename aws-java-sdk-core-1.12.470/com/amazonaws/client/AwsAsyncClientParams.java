package com.amazonaws.client;

import com.amazonaws.annotation.SdkProtectedApi;
import java.util.concurrent.ExecutorService;

@SdkProtectedApi
public abstract class AwsAsyncClientParams extends AwsSyncClientParams {
   public abstract ExecutorService getExecutor();
}
