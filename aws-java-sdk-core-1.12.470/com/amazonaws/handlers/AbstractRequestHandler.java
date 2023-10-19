package com.amazonaws.handlers;

import com.amazonaws.Request;
import com.amazonaws.util.TimingInfo;

@Deprecated
public abstract class AbstractRequestHandler implements RequestHandler {
   @Override
   public void beforeRequest(Request<?> request) {
   }

   @Override
   public void afterResponse(Request<?> request, Object response, TimingInfo timingInfo) {
   }

   @Override
   public void afterError(Request<?> request, Exception e) {
   }
}
