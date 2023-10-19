package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import java.util.Arrays;

public class BooleanCondition extends Condition {
   public BooleanCondition(String key, boolean value) {
      super.type = "Bool";
      super.conditionKey = key;
      super.values = Arrays.asList(Boolean.toString(value));
   }
}
