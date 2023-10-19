package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Tag;

public class ObjectTaggingXmlFactory {
   public byte[] convertToXmlByteArray(ObjectTagging tagging) {
      XmlWriter writer = new XmlWriter();
      writer.start("Tagging").start("TagSet");

      for(Tag tag : tagging.getTagSet()) {
         writer.start("Tag");
         writer.start("Key").value(tag.getKey()).end();
         writer.start("Value").value(tag.getValue()).end();
         writer.end();
      }

      writer.end();
      writer.end();
      return writer.getBytes();
   }
}
