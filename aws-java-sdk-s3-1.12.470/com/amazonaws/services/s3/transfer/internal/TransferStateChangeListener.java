package com.amazonaws.services.s3.transfer.internal;

import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.services.s3.transfer.Transfer;

@SdkInternalApi
public interface TransferStateChangeListener {
   void transferStateChanged(Transfer var1, Transfer.TransferState var2);
}
