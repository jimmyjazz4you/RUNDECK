package com.amazonaws.services.s3.model;

import com.amazonaws.regions.RegionUtils;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public enum Region {
   US_Standard((String[])null),
   US_East_2("us-east-2"),
   US_West("us-west-1"),
   US_West_2("us-west-2"),
   US_GovCloud("us-gov-west-1"),
   US_Gov_East_1("us-gov-east-1", "AWS GovCloud (US-East)"),
   EU_Ireland("eu-west-1", "EU"),
   EU_London("eu-west-2"),
   EU_Paris("eu-west-3"),
   EU_Frankfurt("eu-central-1"),
   EU_Zurich("eu-central-2"),
   EU_North_1("eu-north-1"),
   EU_South_1("eu-south-1"),
   EU_South_2("eu-south-2"),
   AP_HongKong("ap-east-1"),
   AP_Singapore("ap-southeast-1"),
   AP_Sydney("ap-southeast-2"),
   AP_Jakarta("ap-southeast-3"),
   AP_Melbourne("ap-southeast-4"),
   AP_Tokyo("ap-northeast-1"),
   AP_Seoul("ap-northeast-2"),
   AP_Osaka("ap-northeast-3"),
   AP_Mumbai("ap-south-1"),
   AP_Hyderabad("ap-south-2"),
   SA_SaoPaulo("sa-east-1"),
   CA_Central("ca-central-1"),
   CN_Beijing("cn-north-1"),
   CN_Northwest_1("cn-northwest-1"),
   ME_Bahrain("me-south-1"),
   ME_UAE("me-central-1"),
   AF_CapeTown("af-south-1"),
   US_ISO_EAST_1("us-iso-east-1"),
   US_ISOB_EAST_1("us-isob-east-1"),
   US_ISO_WEST_1("us-iso-west-1");

   public static final Pattern S3_REGIONAL_ENDPOINT_PATTERN = Pattern.compile("s3[-.]([^.]+)\\.amazonaws\\.com(\\.[^.]*)?");
   private final List<String> regionIds;

   private Region(String... regionIds) {
      this.regionIds = regionIds != null ? Arrays.asList(regionIds) : null;
   }

   @Override
   public String toString() {
      return this.getFirstRegionId0();
   }

   public String getFirstRegionId() {
      return this.getFirstRegionId0();
   }

   private String getFirstRegionId0() {
      return this.regionIds != null && this.regionIds.size() != 0 ? this.regionIds.get(0) : null;
   }

   public static Region fromValue(String s3RegionId) throws IllegalArgumentException {
      if (s3RegionId != null && !s3RegionId.equals("US") && !s3RegionId.equals("us-east-1")) {
         for(Region region : values()) {
            List<String> regionIds = region.regionIds;
            if (regionIds != null && regionIds.contains(s3RegionId)) {
               return region;
            }
         }

         throw new IllegalArgumentException("Cannot create enum from " + s3RegionId + " value!");
      } else {
         return US_Standard;
      }
   }

   public com.amazonaws.regions.Region toAWSRegion() {
      String s3regionId = this.getFirstRegionId();
      return s3regionId == null ? RegionUtils.getRegion("us-east-1") : RegionUtils.getRegion(s3regionId);
   }
}
