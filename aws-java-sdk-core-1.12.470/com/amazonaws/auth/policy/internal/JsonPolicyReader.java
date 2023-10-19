package com.amazonaws.auth.policy.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.PolicyReaderOptions;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class JsonPolicyReader {
   private static final String PRINCIPAL_SCHEMA_USER = "AWS";
   private static final String PRINCIPAL_SCHEMA_SERVICE = "Service";
   private static final String PRINCIPAL_SCHEMA_FEDERATED = "Federated";
   private final PolicyReaderOptions options;

   public JsonPolicyReader() {
      this(new PolicyReaderOptions());
   }

   public JsonPolicyReader(PolicyReaderOptions options) {
      this.options = options;
   }

   public Policy createPolicyFromJsonString(String jsonString) {
      if (jsonString == null) {
         throw new IllegalArgumentException("JSON string cannot be null");
      } else {
         Policy policy = new Policy();
         List<Statement> statements = new LinkedList<>();

         try {
            JsonNode policyNode = Jackson.jsonNodeOf(jsonString);
            JsonNode idNode = policyNode.get("Id");
            if (this.isNotNull(idNode)) {
               policy.setId(idNode.asText());
            }

            JsonNode statementsNode = policyNode.get("Statement");
            if (this.isNotNull(statementsNode)) {
               if (statementsNode.isObject()) {
                  statements.add(this.statementOf(statementsNode));
               } else if (statementsNode.isArray()) {
                  for(JsonNode statementNode : statementsNode) {
                     statements.add(this.statementOf(statementNode));
                  }
               }
            }
         } catch (Exception var9) {
            String message = "Unable to generate policy object fron JSON string " + var9.getMessage();
            throw new IllegalArgumentException(message, var9);
         }

         policy.setStatements(statements);
         return policy;
      }
   }

   private Statement statementOf(JsonNode jStatement) {
      JsonNode effectNode = jStatement.get("Effect");
      Statement.Effect effect = this.isNotNull(effectNode) ? Statement.Effect.valueOf(effectNode.asText()) : Statement.Effect.Deny;
      Statement statement = new Statement(effect);
      JsonNode id = jStatement.get("Sid");
      if (this.isNotNull(id)) {
         statement.setId(id.asText());
      }

      JsonNode actionNodes = jStatement.get("Action");
      if (this.isNotNull(actionNodes)) {
         statement.setActions(this.actionsOf(actionNodes));
      }

      List<Resource> resources = new LinkedList<>();
      JsonNode resourceNodes = jStatement.get("Resource");
      if (this.isNotNull(resourceNodes)) {
         resources.addAll(this.resourcesOf(resourceNodes, false));
      }

      JsonNode notResourceNodes = jStatement.get("NotResource");
      if (this.isNotNull(notResourceNodes)) {
         resources.addAll(this.resourcesOf(notResourceNodes, true));
      }

      if (!resources.isEmpty()) {
         statement.setResources(resources);
      }

      JsonNode conditionNodes = jStatement.get("Condition");
      if (this.isNotNull(conditionNodes)) {
         statement.setConditions(this.conditionsOf(conditionNodes));
      }

      JsonNode principalNodes = jStatement.get("Principal");
      if (this.isNotNull(principalNodes)) {
         statement.setPrincipals(this.principalOf(principalNodes));
      }

      return statement;
   }

   private List<Action> actionsOf(JsonNode actionNodes) {
      List<Action> actions = new LinkedList<>();
      if (actionNodes.isArray()) {
         for(JsonNode action : actionNodes) {
            actions.add(new JsonPolicyReader.NamedAction(action.asText()));
         }
      } else {
         actions.add(new JsonPolicyReader.NamedAction(actionNodes.asText()));
      }

      return actions;
   }

   private List<Resource> resourcesOf(JsonNode resourceNodes, boolean isNotType) {
      List<Resource> resources = new LinkedList<>();
      if (resourceNodes.isArray()) {
         for(JsonNode resource : resourceNodes) {
            resources.add(new Resource(resource.asText()).withIsNotType(isNotType));
         }
      } else {
         resources.add(new Resource(resourceNodes.asText()).withIsNotType(isNotType));
      }

      return resources;
   }

   private List<Principal> principalOf(JsonNode principalNodes) {
      List<Principal> principals = new LinkedList<>();
      if (principalNodes.asText().equals("*")) {
         principals.add(Principal.All);
         return principals;
      } else {
         Iterator<Entry<String, JsonNode>> mapOfPrincipals = principalNodes.fields();

         while(mapOfPrincipals.hasNext()) {
            Entry<String, JsonNode> principal = mapOfPrincipals.next();
            String schema = principal.getKey();
            JsonNode principalNode = (JsonNode)principal.getValue();
            if (principalNode.isArray()) {
               Iterator<JsonNode> elements = principalNode.elements();

               while(elements.hasNext()) {
                  principals.add(this.createPrincipal(schema, (JsonNode)elements.next()));
               }
            } else {
               principals.add(this.createPrincipal(schema, principalNode));
            }
         }

         return principals;
      }
   }

   private Principal createPrincipal(String schema, JsonNode principalNode) {
      if (schema.equalsIgnoreCase("AWS")) {
         return new Principal("AWS", principalNode.asText(), this.options.isStripAwsPrincipalIdHyphensEnabled());
      } else if (schema.equalsIgnoreCase("Service")) {
         return new Principal(schema, principalNode.asText());
      } else if (schema.equalsIgnoreCase("Federated")) {
         return Principal.WebIdentityProviders.fromString(principalNode.asText()) != null
            ? new Principal(Principal.WebIdentityProviders.fromString(principalNode.asText()))
            : new Principal("Federated", principalNode.asText());
      } else {
         throw new SdkClientException("Schema " + schema + " is not a valid value for the principal.");
      }
   }

   private List<Condition> conditionsOf(JsonNode conditionNodes) {
      List<Condition> conditionList = new LinkedList<>();
      Iterator<Entry<String, JsonNode>> mapOfConditions = conditionNodes.fields();

      while(mapOfConditions.hasNext()) {
         Entry<String, JsonNode> condition = mapOfConditions.next();
         this.convertConditionRecord(conditionList, condition.getKey(), (JsonNode)condition.getValue());
      }

      return conditionList;
   }

   private void convertConditionRecord(List<Condition> conditions, String conditionType, JsonNode conditionNode) {
      List<String> values;
      Entry<String, JsonNode> field;
      for(Iterator<Entry<String, JsonNode>> mapOfFields = conditionNode.fields();
         mapOfFields.hasNext();
         conditions.add(new Condition().withType(conditionType).withConditionKey(field.getKey()).withValues(values))
      ) {
         values = new LinkedList<>();
         field = mapOfFields.next();
         JsonNode fieldValue = (JsonNode)field.getValue();
         if (fieldValue.isArray()) {
            Iterator<JsonNode> elements = fieldValue.elements();

            while(elements.hasNext()) {
               values.add(((JsonNode)elements.next()).asText());
            }
         } else {
            values.add(fieldValue.asText());
         }
      }
   }

   private boolean isNotNull(Object object) {
      return null != object;
   }

   private static class NamedAction implements Action {
      private String actionName;

      public NamedAction(String actionName) {
         this.actionName = actionName;
      }

      @Override
      public String getActionName() {
         return this.actionName;
      }
   }
}
