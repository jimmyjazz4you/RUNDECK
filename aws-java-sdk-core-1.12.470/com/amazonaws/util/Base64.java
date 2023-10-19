package com.amazonaws.util;

import com.amazonaws.log.InternalLogApi;
import com.amazonaws.log.InternalLogFactory;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;

public enum Base64 {
   private static final InternalLogApi LOG = InternalLogFactory.getLog(Base64.class);
   private static final Base64Codec codec = new Base64Codec();
   private static final boolean isJaxbAvailable;

   public static String encodeAsString(byte... bytes) {
      if (bytes == null) {
         return null;
      } else {
         if (isJaxbAvailable) {
            try {
               return DatatypeConverter.printBase64Binary(bytes);
            } catch (NullPointerException var2) {
               LOG.debug("Recovering from JAXB bug: https://netbeans.org/bugzilla/show_bug.cgi?id=224923", var2);
            }
         }

         return bytes.length == 0 ? "" : CodecUtils.toStringDirect(codec.encode(bytes));
      }
   }

   public static byte[] encode(byte[] bytes) {
      return bytes != null && bytes.length != 0 ? codec.encode(bytes) : bytes;
   }

   public static byte[] decode(String b64) {
      if (b64 == null) {
         return null;
      } else if (b64.length() == 0) {
         return new byte[0];
      } else {
         byte[] buf = new byte[b64.length()];
         int len = CodecUtils.sanitize(b64, buf);
         return codec.decode(buf, len);
      }
   }

   public static byte[] decode(byte[] b64) {
      return b64 != null && b64.length != 0 ? codec.decode(b64, b64.length) : b64;
   }

   static {
      boolean available;
      try {
         Class.forName("javax.xml.bind.DatatypeConverter");
         available = true;
      } catch (Exception var6) {
         available = false;
      }

      if (available) {
         Map<String, String> inconsistentJaxbImpls = new HashMap<>();
         inconsistentJaxbImpls.put("org.apache.ws.jaxme.impl.JAXBContextImpl", "Apache JaxMe");

         try {
            String className = JAXBContext.newInstance(new Class[0]).getClass().getName();
            if (inconsistentJaxbImpls.containsKey(className)) {
               LOG.warn(
                  "A JAXB implementation known to produce base64 encodings that are inconsistent with the reference implementation has been detected. The results of the encodeAsString() method may be incorrect. Implementation: "
                     + (String)inconsistentJaxbImpls.get(className)
               );
            }
         } catch (UnsupportedOperationException var3) {
            available = false;
         } catch (Exception var4) {
         } catch (NoClassDefFoundError var5) {
         }
      } else {
         LOG.warn(
            "JAXB is unavailable. Will fallback to SDK implementation which may be less performant.If you are using Java 9+, you will need to include javax.xml.bind:jaxb-api as a dependency."
         );
      }

      isJaxbAvailable = available;
   }
}
