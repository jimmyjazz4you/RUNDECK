package com.amazonaws.services.s3.transfer;

import com.amazonaws.util.StringUtils;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class PersistableTransfer {
   private static final ObjectMapper MAPPER = new ObjectMapper();

   public final String serialize() {
      return Jackson.toJsonString(this);
   }

   public final void serialize(OutputStream out) throws IOException {
      out.write(Jackson.toJsonString(this).getBytes(StringUtils.UTF8));
      out.flush();
   }

   public static <T extends PersistableTransfer> T deserializeFrom(InputStream in) {
      String type;
      JsonNode tree;
      try {
         tree = MAPPER.readTree(in);
         JsonNode pauseType = tree.get("pauseType");
         if (pauseType == null) {
            throw new IllegalArgumentException("Unrecognized serialized state");
         }

         type = pauseType.asText();
      } catch (Exception var6) {
         throw new IllegalArgumentException(var6);
      }

      Class<?> clazz;
      if ("download".equals(type)) {
         clazz = PersistableDownload.class;
      } else {
         if (!"upload".equals(type)) {
            throw new UnsupportedOperationException("Unsupported paused transfer type: " + type);
         }

         clazz = PersistableUpload.class;
      }

      try {
         return (T)MAPPER.treeToValue(tree, clazz);
      } catch (JsonProcessingException var5) {
         throw new IllegalStateException(var5);
      }
   }

   public static <T extends PersistableTransfer> T deserializeFrom(String serialized) {
      if (serialized == null) {
         return null;
      } else {
         ByteArrayInputStream byteStream = new ByteArrayInputStream(serialized.getBytes(StringUtils.UTF8));

         PersistableTransfer var2;
         try {
            var2 = deserializeFrom(byteStream);
         } finally {
            try {
               byteStream.close();
            } catch (IOException var9) {
            }
         }

         return (T)var2;
      }
   }
}
