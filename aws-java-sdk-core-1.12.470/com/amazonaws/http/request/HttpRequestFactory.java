package com.amazonaws.http.request;

import com.amazonaws.Request;
import com.amazonaws.annotation.Beta;
import com.amazonaws.http.settings.HttpClientSettings;
import java.io.IOException;

@Beta
public interface HttpRequestFactory<T> {
   T create(Request<?> var1, HttpClientSettings var2) throws IOException;
}
