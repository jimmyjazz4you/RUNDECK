package com.amazonaws.internal;

import com.amazonaws.annotation.SdkProtectedApi;
import java.util.ArrayList;
import java.util.List;

@SdkProtectedApi
public final class SdkThreadLocalsRegistry {
   private static final List<ThreadLocal<?>> threadLocals = new ArrayList<>();

   private SdkThreadLocalsRegistry() {
   }

   public static synchronized <T> ThreadLocal<T> register(ThreadLocal<T> threadLocal) {
      threadLocals.add(threadLocal);
      return threadLocal;
   }

   public static synchronized void remove() {
      for(ThreadLocal<?> t : threadLocals) {
         t.remove();
      }
   }
}
