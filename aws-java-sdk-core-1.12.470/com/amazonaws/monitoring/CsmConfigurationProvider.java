package com.amazonaws.monitoring;

import com.amazonaws.SdkClientException;

public interface CsmConfigurationProvider {
   CsmConfiguration getConfiguration() throws SdkClientException;
}
