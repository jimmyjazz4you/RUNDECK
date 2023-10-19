package com.amazonaws.regions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RegionMetadataParser {
   private static final Log log = LogFactory.getLog(RegionMetadataParser.class);
   private static final String REGION_TAG = "Region";
   private static final String REGION_ID_TAG = "Name";
   private static final String DOMAIN_TAG = "Domain";
   private static final String ENDPOINT_TAG = "Endpoint";
   private static final String SERVICE_TAG = "ServiceName";
   private static final String HTTP_TAG = "Http";
   private static final String HTTPS_TAG = "Https";
   private static final String HOSTNAME_TAG = "Hostname";

   public static RegionMetadata parse(InputStream input) throws IOException {
      return new RegionMetadata(internalParse(input, false));
   }

   @Deprecated
   public List<Region> parseRegionMetadata(InputStream input) throws IOException {
      return internalParse(input, false);
   }

   @Deprecated
   public List<Region> parseRegionMetadata(InputStream input, boolean endpointVerification) throws IOException {
      return internalParse(input, endpointVerification);
   }

   private static List<Region> internalParse(InputStream input, boolean endpointVerification) throws IOException {
      Document document;
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setXIncludeAware(false);
         factory.setExpandEntityReferences(false);
         factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
         configureDocumentBuilderFactory(factory);
         DocumentBuilder documentBuilder = factory.newDocumentBuilder();
         document = documentBuilder.parse(input);
      } catch (IOException var14) {
         throw var14;
      } catch (Exception var15) {
         throw new IOException("Unable to parse region metadata file: " + var15.getMessage(), var15);
      } finally {
         try {
            input.close();
         } catch (IOException var13) {
         }
      }

      NodeList regionNodes = document.getElementsByTagName("Region");
      List<Region> regions = new ArrayList<>();

      for(int i = 0; i < regionNodes.getLength(); ++i) {
         Node node = regionNodes.item(i);
         if (node.getNodeType() == 1) {
            Element element = (Element)node;
            regions.add(parseRegionElement(element, endpointVerification));
         }
      }

      return regions;
   }

   private static Region parseRegionElement(Element regionElement, boolean endpointVerification) {
      String name = getChildElementValue("Name", regionElement);
      String domain = getChildElementValue("Domain", regionElement);
      InMemoryRegionImpl regionImpl = new InMemoryRegionImpl(name, domain);
      NodeList endpointNodes = regionElement.getElementsByTagName("Endpoint");

      for(int i = 0; i < endpointNodes.getLength(); ++i) {
         addRegionEndpoint(regionImpl, (Element)endpointNodes.item(i), endpointVerification);
      }

      return new Region(regionImpl);
   }

   private static void addRegionEndpoint(InMemoryRegionImpl region, Element endpointElement, boolean endpointVerification) {
      String serviceName = getChildElementValue("ServiceName", endpointElement);
      String hostname = getChildElementValue("Hostname", endpointElement);
      String http = getChildElementValue("Http", endpointElement);
      String https = getChildElementValue("Https", endpointElement);
      if (endpointVerification && !verifyLegacyEndpoint(hostname)) {
         throw new IllegalStateException("Invalid service endpoint (" + hostname + ") is detected.");
      } else {
         region.addEndpoint(serviceName, hostname);
         if (Boolean.valueOf(http)) {
            region.addHttp(serviceName);
         }

         if (Boolean.valueOf(https)) {
            region.addHttps(serviceName);
         }
      }
   }

   private static String getChildElementValue(String tagName, Element element) {
      Node tagNode = element.getElementsByTagName(tagName).item(0);
      if (tagNode == null) {
         return null;
      } else {
         NodeList nodes = tagNode.getChildNodes();
         Node node = nodes.item(0);
         return node.getNodeValue();
      }
   }

   private static boolean verifyLegacyEndpoint(String endpoint) {
      return endpoint.endsWith(".amazonaws.com");
   }

   private static boolean isXerces(DocumentBuilderFactory factory) {
      String canonicalName = factory.getClass().getCanonicalName();
      return canonicalName.startsWith("org.apache.xerces.") || canonicalName.startsWith("com.sun.org.apache.xerces.");
   }

   private static void configureDocumentBuilderFactory(DocumentBuilderFactory factory) {
      try {
         if (isXerces(factory)) {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
         } else {
            factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
            factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
         }
      } catch (Throwable var2) {
         log.warn("Unable to configure DocumentBuilderFactory to protect against XXE attacks", var2);
      }
   }
}
