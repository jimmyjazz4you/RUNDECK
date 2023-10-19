package com.amazonaws.protocol.json;

import com.amazonaws.http.JsonErrorResponseHandler;
import com.amazonaws.http.JsonResponseHandler;
import com.amazonaws.internal.http.ErrorCodeParser;
import com.amazonaws.internal.http.JsonErrorCodeParser;
import com.amazonaws.internal.http.JsonErrorMessageParser;
import com.amazonaws.transform.JsonErrorUnmarshaller;
import com.amazonaws.transform.JsonUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import com.fasterxml.jackson.core.JsonFactory;
import java.util.List;
import java.util.Map;

public abstract class SdkStructuredJsonFactoryImpl implements SdkStructuredJsonFactory {
   private final JsonFactory jsonFactory;
   private final Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> unmarshallers;
   private final Map<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>> customTypeUnmarshallers;

   public SdkStructuredJsonFactoryImpl(
      JsonFactory jsonFactory,
      Map<Class<?>, Unmarshaller<?, JsonUnmarshallerContext>> unmarshallers,
      Map<JsonUnmarshallerContext.UnmarshallerType, Unmarshaller<?, JsonUnmarshallerContext>> customTypeUnmarshallers
   ) {
      this.jsonFactory = jsonFactory;
      this.unmarshallers = unmarshallers;
      this.customTypeUnmarshallers = customTypeUnmarshallers;
   }

   @Override
   public StructuredJsonGenerator createWriter(String contentType) {
      return this.createWriter(this.jsonFactory, contentType);
   }

   protected abstract StructuredJsonGenerator createWriter(JsonFactory var1, String var2);

   @Override
   public <T> JsonResponseHandler<T> createResponseHandler(
      JsonOperationMetadata operationMetadata, Unmarshaller<T, JsonUnmarshallerContext> responseUnmarshaller
   ) {
      return new JsonResponseHandler<>(
         responseUnmarshaller,
         this.unmarshallers,
         this.customTypeUnmarshallers,
         this.jsonFactory,
         operationMetadata.isHasStreamingSuccessResponse(),
         operationMetadata.isPayloadJson()
      );
   }

   @Override
   public JsonErrorResponseHandler createErrorResponseHandler(
      JsonErrorResponseMetadata jsonErrorResponseMetadata, List<JsonErrorUnmarshaller> errorUnmarshallers
   ) {
      boolean hasAwsQueryCompatible = jsonErrorResponseMetadata != null && jsonErrorResponseMetadata.getAwsQueryCompatible();
      String customErrorCodeFieldName = jsonErrorResponseMetadata != null ? jsonErrorResponseMetadata.getCustomErrorCodeFieldName() : null;
      return new JsonErrorResponseHandler(
         errorUnmarshallers,
         this.unmarshallers,
         this.customTypeUnmarshallers,
         this.getErrorCodeParser(customErrorCodeFieldName),
         hasAwsQueryCompatible,
         JsonErrorMessageParser.DEFAULT_ERROR_MESSAGE_PARSER,
         this.jsonFactory
      );
   }

   @Override
   public JsonErrorResponseHandler createErrorResponseHandler(List<JsonErrorUnmarshaller> errorUnmarshallers, String customErrorCodeFieldName) {
      return new JsonErrorResponseHandler(
         errorUnmarshallers,
         this.unmarshallers,
         this.customTypeUnmarshallers,
         this.getErrorCodeParser(customErrorCodeFieldName),
         JsonErrorMessageParser.DEFAULT_ERROR_MESSAGE_PARSER,
         this.jsonFactory
      );
   }

   protected ErrorCodeParser getErrorCodeParser(String customErrorCodeFieldName) {
      return new JsonErrorCodeParser(customErrorCodeFieldName);
   }
}
