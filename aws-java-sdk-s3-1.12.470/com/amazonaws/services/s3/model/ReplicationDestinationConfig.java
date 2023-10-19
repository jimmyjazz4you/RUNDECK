package com.amazonaws.services.s3.model;

import com.amazonaws.util.json.Jackson;
import java.io.Serializable;

public class ReplicationDestinationConfig implements Serializable {
   private String bucketARN;
   private String account;
   private String storageClass;
   private AccessControlTranslation accessControlTranslation;
   private EncryptionConfiguration encryptionConfiguration;
   private ReplicationTime replicationTime;
   private Metrics metrics;

   public String getBucketARN() {
      return this.bucketARN;
   }

   public void setBucketARN(String bucketARN) {
      if (bucketARN == null) {
         throw new IllegalArgumentException("Bucket name cannot be null");
      } else {
         this.bucketARN = bucketARN;
      }
   }

   public ReplicationDestinationConfig withBucketARN(String bucketARN) {
      this.setBucketARN(bucketARN);
      return this;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public ReplicationDestinationConfig withAccount(String account) {
      this.setAccount(account);
      return this;
   }

   public void setStorageClass(String storageClass) {
      this.storageClass = storageClass;
   }

   public void setStorageClass(StorageClass storageClass) {
      this.setStorageClass(storageClass == null ? (String)null : storageClass.toString());
   }

   public ReplicationDestinationConfig withStorageClass(String storageClass) {
      this.setStorageClass(storageClass);
      return this;
   }

   public ReplicationDestinationConfig withStorageClass(StorageClass storageClass) {
      this.setStorageClass(storageClass == null ? (String)null : storageClass.toString());
      return this;
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public AccessControlTranslation getAccessControlTranslation() {
      return this.accessControlTranslation;
   }

   public void setAccessControlTranslation(AccessControlTranslation accessControlTranslation) {
      this.accessControlTranslation = accessControlTranslation;
   }

   public ReplicationDestinationConfig withAccessControlTranslation(AccessControlTranslation accessControlTranslation) {
      this.setAccessControlTranslation(accessControlTranslation);
      return this;
   }

   public EncryptionConfiguration getEncryptionConfiguration() {
      return this.encryptionConfiguration;
   }

   public void setEncryptionConfiguration(EncryptionConfiguration encryptionConfiguration) {
      this.encryptionConfiguration = encryptionConfiguration;
   }

   public ReplicationDestinationConfig withEncryptionConfiguration(EncryptionConfiguration encryptionConfiguration) {
      this.setEncryptionConfiguration(encryptionConfiguration);
      return this;
   }

   public ReplicationTime getReplicationTime() {
      return this.replicationTime;
   }

   public void setReplicationTime(ReplicationTime replicationTime) {
      this.replicationTime = replicationTime;
   }

   public ReplicationDestinationConfig withReplicationTime(ReplicationTime replicationTime) {
      this.setReplicationTime(replicationTime);
      return this;
   }

   public Metrics getMetrics() {
      return this.metrics;
   }

   public void setMetrics(Metrics metrics) {
      this.metrics = metrics;
   }

   public ReplicationDestinationConfig withMetrics(Metrics metrics) {
      this.setMetrics(metrics);
      return this;
   }

   @Override
   public String toString() {
      return Jackson.toJsonString(this);
   }
}
