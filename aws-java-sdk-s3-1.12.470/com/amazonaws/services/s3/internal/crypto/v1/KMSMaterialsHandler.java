package com.amazonaws.services.s3.internal.crypto.v1;

import com.amazonaws.services.s3.internal.crypto.CryptoUtils;
import java.util.Collections;
import java.util.Map;

public class KMSMaterialsHandler {
   public static Map<String, String> createKMSContextMaterialsDescription(Map<String, String> matdesc, String cekAlgo) {
      matdesc.put("aws:x-amz-cek-alg", CryptoUtils.normalizeContentAlgorithmForValidation(cekAlgo));
      matdesc.remove("kms_cmk_id");
      return Collections.unmodifiableMap(matdesc);
   }
}
