package com.amazonaws.endpointdiscovery;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.cache.EndpointDiscoveryCacheLoader;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public abstract class EndpointDiscoveryIdentifiersRefreshCache<K> {
   private static final Log log = LogFactory.getLog(EndpointDiscoveryIdentifiersRefreshCache.class);
   private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(DaemonThreadFactory.INSTANCE);
   private final EndpointDiscoveryCacheLoader<String, Map<String, String>> cacheLoader;
   protected final Map<String, URI> cache = new ConcurrentHashMap<>();

   public EndpointDiscoveryIdentifiersRefreshCache(EndpointDiscoveryCacheLoader cacheLoader) {
      this.cacheLoader = cacheLoader;
   }

   public abstract URI get(K var1, AmazonWebServiceRequest var2, boolean var3, URI var4);

   public abstract URI put(String var1, AmazonWebServiceRequest var2, Map<String, String> var3, URI var4);

   public abstract String constructKey(String var1, AmazonWebServiceRequest var2);

   public void evict(String key) {
      this.cache.remove(key);
   }

   public URI discoverEndpoint(String key, AmazonWebServiceRequest request, boolean required, URI defaultEndpoint) {
      if (required) {
         try {
            return this.put(key, request, this.cacheLoader.load(key, request), defaultEndpoint);
         } catch (Exception var6) {
            throw new SdkClientException("Unable to discover required endpoint for request.", var6);
         }
      } else {
         this.loadAndScheduleRefresh(key, request, 1L, defaultEndpoint);
         return defaultEndpoint;
      }
   }

   public ScheduledFuture<URI> loadAndScheduleRefresh(final String key, final AmazonWebServiceRequest request, long refreshPeriod, final URI defaultEndpoint) {
      return this.executorService
         .schedule(
            new Callable<URI>() {
               public URI call() {
                  try {
                     return EndpointDiscoveryIdentifiersRefreshCache.this.put(
                        key, request, EndpointDiscoveryIdentifiersRefreshCache.this.cacheLoader.load(key, request), defaultEndpoint
                     );
                  } catch (Exception var2) {
                     EndpointDiscoveryIdentifiersRefreshCache.log.debug("Failed to refresh cached endpoint. Scheduling another refresh in 5 minutes");
                     EndpointDiscoveryIdentifiersRefreshCache.this.loadAndScheduleRefresh(key, request, 5L, defaultEndpoint);
                     return null;
                  }
               }
            },
            refreshPeriod,
            TimeUnit.MINUTES
         );
   }

   public ScheduledFuture<?> loadAndScheduleEvict(final String key, long refreshPeriod, TimeUnit refreshPeriodTimeUnit) {
      return this.executorService.schedule(new Runnable() {
         @Override
         public void run() {
            EndpointDiscoveryIdentifiersRefreshCache.this.evict(key);
         }
      }, refreshPeriod, refreshPeriodTimeUnit);
   }

   public void shutdown() {
      this.executorService.shutdownNow();
   }
}
