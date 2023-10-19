package com.amazonaws.transform;

import com.fasterxml.jackson.core.JsonToken;
import java.util.ArrayList;
import java.util.List;

public class ListUnmarshaller<T> implements Unmarshaller<List<T>, JsonUnmarshallerContext> {
   private final Unmarshaller<T, JsonUnmarshallerContext> itemUnmarshaller;

   public ListUnmarshaller(Unmarshaller<T, JsonUnmarshallerContext> itemUnmarshaller) {
      this.itemUnmarshaller = itemUnmarshaller;
   }

   public List<T> unmarshall(JsonUnmarshallerContext context) throws Exception {
      return context.isInsideResponseHeader() ? this.unmarshallResponseHeaderToList(context) : this.unmarshallJsonToList(context);
   }

   private List<T> unmarshallResponseHeaderToList(JsonUnmarshallerContext context) throws Exception {
      String headerValue = context.readText();
      List<T> list = new ArrayList<>();
      String[] headerValues = headerValue.split("[,]");

      for(final String headerVal : headerValues) {
         list.add(this.itemUnmarshaller.unmarshall(new JsonUnmarshallerContext() {
            @Override
            public String readText() {
               return headerVal;
            }
         }));
      }

      return list;
   }

   private List<T> unmarshallJsonToList(JsonUnmarshallerContext context) throws Exception {
      List<T> list = new ArrayList<>();
      if (context.getCurrentToken() == JsonToken.VALUE_NULL) {
         return null;
      } else {
         while(true) {
            JsonToken token = context.nextToken();
            if (token == null) {
               return list;
            }

            if (token == JsonToken.END_ARRAY) {
               return list;
            }

            list.add(this.itemUnmarshaller.unmarshall(context));
         }
      }
   }
}
