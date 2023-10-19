package com.amazonaws.services.s3.transfer.internal.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureImpl<T> implements Future<T> {
   public final DelegatingFuture<T> delegatingFuture = new DelegatingFuture<>();

   public void complete(T value) {
      this.delegatingFuture.setDelegateIfUnset(new CompletedFuture<>(value));
   }

   public void fail(Throwable t) {
      this.delegatingFuture.setDelegateIfUnset(new FailedFuture<>(t));
   }

   @Override
   public boolean cancel(boolean mayInterruptIfRunning) {
      return this.delegatingFuture.cancel(mayInterruptIfRunning);
   }

   @Override
   public boolean isCancelled() {
      return this.delegatingFuture.isCancelled();
   }

   @Override
   public boolean isDone() {
      return this.delegatingFuture.isDone();
   }

   public T getOrThrowUnchecked(String errorMessage) {
      try {
         return this.get();
      } catch (Throwable var3) {
         throw new RuntimeException(errorMessage, var3);
      }
   }

   @Override
   public T get() throws InterruptedException, ExecutionException {
      return this.delegatingFuture.get();
   }

   @Override
   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegatingFuture.get(timeout, unit);
   }
}
