package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;
import java.net.URL;

public class PresignedUrlDownloadRequest extends AmazonWebServiceRequest implements Serializable {
   private URL presignedUrl;
   private long[] range;

   public PresignedUrlDownloadRequest(URL presignedUrl) {
      this.presignedUrl = presignedUrl;
   }

   public URL getPresignedUrl() {
      return this.presignedUrl;
   }

   public void setPresignedUrl(URL presignedUrl) {
      this.presignedUrl = presignedUrl;
   }

   public PresignedUrlDownloadRequest withPresignedUrl(URL presignedUrl) {
      this.setPresignedUrl(presignedUrl);
      return this;
   }

   public long[] getRange() {
      return this.range == null ? null : (long[])this.range.clone();
   }

   public void setRange(long start, long end) {
      this.range = new long[]{start, end};
   }

   public PresignedUrlDownloadRequest withRange(long start, long end) {
      this.setRange(start, end);
      return this;
   }

   public PresignedUrlDownloadRequest clone() {
      PresignedUrlDownloadRequest target = new PresignedUrlDownloadRequest(this.getPresignedUrl());
      this.copyBaseTo(target);
      if (this.getRange() != null) {
         target.setRange(this.getRange()[0], this.getRange()[1]);
      }

      return target;
   }
}
