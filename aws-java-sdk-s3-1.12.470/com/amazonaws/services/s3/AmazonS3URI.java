package com.amazonaws.services.s3;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonS3URI {
   private static final Pattern ENDPOINT_PATTERN = Pattern.compile("^(.+\\.)?s3[.-]([a-z0-9-]+)\\.");
   private static final Pattern VERSION_ID_PATTERN = Pattern.compile("[&;]");
   private final URI uri;
   private final boolean isPathStyle;
   private final String bucket;
   private final String key;
   private final String versionId;
   private final String region;

   public AmazonS3URI(String str) {
      this(str, true);
   }

   public AmazonS3URI(String str, boolean urlEncode) {
      this(URI.create(preprocessUrlStr(str, urlEncode)), urlEncode);
   }

   public AmazonS3URI(URI uri) {
      this(uri, false);
   }

   private AmazonS3URI(URI uri, boolean urlEncode) {
      if (uri == null) {
         throw new IllegalArgumentException("uri cannot be null");
      } else {
         this.uri = uri;
         if ("s3".equalsIgnoreCase(uri.getScheme())) {
            this.region = null;
            this.versionId = null;
            this.isPathStyle = false;
            this.bucket = uri.getAuthority();
            if (this.bucket == null) {
               throw new IllegalArgumentException("Invalid S3 URI: no bucket: " + uri);
            } else {
               String path = uri.getPath();
               if (path.length() <= 1) {
                  this.key = null;
               } else {
                  this.key = uri.getPath().substring(1);
               }
            }
         } else {
            String host = uri.getHost();
            if (host == null) {
               throw new IllegalArgumentException("Invalid S3 URI: no hostname: " + uri);
            } else {
               Matcher matcher = ENDPOINT_PATTERN.matcher(host);
               if (!matcher.find()) {
                  throw new IllegalArgumentException("Invalid S3 URI: hostname does not appear to be a valid S3 endpoint: " + uri);
               } else {
                  String prefix = matcher.group(1);
                  if (prefix != null && !prefix.isEmpty()) {
                     this.isPathStyle = false;
                     this.bucket = prefix.substring(0, prefix.length() - 1);
                     String path = uri.getPath();
                     if (path != null && !path.isEmpty() && !"/".equals(uri.getPath())) {
                        this.key = uri.getPath().substring(1);
                     } else {
                        this.key = null;
                     }
                  } else {
                     this.isPathStyle = true;
                     String path = urlEncode ? uri.getPath() : uri.getRawPath();
                     if (!"".equals(path) && !"/".equals(path)) {
                        int index = path.indexOf(47, 1);
                        if (index == -1) {
                           this.bucket = decode(path.substring(1));
                           this.key = null;
                        } else if (index == path.length() - 1) {
                           this.bucket = decode(path.substring(1, index));
                           this.key = null;
                        } else {
                           this.bucket = decode(path.substring(1, index));
                           this.key = decode(path.substring(index + 1));
                        }
                     } else {
                        this.bucket = null;
                        this.key = null;
                     }
                  }

                  this.versionId = parseVersionId(uri.getRawQuery());
                  if ("amazonaws".equals(matcher.group(2))) {
                     this.region = null;
                  } else {
                     this.region = matcher.group(2);
                  }
               }
            }
         }
      }
   }

   private static String parseVersionId(String query) {
      if (query != null) {
         String[] params = VERSION_ID_PATTERN.split(query);

         for(String param : params) {
            if (param.startsWith("versionId=")) {
               return decode(param.substring(10));
            }
         }
      }

      return null;
   }

   public URI getURI() {
      return this.uri;
   }

   public boolean isPathStyle() {
      return this.isPathStyle;
   }

   public String getBucket() {
      return this.bucket;
   }

   public String getKey() {
      return this.key;
   }

   public String getVersionId() {
      return this.versionId;
   }

   public String getRegion() {
      return this.region;
   }

   @Override
   public String toString() {
      return this.uri.toString();
   }

   private static String preprocessUrlStr(String str, boolean encode) {
      if (encode) {
         try {
            return URLEncoder.encode(str, "UTF-8").replace("%3A", ":").replace("%2F", "/").replace("+", "%20");
         } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException(var3);
         }
      } else {
         return str;
      }
   }

   private static String decode(String str) {
      if (str == null) {
         return null;
      } else {
         for(int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '%') {
               return decode(str, i);
            }
         }

         return str;
      }
   }

   private static String decode(String str, int firstPercent) {
      StringBuilder builder = new StringBuilder();
      builder.append(str.substring(0, firstPercent));
      appendDecoded(builder, str, firstPercent);

      for(int i = firstPercent + 3; i < str.length(); ++i) {
         if (str.charAt(i) == '%') {
            appendDecoded(builder, str, i);
            i += 2;
         } else {
            builder.append(str.charAt(i));
         }
      }

      return builder.toString();
   }

   private static void appendDecoded(StringBuilder builder, String str, int index) {
      if (index > str.length() - 3) {
         throw new IllegalStateException("Invalid percent-encoded string:\"" + str + "\".");
      } else {
         char first = str.charAt(index + 1);
         char second = str.charAt(index + 2);
         char decoded = (char)(fromHex(first) << 4 | fromHex(second));
         builder.append(decoded);
      }
   }

   private static int fromHex(char c) {
      if (c < '0') {
         throw new IllegalStateException("Invalid percent-encoded string: bad character '" + c + "' in escape sequence.");
      } else if (c <= '9') {
         return c - 48;
      } else if (c < 'A') {
         throw new IllegalStateException("Invalid percent-encoded string: bad character '" + c + "' in escape sequence.");
      } else if (c <= 'F') {
         return c - 65 + 10;
      } else if (c < 'a') {
         throw new IllegalStateException("Invalid percent-encoded string: bad character '" + c + "' in escape sequence.");
      } else if (c <= 'f') {
         return c - 97 + 10;
      } else {
         throw new IllegalStateException("Invalid percent-encoded string: bad character '" + c + "' in escape sequence.");
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         AmazonS3URI that = (AmazonS3URI)o;
         if (this.isPathStyle != that.isPathStyle) {
            return false;
         } else if (!this.uri.equals(that.uri)) {
            return false;
         } else if (this.bucket != null ? this.bucket.equals(that.bucket) : that.bucket == null) {
            if (this.key != null ? this.key.equals(that.key) : that.key == null) {
               if (this.versionId != null ? this.versionId.equals(that.versionId) : that.versionId == null) {
                  return this.region != null ? this.region.equals(that.region) : that.region == null;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.uri.hashCode();
      result = 31 * result + (this.isPathStyle ? 1 : 0);
      result = 31 * result + (this.bucket != null ? this.bucket.hashCode() : 0);
      result = 31 * result + (this.key != null ? this.key.hashCode() : 0);
      result = 31 * result + (this.versionId != null ? this.versionId.hashCode() : 0);
      return 31 * result + (this.region != null ? this.region.hashCode() : 0);
   }
}
