package com.amazonaws.waiters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import java.util.concurrent.Future;

public interface Waiter<Input extends AmazonWebServiceRequest> {
   void run(WaiterParameters<Input> var1) throws AmazonServiceException, WaiterTimedOutException, WaiterUnrecoverableException;

   Future<Void> runAsync(WaiterParameters<Input> var1, WaiterHandler var2) throws AmazonServiceException, WaiterTimedOutException, WaiterUnrecoverableException;
}
