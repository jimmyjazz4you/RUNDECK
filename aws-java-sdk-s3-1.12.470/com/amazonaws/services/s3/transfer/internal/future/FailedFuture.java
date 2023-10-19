package com.amazonaws.services.s3.transfer.internal.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FailedFuture<T> implements Future<T> {
   private final ExecutionException exception;

   public FailedFuture(Throwable exception) {
      this.exception = new ExecutionException(exception);
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
   public T get() throws ExecutionException {
      throw this.exception;
   }

   @Override
   public T get(long timeout, TimeUnit unit) throws ExecutionException {
      throw this.exception;
   }
}
