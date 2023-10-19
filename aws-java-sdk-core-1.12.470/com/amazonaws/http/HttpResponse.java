package com.amazonaws.http;

import com.amazonaws.Request;
import com.amazonaws.util.CRC32ChecksumCalculatingInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;

public class HttpResponse {
   private final Request<?> request;
   private final HttpRequestBase httpRequest;
   private String statusText;
   private int statusCode;
   private InputStream content;
   private Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   private Map<String, List<String>> allHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   private HttpContext context;

   public HttpResponse(Request<?> request, HttpRequestBase httpRequest) {
      this(request, httpRequest, null);
   }

   public HttpResponse(Request<?> request, HttpRequestBase httpRequest, HttpContext context) {
      this.request = request;
      this.httpRequest = httpRequest;
      this.context = context;
   }

   public Request<?> getRequest() {
      return this.request;
   }

   public HttpRequestBase getHttpRequest() {
      return this.httpRequest;
   }

   /** @deprecated */
   public Map<String, String> getHeaders() {
      return this.headers;
   }

   public Map<String, List<String>> getAllHeaders() {
      return this.allHeaders;
   }

   public List<String> getHeaderValues(String header) {
      return this.allHeaders.get(header);
   }

   /** @deprecated */
   public String getHeader(String headerName) {
      return this.headers.get(headerName);
   }

   public void addHeader(String name, String value) {
      this.headers.put(name, value);
      List<String> values = this.allHeaders.get(name);
      if (values == null) {
         values = new ArrayList<>();
         this.allHeaders.put(name, values);
      }

      values.add(value);
   }

   public void setContent(InputStream content) {
      this.content = content;
   }

   public InputStream getContent() {
      return this.content;
   }

   public void setStatusText(String statusText) {
      this.statusText = statusText;
   }

   public String getStatusText() {
      return this.statusText;
   }

   public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public long getCRC32Checksum() {
      if (this.context == null) {
         return 0L;
      } else {
         CRC32ChecksumCalculatingInputStream crc32ChecksumInputStream = (CRC32ChecksumCalculatingInputStream)this.context
            .getAttribute(CRC32ChecksumCalculatingInputStream.class.getName());
         return crc32ChecksumInputStream == null ? 0L : crc32ChecksumInputStream.getCRC32Checksum();
      }
   }
}
