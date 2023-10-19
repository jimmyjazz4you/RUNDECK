package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import java.util.Arrays;

public class StringCondition extends Condition {
   public StringCondition(StringCondition.StringComparisonType type, String key, String value) {
      super.type = type.toString();
      super.conditionKey = key;
      super.values = Arrays.asList(value);
   }

   public static enum StringComparisonType {
      StringEquals,
      StringEqualsIgnoreCase,
      StringLike,
      StringNotEquals,
      StringNotEqualsIgnoreCase,
      StringNotLike;
   }
}
