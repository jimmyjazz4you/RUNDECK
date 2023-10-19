package com.amazonaws;

import com.amazonaws.http.HttpResponse;

public final class Response<T> {
   private final T response;
   private final HttpResponse httpResponse;

   public Response(T response, HttpResponse httpResponse) {
      this.response = response;
      this.httpResponse = httpResponse;
   }

   public T getAwsResponse() {
      return this.response;
   }

   public HttpResponse getHttpResponse() {
      return this.httpResponse;
   }
}
