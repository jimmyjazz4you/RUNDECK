package com.amazonaws.cache;

import com.amazonaws.Request;

public interface KeyConverter {
   String getKey(Request var1);
}
