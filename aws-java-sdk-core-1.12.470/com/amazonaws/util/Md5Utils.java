package com.amazonaws.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.LogFactory;

public class Md5Utils {
   private static final int SIXTEEN_K = 16384;

   public static byte[] computeMD5Hash(InputStream is) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(is);

      byte[] var5;
      try {
         MessageDigest messageDigest = MessageDigest.getInstance("MD5");
         byte[] buffer = new byte[16384];

         int bytesRead;
         while((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
            messageDigest.update(buffer, 0, bytesRead);
         }

         var5 = messageDigest.digest();
      } catch (NoSuchAlgorithmException var14) {
         throw new IllegalStateException(var14);
      } finally {
         try {
            bis.close();
         } catch (Exception var13) {
            LogFactory.getLog(Md5Utils.class).debug("Unable to close input stream of hash candidate: " + var13);
         }
      }

      return var5;
   }

   public static String md5AsBase64(InputStream is) throws IOException {
      return Base64.encodeAsString(computeMD5Hash(is));
   }

   public static byte[] computeMD5Hash(byte[] input) {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         return md.digest(input);
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String md5AsBase64(byte[] input) {
      return Base64.encodeAsString(computeMD5Hash(input));
   }

   public static byte[] computeMD5Hash(File file) throws FileNotFoundException, IOException {
      return computeMD5Hash(new FileInputStream(file));
   }

   public static String md5AsBase64(File file) throws FileNotFoundException, IOException {
      return Base64.encodeAsString(computeMD5Hash(file));
   }
}
