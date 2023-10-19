package com.amazonaws.auth.policy;

import com.amazonaws.auth.policy.internal.JsonPolicyReader;
import com.amazonaws.auth.policy.internal.JsonPolicyWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Policy {
   private static final String DEFAULT_POLICY_VERSION = "2012-10-17";
   private String id;
   private String version = "2012-10-17";
   private List<Statement> statements = new ArrayList<>();

   public Policy() {
   }

   public Policy(String id) {
      this.id = id;
   }

   public Policy(String id, Collection<Statement> statements) {
      this(id);
      this.setStatements(statements);
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Policy withId(String id) {
      this.setId(id);
      return this;
   }

   public String getVersion() {
      return this.version;
   }

   public Collection<Statement> getStatements() {
      return this.statements;
   }

   public void setStatements(Collection<Statement> statements) {
      this.statements = new ArrayList<>(statements);
      this.assignUniqueStatementIds();
   }

   public Policy withStatements(Statement... statements) {
      this.setStatements(Arrays.asList(statements));
      return this;
   }

   public String toJson() {
      return new JsonPolicyWriter().writePolicyToString(this);
   }

   public static Policy fromJson(String jsonString) {
      return fromJson(jsonString, new PolicyReaderOptions());
   }

   public static Policy fromJson(String jsonString, PolicyReaderOptions options) {
      return new JsonPolicyReader(options).createPolicyFromJsonString(jsonString);
   }

   private void assignUniqueStatementIds() {
      Set<String> usedStatementIds = new HashSet<>();

      for(Statement statement : this.statements) {
         if (statement.getId() != null) {
            usedStatementIds.add(statement.getId());
         }
      }

      int counter = 0;

      for(Statement statement : this.statements) {
         if (statement.getId() == null) {
            while(usedStatementIds.contains(Integer.toString(++counter))) {
            }

            statement.setId(Integer.toString(counter));
         }
      }
   }
}
