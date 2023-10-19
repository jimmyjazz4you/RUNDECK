package com.amazonaws.auth.policy;

import java.util.Arrays;
import java.util.List;

public class Condition {
   protected String type;
   protected String conditionKey;
   protected List<String> values;

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getConditionKey() {
      return this.conditionKey;
   }

   public void setConditionKey(String conditionKey) {
      this.conditionKey = conditionKey;
   }

   public List<String> getValues() {
      return this.values;
   }

   public void setValues(List<String> values) {
      this.values = values;
   }

   public Condition withType(String type) {
      this.setType(type);
      return this;
   }

   public Condition withConditionKey(String key) {
      this.setConditionKey(key);
      return this;
   }

   public Condition withValues(String... values) {
      this.setValues(Arrays.asList(values));
      return this;
   }

   public Condition withValues(List<String> values) {
      this.setValues(values);
      return this;
   }
}
