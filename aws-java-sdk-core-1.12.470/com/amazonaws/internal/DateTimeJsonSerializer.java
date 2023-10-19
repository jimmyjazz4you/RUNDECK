package com.amazonaws.internal;

import com.amazonaws.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.joda.time.DateTime;

public final class DateTimeJsonSerializer extends JsonSerializer<DateTime> {
   public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
      jgen.writeString(DateUtils.formatISO8601Date(value));
   }
}
