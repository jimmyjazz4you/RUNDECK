package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Map;

public class EncryptedGetObjectRequest extends GetObjectRequest implements Serializable {
   private ExtraMaterialsDescription supplemental = ExtraMaterialsDescription.NONE;
   private String instructionFileSuffix;
   private boolean keyWrapExpected;

   public EncryptedGetObjectRequest(String bucketName, String key) {
      this(bucketName, key, null);
   }

   public EncryptedGetObjectRequest(String bucketName, String key, String versionId) {
      super(bucketName, key, versionId);
      this.setKey(key);
      this.setVersionId(versionId);
   }

   public EncryptedGetObjectRequest(S3ObjectId s3ObjectId) {
      super(s3ObjectId);
   }

   public EncryptedGetObjectRequest(String bucketName, String key, boolean isRequesterPays) {
      super(bucketName, key, isRequesterPays);
   }

   @Deprecated
   public EncryptedGetObjectRequest withRange(long start, long end) {
      super.withRange(start, end);
      return this;
   }

   @Deprecated
   @Override
   public void setRange(long start, long end) {
      super.setRange(start, end);
   }

   @Deprecated
   public EncryptedGetObjectRequest withRange(long start) {
      super.withRange(start);
      return this;
   }

   @Deprecated
   @Override
   public void setRange(long start) {
      super.setRange(start);
   }

   @Deprecated
   @Override
   public long[] getRange() {
      return super.getRange();
   }

   @Deprecated
   public EncryptedGetObjectRequest withPartNumber(Integer partNumber) {
      super.withPartNumber(partNumber);
      return this;
   }

   @Deprecated
   @Override
   public void setPartNumber(Integer partNumber) {
      super.setPartNumber(partNumber);
   }

   @Deprecated
   @Override
   public Integer getPartNumber() {
      return super.getPartNumber();
   }

   public ExtraMaterialsDescription getExtraMaterialDescription() {
      return this.supplemental;
   }

   public void setExtraMaterialDescription(ExtraMaterialsDescription supplemental) {
      this.supplemental = supplemental == null ? ExtraMaterialsDescription.NONE : supplemental;
   }

   public EncryptedGetObjectRequest withExtraMaterialsDescription(ExtraMaterialsDescription supplemental) {
      this.setExtraMaterialDescription(supplemental);
      return this;
   }

   public EncryptedGetObjectRequest withExtraMaterialsDescription(Map<String, String> supplemental) {
      this.setExtraMaterialDescription(supplemental == null ? null : new ExtraMaterialsDescription(supplemental));
      return this;
   }

   public String getInstructionFileSuffix() {
      return this.instructionFileSuffix;
   }

   public void setInstructionFileSuffix(String instructionFileSuffix) {
      this.instructionFileSuffix = instructionFileSuffix;
   }

   public EncryptedGetObjectRequest withInstructionFileSuffix(String instructionFileSuffix) {
      this.instructionFileSuffix = instructionFileSuffix;
      return this;
   }

   public boolean isKeyWrapExpected() {
      return this.keyWrapExpected;
   }

   public void setKeyWrapExpected(boolean keyWrapExpected) {
      this.keyWrapExpected = keyWrapExpected;
   }

   public EncryptedGetObjectRequest withKeyWrapExpected(boolean keyWrapExpected) {
      this.keyWrapExpected = keyWrapExpected;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof EncryptedGetObjectRequest)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         EncryptedGetObjectRequest that = (EncryptedGetObjectRequest)o;
         if (this.keyWrapExpected != that.isKeyWrapExpected()) {
            return false;
         } else if (this.supplemental != null ? this.supplemental.equals(that.supplemental) : that.supplemental == null) {
            return this.getInstructionFileSuffix() != null
               ? this.getInstructionFileSuffix().equals(that.getInstructionFileSuffix())
               : that.getInstructionFileSuffix() == null;
         } else {
            return false;
         }
      }
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = super.hashCode();
      result = prime * result + (this.supplemental != null ? this.supplemental.hashCode() : 0);
      result = prime * result + (this.getInstructionFileSuffix() != null ? this.getInstructionFileSuffix().hashCode() : 0);
      return prime * result + (this.isKeyWrapExpected() ? 1 : 0);
   }
}
