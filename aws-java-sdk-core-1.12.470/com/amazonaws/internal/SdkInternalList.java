package com.amazonaws.internal;

import java.util.ArrayList;
import java.util.Collection;

public class SdkInternalList<T> extends ArrayList<T> {
   private static final long serialVersionUID = 1L;
   private final boolean autoConstruct;

   public SdkInternalList() {
      this.autoConstruct = true;
   }

   public SdkInternalList(Collection<? extends T> c) {
      super(c);
      this.autoConstruct = false;
   }

   public SdkInternalList(int initialCapacity) {
      super(initialCapacity);
      this.autoConstruct = false;
   }

   public boolean isAutoConstruct() {
      return this.autoConstruct;
   }
}
