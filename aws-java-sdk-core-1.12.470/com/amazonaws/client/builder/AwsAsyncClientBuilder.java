package com.amazonaws.client.builder;

import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.client.AwsAsyncClientParams;
import com.amazonaws.regions.AwsRegionProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@NotThreadSafe
@SdkProtectedApi
public abstract class AwsAsyncClientBuilder<Subclass extends AwsAsyncClientBuilder, TypeToBuild> extends AwsClientBuilder<Subclass, TypeToBuild> {
   private ExecutorFactory executorFactory;

   protected AwsAsyncClientBuilder(ClientConfigurationFactory clientConfigFactory) {
      super(clientConfigFactory);
   }

   @SdkTestInternalApi
   protected AwsAsyncClientBuilder(ClientConfigurationFactory clientConfigFactory, AwsRegionProvider regionProvider) {
      super(clientConfigFactory, regionProvider);
   }

   public final ExecutorFactory getExecutorFactory() {
      return this.executorFactory;
   }

   public final void setExecutorFactory(ExecutorFactory executorFactory) {
      this.executorFactory = executorFactory;
   }

   public final Subclass withExecutorFactory(ExecutorFactory executorFactory) {
      this.setExecutorFactory(executorFactory);
      return this.getSubclass();
   }

   @Override
   public final TypeToBuild build() {
      return this.configureMutableProperties(this.build(this.getAsyncClientParams()));
   }

   protected abstract TypeToBuild build(AwsAsyncClientParams var1);

   protected final AwsAsyncClientParams getAsyncClientParams() {
      return new AwsAsyncClientBuilder.AsyncBuilderParams(this.executorFactory);
   }

   protected class AsyncBuilderParams extends AwsClientBuilder<Subclass, TypeToBuild>.SyncBuilderParams {
      private final ExecutorService _executorService;

      protected AsyncBuilderParams(ExecutorFactory executorFactory) {
         this._executorService = executorFactory == null ? this.defaultExecutor() : executorFactory.newExecutor();
      }

      @Override
      public ExecutorService getExecutor() {
         return this._executorService;
      }

      private ExecutorService defaultExecutor() {
         return Executors.newFixedThreadPool(this.getClientConfiguration().getMaxConnections());
      }
   }
}
