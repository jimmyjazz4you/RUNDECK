package com.amazonaws;

import com.amazonaws.handlers.HandlerContextKey;

public interface HandlerContextAware {
   <X> void addHandlerContext(HandlerContextKey<X> var1, X var2);

   <X> X getHandlerContext(HandlerContextKey<X> var1);
}
