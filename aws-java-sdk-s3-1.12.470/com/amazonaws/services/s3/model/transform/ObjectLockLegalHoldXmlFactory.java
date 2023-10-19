package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.internal.XmlWriterUtils;
import com.amazonaws.services.s3.model.ObjectLockLegalHold;

public final class ObjectLockLegalHoldXmlFactory {
   public byte[] convertToXmlByteArray(ObjectLockLegalHold legalHold) {
      XmlWriter writer = new XmlWriter();
      writer.start("LegalHold", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      XmlWriterUtils.addIfNotNull(writer, "Status", legalHold.getStatus());
      writer.end();
      return writer.getBytes();
   }
}
