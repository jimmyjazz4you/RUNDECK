package com.amazonaws.services.s3.model.intelligenttiering;

import java.io.Serializable;
import java.util.List;

public class IntelligentTieringConfiguration implements Serializable {
   private String id;
   private IntelligentTieringFilter filter;
   private IntelligentTieringStatus status;
   private List<Tiering> tierings;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public IntelligentTieringConfiguration withId(String id) {
      this.setId(id);
      return this;
   }

   public IntelligentTieringFilter getFilter() {
      return this.filter;
   }

   public void setFilter(IntelligentTieringFilter filter) {
      this.filter = filter;
   }

   public IntelligentTieringConfiguration withFilter(IntelligentTieringFilter filter) {
      this.setFilter(filter);
      return this;
   }

   public IntelligentTieringStatus getStatus() {
      return this.status;
   }

   public void setStatus(IntelligentTieringStatus status) {
      this.status = status;
   }

   public IntelligentTieringConfiguration withStatus(IntelligentTieringStatus status) {
      this.setStatus(status);
      return this;
   }

   public List<Tiering> getTierings() {
      return this.tierings;
   }

   public void setTierings(List<Tiering> tierings) {
      this.tierings = tierings;
   }

   public IntelligentTieringConfiguration withTierings(List<Tiering> tierings) {
      this.setTierings(tierings);
      return this;
   }
}
