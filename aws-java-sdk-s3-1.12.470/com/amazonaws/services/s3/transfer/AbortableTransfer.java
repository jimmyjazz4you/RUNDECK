package com.amazonaws.services.s3.transfer;

import java.io.IOException;

public interface AbortableTransfer extends Transfer {
   void abort() throws IOException;
}
