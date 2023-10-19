package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
interface JsonContentTypeResolver {
   JsonContentTypeResolver ION_BINARY = new JsonContentTypeResolverImpl("application/x-amz-ion-");
   JsonContentTypeResolver ION_TEXT = new JsonContentTypeResolverImpl("text/x-amz-ion-");
   JsonContentTypeResolver CBOR = new JsonContentTypeResolverImpl("application/x-amz-cbor-");
   JsonContentTypeResolver JSON = new JsonContentTypeResolverImpl("application/x-amz-json-");

   String resolveContentType(JsonClientMetadata var1);
}
