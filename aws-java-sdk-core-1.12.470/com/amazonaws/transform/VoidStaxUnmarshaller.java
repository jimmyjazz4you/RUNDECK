package com.amazonaws.transform;

public class VoidStaxUnmarshaller<T> implements Unmarshaller<T, StaxUnmarshallerContext> {
   public T unmarshall(StaxUnmarshallerContext context) throws Exception {
      while(!context.nextEvent().isEndDocument()) {
      }

      return null;
   }
}
