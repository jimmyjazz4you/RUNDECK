package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.http.JsonErrorResponseHandler;
import com.amazonaws.http.JsonResponseHandler;
import com.amazonaws.transform.JsonErrorUnmarshaller;
import com.amazonaws.transform.JsonUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import java.util.List;

@SdkProtectedApi
public interface SdkStructuredJsonFactory {
   StructuredJsonGenerator createWriter(String var1);

   <T> JsonResponseHandler<T> createResponseHandler(JsonOperationMetadata var1, Unmarshaller<T, JsonUnmarshallerContext> var2);

   @Deprecated
   JsonErrorResponseHandler createErrorResponseHandler(List<JsonErrorUnmarshaller> var1, String var2);

   JsonErrorResponseHandler createErrorResponseHandler(JsonErrorResponseMetadata var1, List<JsonErrorUnmarshaller> var2);
}
