package com.amazonaws.util.json;

import com.amazonaws.SdkClientException;
import com.amazonaws.log.InternalLogApi;
import com.amazonaws.log.InternalLogFactory;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public enum Jackson {
   private static final InternalLogApi log = InternalLogFactory.getLog(Jackson.class);
   private static final ObjectMapper objectMapper = new ObjectMapper();
   private static final ObjectWriter writer = objectMapper.writer();
   private static final ObjectWriter prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();
   private static final TypeReference<HashMap<String, String>> STRING_MAP_TYPEREFERENCE = new TypeReference<HashMap<String, String>>() {
   };

   public static String toJsonPrettyString(Object value) {
      try {
         return prettyWriter.writeValueAsString(value);
      } catch (Exception var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String toJsonString(Object value) {
      try {
         return writer.writeValueAsString(value);
      } catch (Exception var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static <T> T fromJsonString(String json, Class<T> clazz) {
      if (json == null) {
         return null;
      } else {
         try {
            return (T)objectMapper.readValue(json, clazz);
         } catch (Exception var3) {
            throw new SdkClientException("Unable to parse Json String.", var3);
         }
      }
   }

   public static Map<String, String> stringMapFromJsonString(String json) {
      if (json == null) {
         return null;
      } else {
         try {
            return (Map<String, String>)objectMapper.readValue(json, STRING_MAP_TYPEREFERENCE);
         } catch (IOException var2) {
            throw new SdkClientException("Unable to parse Json String.", var2);
         }
      }
   }

   public static <T> T fromSensitiveJsonString(String json, Class<T> clazz) {
      if (json == null) {
         return null;
      } else {
         try {
            return (T)objectMapper.readValue(json, clazz);
         } catch (IOException var3) {
            log.debug("Failed to parse JSON string.", var3);
            throw new SdkClientException(
               "Unable to parse Json string. See debug-level logs for the exact error details, which may include sensitive information."
            );
         }
      }
   }

   public static JsonNode jsonNodeOf(String json) {
      return fromJsonString(json, JsonNode.class);
   }

   public static JsonGenerator jsonGeneratorOf(Writer writer) throws IOException {
      return new JsonFactory().createGenerator(writer);
   }

   public static <T> T loadFrom(File file, Class<T> clazz) throws IOException {
      try {
         return (T)objectMapper.readValue(file, clazz);
      } catch (IOException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new IllegalStateException(var4);
      }
   }

   public static ObjectMapper getObjectMapper() {
      return objectMapper;
   }

   public static ObjectWriter getWriter() {
      return writer;
   }

   public static ObjectWriter getPrettywriter() {
      return prettyWriter;
   }

   static {
      objectMapper.configure(Feature.ALLOW_COMMENTS, true);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   }
}
