package com.amazonaws.regions;

import com.amazonaws.SdkClientException;

public abstract class AwsRegionProvider {
   public abstract String getRegion() throws SdkClientException;
}
