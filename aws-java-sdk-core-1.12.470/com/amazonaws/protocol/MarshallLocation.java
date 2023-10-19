package com.amazonaws.protocol;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public enum MarshallLocation {
   PAYLOAD,
   QUERY_PARAM,
   HEADER,
   PATH,
   GREEDY_PATH;
}
