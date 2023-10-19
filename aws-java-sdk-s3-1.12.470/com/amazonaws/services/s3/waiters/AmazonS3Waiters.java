package com.amazonaws.services.s3.waiters;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.waiters.FixedDelayStrategy;
import com.amazonaws.waiters.HttpFailureStatusAcceptor;
import com.amazonaws.waiters.HttpSuccessStatusAcceptor;
import com.amazonaws.waiters.MaxAttemptsRetryStrategy;
import com.amazonaws.waiters.PollingStrategy;
import com.amazonaws.waiters.Waiter;
import com.amazonaws.waiters.WaiterAcceptor;
import com.amazonaws.waiters.WaiterBuilder;
import com.amazonaws.waiters.WaiterState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AmazonS3Waiters {
   private final AmazonS3 client;
   private final ExecutorService executorService = Executors.newFixedThreadPool(50);

   @SdkInternalApi
   public AmazonS3Waiters(AmazonS3 client) {
      this.client = client;
   }

   public Waiter<HeadBucketRequest> bucketNotExists() {
      return new WaiterBuilder()
         .withSdkFunction(new HeadBucketFunction(this.client))
         .withAcceptors(new WaiterAcceptor[]{new HttpFailureStatusAcceptor(404, WaiterState.SUCCESS)})
         .withDefaultPollingStrategy(new PollingStrategy(new MaxAttemptsRetryStrategy(20), new FixedDelayStrategy(5)))
         .withExecutorService(this.executorService)
         .build();
   }

   public Waiter<HeadBucketRequest> bucketExists() {
      return new WaiterBuilder()
         .withSdkFunction(new HeadBucketFunction(this.client))
         .withAcceptors(
            new WaiterAcceptor[]{
               new HttpSuccessStatusAcceptor(WaiterState.SUCCESS),
               new HttpFailureStatusAcceptor(301, WaiterState.SUCCESS),
               new HttpFailureStatusAcceptor(403, WaiterState.SUCCESS),
               new HttpFailureStatusAcceptor(404, WaiterState.RETRY)
            }
         )
         .withDefaultPollingStrategy(new PollingStrategy(new MaxAttemptsRetryStrategy(20), new FixedDelayStrategy(5)))
         .withExecutorService(this.executorService)
         .build();
   }

   public Waiter<GetObjectMetadataRequest> objectExists() {
      return new WaiterBuilder()
         .withSdkFunction(new HeadObjectFunction(this.client))
         .withAcceptors(new WaiterAcceptor[]{new HttpSuccessStatusAcceptor(WaiterState.SUCCESS), new HttpFailureStatusAcceptor(404, WaiterState.RETRY)})
         .withDefaultPollingStrategy(new PollingStrategy(new MaxAttemptsRetryStrategy(20), new FixedDelayStrategy(5)))
         .withExecutorService(this.executorService)
         .build();
   }

   public Waiter<GetObjectMetadataRequest> objectNotExists() {
      return new WaiterBuilder()
         .withSdkFunction(new HeadObjectFunction(this.client))
         .withAcceptors(new WaiterAcceptor[]{new HttpFailureStatusAcceptor(404, WaiterState.SUCCESS)})
         .withDefaultPollingStrategy(new PollingStrategy(new MaxAttemptsRetryStrategy(20), new FixedDelayStrategy(5)))
         .withExecutorService(this.executorService)
         .build();
   }
}
