package com.amazonaws.services.s3.internal;

import java.util.Date;

public interface ObjectRestoreResult {
   Date getRestoreExpirationTime();

   void setRestoreExpirationTime(Date var1);

   void setOngoingRestore(boolean var1);

   Boolean getOngoingRestore();
}
