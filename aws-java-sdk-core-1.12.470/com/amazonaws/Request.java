package com.amazonaws;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.util.AWSRequestMetrics;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface Request<T> extends SignableRequest<T>, HandlerContextAware {
   void setHeaders(Map<String, String> var1);

   void setResourcePath(String var1);

   Request<T> withParameter(String var1, String var2);

   void setParameters(Map<String, List<String>> var1);

   void addParameters(String var1, List<String> var2);

   void setEndpoint(URI var1);

   void setHttpMethod(HttpMethodName var1);

   String getServiceName();

   AmazonWebServiceRequest getOriginalRequest();

   void setTimeOffset(int var1);

   Request<T> withTimeOffset(int var1);

   AWSRequestMetrics getAWSRequestMetrics();

   void setAWSRequestMetrics(AWSRequestMetrics var1);
}
