package com.amazonaws.internal;

public class DynamoDBBackoffStrategy extends CustomBackoffStrategy {
   public static final CustomBackoffStrategy DEFAULT = new DynamoDBBackoffStrategy();

   @Override
   public int getBackoffPeriod(int retries) {
      if (retries <= 0) {
         return 0;
      } else {
         int delay = 50 * (int)Math.pow(2.0, (double)(retries - 1));
         if (delay < 0) {
            delay = Integer.MAX_VALUE;
         }

         return delay;
      }
   }
}
