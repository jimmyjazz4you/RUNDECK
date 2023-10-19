package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.internal.XmlWriterUtils;
import com.amazonaws.services.s3.model.ObjectLockRetention;
import java.util.Date;

public final class ObjectLockRetentionXmlFactory {
   public byte[] convertToXmlByteArray(ObjectLockRetention retention) {
      XmlWriter writer = new XmlWriter();
      writer.start("Retention", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      XmlWriterUtils.addIfNotNull(writer, "Mode", retention.getMode());
      addDateIfNotNull(writer, "RetainUntilDate", retention.getRetainUntilDate());
      writer.end();
      return writer.getBytes();
   }

   private static void addDateIfNotNull(XmlWriter writer, String tagName, Date tagValue) {
      if (tagValue != null) {
         writer.start(tagName).value(ServiceUtils.formatIso8601Date(tagValue)).end();
      }
   }
}
