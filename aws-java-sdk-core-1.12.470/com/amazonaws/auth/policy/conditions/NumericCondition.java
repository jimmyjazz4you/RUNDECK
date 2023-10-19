package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import java.util.Arrays;

public class NumericCondition extends Condition {
   public NumericCondition(NumericCondition.NumericComparisonType type, String key, String value) {
      super.type = type.toString();
      super.conditionKey = key;
      super.values = Arrays.asList(value);
   }

   public static enum NumericComparisonType {
      NumericEquals,
      NumericGreaterThan,
      NumericGreaterThanEquals,
      NumericLessThan,
      NumericLessThanEquals,
      NumericNotEquals;
   }
}
