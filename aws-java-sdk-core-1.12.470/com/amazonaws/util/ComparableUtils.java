package com.amazonaws.util;

public class ComparableUtils {
   public static <T> int safeCompare(Comparable<T> d1, T d2) {
      if (d1 != null && d2 != null) {
         return d1.compareTo(d2);
      } else if (d1 == null && d2 != null) {
         return -1;
      } else {
         return d1 != null && d2 == null ? 1 : 0;
      }
   }
}
