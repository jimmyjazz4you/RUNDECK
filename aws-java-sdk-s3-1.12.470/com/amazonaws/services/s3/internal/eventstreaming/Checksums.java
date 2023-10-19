package com.amazonaws.services.s3.internal.eventstreaming;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.zip.Checksum;

final class Checksums {
   private Checksums() {
   }

   static void update(Checksum checksum, ByteBuffer buffer) {
      if (buffer.hasArray()) {
         int pos = buffer.position();
         int off = buffer.arrayOffset();
         int limit = buffer.limit();
         int rem = limit - pos;
         checksum.update(buffer.array(), pos + off, rem);
         ((Buffer)buffer).position(limit);
      } else {
         int length = buffer.remaining();
         byte[] b = new byte[length];
         buffer.get(b, 0, length);
         checksum.update(b, 0, length);
      }
   }
}
