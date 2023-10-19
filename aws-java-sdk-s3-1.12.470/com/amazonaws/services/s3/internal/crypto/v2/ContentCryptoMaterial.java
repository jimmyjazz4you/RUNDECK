package com.amazonaws.services.s3.internal.crypto.v2;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.s3.KeyWrapException;
import com.amazonaws.services.s3.internal.crypto.CipherLite;
import com.amazonaws.services.s3.internal.crypto.ContentCryptoScheme;
import com.amazonaws.services.s3.internal.crypto.CryptoUtils;
import com.amazonaws.services.s3.internal.crypto.keywrap.InternalKeyWrapAlgorithm;
import com.amazonaws.services.s3.internal.crypto.keywrap.KMSKeyWrapperContext;
import com.amazonaws.services.s3.internal.crypto.keywrap.KeyWrapAlgorithmResolver;
import com.amazonaws.services.s3.internal.crypto.keywrap.KeyWrapper;
import com.amazonaws.services.s3.internal.crypto.keywrap.KeyWrapperContext;
import com.amazonaws.services.s3.internal.crypto.keywrap.KeyWrapperFactory;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoKeyWrapAlgorithm;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.CryptoRangeGetMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.EncryptionMaterialsAccessor;
import com.amazonaws.services.s3.model.EncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ExtraMaterialsDescription;
import com.amazonaws.services.s3.model.KMSEncryptionMaterials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutInstructionFileRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.Base64;
import com.amazonaws.util.BinaryUtils;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.Throwables;
import com.amazonaws.util.json.Jackson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.Provider;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

final class ContentCryptoMaterial {
   private final InternalKeyWrapAlgorithm keyWrappingAlgorithm;
   private final CipherLite cipherLite;
   private final Map<String, String> kekMaterialsDescription;
   private final byte[] encryptedCEK;

   ContentCryptoMaterial(Map<String, String> kekMaterialsDescription, byte[] encryptedCEK, InternalKeyWrapAlgorithm keyWrappingAlgorithm, CipherLite cipherLite) {
      this.cipherLite = cipherLite;
      this.keyWrappingAlgorithm = keyWrappingAlgorithm;
      this.encryptedCEK = (byte[])encryptedCEK.clone();
      this.kekMaterialsDescription = kekMaterialsDescription;
   }

   InternalKeyWrapAlgorithm getKeyWrappingAlgorithm() {
      return this.keyWrappingAlgorithm;
   }

   ContentCryptoScheme getContentCryptoScheme() {
      return this.cipherLite.getContentCryptoScheme();
   }

   ObjectMetadata toObjectMetadata(ObjectMetadata metadata) {
      byte[] encryptedCEK = this.getEncryptedCEK();
      metadata.addUserMetadata("x-amz-key-v2", Base64.encodeAsString(encryptedCEK));
      byte[] iv = this.cipherLite.getIV();
      metadata.addUserMetadata("x-amz-iv", Base64.encodeAsString(iv));
      metadata.addUserMetadata("x-amz-matdesc", this.kekMaterialDescAsJson());
      ContentCryptoScheme scheme = this.getContentCryptoScheme();
      metadata.addUserMetadata("x-amz-cek-alg", scheme.getCipherAlgorithm());
      int tagLen = scheme.getTagLengthInBits();
      if (tagLen > 0) {
         metadata.addUserMetadata("x-amz-tag-len", String.valueOf(tagLen));
      }

      InternalKeyWrapAlgorithm keyWrapAlgo = this.getKeyWrappingAlgorithm();
      if (keyWrapAlgo != null) {
         metadata.addUserMetadata("x-amz-wrap-alg", keyWrapAlgo.algorithmName());
      }

      return metadata;
   }

   String toJsonString() {
      Map<String, String> map = new HashMap<>();
      byte[] encryptedCEK = this.getEncryptedCEK();
      map.put("x-amz-key-v2", Base64.encodeAsString(encryptedCEK));
      byte[] iv = this.cipherLite.getIV();
      map.put("x-amz-iv", Base64.encodeAsString(iv));
      map.put("x-amz-matdesc", this.kekMaterialDescAsJson());
      ContentCryptoScheme scheme = this.getContentCryptoScheme();
      map.put("x-amz-cek-alg", scheme.getCipherAlgorithm());
      int tagLen = scheme.getTagLengthInBits();
      if (tagLen > 0) {
         map.put("x-amz-tag-len", String.valueOf(tagLen));
      }

      InternalKeyWrapAlgorithm keyWrapAlgo = this.getKeyWrappingAlgorithm();
      if (keyWrapAlgo != null) {
         map.put("x-amz-wrap-alg", keyWrapAlgo.algorithmName());
      }

      return Jackson.toJsonString(map);
   }

   private String kekMaterialDescAsJson() {
      Map<String, String> kekMaterialDesc = this.getKEKMaterialsDescription();
      if (kekMaterialDesc == null) {
         kekMaterialDesc = Collections.emptyMap();
      }

      return Jackson.toJsonString(kekMaterialDesc);
   }

   private static Map<String, String> matdescFromJson(String json) {
      Map<String, String> map = Jackson.stringMapFromJsonString(json);
      return map == null ? null : Collections.unmodifiableMap(map);
   }

   private static SecretKey decryptCEK(KeyWrapperContext context) {
      if (isV1DecryptContext(context)) {
         return decryptV1CEK(context);
      } else {
         if (context.internalKeyWrapAlgorithm().isKMS()) {
            validateKMSParameters(context);
         }

         Key kek = getDecryptionKeyFrom(context.materials());
         String keyGeneratorAlgorithm = context.internalKeyWrapAlgorithm().isKMS()
            ? context.contentCryptoScheme().getKeyGeneratorAlgorithm()
            : kek.getAlgorithm();
         KeyWrapper keyWrapper = KeyWrapperFactory.defaultInstance().createKeyWrapper(context);
         return new SecretKeySpec(keyWrapper.unwrapCek(context.cekSecured(), kek), keyGeneratorAlgorithm);
      }
   }

   private static boolean isV1DecryptContext(KeyWrapperContext context) {
      InternalKeyWrapAlgorithm keyWrapAlgorithm = context.internalKeyWrapAlgorithm();
      return keyWrapAlgorithm == null || keyWrapAlgorithm.isV1Algorithm();
   }

   private static void validateKMSParameters(KeyWrapperContext context) {
      KMSKeyWrapperContext kmsKeyWrapperContext = context.kmsKeyWrapperContext();
      if (kmsKeyWrapperContext == null) {
         throw new IllegalStateException("Missing KMS parameters");
      } else {
         Map<String, String> kmsMaterialsDescription = kmsKeyWrapperContext.kmsMaterialsDescription();
         if (kmsMaterialsDescription == null) {
            throw new IllegalStateException("Key materials from KMS must contain description entries");
         } else {
            String cekAlgoFromMaterials = kmsMaterialsDescription.get("aws:x-amz-cek-alg");
            if (cekAlgoFromMaterials == null) {
               throw new IllegalStateException("Could not find required description in key material: aws:x-amz-cek-alg");
            } else {
               String cekAlgoFromCryptoScheme = CryptoUtils.normalizeContentAlgorithmForValidation(context.contentCryptoScheme().getCipherAlgorithm());
               if (!cekAlgoFromMaterials.equals(cekAlgoFromCryptoScheme)) {
                  throw new IllegalStateException(
                     "Algorithm values from materials and metadata/instruction file don't match:" + cekAlgoFromMaterials + ", " + cekAlgoFromCryptoScheme
                  );
               }
            }
         }
      }
   }

   private static SecretKey decryptV1CEK(KeyWrapperContext context) {
      InternalKeyWrapAlgorithm internalKeyWrapAlgorithm = context.internalKeyWrapAlgorithm();
      if (internalKeyWrapAlgorithm != null && internalKeyWrapAlgorithm.isKMS()) {
         return decryptV1CEKByKMS(context);
      } else {
         String keyWrapAlgo = internalKeyWrapAlgorithm != null ? internalKeyWrapAlgorithm.algorithmName() : null;
         Key kek;
         if (context.materials().getKeyPair() != null) {
            kek = context.materials().getKeyPair().getPrivate();
         } else {
            kek = context.materials().getSymmetricKey();
         }

         if (kek == null) {
            throw new SdkClientException("Key encrypting key not available");
         } else {
            Provider securityProvider = context.cryptoProvider();

            try {
               if (keyWrapAlgo != null) {
                  Cipher cipher = securityProvider == null ? Cipher.getInstance(keyWrapAlgo) : Cipher.getInstance(keyWrapAlgo, securityProvider);
                  cipher.init(4, kek);
                  return (SecretKey)cipher.unwrap(context.cekSecured(), keyWrapAlgo, 3);
               } else {
                  Cipher cipher;
                  if (securityProvider != null) {
                     cipher = Cipher.getInstance(kek.getAlgorithm(), securityProvider);
                  } else {
                     cipher = Cipher.getInstance(kek.getAlgorithm());
                  }

                  cipher.init(2, kek);
                  byte[] decryptedSymmetricKeyBytes = cipher.doFinal(context.cekSecured());
                  return new SecretKeySpec(decryptedSymmetricKeyBytes, "AES");
               }
            } catch (Exception var7) {
               throw Throwables.failure(var7, "Unable to decrypt symmetric key from object metadata");
            }
         }
      }
   }

   private static SecretKey decryptV1CEKByKMS(KeyWrapperContext context) {
      KMSKeyWrapperContext kmsKeyWrapperContext = context.kmsKeyWrapperContext();
      if (kmsKeyWrapperContext == null) {
         throw new IllegalStateException("Missing KMS parameters");
      } else {
         String cmk = context.materials().getCustomerMasterKeyId();
         if (null != cmk && !cmk.isEmpty()) {
            DecryptRequest kmsreq = new DecryptRequest()
               .withEncryptionContext(context.materials().getMaterialsDescription())
               .withCiphertextBlob(ByteBuffer.wrap(context.cekSecured()))
               .withKeyId(cmk);
            DecryptResult result = kmsKeyWrapperContext.kms().decrypt(kmsreq);
            return new SecretKeySpec(BinaryUtils.copyAllBytesFrom(result.getPlaintext()), context.contentCryptoScheme().getKeyGeneratorAlgorithm());
         } else {
            throw new IllegalArgumentException("The CMK must be specified to decrypt KMS protected objects");
         }
      }
   }

   static ContentCryptoMaterial fromObjectMetadata(
      Map<String, String> metadata,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      return fromObjectMetadata0(metadata, kekMaterialAccessor, cryptoConfiguration, null, ExtraMaterialsDescription.NONE, keyWrapExpected, kms);
   }

   static ContentCryptoMaterial fromObjectMetadata(
      Map<String, String> metadata,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      long[] range,
      ExtraMaterialsDescription extra,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      return fromObjectMetadata0(metadata, kekMaterialAccessor, cryptoConfiguration, range, extra, keyWrapExpected, kms);
   }

   private static ContentCryptoMaterial fromObjectMetadata0(
      Map<String, String> userMeta,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      long[] range,
      ExtraMaterialsDescription extra,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      String b64key = userMeta.get("x-amz-key-v2");
      if (b64key == null) {
         b64key = userMeta.get("x-amz-key");
         if (b64key == null) {
            throw new SdkClientException("Content encrypting key not found.");
         }
      }

      byte[] cekWrapped = Base64.decode(b64key);
      byte[] iv = Base64.decode(userMeta.get("x-amz-iv"));
      if (cekWrapped != null && iv != null) {
         String matdescStr = userMeta.get("x-amz-matdesc");
         String keyWrapAlgo = userMeta.get("x-amz-wrap-alg");
         Map<String, String> coreMatDesc = matdescFromJson(matdescStr);
         InternalKeyWrapAlgorithm internalKeyWrapAlgorithm = InternalKeyWrapAlgorithm.fromAlgorithmName(keyWrapAlgo);
         validateKeyWrapAlgorithmForDecrypt(internalKeyWrapAlgorithm, keyWrapExpected, cryptoConfiguration.getCryptoMode());
         boolean isKMS = internalKeyWrapAlgorithm != null && internalKeyWrapAlgorithm.isKMS();
         Map<String, String> mergedMatDesc = !isKMS && extra != null ? extra.mergeInto(coreMatDesc) : coreMatDesc;
         EncryptionMaterials materials;
         if (isKMS) {
            materials = kekMaterialAccessor instanceof EncryptionMaterialsProvider
               ? ((EncryptionMaterialsProvider)kekMaterialAccessor).getEncryptionMaterials()
               : null;
         } else {
            materials = kekMaterialAccessor.getEncryptionMaterials(mergedMatDesc);
         }

         validateMaterialsForDecrypt(materials, mergedMatDesc, cryptoConfiguration.getCryptoMode(), internalKeyWrapAlgorithm);
         String cekAlgo = userMeta.get("x-amz-cek-alg");
         boolean isRangeGet = range != null;
         ContentCryptoScheme contentCryptoScheme = ContentCryptoScheme.fromCEKAlgo(cekAlgo, isRangeGet);
         if (isRangeGet) {
            assertCryptoSchemeAllowedForRangeGet(contentCryptoScheme, cryptoConfiguration.getCryptoMode(), cryptoConfiguration.getRangeGetMode());
            iv = contentCryptoScheme.adjustIV(iv, range[0]);
         } else {
            int tagLenExpected = contentCryptoScheme.getTagLengthInBits();
            if (tagLenExpected > 0) {
               String s = userMeta.get("x-amz-tag-len");
               int tagLenActual = Integer.parseInt(s);
               if (tagLenExpected != tagLenActual) {
                  throw new SdkClientException("Unsupported tag length: " + tagLenActual + ", expected: " + tagLenExpected);
               }
            }
         }

         SecretKey cek = decryptCEK(
            KeyWrapperContext.builder()
               .cekSecured(cekWrapped)
               .internalKeyWrapAlgorithm(internalKeyWrapAlgorithm)
               .materials(materials)
               .cryptoProvider(cryptoConfiguration.getCryptoProvider())
               .secureRandom(cryptoConfiguration.getSecureRandom())
               .contentCryptoScheme(contentCryptoScheme)
               .kmsKeyWrapperContext(KMSKeyWrapperContext.builder().kms(kms).kmsMaterialsDescription(mergedMatDesc).build())
               .build()
         );
         Provider securityProvider = cryptoConfiguration.getCryptoProvider();
         boolean alwaysUseSecurityProvider = cryptoConfiguration.getAlwaysUseCryptoProvider();
         return new ContentCryptoMaterial(
            mergedMatDesc, cekWrapped, internalKeyWrapAlgorithm, contentCryptoScheme.createCipherLite(cek, iv, 2, securityProvider, alwaysUseSecurityProvider)
         );
      } else {
         throw new SdkClientException("Content encrypting key or IV not found.");
      }
   }

   static ContentCryptoMaterial fromInstructionFile(
      Map<String, String> instFile,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      return fromInstructionFile0(instFile, kekMaterialAccessor, cryptoConfiguration, null, ExtraMaterialsDescription.NONE, keyWrapExpected, kms);
   }

   static ContentCryptoMaterial fromInstructionFile(
      Map<String, String> instFile,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      long[] range,
      ExtraMaterialsDescription extra,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      return fromInstructionFile0(instFile, kekMaterialAccessor, cryptoConfiguration, range, extra, keyWrapExpected, kms);
   }

   private static ContentCryptoMaterial fromInstructionFile0(
      Map<String, String> instFile,
      EncryptionMaterialsAccessor kekMaterialAccessor,
      CryptoConfigurationV2 cryptoConfiguration,
      long[] range,
      ExtraMaterialsDescription extra,
      boolean keyWrapExpected,
      AWSKMS kms
   ) {
      String b64key = instFile.get("x-amz-key-v2");
      if (b64key == null) {
         b64key = instFile.get("x-amz-key");
         if (b64key == null) {
            throw new SdkClientException("Content encrypting key not found.");
         }
      }

      byte[] cekWrapped = Base64.decode(b64key);
      byte[] iv = Base64.decode(instFile.get("x-amz-iv"));
      if (cekWrapped != null && iv != null) {
         String keyWrapAlgo = instFile.get("x-amz-wrap-alg");
         InternalKeyWrapAlgorithm internalKeyWrapAlgorithm = InternalKeyWrapAlgorithm.fromAlgorithmName(keyWrapAlgo);
         validateKeyWrapAlgorithmForDecrypt(internalKeyWrapAlgorithm, keyWrapExpected, cryptoConfiguration.getCryptoMode());
         boolean isKMS = internalKeyWrapAlgorithm != null && internalKeyWrapAlgorithm.isKMS();
         String matdescStr = instFile.get("x-amz-matdesc");
         Map<String, String> coreMatDesc = matdescFromJson(matdescStr);
         Map<String, String> mergedMatDesc = extra != null && !isKMS ? extra.mergeInto(coreMatDesc) : coreMatDesc;
         EncryptionMaterials materials;
         if (isKMS) {
            materials = kekMaterialAccessor instanceof EncryptionMaterialsProvider
               ? ((EncryptionMaterialsProvider)kekMaterialAccessor).getEncryptionMaterials()
               : null;
         } else {
            materials = kekMaterialAccessor.getEncryptionMaterials(mergedMatDesc);
         }

         validateMaterialsForDecrypt(materials, mergedMatDesc, cryptoConfiguration.getCryptoMode(), internalKeyWrapAlgorithm);
         String cekAlgo = instFile.get("x-amz-cek-alg");
         boolean isRangeGet = range != null;
         ContentCryptoScheme contentCryptoScheme = ContentCryptoScheme.fromCEKAlgo(cekAlgo, isRangeGet);
         if (isRangeGet) {
            assertCryptoSchemeAllowedForRangeGet(contentCryptoScheme, cryptoConfiguration.getCryptoMode(), cryptoConfiguration.getRangeGetMode());
            iv = contentCryptoScheme.adjustIV(iv, range[0]);
         } else {
            int tagLenExpected = contentCryptoScheme.getTagLengthInBits();
            if (tagLenExpected > 0) {
               String s = instFile.get("x-amz-tag-len");
               int tagLenActual = Integer.parseInt(s);
               if (tagLenExpected != tagLenActual) {
                  throw new SdkClientException("Unsupported tag length: " + tagLenActual + ", expected: " + tagLenExpected);
               }
            }
         }

         SecretKey cek = decryptCEK(
            KeyWrapperContext.builder()
               .cekSecured(cekWrapped)
               .internalKeyWrapAlgorithm(internalKeyWrapAlgorithm)
               .materials(materials)
               .cryptoProvider(cryptoConfiguration.getCryptoProvider())
               .secureRandom(cryptoConfiguration.getSecureRandom())
               .contentCryptoScheme(contentCryptoScheme)
               .kmsKeyWrapperContext(KMSKeyWrapperContext.builder().kms(kms).kmsMaterialsDescription(mergedMatDesc).build())
               .build()
         );
         return new ContentCryptoMaterial(
            mergedMatDesc,
            cekWrapped,
            internalKeyWrapAlgorithm,
            contentCryptoScheme.createCipherLite(cek, iv, 2, cryptoConfiguration.getCryptoProvider(), cryptoConfiguration.getAlwaysUseCryptoProvider())
         );
      } else {
         throw new SdkClientException("Necessary encryption info not found in the instruction file " + instFile);
      }
   }

   static String parseInstructionFile(S3Object instructionFile) {
      try {
         return convertStreamToString(instructionFile.getObjectContent());
      } catch (Exception var2) {
         throw Throwables.failure(var2, "Error parsing JSON instruction file");
      }
   }

   private static String convertStreamToString(InputStream inputStream) throws IOException {
      if (inputStream == null) {
         return "";
      } else {
         StringBuilder stringBuilder = new StringBuilder();

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StringUtils.UTF8));

            String line;
            while((line = reader.readLine()) != null) {
               stringBuilder.append(line);
            }
         } finally {
            inputStream.close();
         }

         return stringBuilder.toString();
      }
   }

   CipherLite getCipherLite() {
      return this.cipherLite;
   }

   Map<String, String> getKEKMaterialsDescription() {
      return this.kekMaterialsDescription;
   }

   byte[] getEncryptedCEK() {
      return (byte[])this.encryptedCEK.clone();
   }

   ContentCryptoMaterial recreate(
      EncryptionMaterialsAccessor accessor, CryptoConfigurationV2 config, String keyWrapAlgoFromMetadata, AWSKMS kms, PutInstructionFileRequest req
   ) {
      EncryptionMaterials newKEK = this.getNewEncryptionMaterials(req, accessor);
      if (!InternalKeyWrapAlgorithm.KMS.equals(this.keyWrappingAlgorithm) && newKEK.getMaterialsDescription().equals(this.kekMaterialsDescription)) {
         throw new SecurityException("Material description of the new KEK must differ from the current one");
      } else if (InternalKeyWrapAlgorithm.KMS.equals(this.keyWrappingAlgorithm)) {
         throw new SdkClientException("Recreating KMS encrypted CEK is not supported.");
      } else {
         EncryptionMaterials origKEK = accessor.getEncryptionMaterials(this.kekMaterialsDescription);
         validateKeyWrapAlgorithmForDecrypt(this.keyWrappingAlgorithm, config.getCryptoMode());
         InternalKeyWrapAlgorithm originalKeyWrapAlgo = InternalKeyWrapAlgorithm.fromAlgorithmName(keyWrapAlgoFromMetadata);
         SecretKey cek = decryptCEK(
            KeyWrapperContext.builder()
               .cekSecured(this.encryptedCEK)
               .internalKeyWrapAlgorithm(originalKeyWrapAlgo)
               .materials(origKEK)
               .cryptoProvider(config.getCryptoProvider())
               .secureRandom(config.getSecureRandom())
               .contentCryptoScheme(this.getContentCryptoScheme())
               .kmsKeyWrapperContext(KMSKeyWrapperContext.builder().kms(kms).build())
               .build()
         );
         ContentCryptoMaterial output = create(cek, this.cipherLite.getIV(), newKEK, this.getContentCryptoScheme(), config, kms, req);
         if (Arrays.equals(output.encryptedCEK, this.encryptedCEK)) {
            throw new SecurityException("The new KEK must differ from the original");
         } else {
            return output;
         }
      }
   }

   private EncryptionMaterials getNewEncryptionMaterials(PutInstructionFileRequest req, EncryptionMaterialsAccessor accessor) {
      EncryptionMaterials newKEK = req.getEncryptionMaterials();
      if (newKEK == null) {
         Map<String, String> materialsDescription = req.getMaterialsDescription();
         newKEK = accessor.getEncryptionMaterials(materialsDescription);
      }

      if (newKEK == null) {
         throw new SdkClientException("No material available with the description " + req.getMaterialsDescription() + " from the encryption material provider");
      } else {
         return newKEK;
      }
   }

   static ContentCryptoMaterial create(
      SecretKey cek,
      byte[] iv,
      EncryptionMaterials kekMaterials,
      ContentCryptoScheme contentCryptoScheme,
      CryptoConfigurationV2 config,
      AWSKMS kms,
      AmazonWebServiceRequest req
   ) {
      KeyWrapperContext keyWrapperContext = createEncryptionKeyWrapperContext(kekMaterials, contentCryptoScheme, config, kms, req);
      SecuredCEK cekSecured = encryptCEK(cek, keyWrapperContext);
      return wrap(cek, iv, contentCryptoScheme, config.getCryptoProvider(), config.getAlwaysUseCryptoProvider(), cekSecured);
   }

   private static KeyWrapperContext createEncryptionKeyWrapperContext(
      EncryptionMaterials materials, ContentCryptoScheme cryptoScheme, CryptoConfigurationV2 config, AWSKMS kms, AmazonWebServiceRequest req
   ) {
      CryptoKeyWrapAlgorithm keyWrapAlgorithm = KeyWrapAlgorithmResolver.getDefaultKeyWrapAlgorithm(materials);
      InternalKeyWrapAlgorithm internalKeyWrapAlgorithm = InternalKeyWrapAlgorithm.fromExternal(keyWrapAlgorithm);
      if (materials.isKMSEnabled()) {
         Map<String, String> matdesc = KMSMaterialsHandler.createKMSContextMaterialsDescription(
            KMSMaterialsHandler.mergeMaterialsDescription((KMSEncryptionMaterials)materials, req), cryptoScheme.getCipherAlgorithm()
         );
         KMSKeyWrapperContext kmsKeyWrapperContext = KMSKeyWrapperContext.builder().kms(kms).kmsMaterialsDescription(matdesc).originalRequest(req).build();
         return KeyWrapperContext.builder()
            .cryptoProvider(config.getCryptoProvider())
            .secureRandom(config.getSecureRandom())
            .materials(materials)
            .internalKeyWrapAlgorithm(internalKeyWrapAlgorithm)
            .kmsKeyWrapperContext(kmsKeyWrapperContext)
            .contentCryptoScheme(cryptoScheme)
            .build();
      } else {
         return KeyWrapperContext.builder()
            .cryptoProvider(config.getCryptoProvider())
            .secureRandom(config.getSecureRandom())
            .materials(materials)
            .internalKeyWrapAlgorithm(internalKeyWrapAlgorithm)
            .contentCryptoScheme(cryptoScheme)
            .build();
      }
   }

   static ContentCryptoMaterial wrap(
      SecretKey cek, byte[] iv, ContentCryptoScheme contentCryptoScheme, Provider provider, boolean alwaysUseProvider, SecuredCEK cekSecured
   ) {
      return new ContentCryptoMaterial(
         cekSecured.getMaterialDescription(),
         cekSecured.getEncrypted(),
         cekSecured.getKeyWrapAlgorithm(),
         contentCryptoScheme.createCipherLite(cek, iv, 1, provider, alwaysUseProvider)
      );
   }

   private static SecuredCEK encryptCEK(SecretKey cek, KeyWrapperContext context) {
      EncryptionMaterials materials = context.materials();
      validateKeyWrapAlgorithmForEncrypt(materials, context.internalKeyWrapAlgorithm());
      Key kek = getEncryptionKeyFrom(materials);
      Map<String, String> matdesc = materials.isKMSEnabled() ? context.kmsKeyWrapperContext().kmsMaterialsDescription() : materials.getMaterialsDescription();
      KeyWrapper keyWrapper = KeyWrapperFactory.defaultInstance().createKeyWrapper(context);
      return new SecuredCEK(keyWrapper.wrapCek(cek.getEncoded(), kek), context.internalKeyWrapAlgorithm(), matdesc);
   }

   private static Key getEncryptionKeyFrom(EncryptionMaterials materials) {
      if (materials.isKMSEnabled()) {
         return null;
      } else {
         return (Key)(materials.getKeyPair() != null ? materials.getKeyPair().getPublic() : materials.getSymmetricKey());
      }
   }

   private static Key getDecryptionKeyFrom(EncryptionMaterials materials) {
      if (materials.isKMSEnabled()) {
         return null;
      } else {
         return (Key)(materials.getKeyPair() != null ? materials.getKeyPair().getPrivate() : materials.getSymmetricKey());
      }
   }

   private static void validateKeyWrapAlgorithmForEncrypt(EncryptionMaterials materials, InternalKeyWrapAlgorithm keyWrapAlgorithm) {
      if (materials.isKMSEnabled()) {
         validateKMSKeyWrapAlgorithmForEncrypt(materials, keyWrapAlgorithm);
      } else if (materials.getKeyPair() != null && !keyWrapAlgorithm.isAsymmetric()) {
         throw new IllegalStateException(
            String.format("Encryption materials with asymmetric keys are not consistent with selected key wrap algorithm %s.", keyWrapAlgorithm)
         );
      } else if (materials.getSymmetricKey() != null && !keyWrapAlgorithm.isSymmetric()) {
         throw new IllegalStateException(
            String.format("Encryption materials with a symmetric key are not consistent with selected key wrap algorithm %s.", keyWrapAlgorithm)
         );
      }
   }

   private static void validateKMSKeyWrapAlgorithmForEncrypt(EncryptionMaterials materials, InternalKeyWrapAlgorithm keyWrapAlgorithm) {
      if (!InternalKeyWrapAlgorithm.KMS.equals(keyWrapAlgorithm)) {
         throw new IllegalStateException(
            String.format("KMS enabled encryption materials are not consistent with selected key wrap algorithm %s.", keyWrapAlgorithm)
         );
      }
   }

   private static void validateKeyWrapAlgorithmForDecrypt(InternalKeyWrapAlgorithm keyWrapAlgo, CryptoMode cryptoMode) {
      validateKeyWrapAlgorithmForDecrypt(keyWrapAlgo, false, cryptoMode);
   }

   private static void validateKeyWrapAlgorithmForDecrypt(InternalKeyWrapAlgorithm keyWrapAlgo, boolean keyWrapExpected, CryptoMode cryptoMode) {
      if (CryptoMode.StrictAuthenticatedEncryption.equals(cryptoMode)) {
         if (keyWrapAlgo == null) {
            throw new KeyWrapException("No key wrap algorithm detected. Use crypto mode " + CryptoMode.AuthenticatedEncryption + " to decrypt object.");
         }

         if (keyWrapAlgo.isV1Algorithm()) {
            throw new KeyWrapException(
               "Detected key wrap algorithm used with previous version of client. Use crypto mode "
                  + CryptoMode.AuthenticatedEncryption
                  + " to decrypt object."
            );
         }
      } else if (keyWrapExpected && keyWrapAlgo == null) {
         throw new KeyWrapException("Key wrap expected, but no key wrap algorithm was found.");
      }
   }

   private static void validateMaterialsForDecrypt(
      EncryptionMaterials materials, Map<String, String> mergedMatDesc, CryptoMode cryptoMode, InternalKeyWrapAlgorithm keyWrapAlgorithm
   ) {
      if (materials == null) {
         throw new SdkClientException("Unable to retrieve the client encryption materials");
      } else if (keyWrapAlgorithm != null && keyWrapAlgorithm.isKMS()) {
         if (keyWrapAlgorithm.isV1Algorithm() || !KMSMaterialsHandler.isValidV2Description(materials.getMaterialsDescription(), mergedMatDesc)) {
            boolean isValidV1MaterialsDescription = KMSMaterialsHandler.isValidV1Description(materials.getMaterialsDescription(), mergedMatDesc);
            if (!keyWrapAlgorithm.isV1Algorithm() || !isValidV1MaterialsDescription) {
               throw new IllegalStateException("Provided encryption materials do not match information retrieved from the encrypted object");
            } else if (!CryptoMode.AuthenticatedEncryption.equals(cryptoMode)) {
               throw new IllegalStateException(
                  "A previous version of the client may have been used to encrypt key via KMS. Use crypto mode "
                     + CryptoMode.AuthenticatedEncryption
                     + " to decrypt object."
               );
            }
         }
      }
   }

   private static void assertCryptoSchemeAllowedForRangeGet(ContentCryptoScheme scheme, CryptoMode cryptoMode, CryptoRangeGetMode rangeGetMode) {
      if (!rangeGetMode.permitsCipherAlgorithm(cryptoMode, scheme.getCipherAlgorithm())) {
         if (CryptoRangeGetMode.DISABLED.equals(rangeGetMode)) {
            throw new SecurityException(
               "Unable to perform range get request: Range get support has been disabled. See https://docs.aws.amazon.com/general/latest/gr/aws_sdk_cryptography.html"
            );
         } else {
            throw new SecurityException(
               "Range get support is not enabled for this content encryption type. Use "
                  + CryptoMode.AuthenticatedEncryption
                  + " instead. See https://docs.aws.amazon.com/general/latest/gr/aws_sdk_cryptography.html"
            );
         }
      }
   }
}
