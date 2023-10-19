package com.amazonaws.services.s3.transfer;

import java.io.IOException;

public interface MultipleFileDownload extends Transfer {
   String getKeyPrefix();

   String getBucketName();

   void abort() throws IOException;
}
