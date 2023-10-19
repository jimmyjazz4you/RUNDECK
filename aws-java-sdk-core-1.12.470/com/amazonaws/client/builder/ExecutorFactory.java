package com.amazonaws.client.builder;

import java.util.concurrent.ExecutorService;

public interface ExecutorFactory {
   ExecutorService newExecutor();
}
