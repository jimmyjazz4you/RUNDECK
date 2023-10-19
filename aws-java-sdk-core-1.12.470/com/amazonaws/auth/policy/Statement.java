package com.amazonaws.auth.policy;

import com.amazonaws.util.PolicyUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Statement {
   private String id;
   private Statement.Effect effect;
   private List<Principal> principals = new ArrayList<>();
   private List<Action> actions = new ArrayList<>();
   private List<Resource> resources;
   private List<Condition> conditions = new ArrayList<>();

   public Statement(Statement.Effect effect) {
      this.effect = effect;
      this.id = null;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Statement withId(String id) {
      this.setId(id);
      return this;
   }

   public Statement.Effect getEffect() {
      return this.effect;
   }

   public void setEffect(Statement.Effect effect) {
      this.effect = effect;
   }

   public List<Action> getActions() {
      return this.actions;
   }

   public void setActions(Collection<Action> actions) {
      this.actions = new ArrayList<>(actions);
   }

   public Statement withActions(Action... actions) {
      this.setActions(Arrays.asList(actions));
      return this;
   }

   public List<Resource> getResources() {
      return this.resources;
   }

   public void setResources(Collection<Resource> resources) {
      List<Resource> resourceList = new ArrayList<>(resources);
      PolicyUtils.validateResourceList(resourceList);
      this.resources = resourceList;
   }

   public Statement withResources(Resource... resources) {
      this.setResources(Arrays.asList(resources));
      return this;
   }

   public List<Condition> getConditions() {
      return this.conditions;
   }

   public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
   }

   public Statement withConditions(Condition... conditions) {
      this.setConditions(Arrays.asList(conditions));
      return this;
   }

   public List<Principal> getPrincipals() {
      return this.principals;
   }

   public void setPrincipals(Collection<Principal> principals) {
      this.principals = new ArrayList<>(principals);
   }

   public void setPrincipals(Principal... principals) {
      this.setPrincipals(new ArrayList<>(Arrays.asList(principals)));
   }

   public Statement withPrincipals(Principal... principals) {
      this.setPrincipals(principals);
      return this;
   }

   public static enum Effect {
      Allow,
      Deny;
   }
}
