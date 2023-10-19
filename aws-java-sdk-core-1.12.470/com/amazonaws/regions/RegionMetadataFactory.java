package com.amazonaws.regions;

import com.amazonaws.partitions.PartitionsLoader;

public class RegionMetadataFactory {
   private RegionMetadataFactory() {
   }

   public static RegionMetadata create() {
      RegionMetadata metadata = createLegacyXmlRegionMetadata();
      if (metadata == null) {
         metadata = new RegionMetadata(new PartitionsLoader().build());
      }

      return metadata;
   }

   private static RegionMetadata createLegacyXmlRegionMetadata() {
      return new LegacyRegionXmlMetadataBuilder().build();
   }
}
