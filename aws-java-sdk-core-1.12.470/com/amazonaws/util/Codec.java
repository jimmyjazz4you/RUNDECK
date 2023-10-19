package com.amazonaws.util;

interface Codec {
   byte[] encode(byte[] var1);

   byte[] decode(byte[] var1, int var2);
}
