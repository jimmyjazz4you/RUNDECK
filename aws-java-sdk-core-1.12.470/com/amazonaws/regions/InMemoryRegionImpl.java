package com.amazonaws.regions;

import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRegionImpl implements RegionImpl {
   private static final String DEFAULT_DOMAIN = "amazonaws.com";
   private final String name;
   private final String domain;
   private final Map<String, String> endpoints = new HashMap<>();
   private final List<String> https = new ArrayList<>();
   private final List<String> http = new ArrayList<>();

   public InMemoryRegionImpl(String name, String domain) {
      ValidationUtils.assertNotNull(name, "region name");
      this.name = name;
      this.domain = domain == null ? "amazonaws.com" : domain;
   }

   public InMemoryRegionImpl addEndpoint(String serviceName, String endpoint) {
      ValidationUtils.assertNotNull(serviceName, "service name");
      ValidationUtils.assertNotNull(endpoint, "endpoint");
      this.endpoints.put(serviceName, endpoint);
      return this;
   }

   public InMemoryRegionImpl addHttps(String serviceName) {
      this.https.add(serviceName);
      return this;
   }

   public InMemoryRegionImpl addHttp(String serviceName) {
      this.http.add(serviceName);
      return this;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDomain() {
      return this.domain;
   }

   @Override
   public String getPartition() {
      throw new UnsupportedOperationException("Partition is not available in the in memory implementation");
   }

   @Override
   public boolean isServiceSupported(String serviceName) {
      return this.endpoints.containsKey(serviceName);
   }

   @Override
   public String getServiceEndpoint(String serviceName) {
      return this.endpoints.get(serviceName);
   }

   @Override
   public boolean hasHttpsEndpoint(String serviceName) {
      return this.https.contains(serviceName);
   }

   @Override
   public boolean hasHttpEndpoint(String serviceName) {
      return this.http.contains(serviceName);
   }

   @Override
   public Collection<String> getAvailableEndpoints() {
      return Collections.unmodifiableCollection(this.endpoints.values());
   }
}
