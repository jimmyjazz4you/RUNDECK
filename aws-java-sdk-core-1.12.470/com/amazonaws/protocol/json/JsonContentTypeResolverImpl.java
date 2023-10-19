package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
class JsonContentTypeResolverImpl implements JsonContentTypeResolver {
   private final String prefix;

   JsonContentTypeResolverImpl(String prefix) {
      this.prefix = prefix;
   }

   @Override
   public String resolveContentType(JsonClientMetadata metadata) {
      return metadata.getContentTypeOverride() != null ? metadata.getContentTypeOverride() : this.prefix + metadata.getProtocolVersion();
   }
}
