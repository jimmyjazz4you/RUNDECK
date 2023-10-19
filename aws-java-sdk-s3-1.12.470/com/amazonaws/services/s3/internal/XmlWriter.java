package com.amazonaws.services.s3.internal;

import com.amazonaws.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class XmlWriter {
   List<String> tags = new ArrayList<>();
   StringBuilder sb = new StringBuilder();

   public XmlWriter start(String name) {
      this.sb.append("<").append(name).append(">");
      this.tags.add(name);
      return this;
   }

   public XmlWriter start(String name, String attr, String value) {
      this.sb.append("<").append(name);
      this.writeAttr(attr, value);
      this.sb.append(">");
      this.tags.add(name);
      return this;
   }

   public XmlWriter start(String name, String[] attrs, String[] values) {
      this.sb.append("<").append(name);

      for(int i = 0; i < Math.min(attrs.length, values.length); ++i) {
         this.writeAttr(attrs[i], values[i]);
      }

      this.sb.append(">");
      this.tags.add(name);
      return this;
   }

   public XmlWriter end() {
      assert this.tags.size() > 0;

      String name = this.tags.remove(this.tags.size() - 1);
      this.sb.append("</").append(name).append(">");
      return this;
   }

   public byte[] getBytes() {
      assert this.tags.size() == 0;

      return this.toString().getBytes(StringUtils.UTF8);
   }

   @Override
   public String toString() {
      return this.sb.toString();
   }

   public XmlWriter value(String value) {
      this.appendEscapedString(value, this.sb);
      return this;
   }

   private void writeAttr(String name, String value) {
      this.sb.append(' ').append(name).append("=\"");
      this.appendEscapedString(value, this.sb);
      this.sb.append("\"");
   }

   private void appendEscapedString(String s, StringBuilder builder) {
      if (s == null) {
         s = "";
      }

      int start = 0;
      int len = s.length();

      int pos;
      for(pos = 0; pos < len; ++pos) {
         char ch = s.charAt(pos);
         String escape;
         switch(ch) {
            case '\t':
               escape = "&#9;";
               break;
            case '\n':
               escape = "&#10;";
               break;
            case '\r':
               escape = "&#13;";
               break;
            case '"':
               escape = "&quot;";
               break;
            case '&':
               escape = "&amp;";
               break;
            case '\'':
               escape = "&apos;";
               break;
            case '<':
               escape = "&lt;";
               break;
            case '>':
               escape = "&gt;";
               break;
            case '\u0085':
               escape = "&#133;";
               break;
            case '\u2028':
               escape = "&#8232;";
               break;
            default:
               escape = null;
         }

         if (escape != null) {
            if (start < pos) {
               builder.append(s, start, pos);
            }

            this.sb.append(escape);
            start = pos + 1;
         }
      }

      if (start < pos) {
         this.sb.append(s, start, pos);
      }
   }
}
