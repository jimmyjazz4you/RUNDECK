package com.amazonaws.retry.internal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.Request;
import com.amazonaws.http.HttpResponse;

public interface AuthErrorRetryStrategy {
   AuthRetryParameters shouldRetryWithAuthParam(Request<?> var1, HttpResponse var2, AmazonServiceException var3);
}
