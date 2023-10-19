package com.amazonaws.services.s3.internal;

import com.amazonaws.services.s3.OnFileDelete;
import java.io.File;

public class PartCreationEvent {
   private final File part;
   private final int partNumber;
   private final boolean isLastPart;
   private final OnFileDelete fileDeleteObserver;

   PartCreationEvent(File part, int partNumber, boolean isLastPart, OnFileDelete fileDeleteObserver) {
      if (part == null) {
         throw new IllegalArgumentException("part must not be specified");
      } else {
         this.part = part;
         this.partNumber = partNumber;
         this.isLastPart = isLastPart;
         this.fileDeleteObserver = fileDeleteObserver;
      }
   }

   public File getPart() {
      return this.part;
   }

   public int getPartNumber() {
      return this.partNumber;
   }

   public boolean isLastPart() {
      return this.isLastPart;
   }

   public OnFileDelete getFileDeleteObserver() {
      return this.fileDeleteObserver;
   }
}
