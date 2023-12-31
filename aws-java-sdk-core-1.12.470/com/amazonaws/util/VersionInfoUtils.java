package com.amazonaws.util;

import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.internal.config.InternalConfig;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
public class VersionInfoUtils {
   static final String VERSION_INFO_FILE = "/com/amazonaws/sdk/versionInfo.properties";
   private static volatile String version;
   private static volatile String platform;
   private static volatile String userAgent;
   private static final Log log = LogFactory.getLog(VersionInfoUtils.class);
   private static final String UNKNOWN = "unknown";

   public static String getVersion() {
      if (version == null) {
         synchronized(VersionInfoUtils.class) {
            if (version == null) {
               initializeVersion();
            }
         }
      }

      return version;
   }

   public static String getPlatform() {
      if (platform == null) {
         synchronized(VersionInfoUtils.class) {
            if (platform == null) {
               initializeVersion();
            }
         }
      }

      return platform;
   }

   public static String getUserAgent() {
      if (userAgent == null) {
         synchronized(VersionInfoUtils.class) {
            if (userAgent == null) {
               initializeUserAgent();
            }
         }
      }

      return userAgent;
   }

   private static void initializeVersion() {
      InputStream inputStream = ClassLoaderHelper.getResourceAsStream("/com/amazonaws/sdk/versionInfo.properties", true, VersionInfoUtils.class);
      Properties versionInfoProperties = new Properties();

      try {
         if (inputStream == null) {
            throw new Exception("/com/amazonaws/sdk/versionInfo.properties not found on classpath");
         }

         versionInfoProperties.load(inputStream);
         version = versionInfoProperties.getProperty("version");
         platform = versionInfoProperties.getProperty("platform");
      } catch (Exception var6) {
         log.info("Unable to load version information for the running SDK: " + var6.getMessage());
         version = "unknown-version";
         platform = "java";
      } finally {
         IOUtils.closeQuietly(inputStream, log);
      }
   }

   private static void initializeUserAgent() {
      userAgent = userAgent();
   }

   static String userAgent() {
      String ua = InternalConfig.Factory.getInternalConfig().getUserAgentTemplate();
      if (ua == null) {
         return "aws-sdk-java";
      } else {
         ua = ua.replace("{platform}", StringUtils.lowerCase(getPlatform()))
            .replace("{version}", getVersion())
            .replace("{os.name}", replaceSpaces(System.getProperty("os.name")))
            .replace("{os.version}", replaceSpaces(System.getProperty("os.version")))
            .replace("{java.vm.name}", replaceSpaces(System.getProperty("java.vm.name")))
            .replace("{java.vm.version}", replaceSpaces(System.getProperty("java.vm.version")))
            .replace("{java.version}", replaceSpaces(System.getProperty("java.version")))
            .replace("{java.vendor}", replaceSpaces(System.getProperty("java.vendor")));
         if (ua.contains("{additional.languages}")) {
            ua = ua.replace("{additional.languages}", getAdditionalJvmLanguages());
         }

         String language = System.getProperty("user.language");
         String region = System.getProperty("user.region");
         String languageAndRegion = "";
         if (language != null && region != null) {
            languageAndRegion = " " + replaceSpaces(language) + "_" + replaceSpaces(region);
         }

         return ua.replace("{language.and.region}", languageAndRegion);
      }
   }

   private static String replaceSpaces(String input) {
      return input == null ? "unknown" : input.replace(' ', '_');
   }

   private static String getAdditionalJvmLanguages() {
      StringBuilder versions = new StringBuilder();
      concat(versions, scalaVersion(), " ");
      concat(versions, clojureVersion(), " ");
      concat(versions, groovyVersion(), " ");
      concat(versions, jythonVersion(), " ");
      concat(versions, jrubyVersion(), " ");
      concat(versions, kotlinVersion(), " ");
      return versions.toString();
   }

   private static String scalaVersion() {
      return languageVersion("scala", "scala.util.Properties", "versionNumberString", true);
   }

   private static String clojureVersion() {
      return languageVersion("clojure", "clojure.core$clojure_version", "invokeStatic", true);
   }

   private static String groovyVersion() {
      return languageVersion("groovy", "groovy.lang.GroovySystem", "getVersion", true);
   }

   private static String jythonVersion() {
      return languageVersion("jython", "org.python.Version", "PY_VERSION", false);
   }

   private static String jrubyVersion() {
      return languageVersion("jruby", "org.jruby.runtime.Constants", "VERSION", false);
   }

   private static String kotlinVersion() {
      String version = kotlinVersionByClass();
      return version.equals("") ? kotlinVersionByJar() : version;
   }

   private static String kotlinVersionByClass() {
      StringBuilder kotlinVersion = new StringBuilder("");

      try {
         Class<?> versionClass = Class.forName("kotlin.KotlinVersion");
         kotlinVersion.append("kotlin");
         String version = versionClass.getField("CURRENT").get(null).toString();
         concat(kotlinVersion, version, "/");
      } catch (ClassNotFoundException var3) {
      } catch (Exception var4) {
         if (log.isTraceEnabled()) {
            log.trace("Exception attempting to get Kotlin version.", var4);
         }
      }

      return kotlinVersion.toString();
   }

   private static String kotlinVersionByJar() {
      StringBuilder kotlinVersion = new StringBuilder("");
      JarInputStream kotlinJar = null;

      try {
         Class<?> kotlinUnit = Class.forName("kotlin.Unit");
         kotlinVersion.append("kotlin");
         kotlinJar = new JarInputStream(kotlinUnit.getProtectionDomain().getCodeSource().getLocation().openStream());
         String version = kotlinJar.getManifest().getMainAttributes().getValue("Implementation-Version");
         concat(kotlinVersion, version, "/");
      } catch (ClassNotFoundException var8) {
      } catch (Exception var9) {
         if (log.isTraceEnabled()) {
            log.trace("Exception attempting to get Kotlin version.", var9);
         }
      } finally {
         IOUtils.closeQuietly(kotlinJar, log);
      }

      return kotlinVersion.toString();
   }

   private static String languageVersion(String language, String className, String methodOrFieldName, boolean isMethod) {
      StringBuilder sb = new StringBuilder();

      try {
         Class<?> clz = Class.forName(className);
         sb.append(language);
         String version = isMethod ? (String)clz.getMethod(methodOrFieldName).invoke(null) : (String)clz.getField(methodOrFieldName).get(null);
         concat(sb, version, "/");
      } catch (ClassNotFoundException var7) {
      } catch (Exception var8) {
         if (log.isTraceEnabled()) {
            log.trace("Exception attempting to get " + language + " version.", var8);
         }
      }

      return sb.toString();
   }

   private static void concat(StringBuilder prefix, String suffix, String separator) {
      if (suffix != null && !suffix.isEmpty()) {
         prefix.append(separator).append(suffix);
      }
   }
}
