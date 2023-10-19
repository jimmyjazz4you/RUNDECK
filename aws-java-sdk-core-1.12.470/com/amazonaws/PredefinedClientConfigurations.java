package com.amazonaws;

import com.amazonaws.retry.PredefinedRetryPolicies;

public class PredefinedClientConfigurations {
   public static ClientConfiguration defaultConfig() {
      return new ClientConfiguration();
   }

   public static ClientConfiguration dynamoDefault() {
      return new ClientConfiguration().withRetryPolicy(PredefinedRetryPolicies.DYNAMODB_DEFAULT);
   }

   public static ClientConfiguration swfDefault() {
      return new ClientConfiguration().withMaxConnections(1000).withSocketTimeout(90000);
   }
}
