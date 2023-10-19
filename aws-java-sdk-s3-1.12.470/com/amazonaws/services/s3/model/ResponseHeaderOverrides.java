package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class ResponseHeaderOverrides implements Serializable {
   private String contentType;
   private String contentLanguage;
   private String expires;
   private String cacheControl;
   private String contentDisposition;
   private String contentEncoding;
   public static final String RESPONSE_HEADER_CONTENT_TYPE = "response-content-type";
   public static final String RESPONSE_HEADER_CONTENT_LANGUAGE = "response-content-language";
   public static final String RESPONSE_HEADER_EXPIRES = "response-expires";
   public static final String RESPONSE_HEADER_CACHE_CONTROL = "response-cache-control";
   public static final String RESPONSE_HEADER_CONTENT_DISPOSITION = "response-content-disposition";
   public static final String RESPONSE_HEADER_CONTENT_ENCODING = "response-content-encoding";
   private static final String[] PARAMETER_ORDER = new String[]{
      "response-cache-control",
      "response-content-disposition",
      "response-content-encoding",
      "response-content-language",
      "response-content-type",
      "response-expires"
   };

   public String getContentType() {
      return this.contentType;
   }

   public void setContentType(String contentType) {
      this.contentType = contentType;
   }

   public ResponseHeaderOverrides withContentType(String contentType) {
      this.setContentType(contentType);
      return this;
   }

   public String getContentLanguage() {
      return this.contentLanguage;
   }

   public void setContentLanguage(String contentLanguage) {
      this.contentLanguage = contentLanguage;
   }

   public ResponseHeaderOverrides withContentLanguage(String contentLanguage) {
      this.setContentLanguage(contentLanguage);
      return this;
   }

   public String getExpires() {
      return this.expires;
   }

   public void setExpires(String expires) {
      this.expires = expires;
   }

   public ResponseHeaderOverrides withExpires(String expires) {
      this.setExpires(expires);
      return this;
   }

   public String getCacheControl() {
      return this.cacheControl;
   }

   public void setCacheControl(String cacheControl) {
      this.cacheControl = cacheControl;
   }

   public ResponseHeaderOverrides withCacheControl(String cacheControl) {
      this.setCacheControl(cacheControl);
      return this;
   }

   public String getContentDisposition() {
      return this.contentDisposition;
   }

   public void setContentDisposition(String contentDisposition) {
      this.contentDisposition = contentDisposition;
   }

   public ResponseHeaderOverrides withContentDisposition(String contentDisposition) {
      this.setContentDisposition(contentDisposition);
      return this;
   }

   public String getContentEncoding() {
      return this.contentEncoding;
   }

   public void setContentEncoding(String contentEncoding) {
      this.contentEncoding = contentEncoding;
   }

   public ResponseHeaderOverrides withContentEncoding(String contentEncoding) {
      this.setContentEncoding(contentEncoding);
      return this;
   }
}
