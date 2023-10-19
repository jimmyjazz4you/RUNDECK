package com.amazonaws.services.s3.internal;

public final class XmlWriterUtils {
   private XmlWriterUtils() {
   }

   public static void addIfNotNull(XmlWriter writer, String tagName, String tagValue) {
      if (tagValue != null) {
         writer.start(tagName).value(tagValue).end();
      }
   }
}
