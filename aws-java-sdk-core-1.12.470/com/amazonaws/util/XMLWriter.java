package com.amazonaws.util;

import com.amazonaws.SdkClientException;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Stack;

public class XMLWriter {
   private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   private final Writer writer;
   private final String xmlns;
   private Stack<String> elementStack = new Stack<>();
   private boolean rootElement = true;

   public XMLWriter(Writer w) {
      this(w, null);
   }

   public XMLWriter(Writer w, String xmlns) {
      this.writer = w;
      this.xmlns = xmlns;
      this.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
   }

   public XMLWriter startElement(String element) {
      this.append("<" + element);
      if (this.rootElement && this.xmlns != null) {
         this.append(" xmlns=\"" + this.xmlns + "\"");
         this.rootElement = false;
      }

      this.append(">");
      this.elementStack.push(element);
      return this;
   }

   public XMLWriter endElement() {
      String lastElement = this.elementStack.pop();
      this.append("</" + lastElement + ">");
      return this;
   }

   public XMLWriter value(String s) {
      this.append(this.escapeXMLEntities(s));
      return this;
   }

   public XMLWriter value(ByteBuffer b) {
      this.append(this.escapeXMLEntities(Base64.encodeAsString(BinaryUtils.copyBytesFrom(b))));
      return this;
   }

   public XMLWriter value(Date date) {
      this.append(this.escapeXMLEntities(StringUtils.fromDate(date)));
      return this;
   }

   public XMLWriter value(Object obj) {
      this.append(this.escapeXMLEntities(obj.toString()));
      return this;
   }

   private void append(String s) {
      try {
         this.writer.append(s);
      } catch (IOException var3) {
         throw new SdkClientException("Unable to write XML document", var3);
      }
   }

   private String escapeXMLEntities(String s) {
      if (s.contains("&")) {
         s = s.replace("&quot;", "\"");
         s = s.replace("&apos;", "'");
         s = s.replace("&lt;", "<");
         s = s.replace("&gt;", ">");
         s = s.replace("&amp;", "&");
      }

      s = s.replace("&", "&amp;");
      s = s.replace("\"", "&quot;");
      s = s.replace("'", "&apos;");
      s = s.replace("<", "&lt;");
      return s.replace(">", "&gt;");
   }
}
