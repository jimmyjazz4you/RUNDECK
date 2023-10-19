package com.amazonaws.http.apache.request.impl;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase {
   public HttpGetWithBody(String uri) {
      this.setURI(URI.create(uri));
   }

   public String getMethod() {
      return "GET";
   }
}
