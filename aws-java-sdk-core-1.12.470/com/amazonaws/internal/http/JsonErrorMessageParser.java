package com.amazonaws.internal.http;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SdkProtectedApi
public class JsonErrorMessageParser {
   private static final List<String> DEFAULT_ERROR_MESSAGE_LOCATIONS = Arrays.asList("message", "Message", "errorMessage", "ErrorMessage");
   public static final String X_AMZN_ERROR_MESSAGE = "x-amzn-error-message";
   public static final JsonErrorMessageParser DEFAULT_ERROR_MESSAGE_PARSER = new JsonErrorMessageParser(DEFAULT_ERROR_MESSAGE_LOCATIONS);
   private static final HttpResponse EMPTY_HTTP_RESPONSE = new HttpResponse(null, null);
   private final List<String> errorMessageJsonLocations;

   public JsonErrorMessageParser(List<String> errorMessageJsonLocations) {
      this.errorMessageJsonLocations = new LinkedList<>(errorMessageJsonLocations);
   }

   @Deprecated
   public String parseErrorMessage(JsonNode jsonNode) {
      return this.parseErrorMessage(EMPTY_HTTP_RESPONSE, jsonNode);
   }

   public String parseErrorMessage(HttpResponse httpResponse, JsonNode jsonNode) {
      String headerMessage = httpResponse.getHeader("x-amzn-error-message");
      if (headerMessage != null) {
         return headerMessage;
      } else {
         for(String field : this.errorMessageJsonLocations) {
            JsonNode value = jsonNode.get(field);
            if (value != null && value.isTextual()) {
               return value.asText();
            }
         }

         return null;
      }
   }
}
