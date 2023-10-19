package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerSideEncryptionConfiguration implements Serializable, Cloneable {
   private List<ServerSideEncryptionRule> rules;

   public List<ServerSideEncryptionRule> getRules() {
      return this.rules;
   }

   public void setRules(Collection<ServerSideEncryptionRule> rules) {
      if (rules == null) {
         this.rules = null;
      } else {
         this.rules = new ArrayList<>(rules);
      }
   }

   public ServerSideEncryptionConfiguration withRules(ServerSideEncryptionRule... rules) {
      if (this.rules == null) {
         this.setRules(new ArrayList<>(rules.length));
      }

      for(ServerSideEncryptionRule ele : rules) {
         this.rules.add(ele);
      }

      return this;
   }

   public ServerSideEncryptionConfiguration withRules(Collection<ServerSideEncryptionRule> rules) {
      this.setRules(rules);
      return this;
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

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof ServerSideEncryptionConfiguration)) {
         return false;
      } else {
         ServerSideEncryptionConfiguration other = (ServerSideEncryptionConfiguration)obj;
         if (other.getRules() == null ^ this.getRules() == null) {
            return false;
         } else {
            return other.getRules() == null || other.getRules().equals(this.getRules());
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int hashCode = 1;
      return 31 * hashCode + (this.getRules() == null ? 0 : this.getRules().hashCode());
   }

   public ServerSideEncryptionConfiguration clone() {
      try {
         return (ServerSideEncryptionConfiguration)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() even though we're Cloneable!", var2);
      }
   }
}
