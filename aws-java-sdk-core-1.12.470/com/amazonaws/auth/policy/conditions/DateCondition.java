package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import com.amazonaws.util.DateUtils;
import java.util.Arrays;
import java.util.Date;

public class DateCondition extends Condition {
   public DateCondition(DateCondition.DateComparisonType type, Date date) {
      super.type = type.toString();
      super.conditionKey = "aws:CurrentTime";
      super.values = Arrays.asList(DateUtils.formatISO8601Date(date));
   }

   public static enum DateComparisonType {
      DateEquals,
      DateGreaterThan,
      DateGreaterThanEquals,
      DateLessThan,
      DateLessThanEquals,
      DateNotEquals;
   }
}
