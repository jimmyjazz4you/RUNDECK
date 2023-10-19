package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import java.util.Arrays;

public class ArnCondition extends Condition {
   public ArnCondition(ArnCondition.ArnComparisonType type, String key, String value) {
      super.type = type.toString();
      super.conditionKey = key;
      super.values = Arrays.asList(value);
   }

   public static enum ArnComparisonType {
      ArnEquals,
      ArnLike,
      ArnNotEquals,
      ArnNotLike;
   }
}
