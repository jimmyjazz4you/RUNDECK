package com.amazonaws.protocol.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import software.amazon.ion.IonSystem;

class IonFactory extends JsonFactory {
   private static final long serialVersionUID = 1L;
   private static final boolean SHOULD_CLOSE_READER_YES = true;
   private static final boolean SHOULD_CLOSE_READER_NO = false;
   private final transient IonSystem ionSystem;

   public IonFactory(IonSystem ionSystem) {
      this.ionSystem = ionSystem;
   }

   public JsonParser createParser(InputStream in) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(in), false);
   }

   public JsonParser createParser(byte[] data) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(data), false);
   }

   public JsonParser createParser(byte[] data, int offset, int length) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(data, offset, length), false);
   }

   public JsonParser createParser(char[] data) throws IOException, JsonParseException {
      throw new UnsupportedOperationException();
   }

   public JsonParser createParser(char[] data, int offset, int length) throws IOException, JsonParseException {
      throw new UnsupportedOperationException();
   }

   public JsonParser createParser(String data) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(data), false);
   }

   public JsonParser createParser(Reader data) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(data), false);
   }

   public JsonParser createParser(File data) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(new FileInputStream(data)), true);
   }

   public JsonParser createParser(URL data) throws IOException, JsonParseException {
      return new IonParser(this.ionSystem.newReader(data.openStream()), true);
   }
}
