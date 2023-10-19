package com.amazonaws.services.s3.transfer.internal.future;

import com.amazonaws.util.ValidationUtils;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DelegatingFuture<T> implements Future<T> {
   private final Object mutationLock = new Object();
   private volatile Future<T> delegate;
   private volatile DelegatingFuture.CancelState cancelState = DelegatingFuture.CancelState.NOT_CANCELLED;

   public void setDelegateIfUnset(Future<T> delegate) {
      if (!this.hasDelegate()) {
         synchronized(this.mutationLock) {
            if (!this.hasDelegate()) {
               this.setDelegate(delegate);
            }
         }
      }
   }

   public void setDelegate(Future<T> delegate) {
      synchronized(this.mutationLock) {
         ValidationUtils.assertAllAreNull("Delegate may only be set once.", new Object[]{this.delegate});
         switch(this.cancelState) {
            case NOT_CANCELLED:
               break;
            case CANCELLED:
               delegate.cancel(false);
               break;
            case CANCELLED_MAY_INTERRUPT:
               delegate.cancel(true);
               break;
            default:
               throw new IllegalStateException();
         }

         this.delegate = delegate;
         this.mutationLock.notifyAll();
      }
   }

   @Override
   public boolean cancel(boolean mayInterruptIfRunning) {
      Future<T> delegate = this.delegate;
      if (delegate != null) {
         return delegate.cancel(mayInterruptIfRunning);
      } else {
         synchronized(this.mutationLock) {
            delegate = this.delegate;
            if (delegate != null) {
               return delegate.cancel(mayInterruptIfRunning);
            } else if (this.cancelState != DelegatingFuture.CancelState.NOT_CANCELLED) {
               return false;
            } else {
               this.cancelState = mayInterruptIfRunning ? DelegatingFuture.CancelState.CANCELLED_MAY_INTERRUPT : DelegatingFuture.CancelState.CANCELLED;
               this.mutationLock.notifyAll();
               return true;
            }
         }
      }
   }

   @Override
   public boolean isCancelled() {
      Future<T> delegate = this.delegate;
      if (delegate != null) {
         return delegate.isCancelled();
      } else {
         return this.cancelState != DelegatingFuture.CancelState.NOT_CANCELLED;
      }
   }

   @Override
   public boolean isDone() {
      Future<T> delegate = this.delegate;
      if (delegate != null) {
         return delegate.isDone();
      } else {
         return this.cancelState != DelegatingFuture.CancelState.NOT_CANCELLED;
      }
   }

   @Override
   public T get() throws InterruptedException, ExecutionException {
      Future<T> delegate = this.delegate;
      if (delegate != null) {
         return delegate.get();
      } else {
         synchronized(this.mutationLock) {
            for(delegate = this.delegate; delegate == null; delegate = this.delegate) {
               if (this.cancelState != DelegatingFuture.CancelState.NOT_CANCELLED) {
                  throw new CancellationException("Future being waited on has been cancelled.");
               }

               this.mutationLock.wait();
            }
         }

         return delegate.get();
      }
   }

   @Override
   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      Future<T> delegate = this.delegate;
      if (delegate != null) {
         return delegate.get(timeout, unit);
      } else {
         long nanosToWait = unit.toNanos(timeout);
         long waitEndTime = System.nanoTime() + nanosToWait;
         synchronized(this.mutationLock) {
            for(delegate = this.delegate; delegate == null; delegate = this.delegate) {
               if (this.cancelState != DelegatingFuture.CancelState.NOT_CANCELLED) {
                  throw new CancellationException("Future being waited on has been cancelled.");
               }

               long totalNanosRemainingOnWait = this.nanosUntil(waitEndTime);
               long millisRemainingPart = TimeUnit.NANOSECONDS.toMillis(totalNanosRemainingOnWait);
               int nanosRemainingPart = toIntExact(totalNanosRemainingOnWait % 1000000L);
               this.mutationLock.wait(millisRemainingPart, nanosRemainingPart);
            }
         }

         return delegate.get(this.nanosUntil(waitEndTime), TimeUnit.NANOSECONDS);
      }
   }

   private boolean hasDelegate() {
      return this.delegate != null;
   }

   private long nanosUntil(long time) throws TimeoutException {
      long nanosRemainingOnWait = time - System.nanoTime();
      if (nanosRemainingOnWait <= 0L) {
         throw new TimeoutException("Timed out waiting for future.");
      } else {
         return nanosRemainingOnWait;
      }
   }

   private static int toIntExact(long value) {
      if ((long)((int)value) != value) {
         throw new ArithmeticException("integer overflow");
      } else {
         return (int)value;
      }
   }

   private static enum CancelState {
      NOT_CANCELLED,
      CANCELLED_MAY_INTERRUPT,
      CANCELLED;
   }
}
