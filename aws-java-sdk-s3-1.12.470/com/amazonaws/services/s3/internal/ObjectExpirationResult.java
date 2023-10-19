package com.amazonaws.services.s3.internal;

import java.util.Date;

public interface ObjectExpirationResult {
   Date getExpirationTime();

   void setExpirationTime(Date var1);

   String getExpirationTimeRuleId();

   void setExpirationTimeRuleId(String var1);
}
