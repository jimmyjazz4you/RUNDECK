package com.amazonaws.services.s3.model;

import java.io.Serializable;

public class MultiFactorAuthentication implements Serializable {
   private String deviceSerialNumber;
   private String token;

   public MultiFactorAuthentication(String deviceSerialNumber, String token) {
      this.deviceSerialNumber = deviceSerialNumber;
      this.token = token;
   }

   public String getDeviceSerialNumber() {
      return this.deviceSerialNumber;
   }

   public void setDeviceSerialNumber(String deviceSerialNumber) {
      this.deviceSerialNumber = deviceSerialNumber;
   }

   public MultiFactorAuthentication withDeviceSerialNumber(String deviceSerialNumber) {
      this.setDeviceSerialNumber(deviceSerialNumber);
      return this;
   }

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public MultiFactorAuthentication withToken(String token) {
      this.setToken(token);
      return this;
   }
}
