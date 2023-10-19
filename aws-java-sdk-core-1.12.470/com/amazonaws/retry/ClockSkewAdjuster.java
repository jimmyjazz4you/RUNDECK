package com.amazonaws.retry;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.Request;
import com.amazonaws.SdkBaseException;
import com.amazonaws.annotation.NotThreadSafe;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.annotation.ThreadSafe;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.ValidationUtils;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

@ThreadSafe
@SdkInternalApi
public final class ClockSkewAdjuster {
   private static final Log log = LogFactory.getLog(ClockSkewAdjuster.class);
   private static final Set<Integer> AUTHENTICATION_ERROR_STATUS_CODES;
   private static final int CLOCK_SKEW_ADJUST_THRESHOLD_IN_SECONDS = 240;
   private volatile Integer estimatedSkew;

   public Integer getEstimatedSkew() {
      return this.estimatedSkew;
   }

   public void updateEstimatedSkew(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      try {
         Date serverDate = this.getServerDate(adjustmentRequest);
         if (serverDate != null) {
            this.estimatedSkew = this.timeSkewInSeconds(this.getCurrentDate(adjustmentRequest), serverDate);
         }
      } catch (RuntimeException var3) {
         log.debug("Unable to update estimated skew.", var3);
      }
   }

   public ClockSkewAdjuster.ClockSkewAdjustment getAdjustment(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      ValidationUtils.assertNotNull(adjustmentRequest, "adjustmentRequest");
      ValidationUtils.assertNotNull(adjustmentRequest.exception, "adjustmentRequest.exception");
      ValidationUtils.assertNotNull(adjustmentRequest.clientRequest, "adjustmentRequest.clientRequest");
      ValidationUtils.assertNotNull(adjustmentRequest.serviceResponse, "adjustmentRequest.serviceResponse");
      int timeSkewInSeconds = 0;
      boolean isAdjustmentRecommended = false;

      try {
         if (this.isAdjustmentRecommended(adjustmentRequest)) {
            Date serverDate = this.getServerDate(adjustmentRequest);
            if (serverDate != null) {
               timeSkewInSeconds = this.timeSkewInSeconds(this.getCurrentDate(adjustmentRequest), serverDate);
               isAdjustmentRecommended = true;
            }
         }
      } catch (RuntimeException var5) {
         log.warn("Unable to correct for clock skew.", var5);
      }

      return new ClockSkewAdjuster.ClockSkewAdjustment(isAdjustmentRecommended, timeSkewInSeconds);
   }

   private boolean isAdjustmentRecommended(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      if (!(adjustmentRequest.exception instanceof AmazonServiceException)) {
         return false;
      } else {
         AmazonServiceException exception = (AmazonServiceException)adjustmentRequest.exception;
         return this.isDefinitelyClockSkewError(exception) || this.mayBeClockSkewError(exception) && this.clientRequestWasSkewed(adjustmentRequest);
      }
   }

   private boolean isDefinitelyClockSkewError(AmazonServiceException exception) {
      return RetryUtils.isClockSkewError(exception);
   }

   private boolean mayBeClockSkewError(AmazonServiceException exception) {
      return AUTHENTICATION_ERROR_STATUS_CODES.contains(exception.getStatusCode());
   }

   private boolean clientRequestWasSkewed(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      Date serverDate = this.getServerDate(adjustmentRequest);
      if (serverDate == null) {
         return false;
      } else {
         int requestClockSkew = this.timeSkewInSeconds(this.getClientDate(adjustmentRequest), serverDate);
         return Math.abs(requestClockSkew) > 240;
      }
   }

   private int timeSkewInSeconds(Date clientTime, Date serverTime) {
      ValidationUtils.assertNotNull(clientTime, "clientTime");
      ValidationUtils.assertNotNull(serverTime, "serverTime");
      long value = (clientTime.getTime() - serverTime.getTime()) / 1000L;
      if ((long)((int)value) != value) {
         throw new IllegalStateException("Time is too skewed to adjust: (clientTime: " + clientTime.getTime() + ", serverTime: " + serverTime.getTime() + ")");
      } else {
         return (int)value;
      }
   }

   private Date getCurrentDate(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      return new Date(adjustmentRequest.currentTime);
   }

   private Date getClientDate(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      return new Date(adjustmentRequest.currentTime - (long)(adjustmentRequest.clientRequest.getTimeOffset() * 1000));
   }

   private Date getServerDate(ClockSkewAdjuster.AdjustmentRequest adjustmentRequest) {
      String serverDateStr = null;

      try {
         Header[] responseDateHeader = adjustmentRequest.serviceResponse.getHeaders("Date");
         if (responseDateHeader.length > 0) {
            serverDateStr = responseDateHeader[0].getValue();
            log.debug("Reported server date (from 'Date' header): " + serverDateStr);
            return DateUtils.parseRFC822Date(serverDateStr);
         } else if (adjustmentRequest.exception == null) {
            return null;
         } else {
            String exceptionMessage = adjustmentRequest.exception.getMessage();
            serverDateStr = this.getServerDateFromException(exceptionMessage);
            if (serverDateStr != null) {
               log.debug("Reported server date (from exception message): " + serverDateStr);
               return DateUtils.parseCompressedISO8601Date(serverDateStr);
            } else {
               log.debug("Server did not return a date, so clock skew adjustments will not be applied.");
               return null;
            }
         }
      } catch (RuntimeException var5) {
         log.warn("Unable to parse clock skew offset from response: " + serverDateStr, var5);
         return null;
      }
   }

   private String getServerDateFromException(String body) {
      int startPos = body.indexOf("(");
      int endPos = body.indexOf(" + ");
      if (endPos == -1) {
         endPos = body.indexOf(" - ");
      }

      return endPos == -1 ? null : body.substring(startPos + 1, endPos);
   }

   static {
      Set<Integer> statusCodes = new HashSet<>();
      statusCodes.add(401);
      statusCodes.add(403);
      AUTHENTICATION_ERROR_STATUS_CODES = Collections.unmodifiableSet(statusCodes);
   }

   @NotThreadSafe
   public static final class AdjustmentRequest {
      private Request<?> clientRequest;
      private HttpResponse serviceResponse;
      private SdkBaseException exception;
      private long currentTime = System.currentTimeMillis();

      public ClockSkewAdjuster.AdjustmentRequest clientRequest(Request<?> clientRequest) {
         this.clientRequest = clientRequest;
         return this;
      }

      public ClockSkewAdjuster.AdjustmentRequest serviceResponse(HttpResponse serviceResponse) {
         this.serviceResponse = serviceResponse;
         return this;
      }

      public ClockSkewAdjuster.AdjustmentRequest exception(SdkBaseException exception) {
         this.exception = exception;
         return this;
      }

      @SdkTestInternalApi
      public ClockSkewAdjuster.AdjustmentRequest currentTime(long currentTime) {
         this.currentTime = currentTime;
         return this;
      }
   }

   @ThreadSafe
   public static final class ClockSkewAdjustment {
      private final boolean shouldAdjustForSkew;
      private final int adjustmentInSeconds;

      private ClockSkewAdjustment(boolean shouldAdjust, int adjustmentInSeconds) {
         this.shouldAdjustForSkew = shouldAdjust;
         this.adjustmentInSeconds = adjustmentInSeconds;
      }

      public boolean shouldAdjustForSkew() {
         return this.shouldAdjustForSkew;
      }

      public int inSeconds() {
         if (!this.shouldAdjustForSkew) {
            throw new IllegalStateException("An adjustment is not recommended.");
         } else {
            return this.adjustmentInSeconds;
         }
      }
   }
}
