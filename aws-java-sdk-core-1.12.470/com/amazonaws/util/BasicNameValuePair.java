package com.amazonaws.util;

import com.amazonaws.annotation.Immutable;
import java.io.Serializable;

@Immutable
class BasicNameValuePair implements NameValuePair, Cloneable, Serializable {
   private static final long serialVersionUID = 1L;
   public static final int HASH_SEED = 17;
   public static final int HASH_OFFSET = 37;
   private final String name;
   private final String value;

   BasicNameValuePair(String name, String value) {
      if (name == null) {
         throw new IllegalArgumentException("Name must not be null");
      } else {
         this.name = name;
         this.value = value;
      }
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      if (this.value == null) {
         return this.name;
      } else {
         int len = this.name.length() + 1 + this.value.length();
         StringBuilder buffer = new StringBuilder(len);
         buffer.append(this.name);
         buffer.append("=");
         buffer.append(this.value);
         return buffer.toString();
      }
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (!(object instanceof NameValuePair)) {
         return false;
      } else {
         BasicNameValuePair that = (BasicNameValuePair)object;
         return this.name.equals(that.name) && equals(this.value, that.value);
      }
   }

   private static boolean equals(Object obj1, Object obj2) {
      return obj1 == null ? obj2 == null : obj1.equals(obj2);
   }

   @Override
   public int hashCode() {
      int hash = 17;
      hash = hashCode(hash, this.name);
      return hashCode(hash, this.value);
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   private static int hashCode(int seed, Object obj) {
      return hashCode(seed, obj != null ? obj.hashCode() : 0);
   }

   private static int hashCode(int seed, int hashcode) {
      return seed * 37 + hashcode;
   }
}
