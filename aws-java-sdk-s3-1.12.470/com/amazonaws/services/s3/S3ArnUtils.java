package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.arn.Arn;
import com.amazonaws.arn.ArnResource;
import com.amazonaws.services.s3.internal.IntermediateOutpostResource;
import com.amazonaws.util.StringUtils;

@SdkInternalApi
public class S3ArnUtils {
   private static final int OUTPOST_ID_START_INDEX = "outpost".length() + 1;

   private S3ArnUtils() {
   }

   public static S3AccessPointResource parseS3AccessPointArn(Arn arn) {
      return S3AccessPointResource.builder()
         .withPartition(arn.getPartition())
         .withRegion(arn.getRegion())
         .withAccountId(arn.getAccountId())
         .withAccessPointName(arn.getResource().getResource())
         .build();
   }

   public static IntermediateOutpostResource parseOutpostArn(Arn arn) {
      String resource = arn.getResourceAsString();
      Integer outpostIdEndIndex = null;

      for(int i = OUTPOST_ID_START_INDEX; i < resource.length(); ++i) {
         char ch = resource.charAt(i);
         if (ch == ':' || ch == '/') {
            outpostIdEndIndex = i;
            break;
         }
      }

      if (outpostIdEndIndex == null) {
         throw new IllegalArgumentException("Invalid format for S3 outpost ARN, missing outpostId");
      } else {
         String outpostId = resource.substring(OUTPOST_ID_START_INDEX, outpostIdEndIndex);
         if (StringUtils.isNullOrEmpty(outpostId)) {
            throw new IllegalArgumentException("Invalid format for S3 outpost ARN, missing outpostId");
         } else {
            String subresource = resource.substring(outpostIdEndIndex + 1);
            if (StringUtils.isNullOrEmpty(subresource)) {
               throw new IllegalArgumentException("Invalid format for S3 outpost ARN");
            } else {
               return IntermediateOutpostResource.builder().withOutpostId(outpostId).withOutpostSubresource(ArnResource.fromString(subresource)).build();
            }
         }
      }
   }
}
