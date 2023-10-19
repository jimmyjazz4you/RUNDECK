package com.amazonaws.protocol.json;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkProtectedApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotThreadSafe
@SdkProtectedApi
public class JsonClientMetadata {
   private final List<JsonErrorShapeMetadata> errorsMetadata = new ArrayList<>();
   private String protocolVersion;
   private String contentTypeOverride;
   private boolean supportsCbor;
   private boolean supportsIon;
   private Class<? extends RuntimeException> baseServiceExceptionClass = AmazonServiceException.class;

   public JsonClientMetadata addErrorMetadata(JsonErrorShapeMetadata errorShapeMetadata) {
      this.errorsMetadata.add(errorShapeMetadata);
      return this;
   }

   public JsonClientMetadata addAllErrorMetadata(JsonErrorShapeMetadata... errorShapeMetadata) {
      Collections.addAll(this.errorsMetadata, errorShapeMetadata);
      return this;
   }

   public List<JsonErrorShapeMetadata> getErrorShapeMetadata() {
      return this.errorsMetadata;
   }

   public String getProtocolVersion() {
      return this.protocolVersion;
   }

   public JsonClientMetadata withProtocolVersion(String protocolVersion) {
      this.protocolVersion = protocolVersion;
      return this;
   }

   public String getContentTypeOverride() {
      return this.contentTypeOverride;
   }

   public JsonClientMetadata withContentTypeOverride(String contentType) {
      this.contentTypeOverride = contentType;
      return this;
   }

   public boolean isSupportsCbor() {
      return this.supportsCbor;
   }

   public JsonClientMetadata withSupportsCbor(boolean supportsCbor) {
      this.supportsCbor = supportsCbor;
      return this;
   }

   public Class<? extends RuntimeException> getBaseServiceExceptionClass() {
      return this.baseServiceExceptionClass;
   }

   public boolean isSupportsIon() {
      return this.supportsIon;
   }

   public JsonClientMetadata withSupportsIon(boolean supportsIon) {
      this.supportsIon = supportsIon;
      return this;
   }

   public JsonClientMetadata withBaseServiceExceptionClass(Class<? extends RuntimeException> baseServiceExceptionClass) {
      this.baseServiceExceptionClass = baseServiceExceptionClass;
      return this;
   }
}
