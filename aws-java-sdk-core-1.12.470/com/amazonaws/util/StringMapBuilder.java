package com.amazonaws.util;

public class StringMapBuilder extends ImmutableMapParameter.Builder<String, String> {
   public StringMapBuilder() {
   }

   public StringMapBuilder(String key, String value) {
      super.put(key, value);
   }
}
