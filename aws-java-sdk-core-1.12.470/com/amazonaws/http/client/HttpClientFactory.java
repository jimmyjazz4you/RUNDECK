package com.amazonaws.http.client;

import com.amazonaws.annotation.Beta;
import com.amazonaws.http.settings.HttpClientSettings;

@Beta
public interface HttpClientFactory<T> {
   T create(HttpClientSettings var1);
}
