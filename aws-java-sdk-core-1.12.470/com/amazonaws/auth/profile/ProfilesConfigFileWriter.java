package com.amazonaws.auth.profile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.internal.AbstractProfilesConfigFileScanner;
import com.amazonaws.auth.profile.internal.Profile;
import com.amazonaws.util.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProfilesConfigFileWriter {
   private static final Log LOG = LogFactory.getLog(ProfilesConfigFileWriter.class);

   public static void dumpToFile(File destination, boolean overwrite, Profile... profiles) {
      if (destination.exists() && !overwrite) {
         throw new SdkClientException(
            "The destination file already exists. Set overwrite=true if you want to clobber the existing content and completely re-write the file."
         );
      } else {
         OutputStreamWriter writer;
         try {
            writer = new OutputStreamWriter(new FileOutputStream(destination, false), StringUtils.UTF8);
         } catch (IOException var16) {
            throw new SdkClientException("Unable to open the destination file.", var16);
         }

         try {
            Map<String, Profile> modifications = new LinkedHashMap<>();

            for(Profile profile : profiles) {
               modifications.put(profile.getProfileName(), profile);
            }

            ProfilesConfigFileWriter.ProfilesConfigFileWriterHelper writerHelper = new ProfilesConfigFileWriter.ProfilesConfigFileWriterHelper(
               writer, modifications
            );
            writerHelper.writeWithoutExistingContent();
         } finally {
            try {
               writer.close();
            } catch (IOException var15) {
            }
         }
      }
   }

   public static void modifyOrInsertProfiles(File destination, Profile... profiles) {
      Map<String, Profile> modifications = new LinkedHashMap<>();

      for(Profile profile : profiles) {
         modifications.put(profile.getProfileName(), profile);
      }

      modifyProfiles(destination, modifications);
   }

   public static void modifyOneProfile(File destination, String profileName, Profile newProfile) {
      Map<String, Profile> modifications = Collections.singletonMap(profileName, newProfile);
      modifyProfiles(destination, modifications);
   }

   public static void deleteProfiles(File destination, String... profileNames) {
      Map<String, Profile> modifications = new LinkedHashMap<>();

      for(String profileName : profileNames) {
         modifications.put(profileName, null);
      }

      modifyProfiles(destination, modifications);
   }

   static void modifyProfiles(File destination, Map<String, Profile> modifications) {
      boolean inPlaceModify = destination.exists();
      File stashLocation = null;
      if (inPlaceModify) {
         boolean stashed = false;

         try {
            stashLocation = new File(destination.getParentFile(), destination.getName() + ".bak." + UUID.randomUUID().toString());
            stashed = destination.renameTo(stashLocation);
            if (LOG.isDebugEnabled()) {
               LOG.debug(String.format("The original credentials file is stashed to location (%s).", stashLocation.getAbsolutePath()));
            }
         } finally {
            if (!stashed) {
               throw new SdkClientException("Failed to stash the existing credentials file before applying the changes.");
            }
         }
      }

      OutputStreamWriter writer = null;

      try {
         writer = new OutputStreamWriter(new FileOutputStream(destination), StringUtils.UTF8);
         ProfilesConfigFileWriter.ProfilesConfigFileWriterHelper writerHelper = new ProfilesConfigFileWriter.ProfilesConfigFileWriterHelper(
            writer, modifications
         );
         if (inPlaceModify) {
            Scanner existingContent = new Scanner(stashLocation, StringUtils.UTF8.name());
            writerHelper.writeWithExistingContent(existingContent);
         } else {
            writerHelper.writeWithoutExistingContent();
         }

         new ProfilesConfigFile(destination);
         if (inPlaceModify && !stashLocation.delete() && LOG.isDebugEnabled()) {
            LOG.debug(
               String.format(
                  "Successfully modified the credentials file. But failed to delete the stashed copy of the original file (%s).",
                  stashLocation.getAbsolutePath()
               )
            );
         }
      } catch (Exception var29) {
         if (inPlaceModify) {
            boolean restored = false;

            try {
               if (!destination.delete()) {
                  LOG.debug("Unable to remove the credentials file before restoring the original one.");
               }

               restored = stashLocation.renameTo(destination);
            } finally {
               if (!restored) {
                  throw new SdkClientException("Unable to restore the original credentials file. File content stashed in " + stashLocation.getAbsolutePath());
               }
            }
         }

         throw new SdkClientException("Unable to modify the credentials file. (The original file has been restored.)", var29);
      } finally {
         try {
            if (writer != null) {
               writer.close();
            }
         } catch (IOException var26) {
         }
      }
   }

   private static class ProfilesConfigFileWriterHelper extends AbstractProfilesConfigFileScanner {
      private final Writer writer;
      private final Map<String, Profile> newProfiles = new LinkedHashMap<>();
      private final Set<String> deletedProfiles = new HashSet<>();
      private final StringBuilder buffer = new StringBuilder();
      private final Map<String, Set<String>> existingProfileProperties = new HashMap<>();

      public ProfilesConfigFileWriterHelper(Writer writer, Map<String, Profile> modifications) {
         this.writer = writer;

         for(Entry<String, Profile> entry : modifications.entrySet()) {
            String profileName = entry.getKey();
            Profile profile = entry.getValue();
            if (profile == null) {
               this.deletedProfiles.add(profileName);
            } else {
               this.newProfiles.put(profileName, profile);
            }
         }
      }

      public void writeWithoutExistingContent() {
         this.buffer.setLength(0);
         this.existingProfileProperties.clear();
         this.run(new Scanner(""));
      }

      public void writeWithExistingContent(Scanner existingContent) {
         this.buffer.setLength(0);
         this.existingProfileProperties.clear();
         this.run(existingContent);
      }

      @Override
      protected void onEmptyOrCommentLine(String profileName, String line) {
         if (profileName == null || !this.deletedProfiles.contains(profileName)) {
            this.buffer(line);
         }
      }

      @Override
      protected void onProfileStartingLine(String profileName, String line) {
         this.existingProfileProperties.put(profileName, new HashSet<>());
         this.flush();
         if (!this.deletedProfiles.contains(profileName)) {
            if (this.newProfiles.get(profileName) != null) {
               String newProfileName = this.newProfiles.get(profileName).getProfileName();
               if (!newProfileName.equals(profileName)) {
                  line = "[" + newProfileName + "]";
               }
            }

            this.writeLine(line);
         }
      }

      @Override
      protected void onProfileEndingLine(String prevProfileName) {
         Profile modifiedProfile = this.newProfiles.get(prevProfileName);
         if (modifiedProfile != null) {
            for(Entry<String, String> entry : modifiedProfile.getProperties().entrySet()) {
               String propertyKey = entry.getKey();
               String propertyValue = entry.getValue();
               if (!this.existingProfileProperties.get(prevProfileName).contains(propertyKey)) {
                  this.writeProperty(propertyKey, propertyValue);
               }
            }
         }

         this.flush();
      }

      @Override
      protected void onProfileProperty(String profileName, String propertyKey, String propertyValue, boolean isSupportedProperty, String line) {
         if (this.existingProfileProperties.get(profileName) == null) {
            this.existingProfileProperties.put(profileName, new HashSet<>());
         }

         this.existingProfileProperties.get(profileName).add(propertyKey);
         if (!this.deletedProfiles.contains(profileName)) {
            if (!isSupportedProperty) {
               this.writeLine(line);
            } else {
               this.flush();
               if (this.newProfiles.containsKey(profileName)) {
                  String newValue = this.newProfiles.get(profileName).getPropertyValue(propertyKey);
                  if (newValue != null) {
                     this.writeProperty(propertyKey, newValue);
                  }
               } else {
                  this.writeLine(line);
               }
            }
         }
      }

      @Override
      protected void onEndOfFile() {
         for(Entry<String, Profile> entry : this.newProfiles.entrySet()) {
            String profileName = entry.getKey();
            Profile profile = entry.getValue();
            if (!this.existingProfileProperties.containsKey(profileName)) {
               this.writeProfile(profile);
               this.writeLine("");
            }
         }

         try {
            this.writer.flush();
         } catch (IOException var5) {
            throw new SdkClientException("Unable to write to the target file to persist the profile credentials.", var5);
         }
      }

      @Override
      protected boolean isSupportedProperty(String propertyName) {
         return "aws_access_key_id".equals(propertyName)
            || "aws_secret_access_key".equals(propertyName)
            || "aws_session_token".equals(propertyName)
            || "external_id".equals(propertyName)
            || "role_arn".equals(propertyName)
            || "role_session_name".equals(propertyName)
            || "source_profile".equals(propertyName);
      }

      private void writeProfile(Profile profile) {
         this.writeProfileName(profile.getProfileName());

         for(Entry<String, String> entry : profile.getProperties().entrySet()) {
            this.writeProperty(entry.getKey(), entry.getValue());
         }
      }

      private void writeProfileName(String profileName) {
         this.writeLine(String.format("[%s]", profileName));
      }

      private void writeProperty(String propertyKey, String propertyValue) {
         this.writeLine(String.format("%s=%s", propertyKey, propertyValue));
      }

      private void writeLine(String line) {
         this.append(String.format("%s%n", line));
      }

      private void append(String str) {
         try {
            this.writer.append(str);
         } catch (IOException var3) {
            throw new SdkClientException("Unable to write to the target file to persist the profile credentials.", var3);
         }
      }

      private void flush() {
         if (this.buffer.length() != 0) {
            this.append(this.buffer.toString());
            this.buffer.setLength(0);
         }
      }

      private void buffer(String line) {
         this.buffer.append(String.format("%s%n", line));
      }
   }
}
