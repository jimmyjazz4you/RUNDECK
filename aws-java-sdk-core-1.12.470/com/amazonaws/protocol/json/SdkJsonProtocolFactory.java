package com.amazonaws.protocol.json;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.protocol.OperationInfo;
import com.amazonaws.protocol.Protocol;
import com.amazonaws.protocol.ProtocolRequestMarshaller;
import com.amazonaws.protocol.json.internal.EmptyBodyJsonMarshaller;
import com.amazonaws.transform.JsonErrorUnmarshaller;
import com.amazonaws.transform.JsonUnmarshallerContext;
import com.amazonaws.transform.Unmarshaller;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@SdkProtectedApi
public class SdkJsonProtocolFactory implements SdkJsonMarshallerFactory {
   private final JsonClientMetadata metadata;
   private final List<JsonErrorUnmarshaller> errorUnmarshallers = new ArrayList<>();

   public SdkJsonProtocolFactory(JsonClientMetadata metadata) {
      this.metadata = metadata;
      this.createErrorUnmarshallers();
   }

   @Override
   public StructuredJsonGenerator createGenerator() {
      return this.getSdkFactory().createWriter(this.getContentType());
   }

   @Override
   public String getContentType() {
      return this.getContentTypeResolver().resolveContentType(this.metadata);
   }

   public <T> ProtocolRequestMarshaller<T> createProtocolMarshaller(OperationInfo operationInfo, T origRequest) {
      return JsonProtocolMarshallerBuilder.<T>standard()
         .jsonGenerator(this.createGenerator(operationInfo))
         .contentType(this.getContentType())
         .operationInfo(operationInfo)
         .originalRequest(origRequest)
         .emptyBodyMarshaller(this.createEmptyBodyMarshaller(operationInfo))
         .build();
   }

   private StructuredJsonGenerator createGenerator(OperationInfo operationInfo) {
      return !operationInfo.hasPayloadMembers() && operationInfo.protocol() != Protocol.AWS_JSON ? StructuredJsonGenerator.NO_OP : this.createGenerator();
   }

   private EmptyBodyJsonMarshaller createEmptyBodyMarshaller(OperationInfo operationInfo) {
      if (operationInfo.protocol() == Protocol.API_GATEWAY) {
         throw new IllegalStateException("Detected the API_GATEWAY protocol which should not be used with this protocol factory.");
      } else {
         return operationInfo.hasPayloadMembers() && operationInfo.protocol() != Protocol.API_GATEWAY
            ? EmptyBodyJsonMarshaller.EMPTY
            : EmptyBodyJsonMarshaller.NULL;
      }
   }

   public <T> HttpResponseHandler<AmazonWebServiceResponse<T>> createResponseHandler(
      JsonOperationMetadata operationMetadata, Unmarshaller<T, JsonUnmarshallerContext> responseUnmarshaller
   ) {
      return this.getSdkFactory().createResponseHandler(operationMetadata, responseUnmarshaller);
   }

   public HttpResponseHandler<AmazonServiceException> createErrorResponseHandler(JsonErrorResponseMetadata errorResponsMetadata) {
      return this.getSdkFactory().createErrorResponseHandler(errorResponsMetadata, this.errorUnmarshallers);
   }

   private void createErrorUnmarshallers() {
      for(JsonErrorShapeMetadata errorMetadata : this.metadata.getErrorShapeMetadata()) {
         if (errorMetadata.getExceptionUnmarshaller() != null) {
            this.errorUnmarshallers.add(errorMetadata.getExceptionUnmarshaller());
         } else if (errorMetadata.getModeledClass() != null) {
            this.errorUnmarshallers.add(new JsonErrorUnmarshaller(errorMetadata.getModeledClass(), errorMetadata.getErrorCode()));
         }
      }

      if (this.metadata.getBaseServiceExceptionClass() != null) {
         this.errorUnmarshallers.add(new JsonErrorUnmarshaller(this.metadata.getBaseServiceExceptionClass(), null));
      }
   }

   private SdkStructuredJsonFactory getSdkFactory() {
      if (this.isCborEnabled()) {
         return SdkStructuredCborFactory.SDK_CBOR_FACTORY;
      } else if (this.isIonEnabled()) {
         return this.isIonBinaryEnabled() ? SdkStructuredIonFactory.SDK_ION_BINARY_FACTORY : SdkStructuredIonFactory.SDK_ION_TEXT_FACTORY;
      } else {
         return SdkStructuredPlainJsonFactory.SDK_JSON_FACTORY;
      }
   }

   private JsonContentTypeResolver getContentTypeResolver() {
      if (this.isCborEnabled()) {
         return JsonContentTypeResolver.CBOR;
      } else if (this.isIonEnabled()) {
         return this.isIonBinaryEnabled() ? JsonContentTypeResolver.ION_BINARY : JsonContentTypeResolver.ION_TEXT;
      } else {
         return JsonContentTypeResolver.JSON;
      }
   }

   private boolean isCborEnabled() {
      return this.metadata.isSupportsCbor() && !SDKGlobalConfiguration.isCborDisabled();
   }

   private boolean isIonEnabled() {
      return this.metadata.isSupportsIon();
   }

   boolean isIonBinaryEnabled() {
      return !SDKGlobalConfiguration.isIonBinaryDisabled();
   }
}
