package com.amazonaws.transform;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.annotation.SdkInternalApi;

@SdkInternalApi
public abstract class EnhancedJsonErrorUnmarshaller extends JsonErrorUnmarshaller {
   public EnhancedJsonErrorUnmarshaller(Class<? extends AmazonServiceException> exceptionClass, String handledErrorCode) {
      super(exceptionClass, handledErrorCode);
   }

   public abstract AmazonServiceException unmarshallFromContext(JsonUnmarshallerContext var1) throws Exception;
}
