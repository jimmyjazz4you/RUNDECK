package com.amazonaws.protocol.json;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public class JsonContent {
   private static final Log LOG = LogFactory.getLog(JsonContent.class);
   private final byte[] rawContent;
   private final JsonNode jsonNode;

   public static JsonContent createJsonContent(HttpResponse httpResponse, JsonFactory jsonFactory) {
      byte[] rawJsonContent = null;

      try {
         if (httpResponse.getContent() != null) {
            rawJsonContent = IOUtils.toByteArray(httpResponse.getContent());
         }
      } catch (Exception var4) {
         LOG.debug("Unable to read HTTP response content", var4);
      }

      return new JsonContent(rawJsonContent, new ObjectMapper(jsonFactory).configure(Feature.ALLOW_COMMENTS, true));
   }

   public JsonContent(byte[] rawJsonContent, JsonNode jsonNode) {
      this.rawContent = rawJsonContent;
      this.jsonNode = jsonNode;
   }

   private JsonContent(byte[] rawJsonContent, ObjectMapper mapper) {
      this.rawContent = rawJsonContent;
      this.jsonNode = parseJsonContent(rawJsonContent, mapper);
   }

   private static JsonNode parseJsonContent(byte[] rawJsonContent, ObjectMapper mapper) {
      if (rawJsonContent == null) {
         return mapper.createObjectNode();
      } else {
         try {
            JsonNode jsonNode = mapper.readTree(rawJsonContent);
            return (JsonNode)(jsonNode.isMissingNode() ? mapper.createObjectNode() : jsonNode);
         } catch (Exception var3) {
            LOG.debug("Unable to parse HTTP response content", var3);
            return mapper.createObjectNode();
         }
      }
   }

   public byte[] getRawContent() {
      return this.rawContent;
   }

   public JsonNode getJsonNode() {
      return this.jsonNode;
   }
}
