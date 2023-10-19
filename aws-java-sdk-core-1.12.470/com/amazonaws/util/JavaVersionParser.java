package com.amazonaws.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaVersionParser {
   public static final String JAVA_VERSION_PROPERTY = "java.version";
   private static String MAJOR_VERSION_FAMILY_PATTERN = "\\d+";
   private static String MAJOR_VERSION_PATTERN = "\\d+";
   private static String MAINTENANCE_NUMBER_PATTERN = "\\d+";
   private static String UPDATE_NUMBER_PATTERN = "\\d+";
   private static Pattern VERSION_REGEX = Pattern.compile(
      String.format("(%s)\\.(%s)\\.(%s)(?:_(%s))?.*", MAJOR_VERSION_FAMILY_PATTERN, MAJOR_VERSION_PATTERN, MAINTENANCE_NUMBER_PATTERN, UPDATE_NUMBER_PATTERN)
   );
   private static final JavaVersionParser.JavaVersion currentJavaVersion = parseJavaVersion(System.getProperty("java.version"));

   private JavaVersionParser() {
   }

   public static JavaVersionParser.JavaVersion getCurrentJavaVersion() {
      return currentJavaVersion;
   }

   public static JavaVersionParser.JavaVersion parseJavaVersion(String fullVersionString) {
      if (!StringUtils.isNullOrEmpty(fullVersionString)) {
         Matcher matcher = VERSION_REGEX.matcher(fullVersionString);
         if (matcher.matches()) {
            Integer majorVersionFamily = NumberUtils.tryParseInt(matcher.group(1));
            Integer majorVersion = NumberUtils.tryParseInt(matcher.group(2));
            Integer maintenanceNumber = NumberUtils.tryParseInt(matcher.group(3));
            Integer updateNumber = NumberUtils.tryParseInt(matcher.group(4));
            return new JavaVersionParser.JavaVersion(majorVersionFamily, majorVersion, maintenanceNumber, updateNumber);
         }
      }

      return JavaVersionParser.JavaVersion.UNKNOWN;
   }

   public static final class JavaVersion implements Comparable<JavaVersionParser.JavaVersion> {
      public static final JavaVersionParser.JavaVersion UNKNOWN = new JavaVersionParser.JavaVersion(null, null, null, null);
      private final Integer[] tokenizedVersion;
      private final Integer majorVersionFamily;
      private final Integer majorVersion;
      private final Integer maintenanceNumber;
      private final Integer updateNumber;
      private final JavaVersionParser.KnownJavaVersions knownVersion;

      public JavaVersion(Integer majorVersionFamily, Integer majorVersion, Integer maintenanceNumber, Integer updateNumber) {
         this.majorVersionFamily = majorVersionFamily;
         this.majorVersion = majorVersion;
         this.maintenanceNumber = maintenanceNumber;
         this.updateNumber = updateNumber;
         this.knownVersion = JavaVersionParser.KnownJavaVersions.fromMajorVersion(majorVersionFamily, majorVersion);
         this.tokenizedVersion = this.getTokenizedVersion();
      }

      private Integer[] getTokenizedVersion() {
         return new Integer[]{this.majorVersionFamily, this.majorVersion, this.maintenanceNumber, this.updateNumber};
      }

      public Integer getMajorVersionFamily() {
         return this.majorVersionFamily;
      }

      public Integer getMajorVersion() {
         return this.majorVersion;
      }

      public String getMajorVersionString() {
         return String.format("%d.%d", this.majorVersionFamily, this.majorVersion);
      }

      public Integer getMaintenanceNumber() {
         return this.maintenanceNumber;
      }

      public Integer getUpdateNumber() {
         return this.updateNumber;
      }

      public JavaVersionParser.KnownJavaVersions getKnownVersion() {
         return this.knownVersion;
      }

      public int compareTo(JavaVersionParser.JavaVersion other) {
         for(int i = 0; i < this.tokenizedVersion.length; ++i) {
            int tokenComparison = ComparableUtils.safeCompare(this.tokenizedVersion[i], other.tokenizedVersion[i]);
            if (tokenComparison != 0) {
               return tokenComparison;
            }
         }

         return 0;
      }

      @Override
      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + (this.knownVersion == null ? 0 : this.knownVersion.hashCode());
         result = 31 * result + (this.maintenanceNumber == null ? 0 : this.maintenanceNumber.hashCode());
         result = 31 * result + (this.majorVersion == null ? 0 : this.majorVersion.hashCode());
         result = 31 * result + (this.majorVersionFamily == null ? 0 : this.majorVersionFamily.hashCode());
         result = 31 * result + Arrays.hashCode((Object[])this.tokenizedVersion);
         return 31 * result + (this.updateNumber == null ? 0 : this.updateNumber.hashCode());
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            JavaVersionParser.JavaVersion other = (JavaVersionParser.JavaVersion)obj;
            if (this.knownVersion != other.knownVersion) {
               return false;
            } else {
               if (this.maintenanceNumber == null) {
                  if (other.maintenanceNumber != null) {
                     return false;
                  }
               } else if (!this.maintenanceNumber.equals(other.maintenanceNumber)) {
                  return false;
               }

               if (this.majorVersion == null) {
                  if (other.majorVersion != null) {
                     return false;
                  }
               } else if (!this.majorVersion.equals(other.majorVersion)) {
                  return false;
               }

               if (this.majorVersionFamily == null) {
                  if (other.majorVersionFamily != null) {
                     return false;
                  }
               } else if (!this.majorVersionFamily.equals(other.majorVersionFamily)) {
                  return false;
               }

               if (!Arrays.equals((Object[])this.tokenizedVersion, (Object[])other.tokenizedVersion)) {
                  return false;
               } else {
                  if (this.updateNumber == null) {
                     if (other.updateNumber != null) {
                        return false;
                     }
                  } else if (!this.updateNumber.equals(other.updateNumber)) {
                     return false;
                  }

                  return true;
               }
            }
         }
      }
   }

   public static enum KnownJavaVersions {
      JAVA_6(1, 6),
      JAVA_7(1, 7),
      JAVA_8(1, 8),
      JAVA_9(1, 9),
      UNKNOWN(0, -1);

      private Integer knownMajorVersionFamily;
      private Integer knownMajorVersion;

      private KnownJavaVersions(int majorVersionFamily, int majorVersion) {
         this.knownMajorVersionFamily = majorVersionFamily;
         this.knownMajorVersion = majorVersion;
      }

      public static JavaVersionParser.KnownJavaVersions fromMajorVersion(Integer majorVersionFamily, Integer majorVersion) {
         for(JavaVersionParser.KnownJavaVersions version : values()) {
            if (version.isMajorVersion(majorVersionFamily, majorVersion)) {
               return version;
            }
         }

         return UNKNOWN;
      }

      private boolean isMajorVersion(Integer majorVersionFamily, Integer majorVersion) {
         return this.knownMajorVersionFamily.equals(majorVersionFamily) && this.knownMajorVersion.equals(majorVersion);
      }
   }
}
