package com.amazonaws.services.s3.internal.eventstreaming;

import com.amazonaws.util.ValidationUtils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map.Entry;

class Header {
   private final String name;
   private final HeaderValue value;

   Header(String name, HeaderValue value) {
      this.name = (String)ValidationUtils.assertNotNull(name, "value");
      this.value = (HeaderValue)ValidationUtils.assertNotNull(value, "value");
   }

   Header(String name, String value) {
      this(name, HeaderValue.fromString(value));
   }

   public String getName() {
      return this.name;
   }

   public HeaderValue getValue() {
      return this.value;
   }

   static Header decode(ByteBuffer buf) {
      String name = Utils.readShortString(buf);
      return new Header(name, HeaderValue.decode(buf));
   }

   static void encode(Entry<String, HeaderValue> header, DataOutputStream dos) throws IOException {
      new Header(header.getKey(), header.getValue()).encode(dos);
   }

   void encode(DataOutputStream dos) throws IOException {
      Utils.writeShortString(dos, this.name);
      this.value.encode(dos);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Header header = (Header)o;
         return !this.name.equals(header.name) ? false : this.value.equals(header.value);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.name.hashCode();
      return 31 * result + this.value.hashCode();
   }

   @Override
   public String toString() {
      return "Header{name='" + this.name + '\'' + ", value=" + this.value + '}';
   }
}
