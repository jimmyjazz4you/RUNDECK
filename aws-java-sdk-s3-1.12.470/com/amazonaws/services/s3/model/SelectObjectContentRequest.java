package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.Serializable;

public class SelectObjectContentRequest extends AmazonWebServiceRequest implements SSECustomerKeyProvider, Serializable, Cloneable, ExpectedBucketOwnerRequest {
   private String bucketName;
   private String key;
   private SSECustomerKey sseCustomerKey;
   private String expression;
   private String expressionType;
   private RequestProgress requestProgress;
   private InputSerialization inputSerialization;
   private OutputSerialization outputSerialization;
   private ScanRange scanRange;
   private String expectedBucketOwner;

   @Override
   public String getExpectedBucketOwner() {
      return this.expectedBucketOwner;
   }

   public SelectObjectContentRequest withExpectedBucketOwner(String expectedBucketOwner) {
      this.expectedBucketOwner = expectedBucketOwner;
      return this;
   }

   @Override
   public void setExpectedBucketOwner(String expectedBucketOwner) {
      this.withExpectedBucketOwner(expectedBucketOwner);
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
   }

   public SelectObjectContentRequest withBucketName(String bucketName) {
      this.setBucketName(bucketName);
      return this;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public SelectObjectContentRequest withKey(String key) {
      this.setKey(key);
      return this;
   }

   public String getExpression() {
      return this.expression;
   }

   public void setExpression(String expression) {
      this.expression = expression;
   }

   public SelectObjectContentRequest withExpression(String expression) {
      this.setExpression(expression);
      return this;
   }

   public String getExpressionType() {
      return this.expressionType;
   }

   public void setExpressionType(String expressionType) {
      this.expressionType = expressionType;
   }

   public SelectObjectContentRequest withExpressionType(String expressionType) {
      this.setExpressionType(expressionType);
      return this;
   }

   public void setExpressionType(ExpressionType expressionType) {
      this.setExpressionType(expressionType == null ? null : expressionType.toString());
   }

   public SelectObjectContentRequest withExpressionType(ExpressionType expressionType) {
      this.setExpressionType(expressionType);
      return this;
   }

   public ScanRange getScanRange() {
      return this.scanRange;
   }

   public void setScanRange(ScanRange scanRange) {
      this.scanRange = scanRange;
   }

   public SelectObjectContentRequest withScanRange(ScanRange scanRange) {
      this.setScanRange(scanRange);
      return this;
   }

   public RequestProgress getRequestProgress() {
      return this.requestProgress;
   }

   public void setRequestProgress(RequestProgress requestProgress) {
      this.requestProgress = requestProgress;
   }

   public SelectObjectContentRequest withRequestProgress(RequestProgress requestProgress) {
      this.setRequestProgress(requestProgress);
      return this;
   }

   public InputSerialization getInputSerialization() {
      return this.inputSerialization;
   }

   public void setInputSerialization(InputSerialization inputSerialization) {
      this.inputSerialization = inputSerialization;
   }

   public SelectObjectContentRequest withInputSerialization(InputSerialization inputSerialization) {
      this.setInputSerialization(inputSerialization);
      return this;
   }

   public OutputSerialization getOutputSerialization() {
      return this.outputSerialization;
   }

   public void setOutputSerialization(OutputSerialization outputSerialization) {
      this.outputSerialization = outputSerialization;
   }

   public SelectObjectContentRequest withOutputSerialization(OutputSerialization outputSerialization) {
      this.setOutputSerialization(outputSerialization);
      return this;
   }

   @Override
   public SSECustomerKey getSSECustomerKey() {
      return this.sseCustomerKey;
   }

   public void setSSECustomerKey(SSECustomerKey sseCustomerKey) {
      this.sseCustomerKey = sseCustomerKey;
   }

   public SelectObjectContentRequest withSSECustomerKey(SSECustomerKey sseCustomerKey) {
      this.setSSECustomerKey(sseCustomerKey);
      return this;
   }
}
