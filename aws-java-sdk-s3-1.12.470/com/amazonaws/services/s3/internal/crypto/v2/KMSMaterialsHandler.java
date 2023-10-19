package com.amazonaws.services.s3.internal.crypto.v2;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.services.s3.model.KMSEncryptionMaterials;
import com.amazonaws.services.s3.model.MaterialsDescriptionProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class KMSMaterialsHandler {
   public static Map<String, String> createKMSContextMaterialsDescription(Map<String, String> matdesc, String cekAlgo) {
      if (matdesc.containsKey("aws:x-amz-cek-alg")) {
         throw new SecurityException(
            "Conflict in reserved KMS Encryption Context key aws:x-amz-cek-alg. This value is reserved for the S3 Encryption Client and cannot be set by the user."
         );
      } else {
         matdesc.put("aws:x-amz-cek-alg", cekAlgo);
         matdesc.remove("kms_cmk_id");
         return Collections.unmodifiableMap(matdesc);
      }
   }

   public static Map<String, String> mergeMaterialsDescription(KMSEncryptionMaterials materials, AmazonWebServiceRequest req) {
      Map<String, String> matdesc = materials.getMaterialsDescription();
      if (req instanceof MaterialsDescriptionProvider) {
         MaterialsDescriptionProvider mdp = (MaterialsDescriptionProvider)req;
         Map<String, String> matdesc_req = mdp.getMaterialsDescription();
         if (matdesc_req != null) {
            matdesc = new TreeMap<>(matdesc);
            matdesc.putAll(matdesc_req);
         }
      }

      return matdesc;
   }

   public static boolean isValidV2Description(Map<String, String> configuredMatDesc, Map<String, String> kmsMatDesc) {
      Map<String, String> configuredMatDescCopy = new HashMap<>(configuredMatDesc);
      Map<String, String> kmsMatDescCopy = new HashMap<>(kmsMatDesc);
      configuredMatDescCopy.remove("kms_cmk_id");
      kmsMatDescCopy.remove("aws:x-amz-cek-alg");
      return configuredMatDescCopy.equals(kmsMatDescCopy);
   }

   public static boolean isValidV1Description(Map<String, String> configuredMatDesc, Map<String, String> kmsMatDesc) {
      return configuredMatDesc.equals(kmsMatDesc);
   }
}
