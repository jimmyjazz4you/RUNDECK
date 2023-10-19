package com.amazonaws.services.s3.internal;

import com.amazonaws.AbortedException;
import com.amazonaws.services.s3.OnFileDelete;
import com.amazonaws.services.s3.UploadObjectObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class MultiFileOutputStream extends OutputStream implements OnFileDelete {
   static final int DEFAULT_PART_SIZE = 5242880;
   private final File root;
   private final String namePrefix;
   private int filesCreated;
   private long partSize = 5242880L;
   private long diskLimit = Long.MAX_VALUE;
   private UploadObjectObserver observer;
   private int currFileBytesWritten;
   private long totalBytesWritten;
   private FileOutputStream os;
   private boolean closed;
   private Semaphore diskPermits;

   public MultiFileOutputStream() {
      this.root = new File(System.getProperty("java.io.tmpdir"));
      this.namePrefix = yyMMdd_hhmmss() + "." + UUID.randomUUID();
   }

   public MultiFileOutputStream(File root, String namePrefix) {
      if (root == null || !root.isDirectory() || !root.canWrite()) {
         throw new IllegalArgumentException(root + " must be a writable directory");
      } else if (namePrefix != null && namePrefix.trim().length() != 0) {
         this.root = root;
         this.namePrefix = namePrefix;
      } else {
         throw new IllegalArgumentException("Please specify a non-empty name prefix");
      }
   }

   public MultiFileOutputStream init(UploadObjectObserver observer, long partSize, long diskLimit) {
      if (observer == null) {
         throw new IllegalArgumentException("Observer must be specified");
      } else {
         this.observer = observer;
         if (diskLimit < partSize << 1) {
            throw new IllegalArgumentException(
               "Maximum temporary disk space must be at least twice as large as the part size: partSize=" + partSize + ", diskSize=" + diskLimit
            );
         } else {
            this.partSize = partSize;
            this.diskLimit = diskLimit;
            int max = (int)(diskLimit / partSize);
            this.diskPermits = max < 0 ? null : new Semaphore(max);
            return this;
         }
      }
   }

   @Override
   public void write(int b) throws IOException {
      this.fos().write(b);
      ++this.currFileBytesWritten;
      ++this.totalBytesWritten;
   }

   @Override
   public void write(byte[] b) throws IOException {
      if (b.length != 0) {
         this.fos().write(b);
         this.currFileBytesWritten += b.length;
         this.totalBytesWritten += (long)b.length;
      }
   }

   @Override
   public void write(byte[] b, int off, int len) throws IOException {
      if (b.length != 0) {
         this.fos().write(b, off, len);
         this.currFileBytesWritten += len;
         this.totalBytesWritten += (long)len;
      }
   }

   private FileOutputStream fos() throws IOException {
      if (this.closed) {
         throw new IOException("Output stream is already closed");
      } else {
         if (this.os == null || (long)this.currFileBytesWritten >= this.partSize) {
            if (this.os != null) {
               this.os.close();
               this.observer.onPartCreate(new PartCreationEvent(this.getFile(this.filesCreated), this.filesCreated, false, this));
            }

            this.currFileBytesWritten = 0;
            ++this.filesCreated;
            this.blockIfNecessary();
            File file = this.getFile(this.filesCreated);
            this.os = new FileOutputStream(file);
         }

         return this.os;
      }
   }

   @Override
   public void onFileDelete(FileDeletionEvent event) {
      if (this.diskPermits != null) {
         this.diskPermits.release();
      }
   }

   private void blockIfNecessary() {
      if (this.diskPermits != null && this.diskLimit != Long.MAX_VALUE) {
         try {
            this.diskPermits.acquire();
         } catch (InterruptedException var2) {
            throw new AbortedException(var2);
         }
      }
   }

   @Override
   public void flush() throws IOException {
      if (this.os != null) {
         this.os.flush();
      }
   }

   @Override
   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         if (this.os != null) {
            this.os.close();
            File lastPart = this.getFile(this.filesCreated);
            if (lastPart.length() == 0L) {
               if (!lastPart.delete()) {
                  LogFactory.getLog(this.getClass()).debug("Ignoring failure to delete empty file " + lastPart);
               }
            } else {
               this.observer.onPartCreate(new PartCreationEvent(this.getFile(this.filesCreated), this.filesCreated, true, this));
            }
         }
      }
   }

   public void cleanup() {
      for(int i = 0; i < this.getNumFilesWritten(); ++i) {
         File f = this.getFile(i);
         if (f.exists() && !f.delete()) {
            LogFactory.getLog(this.getClass()).debug("Ignoring failure to delete file " + f);
         }
      }
   }

   public int getNumFilesWritten() {
      return this.filesCreated;
   }

   public File getFile(int partNumber) {
      return new File(this.root, this.namePrefix + "." + partNumber);
   }

   public long getPartSize() {
      return this.partSize;
   }

   public File getRoot() {
      return this.root;
   }

   public String getNamePrefix() {
      return this.namePrefix;
   }

   public long getTotalBytesWritten() {
      return this.totalBytesWritten;
   }

   static String yyMMdd_hhmmss() {
      return DateTimeFormat.forPattern("yyMMdd-hhmmss").print(new DateTime());
   }

   public boolean isClosed() {
      return this.closed;
   }

   public long getDiskLimit() {
      return this.diskLimit;
   }
}
