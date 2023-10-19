package com.amazonaws.services.s3.internal;

import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.services.s3.transfer.exception.FileLockException;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
public enum FileLocks {
   private static final boolean EXTERNAL_LOCK = false;
   private static final Log log = LogFactory.getLog(FileLocks.class);
   private static final Map<File, RandomAccessFile> lockedFiles = new TreeMap<>();

   public static boolean lock(File file) {
      synchronized(lockedFiles) {
         if (lockedFiles.containsKey(file)) {
            return false;
         }
      }

      FileLock lock = null;
      RandomAccessFile raf = null;

      try {
         raf = new RandomAccessFile(file, "rw");
         FileChannel locked = raf.getChannel();
      } catch (Exception var8) {
         IOUtils.closeQuietly(raf, log);
         throw new FileLockException(var8);
      }

      boolean locked;
      synchronized(lockedFiles) {
         RandomAccessFile prev = lockedFiles.put(file, raf);
         if (prev == null) {
            locked = true;
         } else {
            locked = false;
            lockedFiles.put(file, prev);
         }
      }

      if (locked) {
         if (log.isDebugEnabled()) {
            log.debug("Locked file " + file + " with " + lock);
         }
      } else {
         IOUtils.closeQuietly(raf, log);
      }

      return locked;
   }

   public static boolean isFileLocked(File file) {
      synchronized(lockedFiles) {
         return lockedFiles.containsKey(file);
      }
   }

   public static boolean unlock(File file) {
      synchronized(lockedFiles) {
         RandomAccessFile raf = lockedFiles.get(file);
         if (raf == null) {
            return false;
         }

         IOUtils.closeQuietly(raf, log);
         lockedFiles.remove(file);
      }

      if (log.isDebugEnabled()) {
         log.debug("Unlocked file " + file);
      }

      return true;
   }
}
