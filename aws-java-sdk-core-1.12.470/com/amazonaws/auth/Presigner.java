package com.amazonaws.auth;

import com.amazonaws.SignableRequest;
import java.util.Date;

public interface Presigner {
   void presignRequest(SignableRequest<?> var1, AWSCredentials var2, Date var3);
}
