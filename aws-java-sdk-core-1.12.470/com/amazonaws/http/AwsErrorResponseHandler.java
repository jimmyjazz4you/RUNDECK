package com.amazonaws.http;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.AWSRequestMetrics;

@SdkInternalApi
class AwsErrorResponseHandler implements HttpResponseHandler<AmazonServiceException> {
   private final HttpResponseHandler<AmazonServiceException> delegate;
   private final AWSRequestMetrics awsRequestMetrics;
   private final ClientConfiguration clientConfiguration;

   AwsErrorResponseHandler(
      HttpResponseHandler<AmazonServiceException> errorResponseHandler, AWSRequestMetrics awsRequestMetrics, ClientConfiguration clientConfiguration
   ) {
      this.delegate = errorResponseHandler;
      this.awsRequestMetrics = awsRequestMetrics;
      this.clientConfiguration = clientConfiguration;
   }

   public AmazonServiceException handle(HttpResponse response) throws Exception {
      AmazonServiceException ase = this.handleAse(response);
      ase.setStatusCode(response.getStatusCode());
      ase.setServiceName(response.getRequest().getServiceName());
      ase.setProxyHost(this.clientConfiguration.getProxyHost());
      this.awsRequestMetrics
         .addPropertyWith(AWSRequestMetrics.Field.AWSRequestID, ase.getRequestId())
         .addPropertyWith(AWSRequestMetrics.Field.AWSErrorCode, ase.getErrorCode())
         .addPropertyWith(AWSRequestMetrics.Field.StatusCode, ase.getStatusCode());
      return ase;
   }

   private AmazonServiceException handleAse(HttpResponse response) throws Exception {
      int statusCode = response.getStatusCode();

      try {
         return this.delegate.handle(response);
      } catch (InterruptedException var5) {
         throw var5;
      } catch (Exception var6) {
         if (statusCode == 413) {
            AmazonServiceException exception = new AmazonServiceException("Request entity too large");
            exception.setServiceName(response.getRequest().getServiceName());
            exception.setStatusCode(statusCode);
            exception.setErrorType(AmazonServiceException.ErrorType.Client);
            exception.setErrorCode("Request entity too large");
            return exception;
         } else if (statusCode >= 500 && statusCode < 600) {
            AmazonServiceException exception = new AmazonServiceException(response.getStatusText());
            exception.setServiceName(response.getRequest().getServiceName());
            exception.setStatusCode(statusCode);
            exception.setErrorType(AmazonServiceException.ErrorType.Service);
            exception.setErrorCode(response.getStatusText());
            return exception;
         } else {
            throw var6;
         }
      }
   }

   @Override
   public boolean needsConnectionLeftOpen() {
      return this.delegate.needsConnectionLeftOpen();
   }
}
