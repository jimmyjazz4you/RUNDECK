package com.amazonaws.auth.profile.internal;

import java.util.Scanner;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

public abstract class AbstractProfilesConfigFileScanner {
   protected abstract void onEmptyOrCommentLine(String var1, String var2);

   protected abstract void onProfileStartingLine(String var1, String var2);

   protected abstract void onProfileEndingLine(String var1);

   protected abstract void onEndOfFile();

   protected abstract void onProfileProperty(String var1, String var2, String var3, boolean var4, String var5);

   protected boolean isSupportedProperty(String propertyName) {
      return true;
   }

   protected void run(Scanner scanner) {
      String currentProfileName = null;

      try {
         int lineNumber = 0;

         while(scanner.hasNextLine()) {
            ++lineNumber;
            String line = scanner.nextLine().trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
               String newProfileName = parseProfileName(line);
               boolean atNewProfileStartingLine = newProfileName != null;
               if (atNewProfileStartingLine) {
                  if (currentProfileName != null) {
                     this.onProfileEndingLine(currentProfileName);
                  }

                  this.onProfileStartingLine(newProfileName, line);
                  currentProfileName = newProfileName;
               } else {
                  Entry<String, String> property = parsePropertyLine(line, lineNumber);
                  if (currentProfileName == null) {
                     throw new IllegalArgumentException("Property is defined without a preceding profile name on line " + lineNumber);
                  }

                  this.onProfileProperty(currentProfileName, property.getKey(), property.getValue(), this.isSupportedProperty(property.getKey()), line);
               }
            } else {
               this.onEmptyOrCommentLine(currentProfileName, line);
            }
         }

         if (currentProfileName != null) {
            this.onProfileEndingLine(currentProfileName);
         }

         this.onEndOfFile();
      } finally {
         scanner.close();
      }
   }

   private static String parseProfileName(String trimmedLine) {
      if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) {
         String profileName = trimmedLine.substring(1, trimmedLine.length() - 1);
         return profileName.trim();
      } else {
         return null;
      }
   }

   private static Entry<String, String> parsePropertyLine(String propertyLine, int lineNumber) {
      String[] pair = propertyLine.split("=", 2);
      if (pair.length != 2) {
         throw new IllegalArgumentException("Invalid property format: no '=' character is found on line " + lineNumber);
      } else {
         String propertyKey = pair[0].trim();
         String propertyValue = pair[1].trim();
         return new SimpleImmutableEntry<>(propertyKey, propertyValue);
      }
   }
}
