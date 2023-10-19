package com.amazonaws.waiters;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public class NoOpWaiterHandler extends WaiterHandler<AmazonWebServiceRequest> {
   @Override
   public void onWaitSuccess(AmazonWebServiceRequest request) {
   }

   @Override
   public void onWaitFailure(Exception e) {
   }
}
