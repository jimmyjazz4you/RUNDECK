package com.amazonaws.services.s3.transfer;

import java.util.Collection;

public interface MultipleFileUpload extends Transfer {
   String getKeyPrefix();

   String getBucketName();

   Collection<? extends Upload> getSubTransfers();
}
