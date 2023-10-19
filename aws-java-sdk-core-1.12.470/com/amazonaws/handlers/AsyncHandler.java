package com.amazonaws.handlers;

import com.amazonaws.AmazonWebServiceRequest;

public interface AsyncHandler<REQUEST extends AmazonWebServiceRequest, RESULT> {
   void onError(Exception var1);

   void onSuccess(REQUEST var1, RESULT var2);
}
