package com.amazonaws.handlers;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.HttpResponse;

@SdkInternalApi
public interface IRequestHandler2 {
   AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest var1);

   AmazonWebServiceRequest beforeMarshalling(AmazonWebServiceRequest var1);

   void beforeRequest(Request<?> var1);

   void beforeAttempt(HandlerBeforeAttemptContext var1);

   HttpResponse beforeUnmarshalling(Request<?> var1, HttpResponse var2);

   void afterAttempt(HandlerAfterAttemptContext var1);

   void afterResponse(Request<?> var1, Response<?> var2);

   void afterError(Request<?> var1, Response<?> var2, Exception var3);
}
