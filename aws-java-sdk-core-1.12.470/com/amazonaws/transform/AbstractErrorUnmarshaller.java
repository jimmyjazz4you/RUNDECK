package com.amazonaws.transform;

import com.amazonaws.AmazonServiceException;
import java.lang.reflect.Constructor;

public abstract class AbstractErrorUnmarshaller<T> implements Unmarshaller<AmazonServiceException, T> {
   protected final Class<? extends AmazonServiceException> exceptionClass;

   public AbstractErrorUnmarshaller() {
      this(AmazonServiceException.class);
   }

   public AbstractErrorUnmarshaller(Class<? extends AmazonServiceException> exceptionClass) {
      this.exceptionClass = exceptionClass;
   }

   protected AmazonServiceException newException(String message) throws Exception {
      Constructor<? extends AmazonServiceException> constructor = this.exceptionClass.getConstructor(String.class);
      return constructor.newInstance(message);
   }
}
