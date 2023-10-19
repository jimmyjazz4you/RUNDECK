package com.amazonaws.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;

@SdkInternalApi
public class TokenBucket {
   private static final double MIN_FILL_RATE = 0.5;
   private static final double MIN_CAPACITY = 1.0;
   private static final double SMOOTH = 0.8;
   private static final double BETA = 0.7;
   private static final double SCALE_CONSTANT = 0.4;
   private final TokenBucket.Clock clock;
   private Double fillRate;
   private Double maxCapacity;
   private double currentCapacity;
   private Double lastTimestamp;
   private boolean enabled;
   private double measuredTxRate;
   private double lastTxRateBucket;
   private long requestCount;
   private double lastMaxRate;
   private double lastThrottleTime;
   private double timeWindow;

   public TokenBucket() {
      this.clock = new TokenBucket.DefaultClock();
      this.initialize();
   }

   @SdkTestInternalApi
   TokenBucket(TokenBucket.Clock clock) {
      this.clock = clock;
      this.initialize();
   }

   public boolean acquire(double amount) {
      return this.acquire(amount, false);
   }

   public boolean acquire(double amount, boolean fastFail) {
      double waitTime = 0.0;
      synchronized(this) {
         if (!this.enabled) {
            return true;
         }

         this.refill();
         double originalCapacity = this.currentCapacity;
         double unfulfilled = this.tryAcquireCapacity(amount);
         if (unfulfilled > 0.0 && fastFail) {
            this.currentCapacity = originalCapacity;
            return false;
         }

         if (unfulfilled > 0.0) {
            waitTime = unfulfilled / this.fillRate;
         }
      }

      if (waitTime > 0.0) {
         this.sleep(waitTime);
      }

      return true;
   }

   double tryAcquireCapacity(double amount) {
      double result;
      if (amount <= this.currentCapacity) {
         result = 0.0;
      } else {
         result = amount - this.currentCapacity;
      }

      this.currentCapacity -= amount;
      return result;
   }

   private void initialize() {
      this.fillRate = null;
      this.maxCapacity = null;
      this.currentCapacity = 0.0;
      this.lastTimestamp = null;
      this.enabled = false;
      this.measuredTxRate = 0.0;
      this.lastTxRateBucket = Math.floor(this.clock.time());
      this.requestCount = 0L;
      this.lastMaxRate = 0.0;
      this.lastThrottleTime = this.clock.time();
   }

   synchronized void refill() {
      double timestamp = this.clock.time();
      if (this.lastTimestamp == null) {
         this.lastTimestamp = timestamp;
      } else {
         double fillAmount = (timestamp - this.lastTimestamp) * this.fillRate;
         this.currentCapacity = Math.min(this.maxCapacity, this.currentCapacity + fillAmount);
         this.lastTimestamp = timestamp;
      }
   }

   private synchronized void updateRate(double newRps) {
      this.refill();
      this.fillRate = Math.max(newRps, 0.5);
      this.maxCapacity = Math.max(newRps, 1.0);
      this.currentCapacity = Math.min(this.currentCapacity, this.maxCapacity);
   }

   private synchronized void updateMeasuredRate() {
      double t = this.clock.time();
      double timeBucket = Math.floor(t * 2.0) / 2.0;
      ++this.requestCount;
      if (timeBucket > this.lastTxRateBucket) {
         double currentRate = (double)this.requestCount / (timeBucket - this.lastTxRateBucket);
         this.measuredTxRate = currentRate * 0.8 + this.measuredTxRate * 0.19999999999999996;
         this.requestCount = 0L;
         this.lastTxRateBucket = timeBucket;
      }
   }

   synchronized void enable() {
      this.enabled = true;
   }

   public synchronized void updateClientSendingRate(boolean throttlingResponse) {
      this.updateMeasuredRate();
      double calculatedRate;
      if (throttlingResponse) {
         double rateToUse;
         if (!this.enabled) {
            rateToUse = this.measuredTxRate;
         } else {
            rateToUse = Math.min(this.measuredTxRate, this.fillRate);
         }

         this.lastMaxRate = rateToUse;
         this.calculateTimeWindow();
         this.lastThrottleTime = this.clock.time();
         calculatedRate = this.cubicThrottle(rateToUse);
         this.enable();
      } else {
         this.calculateTimeWindow();
         calculatedRate = this.cubicSuccess(this.clock.time());
      }

      double newRate = Math.min(calculatedRate, 2.0 * this.measuredTxRate);
      this.updateRate(newRate);
   }

   synchronized void calculateTimeWindow() {
      this.timeWindow = Math.pow(this.lastMaxRate * 0.30000000000000004 / 0.4, 0.3333333333333333);
   }

   void sleep(double seconds) {
      long millisToSleep = (long)(seconds * 1000.0);

      try {
         Thread.sleep(millisToSleep);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw new RuntimeException(var6);
      }
   }

   double cubicThrottle(double rateToUse) {
      return rateToUse * 0.7;
   }

   synchronized double cubicSuccess(double timestamp) {
      double dt = timestamp - this.lastThrottleTime;
      return 0.4 * Math.pow(dt - this.timeWindow, 3.0) + this.lastMaxRate;
   }

   @SdkTestInternalApi
   synchronized void setLastMaxRate(double lastMaxRate) {
      this.lastMaxRate = lastMaxRate;
   }

   @SdkTestInternalApi
   synchronized void setLastThrottleTime(double lastThrottleTime) {
      this.lastThrottleTime = lastThrottleTime;
   }

   @SdkTestInternalApi
   synchronized double getMeasuredTxRate() {
      return this.measuredTxRate;
   }

   @SdkTestInternalApi
   synchronized double getFillRate() {
      return this.fillRate;
   }

   @SdkTestInternalApi
   synchronized void setCurrentCapacity(double currentCapacity) {
      this.currentCapacity = currentCapacity;
   }

   @SdkTestInternalApi
   synchronized double getCurrentCapacity() {
      return this.currentCapacity;
   }

   @SdkTestInternalApi
   synchronized void setFillRate(double fillRate) {
      this.fillRate = fillRate;
   }

   public interface Clock {
      double time();
   }

   static class DefaultClock implements TokenBucket.Clock {
      @Override
      public double time() {
         long timeMillis = System.nanoTime();
         return (double)timeMillis / 1.0E9;
      }
   }
}
