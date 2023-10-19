package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public class DownloadS3ObjectCallable implements Callable<Long> {
   private static final Log LOG = LogFactory.getLog(DownloadS3ObjectCallable.class);
   private static final int BUFFER_SIZE = 2097152;
   private final Callable<S3Object> serviceCall;
   private final File destinationFile;
   private final long position;

   public DownloadS3ObjectCallable(Callable<S3Object> serviceCall, File destinationFile, long position) {
      this.serviceCall = serviceCall;
      this.destinationFile = destinationFile;
      this.position = position;
   }

   public Long call() throws Exception {
      RandomAccessFile randomAccessFile = new RandomAccessFile(this.destinationFile, "rw");
      FileChannel channel = randomAccessFile.getChannel();
      channel.position(this.position);
      S3ObjectInputStream objectContent = null;

      long filePosition;
      try {
         S3Object object = this.serviceCall.call();
         objectContent = object.getObjectContent();
         byte[] buffer = new byte[2097152];
         ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);

         int bytesRead;
         while((bytesRead = objectContent.read(buffer)) > -1) {
            ((Buffer)byteBuffer).limit(bytesRead);

            while(byteBuffer.hasRemaining()) {
               channel.write(byteBuffer);
            }

            ((Buffer)byteBuffer).clear();
         }

         filePosition = channel.position();
      } finally {
         IOUtils.closeQuietly(objectContent, LOG);
         IOUtils.closeQuietly(randomAccessFile, LOG);
         IOUtils.closeQuietly(channel, LOG);
      }

      return filePosition;
   }
}
