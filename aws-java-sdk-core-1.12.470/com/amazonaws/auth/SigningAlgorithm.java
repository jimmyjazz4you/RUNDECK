package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.SdkThreadLocalsRegistry;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;

public enum SigningAlgorithm {
   HmacSHA1,
   HmacSHA256;

   private final ThreadLocal<Mac> macReference;

   private SigningAlgorithm() {
      final String algorithmName = this.toString();
      this.macReference = SdkThreadLocalsRegistry.register(new ThreadLocal<Mac>() {
         protected Mac initialValue() {
            try {
               return Mac.getInstance(algorithmName);
            } catch (NoSuchAlgorithmException var2) {
               throw new SdkClientException("Unable to fetch Mac instance for Algorithm " + algorithmName + var2.getMessage(), var2);
            }
         }
      });
   }

   public Mac getMac() {
      return this.macReference.get();
   }
}
