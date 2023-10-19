package com.amazonaws.http.apache.client.impl;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class SdkHttpClient implements ConnectionManagerAwareHttpClient {
   private final HttpClient delegate;
   private final HttpClientConnectionManager cm;

   public SdkHttpClient(HttpClient delegate, HttpClientConnectionManager cm) {
      if (delegate == null) {
         throw new IllegalArgumentException("delegate cannot be null");
      } else if (cm == null) {
         throw new IllegalArgumentException("connection manager cannot be null");
      } else {
         this.delegate = delegate;
         this.cm = cm;
      }
   }

   public HttpParams getParams() {
      return this.delegate.getParams();
   }

   public ClientConnectionManager getConnectionManager() {
      return this.delegate.getConnectionManager();
   }

   public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
      return this.delegate.execute(request);
   }

   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
      return this.delegate.execute(request, context);
   }

   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
      return this.delegate.execute(target, request);
   }

   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
      return this.delegate.execute(target, request, context);
   }

   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
      return (T)this.delegate.execute(request, responseHandler);
   }

   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
      return (T)this.delegate.execute(request, responseHandler, context);
   }

   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
      return (T)this.delegate.execute(target, request, responseHandler);
   }

   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
      return (T)this.delegate.execute(target, request, responseHandler, context);
   }

   @Override
   public HttpClientConnectionManager getHttpClientConnectionManager() {
      return this.cm;
   }
}
