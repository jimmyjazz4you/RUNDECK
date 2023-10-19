package com.amazonaws.waiters;

import com.amazonaws.AmazonWebServiceRequest;

public abstract class WaiterHandler<Input extends AmazonWebServiceRequest> {
   public abstract void onWaitSuccess(Input var1);

   public abstract void onWaitFailure(Exception var1);
}
