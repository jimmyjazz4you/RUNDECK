package com.amazonaws.services.s3.transfer.internal.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompletedFuture<T> implements Future<T> {
   private final T result;

   public CompletedFuture(T result) {
      this.result = result;
   }

   @Override
   public boolean cancel(boolean mayInterruptIfRunning) {
      return false;
   }

   @Override
   public boolean isCancelled() {
      return false;
   }

   @Override
   public boolean isDone() {
      return true;
   }

   @Override
   public T get() {
      return this.result;
   }

   @Override
   public T get(long timeout, TimeUnit unit) {
      return this.result;
   }
}
