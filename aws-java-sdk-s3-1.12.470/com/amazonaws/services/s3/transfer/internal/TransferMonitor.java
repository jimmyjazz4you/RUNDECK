package com.amazonaws.services.s3.transfer.internal;

import java.util.concurrent.Future;

public interface TransferMonitor {
   Future<?> getFuture();

   boolean isDone();
}
