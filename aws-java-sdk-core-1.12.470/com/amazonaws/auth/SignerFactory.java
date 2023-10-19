package com.amazonaws.auth;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.internal.config.InternalConfig;
import com.amazonaws.internal.config.SignerConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SignerFactory {
   public static final String QUERY_STRING_SIGNER = "QueryStringSignerType";
   public static final String VERSION_THREE_SIGNER = "AWS3SignerType";
   public static final String VERSION_FOUR_SIGNER = "AWS4SignerType";
   public static final String VERSION_FOUR_UNSIGNED_PAYLOAD_SIGNER = "AWS4UnsignedPayloadSignerType";
   public static final String NO_OP_SIGNER = "NoOpSignerType";
   private static final String S3_V4_SIGNER = "AWSS3V4SignerType";
   private static final Map<String, Class<? extends Signer>> SIGNERS = new ConcurrentHashMap<>();

   private SignerFactory() {
   }

   public static void registerSigner(String signerType, Class<? extends Signer> signerClass) {
      if (signerType == null) {
         throw new IllegalArgumentException("signerType cannot be null");
      } else if (signerClass == null) {
         throw new IllegalArgumentException("signerClass cannot be null");
      } else {
         SIGNERS.put(signerType, signerClass);
      }
   }

   public static Signer getSigner(String serviceName, String regionName) {
      return lookupAndCreateSigner(serviceName, regionName);
   }

   public static Signer getSignerByTypeAndService(String signerType, String serviceName) {
      return createSigner(signerType, serviceName);
   }

   private static String lookUpSignerTypeByServiceAndRegion(String serviceName, String regionName) {
      InternalConfig config = InternalConfig.Factory.getInternalConfig();
      SignerConfig signerConfig = config.getSignerConfig(serviceName, regionName);
      return signerConfig.getSignerType();
   }

   private static Signer lookupAndCreateSigner(String serviceName, String regionName) {
      String signerType = lookUpSignerTypeByServiceAndRegion(serviceName, regionName);
      return createSigner(signerType, serviceName);
   }

   private static Signer createSigner(String signerType, String serviceName) {
      Class<? extends Signer> signerClass = SIGNERS.get(signerType);
      if (signerClass == null) {
         throw new IllegalArgumentException("unknown signer type: " + signerType);
      } else {
         Signer signer = createSigner(signerType);
         if (signer instanceof ServiceAwareSigner) {
            ((ServiceAwareSigner)signer).setServiceName(serviceName);
         }

         return signer;
      }
   }

   @SdkProtectedApi
   public static Signer createSigner(String signerType, SignerParams params) {
      Signer signer = createSigner(signerType);
      if (signer instanceof ServiceAwareSigner) {
         ((ServiceAwareSigner)signer).setServiceName(params.getServiceName());
      }

      if (signer instanceof RegionAwareSigner) {
         ((RegionAwareSigner)signer).setRegionName(params.getRegionName());
      }

      return signer;
   }

   private static Signer createSigner(String signerType) {
      Class<? extends Signer> signerClass = SIGNERS.get(signerType);

      try {
         return signerClass.newInstance();
      } catch (InstantiationException var4) {
         throw new IllegalStateException("Cannot create an instance of " + signerClass.getName(), var4);
      } catch (IllegalAccessException var5) {
         throw new IllegalStateException("Cannot create an instance of " + signerClass.getName(), var5);
      }
   }

   static {
      SIGNERS.put("QueryStringSignerType", QueryStringSigner.class);
      SIGNERS.put("AWS3SignerType", AWS3Signer.class);
      SIGNERS.put("AWS4SignerType", AWS4Signer.class);
      SIGNERS.put("AWS4UnsignedPayloadSignerType", AWS4UnsignedPayloadSigner.class);
      SIGNERS.put("NoOpSignerType", NoOpSigner.class);

      try {
         SIGNERS.put("AWSS3V4SignerType", Class.forName("com.amazonaws.services.s3.internal.AWSS3V4Signer"));
      } catch (ClassNotFoundException var1) {
      }
   }
}
