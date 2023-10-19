package com.amazonaws;

import com.amazonaws.http.HttpMethodName;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface ImmutableRequest<T> {
   Map<String, String> getHeaders();

   String getResourcePath();

   Map<String, List<String>> getParameters();

   URI getEndpoint();

   HttpMethodName getHttpMethod();

   int getTimeOffset();

   InputStream getContent();

   InputStream getContentUnwrapped();

   ReadLimitInfo getReadLimitInfo();

   Object getOriginalRequestObject();
}
