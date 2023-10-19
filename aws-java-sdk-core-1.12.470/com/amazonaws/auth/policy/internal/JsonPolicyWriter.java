package com.amazonaws.auth.policy.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.util.PolicyUtils;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonPolicyWriter {
   private JsonGenerator generator = null;
   private Writer writer = new StringWriter();
   private static final Log log = LogFactory.getLog("com.amazonaws.auth.policy");

   public JsonPolicyWriter() {
      try {
         this.generator = Jackson.jsonGeneratorOf(this.writer);
      } catch (IOException var2) {
         throw new SdkClientException("Unable to instantiate JsonGenerator.", var2);
      }
   }

   public String writePolicyToString(Policy policy) {
      if (!this.isNotNull(policy)) {
         throw new IllegalArgumentException("Policy cannot be null");
      } else {
         String e;
         try {
            e = this.jsonStringOf(policy);
         } catch (Exception var11) {
            String message = "Unable to serialize policy to JSON string: " + var11.getMessage();
            throw new IllegalArgumentException(message, var11);
         } finally {
            try {
               this.writer.close();
            } catch (Exception var10) {
            }
         }

         return e;
      }
   }

   private String jsonStringOf(Policy policy) throws JsonGenerationException, IOException {
      this.generator.writeStartObject();
      this.writeJsonKeyValue("Version", policy.getVersion());
      if (this.isNotNull(policy.getId())) {
         this.writeJsonKeyValue("Id", policy.getId());
      }

      this.writeJsonArrayStart("Statement");

      for(Statement statement : policy.getStatements()) {
         this.generator.writeStartObject();
         if (this.isNotNull(statement.getId())) {
            this.writeJsonKeyValue("Sid", statement.getId());
         }

         this.writeJsonKeyValue("Effect", statement.getEffect().toString());
         List<Principal> principals = statement.getPrincipals();
         if (this.isNotNull(principals) && !principals.isEmpty()) {
            this.writePrincipals(principals);
         }

         List<Action> actions = statement.getActions();
         if (this.isNotNull(actions) && !actions.isEmpty()) {
            this.writeActions(actions);
         }

         List<Resource> resources = statement.getResources();
         if (this.isNotNull(resources) && !resources.isEmpty()) {
            this.writeResources(resources);
         }

         List<Condition> conditions = statement.getConditions();
         if (this.isNotNull(conditions) && !conditions.isEmpty()) {
            this.writeConditions(conditions);
         }

         this.generator.writeEndObject();
      }

      this.writeJsonArrayEnd();
      this.generator.writeEndObject();
      this.generator.flush();
      return this.writer.toString();
   }

   private void writeConditions(List<Condition> conditions) throws JsonGenerationException, IOException {
      Map<String, JsonPolicyWriter.ConditionsByKey> conditionsByType = this.groupConditionsByTypeAndKey(conditions);
      this.writeJsonObjectStart("Condition");

      for(Entry<String, JsonPolicyWriter.ConditionsByKey> entry : conditionsByType.entrySet()) {
         JsonPolicyWriter.ConditionsByKey conditionsByKey = conditionsByType.get(entry.getKey());
         this.writeJsonObjectStart(entry.getKey());

         for(String key : conditionsByKey.keySet()) {
            this.writeJsonArray(key, conditionsByKey.getConditionsByKey(key));
         }

         this.writeJsonObjectEnd();
      }

      this.writeJsonObjectEnd();
   }

   private void writeResources(List<Resource> resources) throws JsonGenerationException, IOException {
      PolicyUtils.validateResourceList(resources);
      List<String> resourceStrings = new ArrayList<>();

      for(Resource resource : resources) {
         resourceStrings.add(resource.getId());
      }

      if (resources.get(0).isNotType()) {
         this.writeJsonArray("NotResource", resourceStrings);
      } else {
         this.writeJsonArray("Resource", resourceStrings);
      }
   }

   private void writeActions(List<Action> actions) throws JsonGenerationException, IOException {
      List<String> actionStrings = new ArrayList<>();

      for(Action action : actions) {
         actionStrings.add(action.getActionName());
      }

      this.writeJsonArray("Action", actionStrings);
   }

   private void writePrincipals(List<Principal> principals) throws JsonGenerationException, IOException {
      if (principals.size() == 1 && principals.get(0).equals(Principal.All)) {
         this.writeJsonKeyValue("Principal", Principal.All.getId());
      } else {
         this.writeJsonObjectStart("Principal");
         Map<String, List<String>> principalsByScheme = this.groupPrincipalByScheme(principals);

         for(Entry<String, List<String>> entry : principalsByScheme.entrySet()) {
            List<String> principalValues = principalsByScheme.get(entry.getKey());
            if (principalValues.size() == 1) {
               this.writeJsonKeyValue(entry.getKey(), principalValues.get(0));
            } else {
               this.writeJsonArray(entry.getKey(), principalValues);
            }
         }

         this.writeJsonObjectEnd();
      }
   }

   private Map<String, List<String>> groupPrincipalByScheme(List<Principal> principals) {
      Map<String, List<String>> principalsByScheme = new LinkedHashMap<>();

      for(Principal principal : principals) {
         String provider = principal.getProvider();
         if (!principalsByScheme.containsKey(provider)) {
            principalsByScheme.put(provider, new ArrayList<>());
         }

         List<String> principalValues = principalsByScheme.get(provider);
         principalValues.add(principal.getId());
      }

      return principalsByScheme;
   }

   private Map<String, JsonPolicyWriter.ConditionsByKey> groupConditionsByTypeAndKey(List<Condition> conditions) {
      Map<String, JsonPolicyWriter.ConditionsByKey> conditionsByType = new LinkedHashMap<>();

      for(Condition condition : conditions) {
         String type = condition.getType();
         String key = condition.getConditionKey();
         if (!conditionsByType.containsKey(type)) {
            conditionsByType.put(type, new JsonPolicyWriter.ConditionsByKey());
         }

         JsonPolicyWriter.ConditionsByKey conditionsByKey = conditionsByType.get(type);
         conditionsByKey.addValuesToKey(key, condition.getValues());
      }

      return conditionsByType;
   }

   private void writeJsonArray(String arrayName, List<String> values) throws JsonGenerationException, IOException {
      this.writeJsonArrayStart(arrayName);

      for(String value : values) {
         this.generator.writeString(value);
      }

      this.writeJsonArrayEnd();
   }

   private void writeJsonObjectStart(String fieldName) throws JsonGenerationException, IOException {
      this.generator.writeObjectFieldStart(fieldName);
   }

   private void writeJsonObjectEnd() throws JsonGenerationException, IOException {
      this.generator.writeEndObject();
   }

   private void writeJsonArrayStart(String fieldName) throws JsonGenerationException, IOException {
      this.generator.writeArrayFieldStart(fieldName);
   }

   private void writeJsonArrayEnd() throws JsonGenerationException, IOException {
      this.generator.writeEndArray();
   }

   private void writeJsonKeyValue(String fieldName, String value) throws JsonGenerationException, IOException {
      this.generator.writeStringField(fieldName, value);
   }

   private boolean isNotNull(Object object) {
      return null != object;
   }

   static class ConditionsByKey {
      private Map<String, List<String>> conditionsByKey = new LinkedHashMap<>();

      public ConditionsByKey() {
      }

      public Map<String, List<String>> getConditionsByKey() {
         return this.conditionsByKey;
      }

      public void setConditionsByKey(Map<String, List<String>> conditionsByKey) {
         this.conditionsByKey = conditionsByKey;
      }

      public boolean containsKey(String key) {
         return this.conditionsByKey.containsKey(key);
      }

      public List<String> getConditionsByKey(String key) {
         return this.conditionsByKey.get(key);
      }

      public Set<String> keySet() {
         return this.conditionsByKey.keySet();
      }

      public void addValuesToKey(String key, List<String> values) {
         List<String> conditionValues = this.getConditionsByKey(key);
         if (conditionValues == null) {
            this.conditionsByKey.put(key, new ArrayList<>(values));
         } else {
            conditionValues.addAll(values);
         }
      }
   }
}
