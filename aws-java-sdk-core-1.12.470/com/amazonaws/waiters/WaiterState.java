package com.amazonaws.waiters;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public enum WaiterState {
   SUCCESS,
   RETRY,
   FAILURE;
}
