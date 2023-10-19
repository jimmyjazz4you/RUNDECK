package com.amazonaws.services.s3.internal;

import com.amazonaws.http.HttpResponse;

public interface HeaderHandler<T> {
   void handle(T var1, HttpResponse var2);
}
