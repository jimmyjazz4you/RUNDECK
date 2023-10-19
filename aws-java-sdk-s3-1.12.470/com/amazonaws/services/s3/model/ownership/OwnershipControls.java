package com.amazonaws.services.s3.model.ownership;

import java.io.Serializable;
import java.util.List;

public class OwnershipControls implements Serializable {
   private List<OwnershipControlsRule> rules;

   public List<OwnershipControlsRule> getRules() {
      return this.rules;
   }

   public void setRules(List<OwnershipControlsRule> rules) {
      this.rules = rules;
   }

   public OwnershipControls withRules(List<OwnershipControlsRule> rules) {
      this.setRules(rules);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         OwnershipControls that = (OwnershipControls)o;
         return this.rules != null ? this.rules.equals(that.rules) : that.rules == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.rules != null ? this.rules.hashCode() : 0;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      if (this.getRules() != null) {
         sb.append("Rules: ").append(this.getRules()).append(",");
      }

      sb.append("}");
      return sb.toString();
   }
}
