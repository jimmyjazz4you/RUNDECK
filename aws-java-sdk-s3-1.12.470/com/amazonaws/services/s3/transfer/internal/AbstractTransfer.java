package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.event.ProgressListenerChain;
import com.amazonaws.event.SDKProgressPublisher;
import com.amazonaws.services.s3.model.LegacyS3ProgressListener;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferProgress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class AbstractTransfer implements Transfer {
   protected volatile Transfer.TransferState state = Transfer.TransferState.Waiting;
   protected TransferMonitor monitor;
   private final TransferProgress transferProgress;
   private final String description;
   protected final ProgressListenerChain listenerChain;
   protected final Collection<TransferStateChangeListener> stateChangeListeners = new LinkedList<>();

   AbstractTransfer(String description, TransferProgress transferProgress, ProgressListenerChain progressListenerChain) {
      this(description, transferProgress, progressListenerChain, null);
   }

   AbstractTransfer(
      String description, TransferProgress transferProgress, ProgressListenerChain progressListenerChain, TransferStateChangeListener stateChangeListener
   ) {
      this.description = description;
      this.listenerChain = progressListenerChain;
      this.transferProgress = transferProgress;
      this.addStateChangeListener(stateChangeListener);
   }

   @Override
   public final synchronized boolean isDone() {
      return this.state == Transfer.TransferState.Failed || this.state == Transfer.TransferState.Completed || this.state == Transfer.TransferState.Canceled;
   }

   @Override
   public void waitForCompletion() throws AmazonClientException, AmazonServiceException, InterruptedException {
      Future<?> f;
      try {
         for(Object result = null; !this.monitor.isDone() || result == null; result = f.get()) {
            f = this.monitor.getFuture();
         }
      } catch (ExecutionException var3) {
         this.rethrowExecutionException(var3);
      }
   }

   @Override
   public AmazonClientException waitForException() throws InterruptedException {
      try {
         while(!this.monitor.isDone()) {
            this.monitor.getFuture().get();
         }

         this.monitor.getFuture().get();
         return null;
      } catch (ExecutionException var2) {
         return this.unwrapExecutionException(var2);
      }
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public synchronized Transfer.TransferState getState() {
      return this.state;
   }

   public void setState(Transfer.TransferState state) {
      synchronized(this) {
         this.state = state;
      }

      for(TransferStateChangeListener listener : this.stateChangeListeners) {
         listener.transferStateChanged(this, state);
      }
   }

   public void notifyStateChangeListeners(Transfer.TransferState state) {
      for(TransferStateChangeListener listener : this.stateChangeListeners) {
         listener.transferStateChanged(this, state);
      }
   }

   @Override
   public synchronized void addProgressListener(ProgressListener listener) {
      this.listenerChain.addProgressListener(listener);
   }

   @Override
   public synchronized void removeProgressListener(ProgressListener listener) {
      this.listenerChain.removeProgressListener(listener);
   }

   @Deprecated
   @Override
   public synchronized void addProgressListener(com.amazonaws.services.s3.model.ProgressListener listener) {
      this.listenerChain.addProgressListener(new LegacyS3ProgressListener(listener));
   }

   @Deprecated
   @Override
   public synchronized void removeProgressListener(com.amazonaws.services.s3.model.ProgressListener listener) {
      this.listenerChain.removeProgressListener(new LegacyS3ProgressListener(listener));
   }

   public synchronized void addStateChangeListener(TransferStateChangeListener listener) {
      if (listener != null) {
         this.stateChangeListeners.add(listener);
      }
   }

   public synchronized void removeStateChangeListener(TransferStateChangeListener listener) {
      if (listener != null) {
         this.stateChangeListeners.remove(listener);
      }
   }

   @Override
   public TransferProgress getProgress() {
      return this.transferProgress;
   }

   public void setMonitor(TransferMonitor monitor) {
      this.monitor = monitor;
   }

   public TransferMonitor getMonitor() {
      return this.monitor;
   }

   protected void fireProgressEvent(ProgressEventType eventType) {
      SDKProgressPublisher.publishProgress(this.listenerChain, eventType);
   }

   protected void rethrowExecutionException(ExecutionException e) {
      throw this.unwrapExecutionException(e);
   }

   protected AmazonClientException unwrapExecutionException(ExecutionException e) {
      Throwable t = e;

      while(t.getCause() != null && t instanceof ExecutionException) {
         t = t.getCause();
      }

      return t instanceof AmazonClientException ? (AmazonClientException)t : new AmazonClientException("Unable to complete transfer: " + t.getMessage(), t);
   }
}
