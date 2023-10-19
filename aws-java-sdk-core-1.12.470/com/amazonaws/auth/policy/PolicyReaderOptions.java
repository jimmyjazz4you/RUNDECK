package com.amazonaws.auth.policy;

public class PolicyReaderOptions {
   private boolean stripAwsPrincipalIdHyphensEnabled = true;

   public boolean isStripAwsPrincipalIdHyphensEnabled() {
      return this.stripAwsPrincipalIdHyphensEnabled;
   }

   public void setStripAwsPrincipalIdHyphensEnabled(boolean stripAwsPrincipalIdHyphensEnabled) {
      this.stripAwsPrincipalIdHyphensEnabled = stripAwsPrincipalIdHyphensEnabled;
   }

   public PolicyReaderOptions withStripAwsPrincipalIdHyphensEnabled(boolean stripAwsPrincipalIdHyphensEnabled) {
      this.setStripAwsPrincipalIdHyphensEnabled(stripAwsPrincipalIdHyphensEnabled);
      return this;
   }
}
