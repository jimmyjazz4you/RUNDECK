package com.amazonaws.handlers;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ThreadSafe
public class StackedRequestHandler implements IRequestHandler2 {
   private final List<RequestHandler2> inOrderRequestHandlers;
   private final List<RequestHandler2> reverseOrderRequestHandlers;

   public StackedRequestHandler(RequestHandler2... requestHandlers) {
      this(Arrays.asList(ValidationUtils.assertNotNull(requestHandlers, "requestHandlers")));
   }

   public StackedRequestHandler(List<RequestHandler2> requestHandlers) {
      this.inOrderRequestHandlers = ValidationUtils.assertNotNull(requestHandlers, "requestHandlers");
      this.reverseOrderRequestHandlers = new ArrayList<>(requestHandlers);
      Collections.reverse(this.reverseOrderRequestHandlers);
   }

   @Override
   public AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest origRequest) {
      AmazonWebServiceRequest toReturn = origRequest;

      for(RequestHandler2 handler : this.inOrderRequestHandlers) {
         toReturn = handler.beforeExecution(toReturn);
      }

      return toReturn;
   }

   @Override
   public AmazonWebServiceRequest beforeMarshalling(AmazonWebServiceRequest origRequest) {
      AmazonWebServiceRequest toReturn = origRequest;

      for(RequestHandler2 handler : this.inOrderRequestHandlers) {
         toReturn = handler.beforeMarshalling(toReturn);
      }

      return toReturn;
   }

   @Override
   public void beforeRequest(Request<?> request) {
      for(RequestHandler2 handler : this.inOrderRequestHandlers) {
         handler.beforeRequest(request);
      }
   }

   @Override
   public void beforeAttempt(HandlerBeforeAttemptContext context) {
      for(RequestHandler2 handler : this.inOrderRequestHandlers) {
         handler.beforeAttempt(context);
      }
   }

   @Override
   public HttpResponse beforeUnmarshalling(Request<?> request, HttpResponse origHttpResponse) {
      HttpResponse toReturn = origHttpResponse;

      for(RequestHandler2 handler : this.reverseOrderRequestHandlers) {
         toReturn = handler.beforeUnmarshalling(request, toReturn);
      }

      return toReturn;
   }

   @Override
   public void afterAttempt(HandlerAfterAttemptContext context) {
      for(RequestHandler2 handler : this.reverseOrderRequestHandlers) {
         handler.afterAttempt(context);
      }
   }

   @Override
   public void afterResponse(Request<?> request, Response<?> response) {
      for(RequestHandler2 handler : this.reverseOrderRequestHandlers) {
         handler.afterResponse(request, response);
      }
   }

   @Override
   public void afterError(Request<?> request, Response<?> response, Exception e) {
      for(RequestHandler2 handler : this.reverseOrderRequestHandlers) {
         handler.afterError(request, response, e);
      }
   }
}
