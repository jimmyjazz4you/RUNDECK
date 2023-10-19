package com.amazonaws.regions;

import com.amazonaws.SdkClientException;

public class AwsSystemPropertyRegionProvider extends AwsRegionProvider {
   @Override
   public String getRegion() throws SdkClientException {
      return System.getProperty("aws.region");
   }
}
