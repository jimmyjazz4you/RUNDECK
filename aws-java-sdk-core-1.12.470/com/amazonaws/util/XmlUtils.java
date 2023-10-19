package com.amazonaws.util;

import com.amazonaws.internal.SdkThreadLocalsRegistry;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlUtils {
   private static final ThreadLocal<XMLInputFactory> xmlInputFactory = SdkThreadLocalsRegistry.register(new ThreadLocal<XMLInputFactory>() {
      protected XMLInputFactory initialValue() {
         return XmlUtils.createXmlInputFactory();
      }
   });

   public static XMLReader parse(InputStream in, ContentHandler handler) throws SAXException, IOException {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
      reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      reader.setContentHandler(handler);
      reader.parse(new InputSource(in));
      in.close();
      return reader;
   }

   public static XMLInputFactory getXmlInputFactory() {
      return xmlInputFactory.get();
   }

   private static XMLInputFactory createXmlInputFactory() {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      factory.setProperty("javax.xml.stream.supportDTD", false);
      factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
      return factory;
   }
}
