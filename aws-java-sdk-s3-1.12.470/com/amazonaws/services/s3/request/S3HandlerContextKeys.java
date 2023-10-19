package com.amazonaws.services.s3.request;

import com.amazonaws.handlers.HandlerContextKey;

public class S3HandlerContextKeys {
   public static final HandlerContextKey<Boolean> IS_CHUNKED_ENCODING_DISABLED = new HandlerContextKey("IsChunkedEncodingDisabled");
   public static final HandlerContextKey<Boolean> IS_PAYLOAD_SIGNING_ENABLED = new HandlerContextKey("IsPayloadSigningEnabled");
}
