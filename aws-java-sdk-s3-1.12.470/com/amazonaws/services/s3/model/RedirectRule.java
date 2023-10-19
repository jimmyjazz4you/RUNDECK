package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class RedirectRule implements Serializable {
   private String protocol;
   private String hostName;
   private String replaceKeyPrefixWith;
   private String replaceKeyWith;
   private String httpRedirectCode;

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }

   public String getprotocol() {
      return this.protocol;
   }

   public RedirectRule withProtocol(String protocol) {
      this.setProtocol(protocol);
      return this;
   }

   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   public String getHostName() {
      return this.hostName;
   }

   public RedirectRule withHostName(String hostName) {
      this.setHostName(hostName);
      return this;
   }

   public void setReplaceKeyPrefixWith(String replaceKeyPrefixWith) {
      this.replaceKeyPrefixWith = replaceKeyPrefixWith;
   }

   public String getReplaceKeyPrefixWith() {
      return this.replaceKeyPrefixWith;
   }

   public RedirectRule withReplaceKeyPrefixWith(String replaceKeyPrefixWith) {
      this.setReplaceKeyPrefixWith(replaceKeyPrefixWith);
      return this;
   }

   public void setReplaceKeyWith(String replaceKeyWith) {
      this.replaceKeyWith = replaceKeyWith;
   }

   public String getReplaceKeyWith() {
      return this.replaceKeyWith;
   }

   public RedirectRule withReplaceKeyWith(String replaceKeyWith) {
      this.setReplaceKeyWith(replaceKeyWith);
      return this;
   }

   public void setHttpRedirectCode(String httpRedirectCode) {
      this.httpRedirectCode = httpRedirectCode;
   }

   public String getHttpRedirectCode() {
      return this.httpRedirectCode;
   }

   public RedirectRule withHttpRedirectCode(String httpRedirectCode) {
      this.httpRedirectCode = httpRedirectCode;
      return this;
   }
}
