package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Date;

public class Bucket implements Serializable {
   private static final long serialVersionUID = -8646831898339939580L;
   private String name = null;
   private Owner owner = null;
   private Date creationDate = null;

   public Bucket() {
   }

   public Bucket(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return "S3Bucket [name=" + this.getName() + ", creationDate=" + this.getCreationDate() + ", owner=" + this.getOwner() + "]";
   }

   public Owner getOwner() {
      return this.owner;
   }

   public void setOwner(Owner owner) {
      this.owner = owner;
   }

   public Date getCreationDate() {
      return this.creationDate;
   }

   public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
