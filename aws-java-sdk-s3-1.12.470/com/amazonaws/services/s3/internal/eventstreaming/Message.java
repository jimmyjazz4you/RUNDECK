package com.amazonaws.services.s3.internal.eventstreaming;

import com.amazonaws.SdkClientException;
import com.amazonaws.internal.CRC32MismatchException;
import com.amazonaws.util.Base64;
import com.amazonaws.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

public class Message {
   private static final int TRAILING_CRC_LENGTH = 4;
   static final int MESSAGE_OVERHEAD = 16;
   private final Map<String, HeaderValue> headers;
   private final byte[] payload;

   public Message(Map<String, HeaderValue> headers, byte[] payload) {
      this.headers = headers;
      this.payload = (byte[])payload.clone();
   }

   public Map<String, HeaderValue> getHeaders() {
      return this.headers;
   }

   public byte[] getPayload() {
      return (byte[])this.payload.clone();
   }

   public static Message decode(ByteBuffer buf) {
      Prelude prelude = Prelude.decode(buf);
      int totalLength = prelude.getTotalLength();
      validateMessageCrc(buf, totalLength);
      ((Buffer)buf).position(buf.position() + 12);
      long headersLength = prelude.getHeadersLength();
      byte[] headerBytes = new byte[Utils.toIntExact(headersLength)];
      buf.get(headerBytes);
      Map<String, HeaderValue> headers = decodeHeaders(ByteBuffer.wrap(headerBytes));
      byte[] payload = new byte[Utils.toIntExact((long)(totalLength - 16) - headersLength)];
      buf.get(payload);
      buf.getInt();
      return new Message(headers, payload);
   }

   private static void validateMessageCrc(ByteBuffer buf, int totalLength) {
      Checksum crc = new CRC32();
      Checksums.update(crc, (ByteBuffer)((Buffer)buf.duplicate()).limit(buf.position() + totalLength - 4));
      long computedMessageCrc = crc.getValue();
      long wireMessageCrc = Utils.toUnsignedLong(buf.getInt(buf.position() + totalLength - 4));
      if (wireMessageCrc != computedMessageCrc) {
         throw new SdkClientException(
            new CRC32MismatchException(String.format("Message checksum failure: expected 0x%x, computed 0x%x", wireMessageCrc, computedMessageCrc))
         );
      }
   }

   static Map<String, HeaderValue> decodeHeaders(ByteBuffer buf) {
      Map<String, HeaderValue> headers = new HashMap<>();

      while(buf.hasRemaining()) {
         Header header = Header.decode(buf);
         headers.put(header.getName(), header.getValue());
      }

      return Collections.unmodifiableMap(headers);
   }

   public ByteBuffer toByteBuffer() {
      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         this.encode(baos);
         baos.close();
         return ByteBuffer.wrap(baos.toByteArray());
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public void encode(OutputStream os) {
      try {
         CheckedOutputStream checkedOutputStream = new CheckedOutputStream(os, new CRC32());
         this.encodeOrThrow(checkedOutputStream);
         long messageCrc = checkedOutputStream.getChecksum().getValue();
         os.write((int)(255L & messageCrc >> 24));
         os.write((int)(255L & messageCrc >> 16));
         os.write((int)(255L & messageCrc >> 8));
         os.write((int)(255L & messageCrc));
         os.flush();
      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   private void encodeOrThrow(OutputStream os) throws IOException {
      ByteArrayOutputStream headersAndPayload = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(headersAndPayload);

      for(Entry<String, HeaderValue> entry : this.headers.entrySet()) {
         Header.encode(entry, dos);
      }

      dos.write(this.payload);
      dos.flush();
      int totalLength = 12 + headersAndPayload.size() + 4;
      byte[] preludeBytes = this.getPrelude(totalLength);
      Checksum crc = new CRC32();
      crc.update(preludeBytes, 0, preludeBytes.length);
      DataOutputStream dosx = new DataOutputStream(os);
      dosx.write(preludeBytes);
      long value = crc.getValue();
      int value1 = (int)value;
      dosx.writeInt(value1);
      dosx.flush();
      headersAndPayload.writeTo(os);
   }

   private byte[] getPrelude(int totalLength) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(8);
      DataOutputStream dos = new DataOutputStream(baos);
      int headerLength = totalLength - 16 - this.payload.length;
      dos.writeInt(totalLength);
      dos.writeInt(headerLength);
      dos.close();
      return baos.toByteArray();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Message message = (Message)o;
         return !this.headers.equals(message.headers) ? false : Arrays.equals(this.payload, message.payload);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.headers.hashCode();
      return 31 * result + Arrays.hashCode(this.payload);
   }

   @Override
   public String toString() {
      StringBuilder ret = new StringBuilder();

      for(Entry<String, HeaderValue> entry : this.headers.entrySet()) {
         ret.append(entry.getKey());
         ret.append(": ");
         ret.append(entry.getValue().toString());
         ret.append('\n');
      }

      ret.append('\n');
      HeaderValue contentTypeHeader = this.headers.get("content-type");
      if (contentTypeHeader == null) {
         contentTypeHeader = HeaderValue.fromString("application/octet-stream");
      }

      String contentType = contentTypeHeader.getString();
      if (!contentType.contains("json") && !contentType.contains("text")) {
         ret.append(Base64.encodeAsString(this.payload));
      } else {
         ret.append(new String(this.payload, StringUtils.UTF8));
      }

      ret.append('\n');
      return ret.toString();
   }
}
