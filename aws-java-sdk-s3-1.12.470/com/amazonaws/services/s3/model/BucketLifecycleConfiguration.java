package com.amazonaws.services.s3.model;

import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BucketLifecycleConfiguration implements Serializable {
   public static final String ENABLED = "Enabled";
   public static final String DISABLED = "Disabled";
   private List<BucketLifecycleConfiguration.Rule> rules;

   public List<BucketLifecycleConfiguration.Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<BucketLifecycleConfiguration.Rule> rules) {
      this.rules = rules;
   }

   public BucketLifecycleConfiguration withRules(List<BucketLifecycleConfiguration.Rule> rules) {
      this.setRules(rules);
      return this;
   }

   public BucketLifecycleConfiguration withRules(BucketLifecycleConfiguration.Rule... rules) {
      this.setRules(Arrays.asList(rules));
      return this;
   }

   public BucketLifecycleConfiguration(List<BucketLifecycleConfiguration.Rule> rules) {
      this.rules = rules;
   }

   public BucketLifecycleConfiguration() {
   }

   public static class NoncurrentVersionExpiration implements Serializable {
      private int days = -1;
      private int newerNoncurrentVersions = -1;

      public void setDays(int days) {
         this.days = days;
      }

      public int getDays() {
         return this.days;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionExpiration withDays(int noncurrentDays) {
         this.days = noncurrentDays;
         return this;
      }

      public void setNewerNoncurrentVersions(int newerNoncurrentVersions) {
         this.newerNoncurrentVersions = newerNoncurrentVersions;
      }

      public int getNewerNoncurrentVersions() {
         return this.newerNoncurrentVersions;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionExpiration withNewerNoncurrentVersions(int newerNoncurrentVersions) {
         this.newerNoncurrentVersions = newerNoncurrentVersions;
         return this;
      }
   }

   public static class NoncurrentVersionTransition implements Serializable {
      private int days = -1;
      private String storageClass;
      private int newerNoncurrentVersions = -1;

      public void setDays(int expirationInDays) {
         this.days = expirationInDays;
      }

      public int getDays() {
         return this.days;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionTransition withDays(int expirationInDays) {
         this.days = expirationInDays;
         return this;
      }

      public void setStorageClass(StorageClass storageClass) {
         if (storageClass == null) {
            this.setStorageClass((String)null);
         } else {
            this.setStorageClass(storageClass.toString());
         }
      }

      public void setStorageClass(String storageClass) {
         this.storageClass = storageClass;
      }

      @Deprecated
      public StorageClass getStorageClass() {
         try {
            return StorageClass.fromValue(this.storageClass);
         } catch (IllegalArgumentException var2) {
            return null;
         }
      }

      public String getStorageClassAsString() {
         return this.storageClass;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionTransition withStorageClass(StorageClass storageClass) {
         this.setStorageClass(storageClass);
         return this;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionTransition withStorageClass(String storageClass) {
         this.setStorageClass(storageClass);
         return this;
      }

      public void setNewerNoncurrentVersions(int newerNoncurrentVersions) {
         this.newerNoncurrentVersions = newerNoncurrentVersions;
      }

      public int getNewerNoncurrentVersions() {
         return this.newerNoncurrentVersions;
      }

      public BucketLifecycleConfiguration.NoncurrentVersionTransition withNewerNoncurrentVersions(int newerNoncurrentVersions) {
         this.newerNoncurrentVersions = newerNoncurrentVersions;
         return this;
      }
   }

   public static class Rule implements Serializable {
      private String id;
      private String prefix;
      private String status;
      private LifecycleFilter filter;
      private int expirationInDays = -1;
      private boolean expiredObjectDeleteMarker = false;
      private Date expirationDate;
      private List<BucketLifecycleConfiguration.Transition> transitions;
      private List<BucketLifecycleConfiguration.NoncurrentVersionTransition> noncurrentVersionTransitions;
      private BucketLifecycleConfiguration.NoncurrentVersionExpiration noncurrentVersionExpiration;
      private AbortIncompleteMultipartUpload abortIncompleteMultipartUpload;

      public void setId(String id) {
         this.id = id;
      }

      @Deprecated
      public void setPrefix(String prefix) {
         this.prefix = prefix;
      }

      public void setExpirationInDays(int expirationInDays) {
         this.expirationInDays = expirationInDays;
      }

      @Deprecated
      public void setNoncurrentVersionExpirationInDays(int value) {
         BucketLifecycleConfiguration.NoncurrentVersionExpiration ncve = this.noncurrentVersionExpiration;
         if (ncve != null) {
            ncve.setDays(value);
         } else {
            this.noncurrentVersionExpiration = new BucketLifecycleConfiguration.NoncurrentVersionExpiration().withDays(value);
         }
      }

      public String getId() {
         return this.id;
      }

      public BucketLifecycleConfiguration.Rule withId(String id) {
         this.id = id;
         return this;
      }

      @Deprecated
      public String getPrefix() {
         return this.prefix;
      }

      @Deprecated
      public BucketLifecycleConfiguration.Rule withPrefix(String prefix) {
         this.prefix = prefix;
         return this;
      }

      public int getExpirationInDays() {
         return this.expirationInDays;
      }

      public BucketLifecycleConfiguration.Rule withExpirationInDays(int expirationInDays) {
         this.expirationInDays = expirationInDays;
         return this;
      }

      @Deprecated
      public int getNoncurrentVersionExpirationInDays() {
         BucketLifecycleConfiguration.NoncurrentVersionExpiration ncve = this.noncurrentVersionExpiration;
         return ncve != null ? ncve.getDays() : -1;
      }

      @Deprecated
      public BucketLifecycleConfiguration.Rule withNoncurrentVersionExpirationInDays(int value) {
         this.setNoncurrentVersionExpirationInDays(value);
         return this;
      }

      public String getStatus() {
         return this.status;
      }

      public void setStatus(String status) {
         this.status = status;
      }

      public BucketLifecycleConfiguration.Rule withStatus(String status) {
         this.setStatus(status);
         return this;
      }

      public void setExpirationDate(Date expirationDate) {
         this.expirationDate = expirationDate;
      }

      public Date getExpirationDate() {
         return this.expirationDate;
      }

      public BucketLifecycleConfiguration.Rule withExpirationDate(Date expirationDate) {
         this.expirationDate = expirationDate;
         return this;
      }

      @Deprecated
      public void setTransition(BucketLifecycleConfiguration.Transition transition) {
         this.setTransitions(Arrays.asList(transition));
      }

      @Deprecated
      public BucketLifecycleConfiguration.Transition getTransition() {
         List<BucketLifecycleConfiguration.Transition> transitions = this.getTransitions();
         return transitions != null && !transitions.isEmpty() ? transitions.get(transitions.size() - 1) : null;
      }

      @Deprecated
      public BucketLifecycleConfiguration.Rule withTransition(BucketLifecycleConfiguration.Transition transition) {
         this.setTransitions(Arrays.asList(transition));
         return this;
      }

      @Deprecated
      public void setNoncurrentVersionTransition(BucketLifecycleConfiguration.NoncurrentVersionTransition nonCurrentVersionTransition) {
         this.setNoncurrentVersionTransitions(Arrays.asList(nonCurrentVersionTransition));
      }

      @Deprecated
      public BucketLifecycleConfiguration.NoncurrentVersionTransition getNoncurrentVersionTransition() {
         List<BucketLifecycleConfiguration.NoncurrentVersionTransition> transitions = this.getNoncurrentVersionTransitions();
         return transitions != null && !transitions.isEmpty() ? transitions.get(transitions.size() - 1) : null;
      }

      @Deprecated
      public BucketLifecycleConfiguration.Rule withNoncurrentVersionTransition(
         BucketLifecycleConfiguration.NoncurrentVersionTransition nonCurrentVersionTransition
      ) {
         this.setNoncurrentVersionTransitions(Arrays.asList(nonCurrentVersionTransition));
         return this;
      }

      public List<BucketLifecycleConfiguration.Transition> getTransitions() {
         return this.transitions;
      }

      public void setTransitions(List<BucketLifecycleConfiguration.Transition> transitions) {
         this.transitions = new ArrayList<>(transitions);
      }

      public BucketLifecycleConfiguration.Rule withTransitions(List<BucketLifecycleConfiguration.Transition> transitions) {
         this.setTransitions(transitions);
         return this;
      }

      public BucketLifecycleConfiguration.Rule addTransition(BucketLifecycleConfiguration.Transition transition) {
         if (transition == null) {
            throw new IllegalArgumentException("Transition cannot be null.");
         } else {
            if (this.transitions == null) {
               this.transitions = new ArrayList<>();
            }

            this.transitions.add(transition);
            return this;
         }
      }

      public List<BucketLifecycleConfiguration.NoncurrentVersionTransition> getNoncurrentVersionTransitions() {
         return this.noncurrentVersionTransitions;
      }

      public void setNoncurrentVersionTransitions(List<BucketLifecycleConfiguration.NoncurrentVersionTransition> noncurrentVersionTransitions) {
         this.noncurrentVersionTransitions = new ArrayList<>(noncurrentVersionTransitions);
      }

      public BucketLifecycleConfiguration.Rule withNoncurrentVersionTransitions(
         List<BucketLifecycleConfiguration.NoncurrentVersionTransition> noncurrentVersionTransitions
      ) {
         this.setNoncurrentVersionTransitions(noncurrentVersionTransitions);
         return this;
      }

      public BucketLifecycleConfiguration.Rule addNoncurrentVersionTransition(
         BucketLifecycleConfiguration.NoncurrentVersionTransition noncurrentVersionTransition
      ) {
         if (noncurrentVersionTransition == null) {
            throw new IllegalArgumentException("NoncurrentVersionTransition cannot be null.");
         } else {
            if (this.noncurrentVersionTransitions == null) {
               this.noncurrentVersionTransitions = new ArrayList<>();
            }

            this.noncurrentVersionTransitions.add(noncurrentVersionTransition);
            return this;
         }
      }

      public BucketLifecycleConfiguration.NoncurrentVersionExpiration getNoncurrentVersionExpiration() {
         return this.noncurrentVersionExpiration;
      }

      public void setNoncurrentVersionExpiration(BucketLifecycleConfiguration.NoncurrentVersionExpiration noncurrentVersionExpiration) {
         this.noncurrentVersionExpiration = noncurrentVersionExpiration;
      }

      public BucketLifecycleConfiguration.Rule withNoncurrentVersionExpiration(
         BucketLifecycleConfiguration.NoncurrentVersionExpiration noncurrentVersionExpiration
      ) {
         this.setNoncurrentVersionExpiration(noncurrentVersionExpiration);
         return this;
      }

      public AbortIncompleteMultipartUpload getAbortIncompleteMultipartUpload() {
         return this.abortIncompleteMultipartUpload;
      }

      public void setAbortIncompleteMultipartUpload(AbortIncompleteMultipartUpload abortIncompleteMultipartUpload) {
         this.abortIncompleteMultipartUpload = abortIncompleteMultipartUpload;
      }

      public BucketLifecycleConfiguration.Rule withAbortIncompleteMultipartUpload(AbortIncompleteMultipartUpload abortIncompleteMultipartUpload) {
         this.setAbortIncompleteMultipartUpload(abortIncompleteMultipartUpload);
         return this;
      }

      public boolean isExpiredObjectDeleteMarker() {
         return this.expiredObjectDeleteMarker;
      }

      public void setExpiredObjectDeleteMarker(boolean expiredObjectDeleteMarker) {
         this.expiredObjectDeleteMarker = expiredObjectDeleteMarker;
      }

      public BucketLifecycleConfiguration.Rule withExpiredObjectDeleteMarker(boolean expiredObjectDeleteMarker) {
         this.expiredObjectDeleteMarker = expiredObjectDeleteMarker;
         return this;
      }

      public LifecycleFilter getFilter() {
         return this.filter;
      }

      public void setFilter(LifecycleFilter filter) {
         this.filter = filter;
      }

      public BucketLifecycleConfiguration.Rule withFilter(LifecycleFilter filter) {
         this.setFilter(filter);
         return this;
      }
   }

   public static class Transition implements Serializable {
      private int days = -1;
      private Date date;
      private String storageClass;

      public void setDays(int expirationInDays) {
         this.days = expirationInDays;
      }

      public int getDays() {
         return this.days;
      }

      public BucketLifecycleConfiguration.Transition withDays(int expirationInDays) {
         this.days = expirationInDays;
         return this;
      }

      public void setStorageClass(StorageClass storageClass) {
         if (storageClass == null) {
            this.setStorageClass((String)null);
         } else {
            this.setStorageClass(storageClass.toString());
         }
      }

      public void setStorageClass(String storageClass) {
         this.storageClass = storageClass;
      }

      @Deprecated
      public StorageClass getStorageClass() {
         try {
            return StorageClass.fromValue(this.storageClass);
         } catch (IllegalArgumentException var2) {
            return null;
         }
      }

      public String getStorageClassAsString() {
         return this.storageClass;
      }

      public BucketLifecycleConfiguration.Transition withStorageClass(StorageClass storageClass) {
         this.setStorageClass(storageClass);
         return this;
      }

      public BucketLifecycleConfiguration.Transition withStorageClass(String storageClass) {
         this.setStorageClass(storageClass);
         return this;
      }

      public void setDate(Date expirationDate) {
         this.date = expirationDate;
      }

      public Date getDate() {
         return this.date;
      }

      public BucketLifecycleConfiguration.Transition withDate(Date expirationDate) {
         this.date = expirationDate;
         return this;
      }
   }
}
