package com.amazonaws.internal.http;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.protocol.json.JsonContent;
import java.util.Arrays;

@SdkInternalApi
public class CompositeErrorCodeParser implements ErrorCodeParser {
   private final Iterable<ErrorCodeParser> parsers;

   public CompositeErrorCodeParser(Iterable<ErrorCodeParser> parsers) {
      this.parsers = parsers;
   }

   public CompositeErrorCodeParser(ErrorCodeParser... parsers) {
      this.parsers = Arrays.asList(parsers);
   }

   @Override
   public String parseErrorCode(HttpResponse response, JsonContent jsonContent) {
      for(ErrorCodeParser parser : this.parsers) {
         String errorCode = parser.parseErrorCode(response, jsonContent);
         if (errorCode != null) {
            return errorCode;
         }
      }

      return null;
   }
}
