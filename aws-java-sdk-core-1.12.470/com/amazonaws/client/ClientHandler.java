package com.amazonaws.client;

import com.amazonaws.annotation.SdkProtectedApi;

@SdkProtectedApi
public abstract class ClientHandler {
   public abstract <Input, Output> Output execute(ClientExecutionParams<Input, Output> var1);

   public abstract void shutdown();
}
