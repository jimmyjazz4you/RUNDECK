package com.amazonaws.client;

import com.amazonaws.Request;
import com.amazonaws.RequestConfig;
import com.amazonaws.SdkBaseException;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.transform.Marshaller;

@SdkProtectedApi
@NotThreadSafe
public class ClientExecutionParams<Input, Output> {
   private Input input;
   private Marshaller<Request<Input>, Input> marshaller;
   private HttpResponseHandler<Output> responseHandler;
   private HttpResponseHandler<? extends SdkBaseException> errorResponseHandler;
   private RequestConfig requestConfig;

   public Marshaller<Request<Input>, Input> getMarshaller() {
      return this.marshaller;
   }

   public ClientExecutionParams<Input, Output> withMarshaller(Marshaller<Request<Input>, Input> marshaller) {
      this.marshaller = marshaller;
      return this;
   }

   public Input getInput() {
      return this.input;
   }

   public ClientExecutionParams<Input, Output> withInput(Input input) {
      this.input = input;
      return this;
   }

   public HttpResponseHandler<Output> getResponseHandler() {
      return this.responseHandler;
   }

   public ClientExecutionParams<Input, Output> withResponseHandler(HttpResponseHandler<Output> responseHandler) {
      this.responseHandler = responseHandler;
      return this;
   }

   public HttpResponseHandler<? extends SdkBaseException> getErrorResponseHandler() {
      return this.errorResponseHandler;
   }

   public ClientExecutionParams<Input, Output> withErrorResponseHandler(HttpResponseHandler<? extends SdkBaseException> errorResponseHandler) {
      this.errorResponseHandler = errorResponseHandler;
      return this;
   }

   public RequestConfig getRequestConfig() {
      return this.requestConfig;
   }

   public ClientExecutionParams<Input, Output> withRequestConfig(RequestConfig requestConfig) {
      this.requestConfig = requestConfig;
      return this;
   }
}
