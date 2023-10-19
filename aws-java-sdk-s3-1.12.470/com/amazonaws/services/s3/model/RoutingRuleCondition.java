package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class RoutingRuleCondition implements Serializable {
   String keyPrefixEquals;
   String httpErrorCodeReturnedEquals;

   public void setKeyPrefixEquals(String keyPrefixEquals) {
      this.keyPrefixEquals = keyPrefixEquals;
   }

   public String getKeyPrefixEquals() {
      return this.keyPrefixEquals;
   }

   public RoutingRuleCondition withKeyPrefixEquals(String keyPrefixEquals) {
      this.setKeyPrefixEquals(keyPrefixEquals);
      return this;
   }

   public void setHttpErrorCodeReturnedEquals(String httpErrorCodeReturnedEquals) {
      this.httpErrorCodeReturnedEquals = httpErrorCodeReturnedEquals;
   }

   public String getHttpErrorCodeReturnedEquals() {
      return this.httpErrorCodeReturnedEquals;
   }

   public RoutingRuleCondition withHttpErrorCodeReturnedEquals(String httpErrorCodeReturnedEquals) {
      this.setHttpErrorCodeReturnedEquals(httpErrorCodeReturnedEquals);
      return this;
   }
}
