package com.amazonaws.services.s3.model.transform;

import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

abstract class AbstractHandler extends DefaultHandler {
   private final StringBuilder text = new StringBuilder();
   private final LinkedList<String> context = new LinkedList<>();

   @Override
   public final void startElement(String uri, String name, String qName, Attributes attrs) {
      this.text.setLength(0);
      this.doStartElement(uri, name, qName, attrs);
      this.context.add(name);
   }

   protected abstract void doStartElement(String var1, String var2, String var3, Attributes var4);

   @Override
   public final void endElement(String uri, String name, String qName) {
      this.context.removeLast();
      this.doEndElement(uri, name, qName);
   }

   protected abstract void doEndElement(String var1, String var2, String var3);

   @Override
   public final void characters(char[] ch, int start, int length) {
      this.text.append(ch, start, length);
   }

   protected final String getText() {
      return this.text.toString();
   }

   protected final boolean atTopLevel() {
      return this.context.isEmpty();
   }

   protected final boolean in(String... path) {
      if (path.length != this.context.size()) {
         return false;
      } else {
         int i = 0;

         for(String element : this.context) {
            String pattern = path[i];
            if (!pattern.equals("*") && !pattern.equals(element)) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }
}
