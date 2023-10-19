package com.amazonaws.protocol.json.internal;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.protocol.MarshallingInfo;
import com.amazonaws.protocol.MarshallingType;
import com.amazonaws.protocol.OperationInfo;
import com.amazonaws.protocol.ProtocolRequestMarshaller;
import com.amazonaws.protocol.json.StructuredJsonGenerator;
import com.amazonaws.util.BinaryUtils;
import com.amazonaws.util.UriResourcePathUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

@SdkInternalApi
public class JsonProtocolMarshaller<OrigRequest> implements ProtocolRequestMarshaller<OrigRequest> {
   private static final MarshallerRegistry DEFAULT_MARSHALLER_REGISTRY = createDefaultMarshallerRegistry();
   private final StructuredJsonGenerator jsonGenerator;
   private final Request<OrigRequest> request;
   private final String contentType;
   private final boolean hasExplicitPayloadMember;
   private final JsonMarshallerContext marshallerContext;
   private final MarshallerRegistry marshallerRegistry;

   public JsonProtocolMarshaller(
      StructuredJsonGenerator jsonGenerator,
      String contentType,
      OperationInfo operationInfo,
      OrigRequest originalRequest,
      MarshallerRegistry.Builder marshallerRegistryOverrides,
      EmptyBodyJsonMarshaller emptyBodyMarshaller
   ) {
      this.jsonGenerator = jsonGenerator;
      this.contentType = contentType;
      this.hasExplicitPayloadMember = operationInfo.hasExplicitPayloadMember();
      this.request = this.fillBasicRequestParams(operationInfo, originalRequest);
      this.marshallerRegistry = DEFAULT_MARSHALLER_REGISTRY.merge(marshallerRegistryOverrides);
      this.marshallerContext = JsonMarshallerContext.builder()
         .jsonGenerator(jsonGenerator)
         .marshallerRegistry(this.marshallerRegistry)
         .protocolHandler(this)
         .request(this.request)
         .emptyBodyJsonMarshaller(emptyBodyMarshaller)
         .build();
   }

   private Request<OrigRequest> fillBasicRequestParams(OperationInfo operationInfo, OrigRequest originalRequest) {
      Request<OrigRequest> request = this.createRequest(operationInfo, originalRequest);
      request.setHttpMethod(operationInfo.httpMethodName());
      request.setResourcePath(UriResourcePathUtils.addStaticQueryParamtersToRequest(request, operationInfo.requestUri()));
      if (operationInfo.operationIdentifier() != null) {
         request.addHeader("X-Amz-Target", operationInfo.operationIdentifier());
      }

      return request;
   }

   private DefaultRequest<OrigRequest> createRequest(OperationInfo operationInfo, OrigRequest originalRequest) {
      return originalRequest instanceof AmazonWebServiceRequest
         ? new DefaultRequest<>((AmazonWebServiceRequest)originalRequest, operationInfo.serviceName())
         : new DefaultRequest<>(operationInfo.serviceName());
   }

   private static MarshallerRegistry createDefaultMarshallerRegistry() {
      return MarshallerRegistry.builder()
         .payloadMarshaller(MarshallingType.STRING, SimpleTypeJsonMarshallers.STRING)
         .payloadMarshaller(MarshallingType.JSON_VALUE, SimpleTypeJsonMarshallers.STRING)
         .payloadMarshaller(MarshallingType.INTEGER, SimpleTypeJsonMarshallers.INTEGER)
         .payloadMarshaller(MarshallingType.LONG, SimpleTypeJsonMarshallers.LONG)
         .payloadMarshaller(MarshallingType.SHORT, SimpleTypeJsonMarshallers.SHORT)
         .payloadMarshaller(MarshallingType.DOUBLE, SimpleTypeJsonMarshallers.DOUBLE)
         .payloadMarshaller(MarshallingType.FLOAT, SimpleTypeJsonMarshallers.FLOAT)
         .payloadMarshaller(MarshallingType.BIG_DECIMAL, SimpleTypeJsonMarshallers.BIG_DECIMAL)
         .payloadMarshaller(MarshallingType.BOOLEAN, SimpleTypeJsonMarshallers.BOOLEAN)
         .payloadMarshaller(MarshallingType.DATE, SimpleTypeJsonMarshallers.DATE)
         .payloadMarshaller(MarshallingType.BYTE_BUFFER, SimpleTypeJsonMarshallers.BYTE_BUFFER)
         .payloadMarshaller(MarshallingType.STRUCTURED, SimpleTypeJsonMarshallers.STRUCTURED)
         .payloadMarshaller(MarshallingType.LIST, SimpleTypeJsonMarshallers.LIST)
         .payloadMarshaller(MarshallingType.MAP, SimpleTypeJsonMarshallers.MAP)
         .payloadMarshaller(MarshallingType.NULL, SimpleTypeJsonMarshallers.NULL)
         .headerMarshaller(MarshallingType.STRING, HeaderMarshallers.STRING)
         .headerMarshaller(MarshallingType.JSON_VALUE, HeaderMarshallers.JSON_VALUE)
         .headerMarshaller(MarshallingType.INTEGER, HeaderMarshallers.INTEGER)
         .headerMarshaller(MarshallingType.LONG, HeaderMarshallers.LONG)
         .headerMarshaller(MarshallingType.DOUBLE, HeaderMarshallers.DOUBLE)
         .headerMarshaller(MarshallingType.FLOAT, HeaderMarshallers.FLOAT)
         .headerMarshaller(MarshallingType.BOOLEAN, HeaderMarshallers.BOOLEAN)
         .headerMarshaller(MarshallingType.DATE, HeaderMarshallers.DATE)
         .headerMarshaller(MarshallingType.NULL, JsonMarshaller.NULL)
         .queryParamMarshaller(MarshallingType.STRING, QueryParamMarshallers.STRING)
         .queryParamMarshaller(MarshallingType.INTEGER, QueryParamMarshallers.INTEGER)
         .queryParamMarshaller(MarshallingType.LONG, QueryParamMarshallers.LONG)
         .queryParamMarshaller(MarshallingType.SHORT, QueryParamMarshallers.SHORT)
         .queryParamMarshaller(MarshallingType.DOUBLE, QueryParamMarshallers.DOUBLE)
         .queryParamMarshaller(MarshallingType.FLOAT, QueryParamMarshallers.FLOAT)
         .queryParamMarshaller(MarshallingType.BOOLEAN, QueryParamMarshallers.BOOLEAN)
         .queryParamMarshaller(MarshallingType.DATE, QueryParamMarshallers.DATE)
         .queryParamMarshaller(MarshallingType.LIST, QueryParamMarshallers.LIST)
         .queryParamMarshaller(MarshallingType.MAP, QueryParamMarshallers.MAP)
         .queryParamMarshaller(MarshallingType.NULL, JsonMarshaller.NULL)
         .pathParamMarshaller(MarshallingType.STRING, SimpleTypePathMarshallers.STRING)
         .pathParamMarshaller(MarshallingType.INTEGER, SimpleTypePathMarshallers.INTEGER)
         .pathParamMarshaller(MarshallingType.LONG, SimpleTypePathMarshallers.LONG)
         .pathParamMarshaller(MarshallingType.NULL, SimpleTypePathMarshallers.NULL)
         .greedyPathParamMarshaller(MarshallingType.STRING, SimpleTypePathMarshallers.GREEDY_STRING)
         .greedyPathParamMarshaller(MarshallingType.NULL, SimpleTypePathMarshallers.NULL)
         .build();
   }

   @Override
   public void startMarshalling() {
      if (!this.hasExplicitPayloadMember) {
         this.jsonGenerator.writeStartObject();
      }
   }

   @Override
   public <V> void marshall(V val, MarshallingInfo<V> marshallingInfo) {
      this.doMarshall(this.resolveValue(val, marshallingInfo), marshallingInfo);
   }

   private <V> V resolveValue(V val, MarshallingInfo<V> marshallingInfo) {
      return (V)(val == null && marshallingInfo.defaultValueSupplier() != null ? marshallingInfo.defaultValueSupplier().get() : val);
   }

   private <V> void doMarshall(V val, MarshallingInfo<V> marshallingInfo) {
      if (marshallingInfo.isBinary()) {
         this.marshallBinaryPayload(val);
      } else {
         this.marshallerRegistry
            .getMarshaller(marshallingInfo.marshallLocation(), marshallingInfo.marshallingType(), val)
            .marshall(val, this.marshallerContext, marshallingInfo);
      }
   }

   private void marshallBinaryPayload(Object val) {
      if (val instanceof ByteBuffer) {
         this.request.setContent(BinaryUtils.toStream((ByteBuffer)val));
      } else if (val instanceof InputStream) {
         this.request.setContent((InputStream)val);
      }
   }

   @Override
   public Request<OrigRequest> finishMarshalling() {
      if (this.request.getContent() == null) {
         if (!this.hasExplicitPayloadMember) {
            this.jsonGenerator.writeEndObject();
         }

         byte[] content = this.jsonGenerator.getBytes();
         this.request.setContent(new ByteArrayInputStream(content));
         if (content.length > 0) {
            this.request.addHeader("Content-Length", Integer.toString(content.length));
         }
      }

      if (!this.request.getHeaders().containsKey("Content-Type") && this.contentType != null && this.request.getHeaders().containsKey("Content-Length")) {
         this.request.addHeader("Content-Type", this.contentType);
      }

      return this.request;
   }
}
