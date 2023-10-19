package com.amazonaws.protocol;

import com.amazonaws.annotation.SdkProtectedApi;
import com.amazonaws.util.TimestampFormat;

@SdkProtectedApi
public class MarshallingInfo<T> {
   private final MarshallingType<T> marshallingType;
   private final String marshallLocationName;
   private final MarshallLocation marshallLocation;
   private final boolean isExplicitPayloadMember;
   private final boolean isBinary;
   private final DefaultValueSupplier<T> defaultValueSupplier;
   private final TimestampFormat timestampFormat;

   private MarshallingInfo(MarshallingInfo.Builder<T> builder) {
      this.marshallingType = builder.marshallingType;
      this.marshallLocationName = builder.marshallLocationName;
      this.marshallLocation = builder.marshallLocation;
      this.isExplicitPayloadMember = builder.isExplicitPayloadMember;
      this.isBinary = builder.isBinary;
      this.defaultValueSupplier = builder.defaultValueSupplier;
      this.timestampFormat = TimestampFormat.fromValue(builder.timestampFormat);
   }

   public MarshallingType<T> marshallingType() {
      return this.marshallingType;
   }

   public String marshallLocationName() {
      return this.marshallLocationName;
   }

   public MarshallLocation marshallLocation() {
      return this.marshallLocation;
   }

   public boolean isExplicitPayloadMember() {
      return this.isExplicitPayloadMember;
   }

   public boolean isBinary() {
      return this.isBinary;
   }

   public DefaultValueSupplier<T> defaultValueSupplier() {
      return this.defaultValueSupplier;
   }

   public TimestampFormat timestampFormat() {
      return this.timestampFormat;
   }

   public static <T> MarshallingInfo.Builder<T> builder(MarshallingType<T> marshallingType) {
      return new MarshallingInfo.Builder<>(marshallingType);
   }

   public static final class Builder<T> {
      private final MarshallingType<T> marshallingType;
      private String marshallLocationName;
      private MarshallLocation marshallLocation;
      private boolean isExplicitPayloadMember;
      private boolean isBinary;
      private DefaultValueSupplier<T> defaultValueSupplier;
      private String timestampFormat;

      private Builder(MarshallingType<T> marshallingType) {
         this.marshallingType = marshallingType;
      }

      public MarshallingInfo.Builder<T> marshallLocationName(String marshallLocationName) {
         this.marshallLocationName = marshallLocationName;
         return this;
      }

      public MarshallingInfo.Builder<T> marshallLocation(MarshallLocation marshallLocation) {
         this.marshallLocation = marshallLocation;
         return this;
      }

      public MarshallingInfo.Builder<T> isExplicitPayloadMember(boolean isExplicitPayloadMember) {
         this.isExplicitPayloadMember = isExplicitPayloadMember;
         return this;
      }

      public MarshallingInfo.Builder<T> isBinary(boolean isBinary) {
         this.isBinary = isBinary;
         return this;
      }

      public MarshallingInfo.Builder<T> defaultValueSupplier(DefaultValueSupplier<T> defaultValueSupplier) {
         this.defaultValueSupplier = defaultValueSupplier;
         return this;
      }

      public MarshallingInfo.Builder<T> timestampFormat(String timestampFormat) {
         this.timestampFormat = timestampFormat;
         return this;
      }

      public MarshallingInfo<T> build() {
         return new MarshallingInfo<>(this);
      }
   }
}
