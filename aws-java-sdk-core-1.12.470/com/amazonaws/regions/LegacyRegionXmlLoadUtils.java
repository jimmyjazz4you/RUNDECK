package com.amazonaws.regions;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.RuntimeHttpUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Deprecated
@SdkInternalApi
public class LegacyRegionXmlLoadUtils {
   public static RegionMetadata load(URI uri, ClientConfiguration config) throws IOException {
      return RegionMetadataParser.parse(RuntimeHttpUtils.fetchFile(uri, config));
   }

   public static RegionMetadata load(File file) throws IOException {
      return RegionMetadataParser.parse(new BufferedInputStream(new FileInputStream(file)));
   }

   public static RegionMetadata load(InputStream stream) throws IOException {
      return RegionMetadataParser.parse(stream);
   }

   public static RegionMetadata load(Class<?> clazz, String name) throws IOException {
      InputStream stream = clazz.getResourceAsStream(name);
      if (stream == null) {
         throw new FileNotFoundException("No resource '" + name + "' found.");
      } else {
         return load(stream);
      }
   }

   public static RegionMetadata load(ClassLoader classLoader, String name) throws IOException {
      InputStream stream = classLoader.getResourceAsStream(name);
      if (stream == null) {
         throw new FileNotFoundException("No resource '" + name + "' found.");
      } else {
         return load(stream);
      }
   }
}
