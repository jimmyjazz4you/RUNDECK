package com.amazonaws.services.s3.transfer;

import com.amazonaws.services.s3.model.ObjectTagging;

public interface ObjectTaggingProvider {
   ObjectTagging provideObjectTags(UploadContext var1);
}
