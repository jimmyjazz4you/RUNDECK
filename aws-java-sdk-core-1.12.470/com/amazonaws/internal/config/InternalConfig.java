package com.amazonaws.internal.config;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.log.InternalLogApi;
import com.amazonaws.log.InternalLogFactory;
import com.amazonaws.util.ClassLoaderHelper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Immutable
public class InternalConfig {
   private static final ObjectMapper MAPPER = new ObjectMapper()
      .disable(new MapperFeature[]{MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS})
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .configure(Feature.ALLOW_COMMENTS, true);
   private static final InternalLogApi log = InternalLogFactory.getLog(InternalConfig.class);
   static final String DEFAULT_CONFIG_RESOURCE_RELATIVE_PATH = "awssdk_config_default.json";
   static final String DEFAULT_CONFIG_RESOURCE_ABSOLUTE_PATH = "/com/amazonaws/internal/config/awssdk_config_default.json";
   static final String CONFIG_OVERRIDE_RESOURCE = "awssdk_config_override.json";
   static final String ENDPOINT_DISCOVERY_CONFIG_ABSOLUTE_PATH = "/com/amazonaws/endpointdiscovery/endpoint-discovery.json";
   private static final String SERVICE_REGION_DELIMITOR = "/";
   private final SignerConfig defaultSignerConfig;
   private final Map<String, SignerConfig> serviceRegionSigners;
   private final Map<String, SignerConfig> regionSigners;
   private final Map<String, SignerConfig> serviceSigners;
   private final Map<String, HttpClientConfig> httpClients;
   private final List<HostRegexToRegionMapping> hostRegexToRegionMappings;
   private final String userAgentTemplate;
   private final boolean endpointDiscoveryEnabled;
   private final String defaultRetryMode;
   private URL defaultConfigFileLocation;
   private URL overrideConfigFileLocation;

   InternalConfig(InternalConfigJsonHelper defaults, InternalConfigJsonHelper override, EndpointDiscoveryConfig endpointDiscoveryConfig) {
      SignerConfigJsonHelper scb = defaults.getDefaultSigner();
      this.defaultSignerConfig = scb == null ? null : scb.build();
      this.regionSigners = this.mergeSignerMap(defaults.getRegionSigners(), override.getRegionSigners(), "region");
      this.serviceSigners = this.mergeSignerMap(defaults.getServiceSigners(), override.getServiceSigners(), "service");
      this.serviceRegionSigners = this.mergeSignerMap(defaults.getServiceRegionSigners(), override.getServiceRegionSigners(), "service/region");
      this.httpClients = this.merge(defaults.getHttpClients(), override.getHttpClients());
      this.hostRegexToRegionMappings = this.append(override.getHostRegexToRegionMappings(), defaults.getHostRegexToRegionMappings());
      if (override.getUserAgentTemplate() != null) {
         this.userAgentTemplate = override.getUserAgentTemplate();
      } else {
         this.userAgentTemplate = defaults.getUserAgentTemplate();
      }

      this.endpointDiscoveryEnabled = endpointDiscoveryConfig.isEndpointDiscoveryEnabled();
      this.defaultRetryMode = override.getDefaultRetryMode();
   }

   private Map<String, SignerConfig> mergeSignerMap(
      JsonIndex<SignerConfigJsonHelper, SignerConfig>[] defaults, JsonIndex<SignerConfigJsonHelper, SignerConfig>[] overrides, String theme
   ) {
      Map<String, SignerConfig> map = this.buildSignerMap(defaults, theme);
      Map<String, SignerConfig> mapOverride = this.buildSignerMap(overrides, theme);
      map.putAll(mapOverride);
      return Collections.unmodifiableMap(map);
   }

   private <C extends Builder<T>, T> Map<String, T> merge(JsonIndex<C, T>[] defaults, JsonIndex<C, T>[] overrides) {
      Map<String, T> map = this.buildMap(defaults);
      Map<String, T> mapOverride = this.buildMap(overrides);
      map.putAll(mapOverride);
      return Collections.unmodifiableMap(map);
   }

   private <C extends Builder<T>, T> Map<String, T> buildMap(JsonIndex<C, T>[] signerIndexes) {
      Map<String, T> map = new HashMap<>();
      if (signerIndexes != null) {
         for(JsonIndex<C, T> index : signerIndexes) {
            String region = index.getKey();
            T prev = map.put(region, index.newReadOnlyConfig());
            if (prev != null) {
               log.warn("Duplicate definition of signer for " + index.getKey());
            }
         }
      }

      return map;
   }

   private <C extends Builder<T>, T> List<T> append(C[] defaults, C[] overrides) {
      List<T> list = new LinkedList<>();
      if (defaults != null) {
         for(C builder : defaults) {
            list.add(builder.build());
         }
      }

      if (overrides != null) {
         for(C builder : overrides) {
            list.add(builder.build());
         }
      }

      return list;
   }

   private Map<String, SignerConfig> buildSignerMap(JsonIndex<SignerConfigJsonHelper, SignerConfig>[] signerIndexes, String theme) {
      Map<String, SignerConfig> map = new HashMap<>();
      if (signerIndexes != null) {
         for(JsonIndex<SignerConfigJsonHelper, SignerConfig> index : signerIndexes) {
            String region = index.getKey();
            SignerConfig prev = map.put(region, index.newReadOnlyConfig());
            if (prev != null) {
               log.warn("Duplicate definition of signer for " + theme + " " + index.getKey());
            }
         }
      }

      return map;
   }

   public SignerConfig getSignerConfig(String serviceName) {
      return this.getSignerConfig(serviceName, null);
   }

   public HttpClientConfig getHttpClientConfig(String httpClientName) {
      return this.httpClients.get(httpClientName);
   }

   public SignerConfig getSignerConfig(String serviceName, String regionName) {
      if (serviceName == null) {
         throw new IllegalArgumentException();
      } else {
         SignerConfig signerConfig = null;
         if (regionName != null) {
            String key = serviceName + "/" + regionName;
            signerConfig = this.serviceRegionSigners.get(key);
            if (signerConfig != null) {
               return signerConfig;
            }

            signerConfig = this.regionSigners.get(regionName);
            if (signerConfig != null) {
               return signerConfig;
            }
         }

         signerConfig = this.serviceSigners.get(serviceName);
         return signerConfig == null ? this.defaultSignerConfig : signerConfig;
      }
   }

   public List<HostRegexToRegionMapping> getHostRegexToRegionMappings() {
      return Collections.unmodifiableList(this.hostRegexToRegionMappings);
   }

   public String getUserAgentTemplate() {
      return this.userAgentTemplate;
   }

   public boolean endpointDiscoveryEnabled() {
      return this.endpointDiscoveryEnabled;
   }

   public String getDefaultRetryMode() {
      return this.defaultRetryMode;
   }

   static <T> T loadfrom(URL url, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
      if (url == null) {
         throw new IllegalArgumentException();
      } else {
         return (T)MAPPER.readValue(url, clazz);
      }
   }

   static InternalConfig load() throws JsonParseException, JsonMappingException, IOException {
      URL configUrl = getResource("awssdk_config_default.json", true, false);
      if (configUrl == null) {
         configUrl = getResource("/com/amazonaws/internal/config/awssdk_config_default.json", false, false);
      }

      InternalConfigJsonHelper config = loadfrom(configUrl, InternalConfigJsonHelper.class);
      URL overrideUrl = getResource("awssdk_config_override.json", false, true);
      if (overrideUrl == null) {
         overrideUrl = getResource("awssdk_config_override.json", false, false);
      }

      InternalConfigJsonHelper configOverride;
      if (overrideUrl == null) {
         log.debug("Configuration override awssdk_config_override.json not found.");
         configOverride = new InternalConfigJsonHelper();
      } else {
         configOverride = loadfrom(overrideUrl, InternalConfigJsonHelper.class);
      }

      EndpointDiscoveryConfig endpointDiscoveryConfig = new EndpointDiscoveryConfig();
      URL endpointDiscoveryConfigUrl = getResource("/com/amazonaws/endpointdiscovery/endpoint-discovery.json", false, false);
      if (endpointDiscoveryConfigUrl != null) {
         endpointDiscoveryConfig = loadfrom(endpointDiscoveryConfigUrl, EndpointDiscoveryConfig.class);
      }

      InternalConfig merged = new InternalConfig(config, configOverride, endpointDiscoveryConfig);
      merged.setDefaultConfigFileLocation(configUrl);
      merged.setOverrideConfigFileLocation(overrideUrl);
      return merged;
   }

   private static URL getResource(String path, boolean classesFirst, boolean addLeadingSlash) {
      path = addLeadingSlash ? "/" + path : path;
      return ClassLoaderHelper.getResource(path, classesFirst, InternalConfig.class);
   }

   public URL getDefaultConfigFileLocation() {
      return this.defaultConfigFileLocation;
   }

   public URL getOverrideConfigFileLocation() {
      return this.overrideConfigFileLocation;
   }

   void setDefaultConfigFileLocation(URL url) {
      this.defaultConfigFileLocation = url;
   }

   void setOverrideConfigFileLocation(URL url) {
      this.overrideConfigFileLocation = url;
   }

   void dump() {
      StringBuilder sb = new StringBuilder()
         .append("defaultSignerConfig: ")
         .append(this.defaultSignerConfig)
         .append("\n")
         .append("serviceRegionSigners: ")
         .append(this.serviceRegionSigners)
         .append("\n")
         .append("regionSigners: ")
         .append(this.regionSigners)
         .append("\n")
         .append("serviceSigners: ")
         .append(this.serviceSigners)
         .append("\n")
         .append("userAgentTemplate: ")
         .append(this.userAgentTemplate);
      log.debug(sb.toString());
   }

   public static class Factory {
      private static final InternalConfig SINGELTON;

      public static InternalConfig getInternalConfig() {
         return SINGELTON;
      }

      static {
         InternalConfig config = null;

         try {
            config = InternalConfig.load();
         } catch (RuntimeException var2) {
            throw var2;
         } catch (Exception var3) {
            throw new IllegalStateException("Fatal: Failed to load the internal config for AWS Java SDK", var3);
         }

         SINGELTON = config;
      }
   }
}
