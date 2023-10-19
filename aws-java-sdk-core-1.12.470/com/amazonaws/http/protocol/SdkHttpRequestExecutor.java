package com.amazonaws.http.protocol;

import com.amazonaws.internal.SdkMetricsSocket;
import com.amazonaws.internal.SdkSSLMetricsSocket;
import com.amazonaws.util.AWSRequestMetrics;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

public class SdkHttpRequestExecutor extends HttpRequestExecutor {
   protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
      AWSRequestMetrics awsRequestMetrics = (AWSRequestMetrics)context.getAttribute(AWSRequestMetrics.SIMPLE_NAME);
      if (awsRequestMetrics == null) {
         return super.doSendRequest(request, conn, context);
      } else {
         if (conn instanceof ManagedHttpClientConnection) {
            ManagedHttpClientConnection managedConn = (ManagedHttpClientConnection)conn;
            Socket sock = managedConn.getSocket();
            if (sock instanceof SdkMetricsSocket) {
               SdkMetricsSocket sdkMetricsSocket = (SdkMetricsSocket)sock;
               sdkMetricsSocket.setMetrics(awsRequestMetrics);
            } else if (sock instanceof SdkSSLMetricsSocket) {
               SdkSSLMetricsSocket sdkSSLMetricsSocket = (SdkSSLMetricsSocket)sock;
               sdkSSLMetricsSocket.setMetrics(awsRequestMetrics);
            }
         }

         awsRequestMetrics.startEvent(AWSRequestMetrics.Field.HttpClientSendRequestTime);

         HttpResponse var11;
         try {
            var11 = super.doSendRequest(request, conn, context);
         } finally {
            awsRequestMetrics.endEvent(AWSRequestMetrics.Field.HttpClientSendRequestTime);
         }

         return var11;
      }
   }

   protected HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
      AWSRequestMetrics awsRequestMetrics = (AWSRequestMetrics)context.getAttribute(AWSRequestMetrics.SIMPLE_NAME);
      if (awsRequestMetrics == null) {
         return super.doReceiveResponse(request, conn, context);
      } else {
         awsRequestMetrics.startEvent(AWSRequestMetrics.Field.HttpClientReceiveResponseTime);

         HttpResponse var5;
         try {
            var5 = super.doReceiveResponse(request, conn, context);
         } finally {
            awsRequestMetrics.endEvent(AWSRequestMetrics.Field.HttpClientReceiveResponseTime);
         }

         return var5;
      }
   }
}
