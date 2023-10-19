package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.http.HttpMethodName;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public class PresignedUrlUploadRequest extends AmazonWebServiceRequest implements S3DataSource, Serializable {
   private static final long serialVersionUID = 1L;
   private URL presignedUrl;
   private HttpMethodName httpMethodName = HttpMethodName.PUT;
   private File file;
   private transient InputStream inputStream;
   private ObjectMetadata metadata;

   public PresignedUrlUploadRequest(URL presignedUrl) {
      this.presignedUrl = presignedUrl;
   }

   public URL getPresignedUrl() {
      return this.presignedUrl;
   }

   public void setPresignedUrl(URL presignedUrl) {
      this.presignedUrl = presignedUrl;
   }

   public PresignedUrlUploadRequest withPresignedUrl(URL presignedUrl) {
      this.setPresignedUrl(presignedUrl);
      return this;
   }

   public HttpMethodName getHttpMethodName() {
      return this.httpMethodName;
   }

   public void setHttpMethodName(HttpMethodName httpMethodName) {
      this.httpMethodName = httpMethodName;
   }

   public PresignedUrlUploadRequest withHttpMethodName(HttpMethodName httpMethodName) {
      this.setHttpMethodName(httpMethodName);
      return this;
   }

   @Override
   public File getFile() {
      return this.file;
   }

   @Override
   public void setFile(File file) {
      this.file = file;
   }

   public PresignedUrlUploadRequest withFile(File file) {
      this.setFile(file);
      return this;
   }

   @Override
   public InputStream getInputStream() {
      return this.inputStream;
   }

   @Override
   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public PresignedUrlUploadRequest withInputStream(InputStream inputStream) {
      this.setInputStream(inputStream);
      return this;
   }

   public ObjectMetadata getMetadata() {
      return this.metadata;
   }

   public void setMetadata(ObjectMetadata metadata) {
      this.metadata = metadata;
   }

   public PresignedUrlUploadRequest withMetadata(ObjectMetadata metadata) {
      this.setMetadata(metadata);
      return this;
   }
}
