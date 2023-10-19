package com.amazonaws.protocol;

final class DefaultMarshallingType<T> implements MarshallingType<T> {
   private final Class<T> type;

   protected DefaultMarshallingType(Class<T> type) {
      this.type = type;
   }

   @Override
   public boolean isDefaultMarshallerForType(Class<?> type) {
      return this.type.isAssignableFrom(type);
   }
}
