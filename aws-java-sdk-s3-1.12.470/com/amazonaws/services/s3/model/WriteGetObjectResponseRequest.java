package com.amazonaws.services.s3.model;

import com.amazonaws.AmazonWebServiceRequest;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

public class WriteGetObjectResponseRequest extends AmazonWebServiceRequest implements Serializable, S3DataSource {
   private static final long serialVersionUID = 1L;
   private String requestRoute;
   private String requestToken;
   private Integer statusCode;
   private String errorCode;
   private String errorMessage;
   private String contentType;
   private String acceptRanges;
   private String cacheControl;
   private String contentDisposition;
   private String contentEncoding;
   private String contentLanguage;
   private Long contentLength;
   private String contentRange;
   private String deleteMarker;
   private String eTag;
   private Date expires;
   private String expiration;
   private Date lastModified;
   private Integer missingMeta;
   private String objectLockMode;
   private String objectLockLegalHoldStatus;
   private Date objectLockRetainUntilDate;
   private Integer partsCount;
   private String replicationStatus;
   private String requestCharged;
   private String restore;
   private String serverSideEncryption;
   private String sseCustomerAlgorithm;
   private String sseKMSKeyId;
   private String sseCustomerKeyMD5;
   private String storageClass;
   private Integer tagCount;
   private String versionId;
   private Boolean bucketKeyEnabled;
   private ObjectMetadata metadata;
   private transient InputStream inputStream;
   private File file;

   public WriteGetObjectResponseRequest withRequestRoute(String requestRoute) {
      this.requestRoute = requestRoute;
      return this;
   }

   public void setRequestRoute(String requestRoute) {
      this.withRequestRoute(requestRoute);
   }

   public String getRequestRoute() {
      return this.requestRoute;
   }

   public WriteGetObjectResponseRequest withRequestToken(String requestToken) {
      this.requestToken = requestToken;
      return this;
   }

   public void setRequestToken(String requestToken) {
      this.withRequestToken(requestToken);
   }

   public String getRequestToken() {
      return this.requestToken;
   }

   public WriteGetObjectResponseRequest withStatusCode(Integer statusCode) {
      this.statusCode = statusCode;
      return this;
   }

   public void setStatusCode(Integer statusCode) {
      this.withStatusCode(statusCode);
   }

   public Integer getStatusCode() {
      return this.statusCode;
   }

   public WriteGetObjectResponseRequest withErrorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
   }

   public void setErrorCode(String errorCode) {
      this.withErrorCode(errorCode);
   }

   public String getErrorCode() {
      return this.errorCode;
   }

   public WriteGetObjectResponseRequest withErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
   }

   public void setErrorMessage(String errorMessage) {
      this.withErrorMessage(errorMessage);
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public WriteGetObjectResponseRequest withAcceptRanges(String acceptRanges) {
      this.acceptRanges = acceptRanges;
      return this;
   }

   public void setAcceptRanges(String acceptRanges) {
      this.withAcceptRanges(acceptRanges);
   }

   public String getAcceptRanges() {
      return this.acceptRanges;
   }

   public WriteGetObjectResponseRequest withCacheControl(String cacheControl) {
      this.cacheControl = cacheControl;
      return this;
   }

   public void setCacheControl(String cacheControl) {
      this.withCacheControl(cacheControl);
   }

   public String getCacheControl() {
      return this.cacheControl;
   }

   public WriteGetObjectResponseRequest withContentDisposition(String contentDisposition) {
      this.contentDisposition = contentDisposition;
      return this;
   }

   public void setContentDisposition(String contentDisposition) {
      this.withContentDisposition(contentDisposition);
   }

   public String getContentDisposition() {
      return this.contentDisposition;
   }

   public WriteGetObjectResponseRequest withContentEncoding(String contentEncoding) {
      this.contentEncoding = contentEncoding;
      return this;
   }

   public void setContentEncoding(String contentEncoding) {
      this.withContentEncoding(contentEncoding);
   }

   public String getContentEncoding() {
      return this.contentEncoding;
   }

   public WriteGetObjectResponseRequest withContentLanguage(String contentLanguage) {
      this.contentLanguage = contentLanguage;
      return this;
   }

   public void setContentLanguage(String contentLanguage) {
      this.withContentLanguage(contentLanguage);
   }

   public String getContentLanguage() {
      return this.contentLanguage;
   }

   public WriteGetObjectResponseRequest withContentLength(Long contentLength) {
      this.contentLength = contentLength;
      return this;
   }

   public void setContentLength(Long contentLength) {
      this.withContentLength(contentLength);
   }

   public Long getContentLength() {
      return this.contentLength;
   }

   public WriteGetObjectResponseRequest withContentRange(String contentRange) {
      this.contentRange = contentRange;
      return this;
   }

   public void setContentRange(String contentRange) {
      this.withContentRange(contentRange);
   }

   public String getContentRange() {
      return this.contentRange;
   }

   public WriteGetObjectResponseRequest withDeleteMarker(String deleteMarker) {
      this.deleteMarker = deleteMarker;
      return this;
   }

   public void setDeleteMarker(String deleteMarker) {
      this.withDeleteMarker(deleteMarker);
   }

   public String getDeleteMarker() {
      return this.deleteMarker;
   }

   public WriteGetObjectResponseRequest withETag(String eTag) {
      this.eTag = eTag;
      return this;
   }

   public void setETag(String eTag) {
      this.withETag(eTag);
   }

   public String getETag() {
      return this.eTag;
   }

   public WriteGetObjectResponseRequest withExpires(Date expires) {
      this.expires = expires;
      return this;
   }

   public void setExpires(Date expires) {
      this.withExpires(expires);
   }

   public Date getExpires() {
      return this.expires;
   }

   public WriteGetObjectResponseRequest withExpiration(String expiration) {
      this.expiration = expiration;
      return this;
   }

   public void setExpiration(String expiration) {
      this.withExpiration(expiration);
   }

   public String getExpiration() {
      return this.expiration;
   }

   public WriteGetObjectResponseRequest withLastModified(Date lastModified) {
      this.lastModified = lastModified;
      return this;
   }

   public void setLastModified(Date lastModified) {
      this.withLastModified(lastModified);
   }

   public Date getLastModified() {
      return this.lastModified;
   }

   public WriteGetObjectResponseRequest withMissingMeta(Integer missingMeta) {
      this.missingMeta = missingMeta;
      return this;
   }

   public void setMissingMeta(Integer missingMeta) {
      this.withMissingMeta(missingMeta);
   }

   public Integer getMissingMeta() {
      return this.missingMeta;
   }

   public WriteGetObjectResponseRequest withObjectLockMode(String objectLockMode) {
      this.objectLockMode = objectLockMode;
      return this;
   }

   public WriteGetObjectResponseRequest withObjectLockMode(ObjectLockMode objectLockMode) {
      this.objectLockMode = objectLockMode.toString();
      return this;
   }

   public void setObjectLockMode(String objectLockMode) {
      this.withObjectLockMode(objectLockMode);
   }

   public void setObjectLockMode(ObjectLockMode objectLockMode) {
      this.withObjectLockMode(objectLockMode);
   }

   public String getObjectLockMode() {
      return this.objectLockMode;
   }

   public WriteGetObjectResponseRequest withObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus;
      return this;
   }

   public WriteGetObjectResponseRequest withObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      this.objectLockLegalHoldStatus = objectLockLegalHoldStatus.toString();
      return this;
   }

   public void setObjectLockLegalHoldStatus(String objectLockLegalHoldStatus) {
      this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus);
   }

   public void setObjectLockLegalHoldStatus(ObjectLockLegalHoldStatus objectLockLegalHoldStatus) {
      this.withObjectLockLegalHoldStatus(objectLockLegalHoldStatus);
   }

   public String getObjectLockLegalHoldStatus() {
      return this.objectLockLegalHoldStatus;
   }

   public WriteGetObjectResponseRequest withObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.objectLockRetainUntilDate = objectLockRetainUntilDate;
      return this;
   }

   public void setObjectLockRetainUntilDate(Date objectLockRetainUntilDate) {
      this.withObjectLockRetainUntilDate(objectLockRetainUntilDate);
   }

   public Date getObjectLockRetainUntilDate() {
      return this.objectLockRetainUntilDate;
   }

   public WriteGetObjectResponseRequest withPartsCount(Integer partsCount) {
      this.partsCount = partsCount;
      return this;
   }

   public void setPartsCount(Integer partsCount) {
      this.withPartsCount(partsCount);
   }

   public Integer getPartsCount() {
      return this.partsCount;
   }

   public WriteGetObjectResponseRequest withReplicationStatus(String replicationStatus) {
      this.replicationStatus = replicationStatus;
      return this;
   }

   public void setReplicationStatus(String replicationStatus) {
      this.withReplicationStatus(replicationStatus);
   }

   public String getReplicationStatus() {
      return this.replicationStatus;
   }

   public WriteGetObjectResponseRequest withRequestCharged(String requestCharged) {
      this.requestCharged = requestCharged;
      return this;
   }

   public void setRequestCharged(String requestCharged) {
      this.withRequestCharged(requestCharged);
   }

   public String getRequestCharged() {
      return this.requestCharged;
   }

   public WriteGetObjectResponseRequest withRestore(String restore) {
      this.restore = restore;
      return this;
   }

   public void setRestore(String restore) {
      this.withRestore(restore);
   }

   public String getRestore() {
      return this.restore;
   }

   public WriteGetObjectResponseRequest withServerSideEncryption(String serverSideEncryption) {
      this.serverSideEncryption = serverSideEncryption;
      return this;
   }

   public void setServerSideEncryption(String serverSideEncryption) {
      this.withServerSideEncryption(serverSideEncryption);
   }

   public String getServerSideEncryption() {
      return this.serverSideEncryption;
   }

   public WriteGetObjectResponseRequest withSSECustomerAlgorithm(String sSECustomerAlgorithm) {
      this.sseCustomerAlgorithm = sSECustomerAlgorithm;
      return this;
   }

   public void setSSECustomerAlgorithm(String sSECustomerAlgorithm) {
      this.withSSECustomerAlgorithm(sSECustomerAlgorithm);
   }

   public String getSSECustomerAlgorithm() {
      return this.sseCustomerAlgorithm;
   }

   public WriteGetObjectResponseRequest withSSEKMSKeyId(String sSEKMSKeyId) {
      this.sseKMSKeyId = sSEKMSKeyId;
      return this;
   }

   public void setSSEKMSKeyId(String sSEKMSKeyId) {
      this.withSSEKMSKeyId(sSEKMSKeyId);
   }

   public String getSSEKMSKeyId() {
      return this.sseKMSKeyId;
   }

   public WriteGetObjectResponseRequest withSSECustomerKeyMD5(String sSECustomerKeyMD5) {
      this.sseCustomerKeyMD5 = sSECustomerKeyMD5;
      return this;
   }

   public void setSSECustomerKeyMD5(String sSECustomerKeyMD5) {
      this.withSSECustomerKeyMD5(sSECustomerKeyMD5);
   }

   public String getSSECustomerKeyMD5() {
      return this.sseCustomerKeyMD5;
   }

   public WriteGetObjectResponseRequest withStorageClass(String storageClass) {
      this.storageClass = storageClass;
      return this;
   }

   public WriteGetObjectResponseRequest withStorageClass(StorageClass storageClass) {
      this.storageClass = storageClass.toString();
      return this;
   }

   public void setStorageClass(String storageClass) {
      this.withStorageClass(storageClass);
   }

   public void setStorageClass(StorageClass storageClass) {
      this.withStorageClass(storageClass);
   }

   public String getStorageClass() {
      return this.storageClass;
   }

   public WriteGetObjectResponseRequest withTagCount(Integer tagCount) {
      this.tagCount = tagCount;
      return this;
   }

   public void setTagCount(Integer tagCount) {
      this.withTagCount(tagCount);
   }

   public Integer getTagCount() {
      return this.tagCount;
   }

   public WriteGetObjectResponseRequest withVersionId(String versionId) {
      this.versionId = versionId;
      return this;
   }

   public void setVersionId(String versionId) {
      this.withVersionId(versionId);
   }

   public String getVersionId() {
      return this.versionId;
   }

   public WriteGetObjectResponseRequest withBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.bucketKeyEnabled = bucketKeyEnabled;
      return this;
   }

   public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
      this.withBucketKeyEnabled(bucketKeyEnabled);
   }

   public Boolean getBucketKeyEnabled() {
      return this.bucketKeyEnabled;
   }

   public WriteGetObjectResponseRequest withContentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   public void setContentType(String contentType) {
      this.withContentType(contentType);
   }

   public String getContentType() {
      return this.contentType;
   }

   public WriteGetObjectResponseRequest withMetadata(ObjectMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   public void setMetadata(ObjectMetadata metadata) {
      this.withMetadata(metadata);
   }

   public ObjectMetadata getMetadata() {
      return this.metadata;
   }

   public WriteGetObjectResponseRequest withInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
      return this;
   }

   @Override
   public void setInputStream(InputStream inputStream) {
      this.withInputStream(inputStream);
   }

   @Override
   public InputStream getInputStream() {
      return this.inputStream;
   }

   public WriteGetObjectResponseRequest withFile(File file) {
      this.file = file;
      return this;
   }

   @Override
   public void setFile(File file) {
      this.withFile(file);
   }

   @Override
   public File getFile() {
      return this.file;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         WriteGetObjectResponseRequest that = (WriteGetObjectResponseRequest)o;
         if (this.requestRoute != null ? this.requestRoute.equals(that.requestRoute) : that.requestRoute == null) {
            if (this.requestToken != null ? this.requestToken.equals(that.requestToken) : that.requestToken == null) {
               if (this.statusCode != null ? this.statusCode.equals(that.statusCode) : that.statusCode == null) {
                  if (this.errorCode != null ? this.errorCode.equals(that.errorCode) : that.errorCode == null) {
                     if (this.errorMessage != null ? this.errorMessage.equals(that.errorMessage) : that.errorMessage == null) {
                        if (this.contentType != null ? this.contentType.equals(that.contentType) : that.contentType == null) {
                           if (this.acceptRanges != null ? this.acceptRanges.equals(that.acceptRanges) : that.acceptRanges == null) {
                              if (this.cacheControl != null ? this.cacheControl.equals(that.cacheControl) : that.cacheControl == null) {
                                 if (this.contentDisposition != null
                                    ? this.contentDisposition.equals(that.contentDisposition)
                                    : that.contentDisposition == null) {
                                    if (this.contentEncoding != null ? this.contentEncoding.equals(that.contentEncoding) : that.contentEncoding == null) {
                                       if (this.contentLanguage != null ? this.contentLanguage.equals(that.contentLanguage) : that.contentLanguage == null) {
                                          if (this.contentLength != null ? this.contentLength.equals(that.contentLength) : that.contentLength == null) {
                                             if (this.contentRange != null ? this.contentRange.equals(that.contentRange) : that.contentRange == null) {
                                                if (this.deleteMarker != null ? this.deleteMarker.equals(that.deleteMarker) : that.deleteMarker == null) {
                                                   if (this.eTag != null ? this.eTag.equals(that.eTag) : that.eTag == null) {
                                                      if (this.expires != null ? this.expires.equals(that.expires) : that.expires == null) {
                                                         if (this.expiration != null ? this.expiration.equals(that.expiration) : that.expiration == null) {
                                                            if (this.lastModified != null
                                                               ? this.lastModified.equals(that.lastModified)
                                                               : that.lastModified == null) {
                                                               if (this.missingMeta != null
                                                                  ? this.missingMeta.equals(that.missingMeta)
                                                                  : that.missingMeta == null) {
                                                                  if (this.objectLockMode != null
                                                                     ? this.objectLockMode.equals(that.objectLockMode)
                                                                     : that.objectLockMode == null) {
                                                                     if (this.objectLockLegalHoldStatus != null
                                                                        ? this.objectLockLegalHoldStatus.equals(that.objectLockLegalHoldStatus)
                                                                        : that.objectLockLegalHoldStatus == null) {
                                                                        if (this.objectLockRetainUntilDate != null
                                                                           ? this.objectLockRetainUntilDate.equals(that.objectLockRetainUntilDate)
                                                                           : that.objectLockRetainUntilDate == null) {
                                                                           if (this.partsCount != null
                                                                              ? this.partsCount.equals(that.partsCount)
                                                                              : that.partsCount == null) {
                                                                              if (this.replicationStatus != null
                                                                                 ? this.replicationStatus.equals(that.replicationStatus)
                                                                                 : that.replicationStatus == null) {
                                                                                 if (this.requestCharged != null
                                                                                    ? this.requestCharged.equals(that.requestCharged)
                                                                                    : that.requestCharged == null) {
                                                                                    if (this.restore != null
                                                                                       ? this.restore.equals(that.restore)
                                                                                       : that.restore == null) {
                                                                                       if (this.serverSideEncryption != null
                                                                                          ? this.serverSideEncryption.equals(that.serverSideEncryption)
                                                                                          : that.serverSideEncryption == null) {
                                                                                          if (this.sseCustomerAlgorithm != null
                                                                                             ? this.sseCustomerAlgorithm.equals(that.sseCustomerAlgorithm)
                                                                                             : that.sseCustomerAlgorithm == null) {
                                                                                             if (this.sseKMSKeyId != null
                                                                                                ? this.sseKMSKeyId.equals(that.sseKMSKeyId)
                                                                                                : that.sseKMSKeyId == null) {
                                                                                                if (this.sseCustomerKeyMD5 != null
                                                                                                   ? this.sseCustomerKeyMD5.equals(that.sseCustomerKeyMD5)
                                                                                                   : that.sseCustomerKeyMD5 == null) {
                                                                                                   if (this.storageClass != null
                                                                                                      ? this.storageClass.equals(that.storageClass)
                                                                                                      : that.storageClass == null) {
                                                                                                      if (this.tagCount != null
                                                                                                         ? this.tagCount.equals(that.tagCount)
                                                                                                         : that.tagCount == null) {
                                                                                                         if (this.versionId != null
                                                                                                            ? this.versionId.equals(that.versionId)
                                                                                                            : that.versionId == null) {
                                                                                                            if (this.bucketKeyEnabled != null
                                                                                                               ? this.bucketKeyEnabled
                                                                                                                  .equals(that.bucketKeyEnabled)
                                                                                                               : that.bucketKeyEnabled == null) {
                                                                                                               if (this.metadata != null
                                                                                                                  ? this.metadata.equals(that.metadata)
                                                                                                                  : that.metadata == null) {
                                                                                                                  if (this.inputStream != null
                                                                                                                     ? this.inputStream
                                                                                                                        .equals(that.inputStream)
                                                                                                                     : that.inputStream == null) {
                                                                                                                     return this.file != null
                                                                                                                        ? this.file.equals(that.file)
                                                                                                                        : that.file == null;
                                                                                                                  } else {
                                                                                                                     return false;
                                                                                                                  }
                                                                                                               } else {
                                                                                                                  return false;
                                                                                                               }
                                                                                                            } else {
                                                                                                               return false;
                                                                                                            }
                                                                                                         } else {
                                                                                                            return false;
                                                                                                         }
                                                                                                      } else {
                                                                                                         return false;
                                                                                                      }
                                                                                                   } else {
                                                                                                      return false;
                                                                                                   }
                                                                                                } else {
                                                                                                   return false;
                                                                                                }
                                                                                             } else {
                                                                                                return false;
                                                                                             }
                                                                                          } else {
                                                                                             return false;
                                                                                          }
                                                                                       } else {
                                                                                          return false;
                                                                                       }
                                                                                    } else {
                                                                                       return false;
                                                                                    }
                                                                                 } else {
                                                                                    return false;
                                                                                 }
                                                                              } else {
                                                                                 return false;
                                                                              }
                                                                           } else {
                                                                              return false;
                                                                           }
                                                                        } else {
                                                                           return false;
                                                                        }
                                                                     } else {
                                                                        return false;
                                                                     }
                                                                  } else {
                                                                     return false;
                                                                  }
                                                               } else {
                                                                  return false;
                                                               }
                                                            } else {
                                                               return false;
                                                            }
                                                         } else {
                                                            return false;
                                                         }
                                                      } else {
                                                         return false;
                                                      }
                                                   } else {
                                                      return false;
                                                   }
                                                } else {
                                                   return false;
                                                }
                                             } else {
                                                return false;
                                             }
                                          } else {
                                             return false;
                                          }
                                       } else {
                                          return false;
                                       }
                                    } else {
                                       return false;
                                    }
                                 } else {
                                    return false;
                                 }
                              } else {
                                 return false;
                              }
                           } else {
                              return false;
                           }
                        } else {
                           return false;
                        }
                     } else {
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.requestRoute != null ? this.requestRoute.hashCode() : 0;
      result = 31 * result + (this.requestToken != null ? this.requestToken.hashCode() : 0);
      result = 31 * result + (this.statusCode != null ? this.statusCode.hashCode() : 0);
      result = 31 * result + (this.errorCode != null ? this.errorCode.hashCode() : 0);
      result = 31 * result + (this.errorMessage != null ? this.errorMessage.hashCode() : 0);
      result = 31 * result + (this.contentType != null ? this.contentType.hashCode() : 0);
      result = 31 * result + (this.acceptRanges != null ? this.acceptRanges.hashCode() : 0);
      result = 31 * result + (this.cacheControl != null ? this.cacheControl.hashCode() : 0);
      result = 31 * result + (this.contentDisposition != null ? this.contentDisposition.hashCode() : 0);
      result = 31 * result + (this.contentEncoding != null ? this.contentEncoding.hashCode() : 0);
      result = 31 * result + (this.contentLanguage != null ? this.contentLanguage.hashCode() : 0);
      result = 31 * result + (this.contentLength != null ? this.contentLength.hashCode() : 0);
      result = 31 * result + (this.contentRange != null ? this.contentRange.hashCode() : 0);
      result = 31 * result + (this.deleteMarker != null ? this.deleteMarker.hashCode() : 0);
      result = 31 * result + (this.eTag != null ? this.eTag.hashCode() : 0);
      result = 31 * result + (this.expires != null ? this.expires.hashCode() : 0);
      result = 31 * result + (this.expiration != null ? this.expiration.hashCode() : 0);
      result = 31 * result + (this.lastModified != null ? this.lastModified.hashCode() : 0);
      result = 31 * result + (this.missingMeta != null ? this.missingMeta.hashCode() : 0);
      result = 31 * result + (this.objectLockMode != null ? this.objectLockMode.hashCode() : 0);
      result = 31 * result + (this.objectLockLegalHoldStatus != null ? this.objectLockLegalHoldStatus.hashCode() : 0);
      result = 31 * result + (this.objectLockRetainUntilDate != null ? this.objectLockRetainUntilDate.hashCode() : 0);
      result = 31 * result + (this.partsCount != null ? this.partsCount.hashCode() : 0);
      result = 31 * result + (this.replicationStatus != null ? this.replicationStatus.hashCode() : 0);
      result = 31 * result + (this.requestCharged != null ? this.requestCharged.hashCode() : 0);
      result = 31 * result + (this.restore != null ? this.restore.hashCode() : 0);
      result = 31 * result + (this.serverSideEncryption != null ? this.serverSideEncryption.hashCode() : 0);
      result = 31 * result + (this.sseCustomerAlgorithm != null ? this.sseCustomerAlgorithm.hashCode() : 0);
      result = 31 * result + (this.sseKMSKeyId != null ? this.sseKMSKeyId.hashCode() : 0);
      result = 31 * result + (this.sseCustomerKeyMD5 != null ? this.sseCustomerKeyMD5.hashCode() : 0);
      result = 31 * result + (this.storageClass != null ? this.storageClass.hashCode() : 0);
      result = 31 * result + (this.tagCount != null ? this.tagCount.hashCode() : 0);
      result = 31 * result + (this.versionId != null ? this.versionId.hashCode() : 0);
      result = 31 * result + (this.bucketKeyEnabled != null ? this.bucketKeyEnabled.hashCode() : 0);
      result = 31 * result + (this.metadata != null ? this.metadata.hashCode() : 0);
      result = 31 * result + (this.inputStream != null ? this.inputStream.hashCode() : 0);
      return 31 * result + (this.file != null ? this.file.hashCode() : 0);
   }
}
