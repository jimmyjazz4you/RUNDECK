package com.amazonaws.services.s3;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.arn.Arn;
import com.amazonaws.arn.ArnConverter;
import com.amazonaws.arn.ArnResource;
import com.amazonaws.services.s3.internal.IntermediateOutpostResource;
import com.amazonaws.services.s3.internal.OutpostResourceType;
import com.amazonaws.services.s3.internal.S3ObjectLambdasResource;
import com.amazonaws.services.s3.internal.S3OutpostResource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SdkInternalApi
public class S3ArnConverter implements ArnConverter<S3Resource> {
   private static final S3ArnConverter INSTANCE = new S3ArnConverter();
   private static final Pattern OBJECT_AP_PATTERN = Pattern.compile("^([0-9a-zA-Z-]+)/object/(.*)$");
   private static final String OBJECT_LAMBDAS_SERVICE = "s3-object-lambda";

   private S3ArnConverter() {
   }

   public static S3ArnConverter getInstance() {
      return INSTANCE;
   }

   public S3Resource convertArn(Arn arn) {
      if (isV1Arn(arn)) {
         return this.convertV1Arn(arn);
      } else {
         S3ResourceType s3ResourceType;
         try {
            s3ResourceType = S3ResourceType.fromValue(arn.getResource().getResourceType());
         } catch (IllegalArgumentException var4) {
            throw new IllegalArgumentException("Unknown ARN type '" + arn.getResource().getResourceType() + "'");
         }

         switch(s3ResourceType) {
            case ACCESS_POINT:
               return this.parseS3AccessPointArn(arn);
            case BUCKET:
               return this.parseS3BucketArn(arn);
            case OUTPOST:
               return this.parseS3OutpostAccessPointArn(arn);
            default:
               throw new IllegalArgumentException("Unknown ARN type '" + arn.getResource().getResourceType() + "'");
         }
      }
   }

   private S3Resource convertV1Arn(Arn arn) {
      String resource = arn.getResourceAsString();
      String[] splitResource = resource.split("/", 2);
      if (splitResource.length > 1) {
         S3BucketResource parentBucket = S3BucketResource.builder().withPartition(arn.getPartition()).withBucketName(splitResource[0]).build();
         return S3ObjectResource.builder().withParentS3Resource(parentBucket).withKey(splitResource[1]).build();
      } else {
         return S3BucketResource.builder().withPartition(arn.getPartition()).withBucketName(resource).build();
      }
   }

   private S3Resource parseS3OutpostAccessPointArn(Arn arn) {
      IntermediateOutpostResource intermediateOutpostResource = S3ArnUtils.parseOutpostArn(arn);
      ArnResource outpostSubResource = intermediateOutpostResource.getOutpostSubresource();
      if (!OutpostResourceType.OUTPOST_ACCESS_POINT.toString().equals(outpostSubResource.getResourceType())) {
         throw new IllegalArgumentException("Unknown outpost ARN type '" + outpostSubResource.getResourceType() + "'");
      } else {
         return S3AccessPointResource.builder()
            .withAccessPointName(outpostSubResource.getResource())
            .withParentS3Resource(
               S3OutpostResource.builder()
                  .withPartition(arn.getPartition())
                  .withRegion(arn.getRegion())
                  .withAccountId(arn.getAccountId())
                  .withOutpostId(intermediateOutpostResource.getOutpostId())
                  .build()
            )
            .build();
      }
   }

   private S3BucketResource parseS3BucketArn(Arn arn) {
      return S3BucketResource.builder()
         .withPartition(arn.getPartition())
         .withRegion(arn.getRegion())
         .withAccountId(arn.getAccountId())
         .withBucketName(arn.getResource().getResource())
         .build();
   }

   private S3Resource parseS3AccessPointArn(Arn arn) {
      Matcher objectMatcher = OBJECT_AP_PATTERN.matcher(arn.getResource().getResource());
      if (objectMatcher.matches()) {
         String accessPointName = objectMatcher.group(1);
         String objectKey = objectMatcher.group(2);
         S3AccessPointResource parentResource = S3AccessPointResource.builder()
            .withPartition(arn.getPartition())
            .withRegion(arn.getRegion())
            .withAccountId(arn.getAccountId())
            .withAccessPointName(accessPointName)
            .build();
         return S3ObjectResource.builder().withParentS3Resource(parentResource).withKey(objectKey).build();
      } else {
         return (S3Resource)("s3-object-lambda".equals(arn.getService())
            ? this.parseS3ObjectLambdasAccessPointArn(arn)
            : S3AccessPointResource.builder()
               .withPartition(arn.getPartition())
               .withRegion(arn.getRegion())
               .withAccountId(arn.getAccountId())
               .withAccessPointName(arn.getResource().getResource())
               .build());
      }
   }

   private S3Resource parseS3ObjectLambdasAccessPointArn(Arn arn) {
      S3ObjectLambdasResource objectLambdasResource = S3ObjectLambdasResource.builder()
         .withAccountId(arn.getAccountId())
         .withRegion(arn.getRegion())
         .withPartition(arn.getPartition())
         .withAccessPointName(arn.getResource().getResource())
         .build();
      return S3AccessPointResource.builder()
         .withAccessPointName(objectLambdasResource.getAccessPointName())
         .withParentS3Resource(objectLambdasResource)
         .build();
   }

   private static boolean isV1Arn(Arn arn) {
      return arn.getAccountId() == null && arn.getRegion() == null;
   }
}
