package com.amazonaws.http;

import com.amazonaws.Request;
import com.amazonaws.metrics.MetricInputStreamEntity;
import com.amazonaws.metrics.ThroughputMetricType;
import com.amazonaws.metrics.internal.ServiceMetricTypeGuesser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;

public class RepeatableInputStreamRequestEntity extends BasicHttpEntity {
   private boolean firstAttempt = true;
   private InputStreamEntity inputStreamRequestEntity;
   private InputStream content;
   private static final Log log = LogFactory.getLog(RepeatableInputStreamRequestEntity.class);
   private IOException originalException;

   public RepeatableInputStreamRequestEntity(Request<?> request) {
      this.setChunked(false);
      long contentLength = -1L;

      try {
         String contentLengthString = request.getHeaders().get("Content-Length");
         if (contentLengthString != null) {
            contentLength = Long.parseLong(contentLengthString);
         }
      } catch (NumberFormatException var6) {
         log.warn("Unable to parse content length from request.  Buffering contents in memory.");
      }

      String contentType = request.getHeaders().get("Content-Type");
      ThroughputMetricType type = ServiceMetricTypeGuesser.guessThroughputMetricType(request, "UploadThroughput", "UploadByteCount");
      this.content = this.getContent(request);
      this.inputStreamRequestEntity = (InputStreamEntity)(type == null
         ? new InputStreamEntity(this.content, contentLength)
         : new MetricInputStreamEntity(type, this.content, contentLength));
      this.inputStreamRequestEntity.setContentType(contentType);
      this.setContent(this.content);
      this.setContentType(contentType);
      this.setContentLength(contentLength);
   }

   private InputStream getContent(Request<?> request) {
      return (InputStream)(request.getContent() == null ? new ByteArrayInputStream(new byte[0]) : request.getContent());
   }

   public boolean isChunked() {
      return false;
   }

   public boolean isRepeatable() {
      return this.content.markSupported() || this.inputStreamRequestEntity.isRepeatable();
   }

   public void writeTo(OutputStream output) throws IOException {
      try {
         if (!this.firstAttempt && this.isRepeatable()) {
            this.content.reset();
         }

         this.firstAttempt = false;
         this.inputStreamRequestEntity.writeTo(output);
      } catch (IOException var3) {
         if (this.originalException == null) {
            this.originalException = var3;
         }

         throw this.originalException;
      }
   }
}
