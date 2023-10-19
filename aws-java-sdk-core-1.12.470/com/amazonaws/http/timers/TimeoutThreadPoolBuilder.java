package com.amazonaws.http.timers;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@SdkInternalApi
public class TimeoutThreadPoolBuilder {
   public static ScheduledThreadPoolExecutor buildDefaultTimeoutThreadPool(String name) {
      ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5, getThreadFactory(name));
      safeSetRemoveOnCancel(executor);
      executor.setKeepAliveTime(5L, TimeUnit.SECONDS);
      executor.allowCoreThreadTimeOut(true);
      return executor;
   }

   private static ThreadFactory getThreadFactory(final String name) {
      return new ThreadFactory() {
         private int threadCount = 1;

         @Override
         public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            if (name != null) {
               thread.setName(name + "-" + this.threadCount++);
            }

            thread.setPriority(10);
            return thread;
         }
      };
   }

   private static void safeSetRemoveOnCancel(ScheduledThreadPoolExecutor executor) {
      try {
         executor.getClass().getMethod("setRemoveOnCancelPolicy", Boolean.TYPE).invoke(executor, Boolean.TRUE);
      } catch (IllegalAccessException var2) {
         throwSetRemoveOnCancelException(var2);
      } catch (IllegalArgumentException var3) {
         throwSetRemoveOnCancelException(var3);
      } catch (InvocationTargetException var4) {
         throwSetRemoveOnCancelException(var4.getCause());
      } catch (NoSuchMethodException var5) {
         throw new SdkClientException("The request timeout feature is only available for Java 1.7 and above.");
      } catch (SecurityException var6) {
         throw new SdkClientException("The request timeout feature needs additional permissions to function.", var6);
      }
   }

   private static void throwSetRemoveOnCancelException(Throwable cause) {
      throw new SdkClientException("Unable to setRemoveOnCancelPolicy for request timeout thread pool", cause);
   }
}
