package com.amazonaws.auth.profile.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

@SdkInternalApi
public class BasicProfileConfigLoader {
   public static final BasicProfileConfigLoader INSTANCE = new BasicProfileConfigLoader();

   private BasicProfileConfigLoader() {
   }

   public AllProfiles loadProfiles(File file) {
      if (file == null) {
         throw new IllegalArgumentException("Unable to load AWS profiles: specified file is null.");
      } else if (file.exists() && file.isFile()) {
         FileInputStream fis = null;

         AllProfiles ioe;
         try {
            fis = new FileInputStream(file);
            ioe = this.loadProfiles(fis);
         } catch (IOException var12) {
            throw new SdkClientException("Unable to load AWS credential profiles file at: " + file.getAbsolutePath(), var12);
         } finally {
            if (fis != null) {
               try {
                  fis.close();
               } catch (IOException var11) {
               }
            }
         }

         return ioe;
      } else {
         throw new IllegalArgumentException("AWS credential profiles file not found in the given path: " + file.getAbsolutePath());
      }
   }

   private AllProfiles loadProfiles(InputStream is) throws IOException {
      BasicProfileConfigLoader.ProfilesConfigFileLoaderHelper helper = new BasicProfileConfigLoader.ProfilesConfigFileLoaderHelper();
      Map<String, Map<String, String>> allProfileProperties = helper.parseProfileProperties(new Scanner(is, StringUtils.UTF8.name()));
      Map<String, BasicProfile> profilesByName = new LinkedHashMap<>();

      for(Entry<String, Map<String, String>> entry : allProfileProperties.entrySet()) {
         String profileName = entry.getKey();
         Map<String, String> properties = entry.getValue();
         this.assertParameterNotEmpty(profileName, "Unable to load properties from profile: Profile name is empty.");
         profilesByName.put(profileName, new BasicProfile(profileName, properties));
      }

      return new AllProfiles(profilesByName);
   }

   private void assertParameterNotEmpty(String parameterValue, String errorMessage) {
      if (StringUtils.isNullOrEmpty(parameterValue)) {
         throw new SdkClientException(errorMessage);
      }
   }

   private static class ProfilesConfigFileLoaderHelper extends AbstractProfilesConfigFileScanner {
      protected final Map<String, Map<String, String>> allProfileProperties = new LinkedHashMap<>();

      private ProfilesConfigFileLoaderHelper() {
      }

      public Map<String, Map<String, String>> parseProfileProperties(Scanner scanner) {
         this.allProfileProperties.clear();
         this.run(scanner);
         return new LinkedHashMap<>(this.allProfileProperties);
      }

      @Override
      protected void onEmptyOrCommentLine(String profileName, String line) {
      }

      @Override
      protected void onProfileStartingLine(String newProfileName, String line) {
         this.allProfileProperties.put(newProfileName, new HashMap<>());
      }

      @Override
      protected void onProfileEndingLine(String prevProfileName) {
      }

      @Override
      protected void onProfileProperty(String profileName, String propertyKey, String propertyValue, boolean isSupportedProperty, String line) {
         Map<String, String> properties = this.allProfileProperties.get(profileName);
         if (properties.containsKey(propertyKey)) {
            throw new IllegalArgumentException("Duplicate property values for [" + propertyKey + "].");
         } else {
            properties.put(propertyKey, propertyValue);
         }
      }

      @Override
      protected void onEndOfFile() {
      }
   }
}
