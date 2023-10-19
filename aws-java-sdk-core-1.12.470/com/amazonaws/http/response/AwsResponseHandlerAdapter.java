package com.amazonaws.http.response;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.Request;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.MetadataCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AwsResponseHandlerAdapter<T> implements HttpResponseHandler<T> {
   private static final Log requestIdLog = LogFactory.getLog("com.amazonaws.requestId");
   private static final Log requestLog = AmazonHttpClient.requestLog;
   private final HttpResponseHandler<AmazonWebServiceResponse<T>> delegate;
   private final Request<?> request;
   private final AWSRequestMetrics awsRequestMetrics;
   private final MetadataCache responseMetadataCache;

   public AwsResponseHandlerAdapter(
      HttpResponseHandler<AmazonWebServiceResponse<T>> delegate, Request<?> request, AWSRequestMetrics awsRequestMetrics, MetadataCache responseMetadataCache
   ) {
      this.delegate = delegate;
      this.request = request;
      this.awsRequestMetrics = awsRequestMetrics;
      this.responseMetadataCache = responseMetadataCache;
   }

   @Override
   public T handle(HttpResponse response) throws Exception {
      AmazonWebServiceResponse<T> awsResponse = this.delegate.handle(response);
      if (awsResponse == null) {
         throw new RuntimeException(
            "Unable to unmarshall response metadata. Response Code: " + response.getStatusCode() + ", Response Text: " + response.getStatusText()
         );
      } else {
         AmazonWebServiceRequest userRequest = this.request.getOriginalRequest();
         if (userRequest.getCloneRoot() != null) {
            userRequest = userRequest.getCloneRoot();
         }

         this.responseMetadataCache.add(userRequest, awsResponse.getResponseMetadata());
         String awsRequestId = awsResponse.getRequestId();
         if (requestLog.isDebugEnabled()) {
            requestLog.debug("Received successful response: " + response.getStatusCode() + ", AWS Request ID: " + awsRequestId);
         }

         if (!this.logHeaderRequestId(response)) {
            this.logResponseRequestId(awsRequestId);
         }

         this.logExtendedRequestId(response);
         this.awsRequestMetrics.addProperty(AWSRequestMetrics.Field.AWSRequestID, awsRequestId);
         return this.fillInResponseMetadata(awsResponse, response);
      }
   }

   private <T> T fillInResponseMetadata(AmazonWebServiceResponse<T> awsResponse, HttpResponse httpResponse) {
      T result = awsResponse.getResult();
      if (result instanceof AmazonWebServiceResult) {
         ((AmazonWebServiceResult)result).setSdkResponseMetadata(awsResponse.getResponseMetadata()).setSdkHttpMetadata(SdkHttpMetadata.from(httpResponse));
      }

      return result;
   }

   @Override
   public boolean needsConnectionLeftOpen() {
      return this.delegate.needsConnectionLeftOpen();
   }

   private boolean logHeaderRequestId(HttpResponse response) {
      String reqIdHeader = response.getHeaders().get("x-amzn-RequestId");
      boolean isHeaderReqIdAvail = reqIdHeader != null;
      if (requestIdLog.isDebugEnabled() || requestLog.isDebugEnabled()) {
         String msg = "x-amzn-RequestId: " + (isHeaderReqIdAvail ? reqIdHeader : "not available");
         if (requestIdLog.isDebugEnabled()) {
            requestIdLog.debug(msg);
         } else {
            requestLog.debug(msg);
         }
      }

      return isHeaderReqIdAvail;
   }

   private void logResponseRequestId(String awsRequestId) {
      if (requestIdLog.isDebugEnabled() || requestLog.isDebugEnabled()) {
         String msg = "AWS Request ID: " + (awsRequestId == null ? "not available" : awsRequestId);
         if (requestIdLog.isDebugEnabled()) {
            requestIdLog.debug(msg);
         } else {
            requestLog.debug(msg);
         }
      }
   }

   private void logExtendedRequestId(HttpResponse response) {
      String reqId = response.getHeaders().get("x-amz-id-2");
      if (reqId != null && (requestIdLog.isDebugEnabled() || requestLog.isDebugEnabled())) {
         String msg = "AWS Extended Request ID: " + reqId;
         if (requestIdLog.isDebugEnabled()) {
            requestIdLog.debug(msg);
         } else {
            requestLog.debug(msg);
         }
      }
   }
}
