package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class RequestPaymentConfiguration implements Serializable {
   private RequestPaymentConfiguration.Payer payer;

   public RequestPaymentConfiguration(RequestPaymentConfiguration.Payer payer) {
      this.payer = payer;
   }

   public RequestPaymentConfiguration.Payer getPayer() {
      return this.payer;
   }

   public void setPayer(RequestPaymentConfiguration.Payer payer) {
      this.payer = payer;
   }

   public static enum Payer {
      Requester,
      BucketOwner;
   }
}
