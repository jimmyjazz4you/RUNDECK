package com.amazonaws.http.apache.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;

public interface ConnectionManagerAwareHttpClient extends HttpClient {
   HttpClientConnectionManager getHttpClientConnectionManager();
}
