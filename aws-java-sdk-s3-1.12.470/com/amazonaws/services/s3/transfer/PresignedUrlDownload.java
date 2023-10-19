package com.amazonaws.services.s3.transfer;

import java.net.URL;

public interface PresignedUrlDownload extends AbortableTransfer {
   URL getPresignedUrl();
}
