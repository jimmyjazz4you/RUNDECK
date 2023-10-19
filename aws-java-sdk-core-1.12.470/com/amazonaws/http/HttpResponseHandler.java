package com.amazonaws.http;

public interface HttpResponseHandler<T> {
   String X_AMZN_REQUEST_ID_HEADER = "x-amzn-RequestId";
   String X_AMZN_EXTENDED_REQUEST_ID_HEADER = "x-amz-id-2";
   String X_AMZ_REQUEST_ID_ALTERNATIVE_HEADER = "x-amz-request-id";
   String X_AMZN_QUERY_ERROR = "x-amzn-query-error";

   T handle(HttpResponse var1) throws Exception;

   boolean needsConnectionLeftOpen();
}
