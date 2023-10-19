package com.amazonaws.services.s3.transfer;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventFilter;
import com.amazonaws.event.ProgressEventType;

final class TransferCompletionFilter implements ProgressEventFilter {
   public ProgressEvent filter(ProgressEvent progressEvent) {
      return progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT ? null : progressEvent;
   }
}
