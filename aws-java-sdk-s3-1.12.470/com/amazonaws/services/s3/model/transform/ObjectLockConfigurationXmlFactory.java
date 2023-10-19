package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.internal.XmlWriterUtils;
import com.amazonaws.services.s3.model.DefaultRetention;
import com.amazonaws.services.s3.model.ObjectLockConfiguration;
import com.amazonaws.services.s3.model.ObjectLockRule;

public final class ObjectLockConfigurationXmlFactory {
   public byte[] convertToXmlByteArray(ObjectLockConfiguration config) {
      XmlWriter writer = new XmlWriter();
      writer.start("ObjectLockConfiguration", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
      XmlWriterUtils.addIfNotNull(writer, "ObjectLockEnabled", config.getObjectLockEnabled());
      addRuleIfNotNull(writer, config.getRule());
      writer.end();
      return writer.getBytes();
   }

   private static void addRuleIfNotNull(XmlWriter writer, ObjectLockRule rule) {
      if (rule != null) {
         writer.start("Rule");
         writeDefaultRetention(writer, rule.getDefaultRetention());
         writer.end();
      }
   }

   private static void writeDefaultRetention(XmlWriter writer, DefaultRetention retention) {
      if (retention != null) {
         writer.start("DefaultRetention");
         XmlWriterUtils.addIfNotNull(writer, "Mode", retention.getMode());
         Integer days = retention.getDays();
         if (days != null) {
            writer.start("Days").value(Integer.toString(days)).end();
         }

         Integer years = retention.getYears();
         if (years != null) {
            writer.start("Years").value(Integer.toString(years)).end();
         }

         writer.end();
      }
   }
}
