package com.amazonaws.auth;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
abstract class BaseCredentialsFetcher {
   private static final Log LOG = LogFactory.getLog(BaseCredentialsFetcher.class);
   private static final Random JITTER = new Random();
   private static final int REFRESH_THRESHOLD = 3600000;
   private static final int FIFTEEN_MINUTES_IN_MILLIS = 900000;
   private static final int EXPIRATION_THRESHOLD = 900000;
   private static final String ACCESS_KEY_ID = "AccessKeyId";
   private static final String SECRET_ACCESS_KEY = "SecretAccessKey";
   private static final String TOKEN = "Token";
   private final SdkClock clock;
   private final boolean allowExpiredCredentials;
   private volatile AWSCredentials credentials;
   private volatile Date credentialsExpiration;
   private volatile Date credentialExpirationRefreshTime;
   protected volatile Date lastInstanceProfileCheck;

   protected BaseCredentialsFetcher(SdkClock clock, boolean allowExpiredCredentials) {
      this.clock = clock;
      this.allowExpiredCredentials = allowExpiredCredentials;
   }

   public AWSCredentials getCredentials() {
      if (this.needsToLoadCredentials()) {
         this.fetchCredentials();
      }

      if (!this.allowExpiredCredentials && this.expired()) {
         throw new SdkClientException("The credentials received have been expired");
      } else {
         return this.credentials;
      }
   }

   boolean needsToLoadCredentials() {
      return this.credentials == null || this.isExpiring() || this.noRecentInstanceProfileCheck();
   }

   private boolean isExpiring() {
      return this.credentialExpirationRefreshTime != null && this.credentialExpirationRefreshTime.getTime() <= this.clock.currentTimeMillis();
   }

   private boolean noRecentInstanceProfileCheck() {
      return this.lastInstanceProfileCheck != null && this.lastInstanceProfileCheck.getTime() + 3600000L <= this.clock.currentTimeMillis();
   }

   abstract String getCredentialsResponse();

   private synchronized void fetchCredentials() {
      if (this.needsToLoadCredentials()) {
         if (LOG.isDebugEnabled()) {
            if (this.credentialsExpiration != null) {
               LOG.debug("Updating credentials, because currently-cached credentials expire on " + this.credentialsExpiration);
            } else {
               LOG.debug("Retrieving credentials.");
            }
         }

         try {
            this.lastInstanceProfileCheck = new Date();
            String credentialsResponse = this.getCredentialsResponse();
            JsonNode node = Jackson.fromSensitiveJsonString(credentialsResponse, JsonNode.class);
            JsonNode accessKey = node.get("AccessKeyId");
            JsonNode secretKey = node.get("SecretAccessKey");
            JsonNode token = node.get("Token");
            if (null == accessKey || null == secretKey) {
               throw new SdkClientException("Unable to load credentials. Access key or secret key are null.");
            }

            if (null != token) {
               this.credentials = new BasicSessionCredentials(accessKey.asText(), secretKey.asText(), token.asText());
            } else {
               this.credentials = new BasicAWSCredentials(accessKey.asText(), secretKey.asText());
            }

            JsonNode expirationJsonNode = node.get("Expiration");
            if (null != expirationJsonNode) {
               String expiration = expirationJsonNode.asText();
               expiration = expiration.replaceAll("\\+0000$", "Z");

               try {
                  this.credentialsExpiration = DateUtils.parseISO8601Date(expiration);
                  this.credentialExpirationRefreshTime = new Date(this.credentialsExpiration.getTime() - 900000L);
                  LOG.debug("Successfully retrieved credentials with expiration " + expiration);
               } catch (Exception var25) {
                  this.handleError("Unable to parse credentials expiration date from Amazon EC2 instance", var25);
               }
            }
         } catch (Exception var26) {
            this.handleError("Unable to load credentials from service endpoint", var26);
         } finally {
            if (this.allowExpiredCredentials && this.credentials != null && this.isExpiring()) {
               long now = this.clock.currentTimeMillis();
               long waitUntilNextRefresh = (long)(50000 + JITTER.nextInt(20001));
               long nextRefreshTime = now + waitUntilNextRefresh;
               long effectiveExpiration = this.credentialsExpiration.getTime() - 15000L;
               if (nextRefreshTime > effectiveExpiration) {
                  LOG.warn(
                     "Credential expiration has been extended due to a credential service availability issue. A refresh of these credentials will be attempted again in "
                        + waitUntilNextRefresh
                        + " ms."
                  );
               }

               this.credentialExpirationRefreshTime = new Date(nextRefreshTime);
            }
         }
      }
   }

   private void handleError(String errorMessage, Exception e) {
      if (this.credentials == null || !this.allowExpiredCredentials && this.expired()) {
         if (e instanceof SdkClientException) {
            throw (SdkClientException)e;
         } else {
            throw new SdkClientException(errorMessage, e);
         }
      } else {
         LOG.warn(errorMessage, e);
      }
   }

   public void refresh() {
      this.credentials = null;
   }

   private boolean expired() {
      if (this.credentialsExpiration == null) {
         return false;
      } else {
         return this.credentialsExpiration.getTime() <= this.clock.currentTimeMillis();
      }
   }

   Date getCredentialsExpiration() {
      return this.credentialsExpiration;
   }

   @Override
   public String toString() {
      return "BaseCredentialsFetcher";
   }
}
