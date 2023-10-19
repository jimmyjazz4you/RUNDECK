package com.amazonaws.services.s3.internal.eventstreaming;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class MessageDecoder {
   private ByteBuffer buf = ByteBuffer.allocate(131072);

   public boolean hasPendingContent() {
      return this.buf.position() != 0;
   }

   public List<Message> feed(byte[] bytes) {
      return this.feed(bytes, 0, bytes.length);
   }

   public List<Message> feed(byte[] bytes, int offset, int length) {
      this.buf.put(bytes, offset, length);
      ByteBuffer readView = (ByteBuffer)((Buffer)this.buf.duplicate()).flip();
      int bytesConsumed = 0;

      List<Message> result;
      int totalMessageLength;
      for(result = new ArrayList<>(); readView.remaining() >= 15; bytesConsumed += totalMessageLength) {
         totalMessageLength = Utils.toIntExact((long)Prelude.decode(readView.duplicate()).getTotalLength());
         if (readView.remaining() < totalMessageLength) {
            break;
         }

         Message decoded = Message.decode(readView);
         result.add(decoded);
      }

      if (bytesConsumed > 0) {
         ((Buffer)this.buf).flip();
         ((Buffer)this.buf).position(this.buf.position() + bytesConsumed);
         this.buf.compact();
      }

      return result;
   }
}
