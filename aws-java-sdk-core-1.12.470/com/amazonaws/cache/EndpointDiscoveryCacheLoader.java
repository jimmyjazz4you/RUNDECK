package com.amazonaws.cache;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public interface EndpointDiscoveryCacheLoader<K, V> {
   V load(K var1, AmazonWebServiceRequest var2);
}
