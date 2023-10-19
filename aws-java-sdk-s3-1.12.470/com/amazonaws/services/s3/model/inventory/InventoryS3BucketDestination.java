package com.amazonaws.services.s3.model.inventory;

import java.io.Serializable;

public class InventoryS3BucketDestination implements Serializable {
   private String accountId;
   private String bucketArn;
   private String format;
   private String prefix;
   private InventoryEncryption encryption;

   public String getAccountId() {
      return this.accountId;
   }

   public void setAccountId(String accountId) {
      this.accountId = accountId;
   }

   public InventoryS3BucketDestination withAccountId(String accountId) {
      this.setAccountId(accountId);
      return this;
   }

   public String getBucketArn() {
      return this.bucketArn;
   }

   public void setBucketArn(String bucketArn) {
      this.bucketArn = bucketArn;
   }

   public InventoryS3BucketDestination withBucketArn(String bucketArn) {
      this.setBucketArn(bucketArn);
      return this;
   }

   public String getFormat() {
      return this.format;
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public void setFormat(InventoryFormat format) {
      this.setFormat(format == null ? (String)null : format.toString());
   }

   public InventoryS3BucketDestination withFormat(String format) {
      this.setFormat(format);
      return this;
   }

   public InventoryS3BucketDestination withFormat(InventoryFormat format) {
      this.setFormat(format);
      return this;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public InventoryS3BucketDestination withPrefix(String prefix) {
      this.setPrefix(prefix);
      return this;
   }

   public InventoryEncryption getEncryption() {
      return this.encryption;
   }

   public void setEncryption(InventoryEncryption encryption) {
      this.encryption = encryption;
   }

   public InventoryS3BucketDestination withEncryption(InventoryEncryption encryption) {
      this.setEncryption(encryption);
      return this;
   }
}
