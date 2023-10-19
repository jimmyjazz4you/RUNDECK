package com.amazonaws.event;

import com.amazonaws.annotation.Immutable;
import java.util.EnumMap;
import java.util.Map;

@Immutable
public class ProgressEvent {
   private final long bytes;
   private final ProgressEventType eventType;
   @Deprecated
   public static final int PREPARING_EVENT_CODE = 1;
   @Deprecated
   public static final int STARTED_EVENT_CODE = 2;
   @Deprecated
   public static final int COMPLETED_EVENT_CODE = 4;
   @Deprecated
   public static final int FAILED_EVENT_CODE = 8;
   @Deprecated
   public static final int CANCELED_EVENT_CODE = 16;
   @Deprecated
   public static final int RESET_EVENT_CODE = 32;
   @Deprecated
   public static final int PART_STARTED_EVENT_CODE = 1024;
   @Deprecated
   public static final int PART_COMPLETED_EVENT_CODE = 2048;
   @Deprecated
   public static final int PART_FAILED_EVENT_CODE = 4096;
   private static final Map<ProgressEventType, Integer> legacyEventCodes = new EnumMap<>(ProgressEventType.class);

   @Deprecated
   public ProgressEvent(long bytes) {
      this(ProgressEventType.BYTE_TRANSFER_EVENT, bytes);
   }

   public ProgressEvent(ProgressEventType eventType) {
      this(eventType, 0L);
   }

   public ProgressEvent(ProgressEventType eventType, long bytes) {
      if (eventType == null) {
         throw new IllegalArgumentException("eventType must not be null.");
      } else if (bytes < 0L) {
         throw new IllegalArgumentException("bytes reported must be non-negative");
      } else {
         this.eventType = eventType;
         this.bytes = bytes;
      }
   }

   public long getBytes() {
      return this.bytes;
   }

   public long getBytesTransferred() {
      switch(this.eventType) {
         case REQUEST_BYTE_TRANSFER_EVENT:
         case RESPONSE_BYTE_TRANSFER_EVENT:
            return this.bytes;
         case HTTP_RESPONSE_CONTENT_RESET_EVENT:
         case HTTP_REQUEST_CONTENT_RESET_EVENT:
         case RESPONSE_BYTE_DISCARD_EVENT:
            return 0L - this.bytes;
         default:
            return 0L;
      }
   }

   @Deprecated
   public int getEventCode() {
      Integer legacyCode = legacyEventCodes.get(this.eventType);
      return legacyCode == null ? -1 : legacyCode;
   }

   public ProgressEventType getEventType() {
      return this.eventType;
   }

   @Override
   public String toString() {
      return this.eventType + ", bytes: " + this.bytes;
   }

   static {
      legacyEventCodes.put(ProgressEventType.BYTE_TRANSFER_EVENT, 0);
      legacyEventCodes.put(ProgressEventType.TRANSFER_PREPARING_EVENT, 1);
      legacyEventCodes.put(ProgressEventType.TRANSFER_STARTED_EVENT, 2);
      legacyEventCodes.put(ProgressEventType.TRANSFER_COMPLETED_EVENT, 4);
      legacyEventCodes.put(ProgressEventType.TRANSFER_FAILED_EVENT, 8);
      legacyEventCodes.put(ProgressEventType.TRANSFER_CANCELED_EVENT, 16);
      legacyEventCodes.put(ProgressEventType.HTTP_REQUEST_CONTENT_RESET_EVENT, 32);
      legacyEventCodes.put(ProgressEventType.HTTP_RESPONSE_CONTENT_RESET_EVENT, 32);
      legacyEventCodes.put(ProgressEventType.TRANSFER_PART_STARTED_EVENT, 1024);
      legacyEventCodes.put(ProgressEventType.TRANSFER_PART_COMPLETED_EVENT, 2048);
      legacyEventCodes.put(ProgressEventType.TRANSFER_PART_FAILED_EVENT, 4096);
   }
}
