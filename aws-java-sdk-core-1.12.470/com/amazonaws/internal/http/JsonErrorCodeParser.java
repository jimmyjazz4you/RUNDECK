package com.amazonaws.internal.http;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.protocol.json.JsonContent;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

@SdkInternalApi
public class JsonErrorCodeParser implements ErrorCodeParser {
   static final String X_AMZN_ERROR_TYPE = "x-amzn-ErrorType";
   private final String errorCodeFieldName;

   public JsonErrorCodeParser() {
      this(null);
   }

   public JsonErrorCodeParser(String errorCodeFieldName) {
      this.errorCodeFieldName = errorCodeFieldName == null ? "__type" : errorCodeFieldName;
   }

   @Override
   public String parseErrorCode(HttpResponse response, JsonContent jsonContent) {
      String errorCodeFromHeader = this.parseErrorCodeFromHeader(response.getHeaders());
      if (errorCodeFromHeader != null) {
         return errorCodeFromHeader;
      } else {
         return jsonContent != null ? this.parseErrorCodeFromContents(jsonContent.getJsonNode()) : null;
      }
   }

   private String parseErrorCodeFromHeader(Map<String, String> httpHeaders) {
      String headerValue = httpHeaders.get("x-amzn-ErrorType");
      if (headerValue != null) {
         int separator = headerValue.indexOf(58);
         if (separator != -1) {
            headerValue = headerValue.substring(0, separator);
         }
      }

      return headerValue;
   }

   private String parseErrorCodeFromContents(JsonNode jsonContents) {
      if (jsonContents != null && jsonContents.has(this.errorCodeFieldName)) {
         String code = jsonContents.findValue(this.errorCodeFieldName).asText();
         int separator = code.lastIndexOf("#");
         return code.substring(separator + 1);
      } else {
         return null;
      }
   }
}
