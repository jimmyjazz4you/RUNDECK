package com.amazonaws.regions;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.util.SdkHttpUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class RegionUtils {
   private static volatile RegionMetadata regionMetadata;

   public static RegionMetadata getRegionMetadata() {
      RegionMetadata rval = regionMetadata;
      if (rval != null) {
         return rval;
      } else {
         synchronized(RegionUtils.class) {
            if (regionMetadata == null) {
               initialize();
            }
         }

         return regionMetadata;
      }
   }

   public static void initialize() {
      regionMetadata = RegionMetadataFactory.create();
   }

   public static void initializeWithMetadata(RegionMetadata metadata) {
      if (metadata == null) {
         throw new IllegalArgumentException("metadata cannot be null");
      } else {
         regionMetadata = metadata;
      }
   }

   public static List<Region> getRegions() {
      return getRegionMetadata().getRegions();
   }

   public static List<Region> getRegionsForService(String serviceAbbreviation) {
      return getRegionMetadata().getRegionsForService(serviceAbbreviation);
   }

   public static Region getRegion(String regionName) {
      String urlEncodedRegionName = regionName == null ? null : SdkHttpUtils.urlEncode(regionName, false);
      return getRegionMetadata().getRegion(urlEncodedRegionName);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromURI(URI uri) throws IOException {
      return loadMetadataFromURI(uri, null);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromURI(URI uri, ClientConfiguration config) throws IOException {
      return LegacyRegionXmlLoadUtils.load(uri, config);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromFile(File file) throws IOException {
      return LegacyRegionXmlLoadUtils.load(file);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromResource(String name) throws IOException {
      return LegacyRegionXmlLoadUtils.load(RegionUtils.class, name);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromResource(Class<?> clazz, String name) throws IOException {
      return LegacyRegionXmlLoadUtils.load(clazz, name);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromResource(ClassLoader classLoader, String name) throws IOException {
      return LegacyRegionXmlLoadUtils.load(classLoader, name);
   }

   @Deprecated
   public static RegionMetadata loadMetadataFromInputStream(InputStream stream) throws IOException {
      return LegacyRegionXmlLoadUtils.load(stream);
   }

   @Deprecated
   public static void init() {
      initialize();
   }

   @Deprecated
   public static synchronized void initializeFromURI(URI uri) {
      initializeFromURI(uri, null);
   }

   @Deprecated
   public static synchronized void initializeFromURI(URI uri, ClientConfiguration config) {
      try {
         regionMetadata = loadMetadataFromURI(uri, config);
      } catch (IOException var3) {
         throw new SdkClientException("Error parsing region metadata from " + uri, var3);
      }
   }

   @Deprecated
   public static synchronized void initializeFromFile(File file) {
      try {
         regionMetadata = loadMetadataFromFile(file);
      } catch (IOException var2) {
         throw new SdkClientException("Error parsing region metadata from " + file, var2);
      }
   }

   @Deprecated
   public static synchronized void initializeFromResource(String name) {
      initializeFromResource(RegionUtils.class, name);
   }

   @Deprecated
   public static synchronized void initializeFromResource(Class<?> clazz, String name) {
      try {
         regionMetadata = loadMetadataFromResource(clazz, name);
      } catch (IOException var3) {
         throw new SdkClientException("Error parsing region metadata from resource " + name, var3);
      }
   }

   @Deprecated
   public static synchronized void initializeFromResource(ClassLoader classLoader, String name) {
      try {
         regionMetadata = loadMetadataFromResource(classLoader, name);
      } catch (IOException var3) {
         throw new SdkClientException("Error parsing region metadata from resource " + name, var3);
      }
   }
}
