package com.amazonaws.partitions;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.partitions.model.Partitions;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

@SdkInternalApi
public class PartitionsLoader {
   public static final String PARTITIONS_RESOURCE_PATH = "com/amazonaws/partitions/endpoints.json";
   public static final String PARTITIONS_OVERRIDE_RESOURCE_PATH = "com/amazonaws/partitions/override/endpoints.json";
   private static final ObjectMapper mapper = new ObjectMapper()
      .disable(new MapperFeature[]{MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS})
      .disable(new MapperFeature[]{MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS})
      .enable(new Feature[]{Feature.ALLOW_COMMENTS})
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
   private final ClassLoader classLoader = PartitionsLoader.class.getClassLoader();

   public PartitionMetadataProvider build() {
      InputStream stream = this.classLoader.getResourceAsStream("com/amazonaws/partitions/override/endpoints.json");
      if (stream != null) {
         return new PartitionMetadataProvider(this.loadPartitionFromStream(stream, "com/amazonaws/partitions/override/endpoints.json").getPartitions());
      } else {
         stream = this.classLoader.getResourceAsStream("com/amazonaws/partitions/endpoints.json");
         if (stream == null) {
            throw new SdkClientException("Unable to load partition metadata from com/amazonaws/partitions/endpoints.json");
         } else {
            return new PartitionMetadataProvider(this.loadPartitionFromStream(stream, "com/amazonaws/partitions/endpoints.json").getPartitions());
         }
      }
   }

   private Partitions loadPartitionFromStream(InputStream stream, String location) {
      Partitions e;
      try {
         e = (Partitions)mapper.readValue(stream, Partitions.class);
      } catch (IOException var7) {
         throw new SdkClientException("Error while loading partitions file from " + location, var7);
      } finally {
         IOUtils.closeQuietly(stream, null);
      }

      return e;
   }
}
