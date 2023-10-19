package com.amazonaws.util;

public interface EncodingScheme {
   String encodeAsString(byte[] var1);

   byte[] decode(String var1);
}
