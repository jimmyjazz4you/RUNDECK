package com.amazonaws;

import com.amazonaws.internal.SdkThreadLocalsRegistry;

public final class SdkThreadLocals {
   private SdkThreadLocals() {
   }

   public static void remove() {
      SdkThreadLocalsRegistry.remove();
   }
}
