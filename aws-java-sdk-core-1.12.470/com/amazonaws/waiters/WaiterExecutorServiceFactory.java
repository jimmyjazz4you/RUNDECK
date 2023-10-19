package com.amazonaws.waiters;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.NamedDefaultThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SdkProtectedApi
public class WaiterExecutorServiceFactory {
   public static ExecutorService buildExecutorServiceForWaiter(String name) {
      return Executors.newCachedThreadPool(NamedDefaultThreadFactory.of(name));
   }
}
