package com.amazonaws.handlers;

import com.amazonaws.Request;
import com.amazonaws.util.TimingInfo;

@Deprecated
public interface RequestHandler {
   void beforeRequest(Request<?> var1);

   void afterResponse(Request<?> var1, Object var2, TimingInfo var3);

   void afterError(Request<?> var1, Exception var2);
}
