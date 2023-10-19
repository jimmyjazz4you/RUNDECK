package com.amazonaws.regions;

import com.amazonaws.SdkClientException;

public class AwsEnvVarOverrideRegionProvider extends AwsRegionProvider {
   @Override
   public String getRegion() throws SdkClientException {
      return System.getenv("AWS_REGION");
   }
}
