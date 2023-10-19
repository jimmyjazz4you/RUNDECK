package com.amazonaws.services.s3.model.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.internal.DeleteObjectsResponse;
import com.amazonaws.services.s3.internal.ObjectExpirationResult;
import com.amazonaws.services.s3.internal.S3RequesterChargedResult;
import com.amazonaws.services.s3.internal.S3VersionResult;
import com.amazonaws.services.s3.internal.ServerSideEncryptionResult;
import com.amazonaws.services.s3.internal.ServiceUtils;
import com.amazonaws.services.s3.model.AbortIncompleteMultipartUpload;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AccessControlTranslation;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.BucketTaggingConfiguration;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DefaultRetention;
import com.amazonaws.services.s3.model.DeleteMarkerReplication;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.EncryptionConfiguration;
import com.amazonaws.services.s3.model.ExistingObjectReplication;
import com.amazonaws.services.s3.model.GetBucketAnalyticsConfigurationResult;
import com.amazonaws.services.s3.model.GetBucketIntelligentTieringConfigurationResult;
import com.amazonaws.services.s3.model.GetBucketInventoryConfigurationResult;
import com.amazonaws.services.s3.model.GetBucketMetricsConfigurationResult;
import com.amazonaws.services.s3.model.GetBucketOwnershipControlsResult;
import com.amazonaws.services.s3.model.GetObjectLegalHoldResult;
import com.amazonaws.services.s3.model.GetObjectLockConfigurationResult;
import com.amazonaws.services.s3.model.GetObjectRetentionResult;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListBucketAnalyticsConfigurationsResult;
import com.amazonaws.services.s3.model.ListBucketIntelligentTieringConfigurationsResult;
import com.amazonaws.services.s3.model.ListBucketInventoryConfigurationsResult;
import com.amazonaws.services.s3.model.ListBucketMetricsConfigurationsResult;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.Metrics;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.MultipartUpload;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectLockConfiguration;
import com.amazonaws.services.s3.model.ObjectLockLegalHold;
import com.amazonaws.services.s3.model.ObjectLockRetention;
import com.amazonaws.services.s3.model.ObjectLockRule;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.PartSummary;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.RedirectRule;
import com.amazonaws.services.s3.model.ReplicaModifications;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationTime;
import com.amazonaws.services.s3.model.ReplicationTimeValue;
import com.amazonaws.services.s3.model.RequestPaymentConfiguration;
import com.amazonaws.services.s3.model.RoutingRule;
import com.amazonaws.services.s3.model.RoutingRuleCondition;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.SourceSelectionCriteria;
import com.amazonaws.services.s3.model.SseKmsEncryptedObjects;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.TagSet;
import com.amazonaws.services.s3.model.VersionListing;
import com.amazonaws.services.s3.model.analytics.AnalyticsAndOperator;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import com.amazonaws.services.s3.model.analytics.AnalyticsExportDestination;
import com.amazonaws.services.s3.model.analytics.AnalyticsFilter;
import com.amazonaws.services.s3.model.analytics.AnalyticsFilterPredicate;
import com.amazonaws.services.s3.model.analytics.AnalyticsPrefixPredicate;
import com.amazonaws.services.s3.model.analytics.AnalyticsS3BucketDestination;
import com.amazonaws.services.s3.model.analytics.AnalyticsTagPredicate;
import com.amazonaws.services.s3.model.analytics.StorageClassAnalysis;
import com.amazonaws.services.s3.model.analytics.StorageClassAnalysisDataExport;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringAccessTier;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringAndOperator;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringFilter;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringFilterPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringPrefixPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringStatus;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringTagPredicate;
import com.amazonaws.services.s3.model.intelligenttiering.Tiering;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import com.amazonaws.services.s3.model.inventory.InventoryDestination;
import com.amazonaws.services.s3.model.inventory.InventoryFilter;
import com.amazonaws.services.s3.model.inventory.InventoryPrefixPredicate;
import com.amazonaws.services.s3.model.inventory.InventoryS3BucketDestination;
import com.amazonaws.services.s3.model.inventory.InventorySchedule;
import com.amazonaws.services.s3.model.inventory.ServerSideEncryptionKMS;
import com.amazonaws.services.s3.model.inventory.ServerSideEncryptionS3;
import com.amazonaws.services.s3.model.lifecycle.LifecycleAndOperator;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilterPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeGreaterThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeLessThanPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePrefixPredicate;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsAccessPointArnPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsAndOperator;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import com.amazonaws.services.s3.model.metrics.MetricsFilter;
import com.amazonaws.services.s3.model.metrics.MetricsFilterPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsPrefixPredicate;
import com.amazonaws.services.s3.model.metrics.MetricsTagPredicate;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import com.amazonaws.services.s3.model.ownership.OwnershipControlsRule;
import com.amazonaws.services.s3.model.replication.ReplicationAndOperator;
import com.amazonaws.services.s3.model.replication.ReplicationFilter;
import com.amazonaws.services.s3.model.replication.ReplicationFilterPredicate;
import com.amazonaws.services.s3.model.replication.ReplicationPrefixPredicate;
import com.amazonaws.services.s3.model.replication.ReplicationTagPredicate;
import com.amazonaws.util.DateUtils;
import com.amazonaws.util.SdkHttpUtils;
import com.amazonaws.util.StringUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlResponsesSaxParser {
   private static final Log log = LogFactory.getLog(XmlResponsesSaxParser.class);
   private XMLReader xr = null;
   private boolean sanitizeXmlDocument = true;

   public XmlResponsesSaxParser() throws SdkClientException {
      try {
         this.xr = XMLReaderFactory.createXMLReader();
         this.disableExternalResourceFetching(this.xr);
      } catch (SAXException var2) {
         throw new SdkClientException("Couldn't initialize a SAX driver to create an XMLReader", var2);
      }
   }

   protected void parseXmlInputStream(DefaultHandler handler, InputStream inputStream) throws IOException {
      try {
         if (log.isDebugEnabled()) {
            log.debug("Parsing XML response document with handler: " + handler.getClass());
         }

         BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
         this.xr.setContentHandler(handler);
         this.xr.setErrorHandler(handler);
         this.xr.parse(new InputSource(breader));
      } catch (IOException var6) {
         throw var6;
      } catch (Throwable var7) {
         try {
            inputStream.close();
         } catch (IOException var5) {
            if (log.isErrorEnabled()) {
               log.error("Unable to close response InputStream up after XML parse failure", var5);
            }
         }

         throw new SdkClientException("Failed to parse XML document with handler " + handler.getClass(), var7);
      }
   }

   protected InputStream sanitizeXmlDocument(DefaultHandler handler, InputStream inputStream) throws IOException {
      if (!this.sanitizeXmlDocument) {
         return inputStream;
      } else {
         if (log.isDebugEnabled()) {
            log.debug("Sanitizing XML document destined for handler " + handler.getClass());
         }

         InputStream sanitizedInputStream = null;

         try {
            StringBuilder listingDocBuffer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            char[] buf = new char[8192];
            int read = -1;

            while((read = br.read(buf)) != -1) {
               listingDocBuffer.append(buf, 0, read);
            }

            br.close();
            String listingDoc = listingDocBuffer.toString().replaceAll("\r", "&#013;");
            return new ByteArrayInputStream(listingDoc.getBytes(StringUtils.UTF8));
         } catch (IOException var10) {
            throw var10;
         } catch (Throwable var11) {
            try {
               inputStream.close();
            } catch (IOException var9) {
               if (log.isErrorEnabled()) {
                  log.error("Unable to close response InputStream after failure sanitizing XML document", var9);
               }
            }

            throw new SdkClientException("Failed to sanitize XML document destined for handler " + handler.getClass(), var11);
         }
      }
   }

   private void disableExternalResourceFetching(XMLReader reader) throws SAXNotRecognizedException, SAXNotSupportedException {
      reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
      reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
   }

   private static String checkForEmptyString(String s) {
      if (s == null) {
         return null;
      } else {
         return s.length() == 0 ? null : s;
      }
   }

   private static int parseInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException var2) {
         log.error("Unable to parse integer value '" + s + "'", var2);
         return -1;
      }
   }

   private static long parseLong(String s) {
      try {
         return Long.parseLong(s);
      } catch (NumberFormatException var2) {
         log.error("Unable to parse long value '" + s + "'", var2);
         return -1L;
      }
   }

   private static String decodeIfSpecified(String value, boolean decode) {
      return decode ? SdkHttpUtils.urlDecode(value) : value;
   }

   public XmlResponsesSaxParser.ListBucketHandler parseListBucketObjectsResponse(InputStream inputStream, boolean shouldSDKDecodeResponse) throws IOException {
      XmlResponsesSaxParser.ListBucketHandler handler = new XmlResponsesSaxParser.ListBucketHandler(shouldSDKDecodeResponse);
      this.parseXmlInputStream(handler, this.sanitizeXmlDocument(handler, inputStream));
      return handler;
   }

   public XmlResponsesSaxParser.ListObjectsV2Handler parseListObjectsV2Response(InputStream inputStream, boolean shouldSDKDecodeResponse) throws IOException {
      XmlResponsesSaxParser.ListObjectsV2Handler handler = new XmlResponsesSaxParser.ListObjectsV2Handler(shouldSDKDecodeResponse);
      this.parseXmlInputStream(handler, this.sanitizeXmlDocument(handler, inputStream));
      return handler;
   }

   public XmlResponsesSaxParser.ListVersionsHandler parseListVersionsResponse(InputStream inputStream, boolean shouldSDKDecodeResponse) throws IOException {
      XmlResponsesSaxParser.ListVersionsHandler handler = new XmlResponsesSaxParser.ListVersionsHandler(shouldSDKDecodeResponse);
      this.parseXmlInputStream(handler, this.sanitizeXmlDocument(handler, inputStream));
      return handler;
   }

   public XmlResponsesSaxParser.ListAllMyBucketsHandler parseListMyBucketsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListAllMyBucketsHandler handler = new XmlResponsesSaxParser.ListAllMyBucketsHandler();
      this.parseXmlInputStream(handler, this.sanitizeXmlDocument(handler, inputStream));
      return handler;
   }

   public XmlResponsesSaxParser.AccessControlListHandler parseAccessControlListResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.AccessControlListHandler handler = new XmlResponsesSaxParser.AccessControlListHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketLoggingConfigurationHandler parseLoggingStatusResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketLoggingConfigurationHandler handler = new XmlResponsesSaxParser.BucketLoggingConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketLifecycleConfigurationHandler parseBucketLifecycleConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketLifecycleConfigurationHandler handler = new XmlResponsesSaxParser.BucketLifecycleConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketCrossOriginConfigurationHandler parseBucketCrossOriginConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketCrossOriginConfigurationHandler handler = new XmlResponsesSaxParser.BucketCrossOriginConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public String parseBucketLocationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketLocationHandler handler = new XmlResponsesSaxParser.BucketLocationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler.getLocation();
   }

   public XmlResponsesSaxParser.BucketVersioningConfigurationHandler parseVersioningConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketVersioningConfigurationHandler handler = new XmlResponsesSaxParser.BucketVersioningConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketWebsiteConfigurationHandler parseWebsiteConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketWebsiteConfigurationHandler handler = new XmlResponsesSaxParser.BucketWebsiteConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketReplicationConfigurationHandler parseReplicationConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketReplicationConfigurationHandler handler = new XmlResponsesSaxParser.BucketReplicationConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketTaggingConfigurationHandler parseTaggingConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketTaggingConfigurationHandler handler = new XmlResponsesSaxParser.BucketTaggingConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.BucketAccelerateConfigurationHandler parseAccelerateConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.BucketAccelerateConfigurationHandler handler = new XmlResponsesSaxParser.BucketAccelerateConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.DeleteObjectsHandler parseDeletedObjectsResult(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.DeleteObjectsHandler handler = new XmlResponsesSaxParser.DeleteObjectsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.CopyObjectResultHandler parseCopyObjectResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.CopyObjectResultHandler handler = new XmlResponsesSaxParser.CopyObjectResultHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.CompleteMultipartUploadHandler parseCompleteMultipartUploadResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.CompleteMultipartUploadHandler handler = new XmlResponsesSaxParser.CompleteMultipartUploadHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.InitiateMultipartUploadHandler parseInitiateMultipartUploadResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.InitiateMultipartUploadHandler handler = new XmlResponsesSaxParser.InitiateMultipartUploadHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListMultipartUploadsHandler parseListMultipartUploadsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListMultipartUploadsHandler handler = new XmlResponsesSaxParser.ListMultipartUploadsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListPartsHandler parseListPartsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListPartsHandler handler = new XmlResponsesSaxParser.ListPartsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetObjectTaggingHandler parseObjectTaggingResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetObjectTaggingHandler handler = new XmlResponsesSaxParser.GetObjectTaggingHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetBucketMetricsConfigurationHandler parseGetBucketMetricsConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetBucketMetricsConfigurationHandler handler = new XmlResponsesSaxParser.GetBucketMetricsConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListBucketMetricsConfigurationsHandler parseListBucketMetricsConfigurationsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListBucketMetricsConfigurationsHandler handler = new XmlResponsesSaxParser.ListBucketMetricsConfigurationsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetBucketOwnershipControlsHandler parseGetBucketOwnershipControlsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetBucketOwnershipControlsHandler handler = new XmlResponsesSaxParser.GetBucketOwnershipControlsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetBucketAnalyticsConfigurationHandler parseGetBucketAnalyticsConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetBucketAnalyticsConfigurationHandler handler = new XmlResponsesSaxParser.GetBucketAnalyticsConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListBucketAnalyticsConfigurationHandler parseListBucketAnalyticsConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListBucketAnalyticsConfigurationHandler handler = new XmlResponsesSaxParser.ListBucketAnalyticsConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetBucketIntelligentTieringConfigurationHandler parseGetBucketIntelligentTieringConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetBucketIntelligentTieringConfigurationHandler handler = new XmlResponsesSaxParser.GetBucketIntelligentTieringConfigurationHandler(
         
      );
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListBucketIntelligentTieringConfigurationHandler parseListBucketIntelligentTieringConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListBucketIntelligentTieringConfigurationHandler handler = new XmlResponsesSaxParser.ListBucketIntelligentTieringConfigurationHandler(
         
      );
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetBucketInventoryConfigurationHandler parseGetBucketInventoryConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetBucketInventoryConfigurationHandler handler = new XmlResponsesSaxParser.GetBucketInventoryConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.ListBucketInventoryConfigurationsHandler parseBucketListInventoryConfigurationsResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.ListBucketInventoryConfigurationsHandler handler = new XmlResponsesSaxParser.ListBucketInventoryConfigurationsHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.RequestPaymentConfigurationHandler parseRequestPaymentConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.RequestPaymentConfigurationHandler handler = new XmlResponsesSaxParser.RequestPaymentConfigurationHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetObjectLegalHoldResponseHandler parseGetObjectLegalHoldResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetObjectLegalHoldResponseHandler handler = new XmlResponsesSaxParser.GetObjectLegalHoldResponseHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetObjectLockConfigurationResponseHandler parseGetObjectLockConfigurationResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetObjectLockConfigurationResponseHandler handler = new XmlResponsesSaxParser.GetObjectLockConfigurationResponseHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   public XmlResponsesSaxParser.GetObjectRetentionResponseHandler parseGetObjectRetentionResponse(InputStream inputStream) throws IOException {
      XmlResponsesSaxParser.GetObjectRetentionResponseHandler handler = new XmlResponsesSaxParser.GetObjectRetentionResponseHandler();
      this.parseXmlInputStream(handler, inputStream);
      return handler;
   }

   private static String findAttributeValue(String qnameToFind, Attributes attrs) {
      for(int i = 0; i < attrs.getLength(); ++i) {
         String qname = attrs.getQName(i);
         if (qname.trim().equalsIgnoreCase(qnameToFind.trim())) {
            return attrs.getValue(i);
         }
      }

      return null;
   }

   public static class AccessControlListHandler extends AbstractHandler {
      private final AccessControlList accessControlList = new AccessControlList();
      private Grantee currentGrantee = null;
      private Permission currentPermission = null;

      public AccessControlList getAccessControlList() {
         return this.accessControlList;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"AccessControlPolicy"})) {
            if (name.equals("Owner")) {
               this.accessControlList.setOwner(new Owner());
            }
         } else if (this.in(new String[]{"AccessControlPolicy", "AccessControlList", "Grant"}) && name.equals("Grantee")) {
            String type = XmlResponsesSaxParser.findAttributeValue("xsi:type", attrs);
            if ("AmazonCustomerByEmail".equals(type)) {
               this.currentGrantee = new EmailAddressGrantee(null);
            } else if ("CanonicalUser".equals(type)) {
               this.currentGrantee = new CanonicalGrantee(null);
            } else if ("Group".equals(type)) {
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"AccessControlPolicy", "Owner"})) {
            if (name.equals("ID")) {
               this.accessControlList.getOwner().setId(this.getText());
            } else if (name.equals("DisplayName")) {
               this.accessControlList.getOwner().setDisplayName(this.getText());
            }
         } else if (this.in(new String[]{"AccessControlPolicy", "AccessControlList"})) {
            if (name.equals("Grant")) {
               this.accessControlList.grantPermission(this.currentGrantee, this.currentPermission);
               this.currentGrantee = null;
               this.currentPermission = null;
            }
         } else if (this.in(new String[]{"AccessControlPolicy", "AccessControlList", "Grant"})) {
            if (name.equals("Permission")) {
               this.currentPermission = Permission.parsePermission(this.getText());
            }
         } else if (this.in(new String[]{"AccessControlPolicy", "AccessControlList", "Grant", "Grantee"})) {
            if (name.equals("ID")) {
               this.currentGrantee.setIdentifier(this.getText());
            } else if (name.equals("EmailAddress")) {
               this.currentGrantee.setIdentifier(this.getText());
            } else if (name.equals("URI")) {
               this.currentGrantee = GroupGrantee.parseGroupGrantee(this.getText());
            } else if (name.equals("DisplayName")) {
               ((CanonicalGrantee)this.currentGrantee).setDisplayName(this.getText());
            }
         }
      }
   }

   public static class BucketAccelerateConfigurationHandler extends AbstractHandler {
      private final BucketAccelerateConfiguration configuration = new BucketAccelerateConfiguration((String)null);

      public BucketAccelerateConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"AccelerateConfiguration"}) && name.equals("Status")) {
            this.configuration.setStatus(this.getText());
         }
      }
   }

   public static class BucketCrossOriginConfigurationHandler extends AbstractHandler {
      private final BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration(new ArrayList<>());
      private CORSRule currentRule;
      private List<CORSRule.AllowedMethods> allowedMethods = null;
      private List<String> allowedOrigins = null;
      private List<String> exposedHeaders = null;
      private List<String> allowedHeaders = null;

      public BucketCrossOriginConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"CORSConfiguration"})) {
            if (name.equals("CORSRule")) {
               this.currentRule = new CORSRule();
            }
         } else if (this.in(new String[]{"CORSConfiguration", "CORSRule"})) {
            if (name.equals("AllowedOrigin")) {
               if (this.allowedOrigins == null) {
                  this.allowedOrigins = new ArrayList<>();
               }
            } else if (name.equals("AllowedMethod")) {
               if (this.allowedMethods == null) {
                  this.allowedMethods = new ArrayList<>();
               }
            } else if (name.equals("ExposeHeader")) {
               if (this.exposedHeaders == null) {
                  this.exposedHeaders = new ArrayList<>();
               }
            } else if (name.equals("AllowedHeader") && this.allowedHeaders == null) {
               this.allowedHeaders = new LinkedList<>();
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"CORSConfiguration"})) {
            if (name.equals("CORSRule")) {
               this.currentRule.setAllowedHeaders(this.allowedHeaders);
               this.currentRule.setAllowedMethods(this.allowedMethods);
               this.currentRule.setAllowedOrigins(this.allowedOrigins);
               this.currentRule.setExposedHeaders(this.exposedHeaders);
               this.allowedHeaders = null;
               this.allowedMethods = null;
               this.allowedOrigins = null;
               this.exposedHeaders = null;
               this.configuration.getRules().add(this.currentRule);
               this.currentRule = null;
            }
         } else if (this.in(new String[]{"CORSConfiguration", "CORSRule"})) {
            if (name.equals("ID")) {
               this.currentRule.setId(this.getText());
            } else if (name.equals("AllowedOrigin")) {
               this.allowedOrigins.add(this.getText());
            } else if (name.equals("AllowedMethod")) {
               this.allowedMethods.add(CORSRule.AllowedMethods.fromValue(this.getText()));
            } else if (name.equals("MaxAgeSeconds")) {
               this.currentRule.setMaxAgeSeconds(Integer.parseInt(this.getText()));
            } else if (name.equals("ExposeHeader")) {
               this.exposedHeaders.add(this.getText());
            } else if (name.equals("AllowedHeader")) {
               this.allowedHeaders.add(this.getText());
            }
         }
      }
   }

   public static class BucketLifecycleConfigurationHandler extends AbstractHandler {
      private final BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration(new ArrayList<>());
      private BucketLifecycleConfiguration.Rule currentRule;
      private BucketLifecycleConfiguration.Transition currentTransition;
      private BucketLifecycleConfiguration.NoncurrentVersionTransition currentNcvTransition;
      private BucketLifecycleConfiguration.NoncurrentVersionExpiration ncvExpiration;
      private AbortIncompleteMultipartUpload abortIncompleteMultipartUpload;
      private LifecycleFilter currentFilter;
      private List<LifecycleFilterPredicate> andOperandsList;
      private String currentTagKey;
      private String currentTagValue;

      public BucketLifecycleConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"LifecycleConfiguration"})) {
            if (name.equals("Rule")) {
               this.currentRule = new BucketLifecycleConfiguration.Rule();
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule"})) {
            if (name.equals("Transition")) {
               this.currentTransition = new BucketLifecycleConfiguration.Transition();
            } else if (name.equals("NoncurrentVersionTransition")) {
               this.currentNcvTransition = new BucketLifecycleConfiguration.NoncurrentVersionTransition();
            } else if (name.equals("NoncurrentVersionExpiration")) {
               this.ncvExpiration = new BucketLifecycleConfiguration.NoncurrentVersionExpiration();
            } else if (name.equals("AbortIncompleteMultipartUpload")) {
               this.abortIncompleteMultipartUpload = new AbortIncompleteMultipartUpload();
            } else if (name.equals("Filter")) {
               this.currentFilter = new LifecycleFilter();
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"LifecycleConfiguration"})) {
            if (name.equals("Rule")) {
               this.configuration.getRules().add(this.currentRule);
               this.currentRule = null;
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule"})) {
            if (name.equals("ID")) {
               this.currentRule.setId(this.getText());
            } else if (name.equals("Prefix")) {
               this.currentRule.setPrefix(this.getText());
            } else if (name.equals("Status")) {
               this.currentRule.setStatus(this.getText());
            } else if (name.equals("Transition")) {
               this.currentRule.addTransition(this.currentTransition);
               this.currentTransition = null;
            } else if (name.equals("NoncurrentVersionTransition")) {
               this.currentRule.addNoncurrentVersionTransition(this.currentNcvTransition);
               this.currentNcvTransition = null;
            } else if (name.equals("NoncurrentVersionExpiration")) {
               this.currentRule.setNoncurrentVersionExpiration(this.ncvExpiration);
               this.ncvExpiration = null;
            } else if (name.equals("AbortIncompleteMultipartUpload")) {
               this.currentRule.setAbortIncompleteMultipartUpload(this.abortIncompleteMultipartUpload);
               this.abortIncompleteMultipartUpload = null;
            } else if (name.equals("Filter")) {
               this.currentRule.setFilter(this.currentFilter);
               this.currentFilter = null;
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Expiration"})) {
            if (name.equals("Date")) {
               this.currentRule.setExpirationDate(ServiceUtils.parseIso8601Date(this.getText()));
            } else if (name.equals("Days")) {
               this.currentRule.setExpirationInDays(Integer.parseInt(this.getText()));
            } else if (name.equals("ExpiredObjectDeleteMarker") && "true".equals(this.getText())) {
               this.currentRule.setExpiredObjectDeleteMarker(true);
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Transition"})) {
            if (name.equals("StorageClass")) {
               this.currentTransition.setStorageClass(this.getText());
            } else if (name.equals("Date")) {
               this.currentTransition.setDate(ServiceUtils.parseIso8601Date(this.getText()));
            } else if (name.equals("Days")) {
               this.currentTransition.setDays(Integer.parseInt(this.getText()));
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "NoncurrentVersionExpiration"})) {
            if (name.equals("NoncurrentDays")) {
               this.ncvExpiration.setDays(Integer.parseInt(this.getText()));
            } else if (name.equals("NewerNoncurrentVersions")) {
               this.ncvExpiration.setNewerNoncurrentVersions(Integer.parseInt(this.getText()));
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "NoncurrentVersionTransition"})) {
            if (name.equals("StorageClass")) {
               this.currentNcvTransition.setStorageClass(this.getText());
            } else if (name.equals("NoncurrentDays")) {
               this.currentNcvTransition.setDays(Integer.parseInt(this.getText()));
            } else if (name.equals("NewerNoncurrentVersions")) {
               this.currentNcvTransition.setNewerNoncurrentVersions(Integer.parseInt(this.getText()));
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "AbortIncompleteMultipartUpload"})) {
            if (name.equals("DaysAfterInitiation")) {
               this.abortIncompleteMultipartUpload.setDaysAfterInitiation(Integer.parseInt(this.getText()));
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new LifecyclePrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.currentFilter.setPredicate(new LifecycleTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("ObjectSizeGreaterThan")) {
               this.currentFilter.setPredicate(new LifecycleObjectSizeGreaterThanPredicate(Long.parseLong(this.getText())));
            } else if (name.equals("ObjectSizeLessThan")) {
               this.currentFilter.setPredicate(new LifecycleObjectSizeLessThanPredicate(Long.parseLong(this.getText())));
            } else if (name.equals("And")) {
               this.currentFilter.setPredicate(new LifecycleAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new LifecyclePrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new LifecycleTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("ObjectSizeGreaterThan")) {
               this.andOperandsList.add(new LifecycleObjectSizeGreaterThanPredicate(Long.parseLong(this.getText())));
            } else if (name.equals("ObjectSizeLessThan")) {
               this.andOperandsList.add(new LifecycleObjectSizeLessThanPredicate(Long.parseLong(this.getText())));
            }
         } else if (this.in(new String[]{"LifecycleConfiguration", "Rule", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         }
      }
   }

   public static class BucketLocationHandler extends AbstractHandler {
      private String location = null;

      public String getLocation() {
         return this.location;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.atTopLevel() && name.equals("LocationConstraint")) {
            String elementText = this.getText();
            if (elementText.length() == 0) {
               this.location = null;
            } else {
               this.location = elementText;
            }
         }
      }
   }

   public static class BucketLoggingConfigurationHandler extends AbstractHandler {
      private final BucketLoggingConfiguration bucketLoggingConfiguration = new BucketLoggingConfiguration();

      public BucketLoggingConfiguration getBucketLoggingConfiguration() {
         return this.bucketLoggingConfiguration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"BucketLoggingStatus", "LoggingEnabled"})) {
            if (name.equals("TargetBucket")) {
               this.bucketLoggingConfiguration.setDestinationBucketName(this.getText());
            } else if (name.equals("TargetPrefix")) {
               this.bucketLoggingConfiguration.setLogFilePrefix(this.getText());
            }
         }
      }
   }

   public static class BucketReplicationConfigurationHandler extends AbstractHandler {
      private final BucketReplicationConfiguration bucketReplicationConfiguration = new BucketReplicationConfiguration();
      private String currentRuleId;
      private ReplicationRule currentRule;
      private ReplicationFilter currentFilter;
      private List<ReplicationFilterPredicate> andOperandsList;
      private String currentTagKey;
      private String currentTagValue;
      private ExistingObjectReplication existingObjectReplication;
      private DeleteMarkerReplication deleteMarkerReplication;
      private ReplicationDestinationConfig destinationConfig;
      private AccessControlTranslation accessControlTranslation;
      private EncryptionConfiguration encryptionConfiguration;
      private ReplicationTime replicationTime;
      private Metrics metrics;
      private SourceSelectionCriteria sourceSelectionCriteria;
      private SseKmsEncryptedObjects sseKmsEncryptedObjects;
      private ReplicaModifications replicaModifications;
      private static final String REPLICATION_CONFIG = "ReplicationConfiguration";
      private static final String ROLE = "Role";
      private static final String RULE = "Rule";
      private static final String DESTINATION = "Destination";
      private static final String ID = "ID";
      private static final String PREFIX = "Prefix";
      private static final String FILTER = "Filter";
      private static final String AND = "And";
      private static final String TAG = "Tag";
      private static final String TAG_KEY = "Key";
      private static final String TAG_VALUE = "Value";
      private static final String EXISTING_OBJECT_REPLICATION = "ExistingObjectReplication";
      private static final String DELETE_MARKER_REPLICATION = "DeleteMarkerReplication";
      private static final String PRIORITY = "Priority";
      private static final String STATUS = "Status";
      private static final String BUCKET = "Bucket";
      private static final String STORAGECLASS = "StorageClass";
      private static final String ACCOUNT = "Account";
      private static final String ACCESS_CONTROL_TRANSLATION = "AccessControlTranslation";
      private static final String OWNER = "Owner";
      private static final String ENCRYPTION_CONFIGURATION = "EncryptionConfiguration";
      private static final String REPLICATION_TIME = "ReplicationTime";
      private static final String TIME = "Time";
      private static final String MINUTES = "Minutes";
      private static final String METRICS = "Metrics";
      private static final String EVENT_THRESHOLD = "EventThreshold";
      private static final String REPLICA_KMS_KEY_ID = "ReplicaKmsKeyID";
      private static final String SOURCE_SELECTION_CRITERIA = "SourceSelectionCriteria";
      private static final String SSE_KMS_ENCRYPTED_OBJECTS = "SseKmsEncryptedObjects";
      private static final String REPLICA_MODIFICATIONS = "ReplicaModifications";

      public BucketReplicationConfiguration getConfiguration() {
         return this.bucketReplicationConfiguration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ReplicationConfiguration"})) {
            if (name.equals("Rule")) {
               this.currentRule = new ReplicationRule();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule"})) {
            if (name.equals("Destination")) {
               this.destinationConfig = new ReplicationDestinationConfig();
            } else if (name.equals("SourceSelectionCriteria")) {
               this.sourceSelectionCriteria = new SourceSelectionCriteria();
            } else if (name.equals("ExistingObjectReplication")) {
               this.existingObjectReplication = new ExistingObjectReplication();
            } else if (name.equals("DeleteMarkerReplication")) {
               this.deleteMarkerReplication = new DeleteMarkerReplication();
            } else if (name.equals("Filter")) {
               this.currentFilter = new ReplicationFilter();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination"})) {
            if (name.equals("AccessControlTranslation")) {
               this.accessControlTranslation = new AccessControlTranslation();
            } else if (name.equals("EncryptionConfiguration")) {
               this.encryptionConfiguration = new EncryptionConfiguration();
            } else if (name.equals("ReplicationTime")) {
               this.replicationTime = new ReplicationTime();
            } else if (name.equals("Metrics")) {
               this.metrics = new Metrics();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "ReplicationTime"})) {
            if (name.equals("Time")) {
               this.replicationTime.setTime(new ReplicationTimeValue());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "Metrics"})) {
            if (name.equals("EventThreshold")) {
               this.metrics.setEventThreshold(new ReplicationTimeValue());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "SourceSelectionCriteria"})) {
            if (name.equals("SseKmsEncryptedObjects")) {
               this.sseKmsEncryptedObjects = new SseKmsEncryptedObjects();
            } else if (name.equals("ReplicaModifications")) {
               this.replicaModifications = new ReplicaModifications();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ReplicationConfiguration"})) {
            if (name.equals("Rule")) {
               this.bucketReplicationConfiguration.addRule(this.currentRuleId, this.currentRule);
               this.currentRule = null;
               this.currentRuleId = null;
               this.existingObjectReplication = null;
               this.deleteMarkerReplication = null;
               this.destinationConfig = null;
               this.sseKmsEncryptedObjects = null;
               this.accessControlTranslation = null;
               this.encryptionConfiguration = null;
               this.replicaModifications = null;
            } else if (name.equals("Role")) {
               this.bucketReplicationConfiguration.setRoleARN(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule"})) {
            if (name.equals("ID")) {
               this.currentRuleId = this.getText();
            } else if (name.equals("Prefix")) {
               this.currentRule.setPrefix(this.getText());
            } else if (name.equals("Priority")) {
               this.currentRule.setPriority(Integer.valueOf(this.getText()));
            } else if (name.equals("ExistingObjectReplication")) {
               this.currentRule.setExistingObjectReplication(this.existingObjectReplication);
            } else if (name.equals("DeleteMarkerReplication")) {
               this.currentRule.setDeleteMarkerReplication(this.deleteMarkerReplication);
            } else if (name.equals("SourceSelectionCriteria")) {
               this.currentRule.setSourceSelectionCriteria(this.sourceSelectionCriteria);
            } else if (name.equals("Filter")) {
               this.currentRule.setFilter(this.currentFilter);
               this.currentFilter = null;
            } else if (name.equals("Status")) {
               this.currentRule.setStatus(this.getText());
            } else if (name.equals("Destination")) {
               this.currentRule.setDestinationConfig(this.destinationConfig);
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new ReplicationPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.currentFilter.setPredicate(new ReplicationTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.currentFilter.setPredicate(new ReplicationAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new ReplicationPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new ReplicationTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "SourceSelectionCriteria"})) {
            if (name.equals("SseKmsEncryptedObjects")) {
               this.sourceSelectionCriteria.setSseKmsEncryptedObjects(this.sseKmsEncryptedObjects);
            } else if (name.equals("ReplicaModifications")) {
               this.sourceSelectionCriteria.setReplicaModifications(this.replicaModifications);
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "SourceSelectionCriteria", "SseKmsEncryptedObjects"})) {
            if (name.equals("Status")) {
               this.sseKmsEncryptedObjects.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "SourceSelectionCriteria", "ReplicaModifications"})) {
            if (name.equals("Status")) {
               this.replicaModifications.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "ExistingObjectReplication"})) {
            if (name.equals("Status")) {
               this.existingObjectReplication.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "DeleteMarkerReplication"})) {
            if (name.equals("Status")) {
               this.deleteMarkerReplication.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination"})) {
            if (name.equals("Bucket")) {
               this.destinationConfig.setBucketARN(this.getText());
            } else if (name.equals("StorageClass")) {
               this.destinationConfig.setStorageClass(this.getText());
            } else if (name.equals("Account")) {
               this.destinationConfig.setAccount(this.getText());
            } else if (name.equals("AccessControlTranslation")) {
               this.destinationConfig.setAccessControlTranslation(this.accessControlTranslation);
            } else if (name.equals("EncryptionConfiguration")) {
               this.destinationConfig.setEncryptionConfiguration(this.encryptionConfiguration);
            } else if (name.equals("ReplicationTime")) {
               this.destinationConfig.setReplicationTime(this.replicationTime);
            } else if (name.equals("Metrics")) {
               this.destinationConfig.setMetrics(this.metrics);
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "AccessControlTranslation"})) {
            if (name.equals("Owner")) {
               this.accessControlTranslation.setOwner(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "EncryptionConfiguration"})) {
            if (name.equals("ReplicaKmsKeyID")) {
               this.encryptionConfiguration.setReplicaKmsKeyID(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "ReplicationTime"})) {
            if (name.equals("Status")) {
               this.replicationTime.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "ReplicationTime", "Time"})) {
            if (name.equals("Minutes")) {
               this.replicationTime.getTime().setMinutes(Integer.parseInt(this.getText()));
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "Metrics"})) {
            if (name.equals("Status")) {
               this.metrics.setStatus(this.getText());
            }
         } else if (this.in(new String[]{"ReplicationConfiguration", "Rule", "Destination", "Metrics", "EventThreshold"}) && name.equals("Minutes")) {
            this.metrics.getEventThreshold().setMinutes(Integer.parseInt(this.getText()));
         }
      }
   }

   public static class BucketTaggingConfigurationHandler extends AbstractHandler {
      private final BucketTaggingConfiguration configuration = new BucketTaggingConfiguration();
      private Map<String, String> currentTagSet;
      private String currentTagKey;
      private String currentTagValue;

      public BucketTaggingConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"Tagging"}) && name.equals("TagSet")) {
            this.currentTagSet = new LinkedHashMap<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"Tagging"})) {
            if (name.equals("TagSet")) {
               this.configuration.getAllTagSets().add(new TagSet(this.currentTagSet));
               this.currentTagSet = null;
            }
         } else if (this.in(new String[]{"Tagging", "TagSet"})) {
            if (name.equals("Tag")) {
               if (this.currentTagKey != null && this.currentTagValue != null) {
                  this.currentTagSet.put(this.currentTagKey, this.currentTagValue);
               }

               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"Tagging", "TagSet", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         }
      }
   }

   public static class BucketVersioningConfigurationHandler extends AbstractHandler {
      private final BucketVersioningConfiguration configuration = new BucketVersioningConfiguration();

      public BucketVersioningConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"VersioningConfiguration"})) {
            if (name.equals("Status")) {
               this.configuration.setStatus(this.getText());
            } else if (name.equals("MfaDelete")) {
               String mfaDeleteStatus = this.getText();
               if (mfaDeleteStatus.equals("Disabled")) {
                  this.configuration.setMfaDeleteEnabled(false);
               } else if (mfaDeleteStatus.equals("Enabled")) {
                  this.configuration.setMfaDeleteEnabled(true);
               } else {
                  this.configuration.setMfaDeleteEnabled(null);
               }
            }
         }
      }
   }

   public static class BucketWebsiteConfigurationHandler extends AbstractHandler {
      private final BucketWebsiteConfiguration configuration = new BucketWebsiteConfiguration(null);
      private RoutingRuleCondition currentCondition = null;
      private RedirectRule currentRedirectRule = null;
      private RoutingRule currentRoutingRule = null;

      public BucketWebsiteConfiguration getConfiguration() {
         return this.configuration;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"WebsiteConfiguration"})) {
            if (name.equals("RedirectAllRequestsTo")) {
               this.currentRedirectRule = new RedirectRule();
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RoutingRules"})) {
            if (name.equals("RoutingRule")) {
               this.currentRoutingRule = new RoutingRule();
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RoutingRules", "RoutingRule"})) {
            if (name.equals("Condition")) {
               this.currentCondition = new RoutingRuleCondition();
            } else if (name.equals("Redirect")) {
               this.currentRedirectRule = new RedirectRule();
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"WebsiteConfiguration"})) {
            if (name.equals("RedirectAllRequestsTo")) {
               this.configuration.setRedirectAllRequestsTo(this.currentRedirectRule);
               this.currentRedirectRule = null;
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "IndexDocument"})) {
            if (name.equals("Suffix")) {
               this.configuration.setIndexDocumentSuffix(this.getText());
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "ErrorDocument"})) {
            if (name.equals("Key")) {
               this.configuration.setErrorDocument(this.getText());
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RoutingRules"})) {
            if (name.equals("RoutingRule")) {
               this.configuration.getRoutingRules().add(this.currentRoutingRule);
               this.currentRoutingRule = null;
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RoutingRules", "RoutingRule"})) {
            if (name.equals("Condition")) {
               this.currentRoutingRule.setCondition(this.currentCondition);
               this.currentCondition = null;
            } else if (name.equals("Redirect")) {
               this.currentRoutingRule.setRedirect(this.currentRedirectRule);
               this.currentRedirectRule = null;
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RoutingRules", "RoutingRule", "Condition"})) {
            if (name.equals("KeyPrefixEquals")) {
               this.currentCondition.setKeyPrefixEquals(this.getText());
            } else if (name.equals("HttpErrorCodeReturnedEquals")) {
               this.currentCondition.setHttpErrorCodeReturnedEquals(this.getText());
            }
         } else if (this.in(new String[]{"WebsiteConfiguration", "RedirectAllRequestsTo"})
            || this.in(new String[]{"WebsiteConfiguration", "RoutingRules", "RoutingRule", "Redirect"})) {
            if (name.equals("Protocol")) {
               this.currentRedirectRule.setProtocol(this.getText());
            } else if (name.equals("HostName")) {
               this.currentRedirectRule.setHostName(this.getText());
            } else if (name.equals("ReplaceKeyPrefixWith")) {
               this.currentRedirectRule.setReplaceKeyPrefixWith(this.getText());
            } else if (name.equals("ReplaceKeyWith")) {
               this.currentRedirectRule.setReplaceKeyWith(this.getText());
            } else if (name.equals("HttpRedirectCode")) {
               this.currentRedirectRule.setHttpRedirectCode(this.getText());
            }
         }
      }
   }

   public static class CompleteMultipartUploadHandler extends AbstractSSEHandler implements ObjectExpirationResult, S3VersionResult, S3RequesterChargedResult {
      private CompleteMultipartUploadResult result;
      private AmazonS3Exception ase;
      private String hostId;
      private String requestId;
      private String errorCode;

      @Override
      protected ServerSideEncryptionResult sseResult() {
         return this.result;
      }

      @Override
      public Date getExpirationTime() {
         return this.result == null ? null : this.result.getExpirationTime();
      }

      @Override
      public void setExpirationTime(Date expirationTime) {
         if (this.result != null) {
            this.result.setExpirationTime(expirationTime);
         }
      }

      @Override
      public String getExpirationTimeRuleId() {
         return this.result == null ? null : this.result.getExpirationTimeRuleId();
      }

      @Override
      public void setExpirationTimeRuleId(String expirationTimeRuleId) {
         if (this.result != null) {
            this.result.setExpirationTimeRuleId(expirationTimeRuleId);
         }
      }

      @Override
      public void setVersionId(String versionId) {
         if (this.result != null) {
            this.result.setVersionId(versionId);
         }
      }

      @Override
      public String getVersionId() {
         return this.result == null ? null : this.result.getVersionId();
      }

      @Override
      public boolean isRequesterCharged() {
         return this.result == null ? false : this.result.isRequesterCharged();
      }

      @Override
      public void setRequesterCharged(boolean isRequesterCharged) {
         if (this.result != null) {
            this.result.setRequesterCharged(isRequesterCharged);
         }
      }

      public CompleteMultipartUploadResult getCompleteMultipartUploadResult() {
         return this.result;
      }

      public AmazonS3Exception getAmazonS3Exception() {
         return this.ase;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.atTopLevel() && name.equals("CompleteMultipartUploadResult")) {
            this.result = new CompleteMultipartUploadResult();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.atTopLevel()) {
            if (name.equals("Error") && this.ase != null) {
               this.ase.setErrorCode(this.errorCode);
               this.ase.setRequestId(this.requestId);
               this.ase.setExtendedRequestId(this.hostId);
            }
         } else if (this.in(new String[]{"CompleteMultipartUploadResult"})) {
            if (name.equals("Location")) {
               this.result.setLocation(this.getText());
            } else if (name.equals("Bucket")) {
               this.result.setBucketName(this.getText());
            } else if (name.equals("Key")) {
               this.result.setKey(this.getText());
            } else if (name.equals("ETag")) {
               this.result.setETag(ServiceUtils.removeQuotes(this.getText()));
            }
         } else if (this.in(new String[]{"Error"})) {
            if (name.equals("Code")) {
               this.errorCode = this.getText();
            } else if (name.equals("Message")) {
               this.ase = new AmazonS3Exception(this.getText());
            } else if (name.equals("RequestId")) {
               this.requestId = this.getText();
            } else if (name.equals("HostId")) {
               this.hostId = this.getText();
            }
         }
      }
   }

   public static class CopyObjectResultHandler extends AbstractSSEHandler implements ObjectExpirationResult, S3RequesterChargedResult, S3VersionResult {
      private final CopyObjectResult result = new CopyObjectResult();
      private String errorCode = null;
      private String errorMessage = null;
      private String errorRequestId = null;
      private String errorHostId = null;
      private boolean receivedErrorResponse = false;

      @Override
      protected ServerSideEncryptionResult sseResult() {
         return this.result;
      }

      public Date getLastModified() {
         return this.result.getLastModifiedDate();
      }

      @Override
      public String getVersionId() {
         return this.result.getVersionId();
      }

      @Override
      public void setVersionId(String versionId) {
         this.result.setVersionId(versionId);
      }

      @Override
      public Date getExpirationTime() {
         return this.result.getExpirationTime();
      }

      @Override
      public void setExpirationTime(Date expirationTime) {
         this.result.setExpirationTime(expirationTime);
      }

      @Override
      public String getExpirationTimeRuleId() {
         return this.result.getExpirationTimeRuleId();
      }

      @Override
      public void setExpirationTimeRuleId(String expirationTimeRuleId) {
         this.result.setExpirationTimeRuleId(expirationTimeRuleId);
      }

      public String getETag() {
         return this.result.getETag();
      }

      public String getErrorCode() {
         return this.errorCode;
      }

      public String getErrorHostId() {
         return this.errorHostId;
      }

      public String getErrorMessage() {
         return this.errorMessage;
      }

      public String getErrorRequestId() {
         return this.errorRequestId;
      }

      public boolean isErrorResponse() {
         return this.receivedErrorResponse;
      }

      @Override
      public boolean isRequesterCharged() {
         return this.result.isRequesterCharged();
      }

      @Override
      public void setRequesterCharged(boolean isRequesterCharged) {
         this.result.setRequesterCharged(isRequesterCharged);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.atTopLevel()) {
            if (name.equals("CopyObjectResult") || name.equals("CopyPartResult")) {
               this.receivedErrorResponse = false;
            } else if (name.equals("Error")) {
               this.receivedErrorResponse = true;
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (!this.in(new String[]{"CopyObjectResult"}) && !this.in(new String[]{"CopyPartResult"})) {
            if (this.in(new String[]{"Error"})) {
               if (name.equals("Code")) {
                  this.errorCode = this.getText();
               } else if (name.equals("Message")) {
                  this.errorMessage = this.getText();
               } else if (name.equals("RequestId")) {
                  this.errorRequestId = this.getText();
               } else if (name.equals("HostId")) {
                  this.errorHostId = this.getText();
               }
            }
         } else if (name.equals("LastModified")) {
            this.result.setLastModifiedDate(ServiceUtils.parseIso8601Date(this.getText()));
         } else if (name.equals("ETag")) {
            this.result.setETag(ServiceUtils.removeQuotes(this.getText()));
         }
      }
   }

   public static class DeleteObjectsHandler extends AbstractHandler {
      private final DeleteObjectsResponse response = new DeleteObjectsResponse();
      private DeleteObjectsResult.DeletedObject currentDeletedObject = null;
      private MultiObjectDeleteException.DeleteError currentError = null;

      public DeleteObjectsResponse getDeleteObjectResult() {
         return this.response;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"DeleteResult"})) {
            if (name.equals("Deleted")) {
               this.currentDeletedObject = new DeleteObjectsResult.DeletedObject();
            } else if (name.equals("Error")) {
               this.currentError = new MultiObjectDeleteException.DeleteError();
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"DeleteResult"})) {
            if (name.equals("Deleted")) {
               this.response.getDeletedObjects().add(this.currentDeletedObject);
               this.currentDeletedObject = null;
            } else if (name.equals("Error")) {
               this.response.getErrors().add(this.currentError);
               this.currentError = null;
            }
         } else if (this.in(new String[]{"DeleteResult", "Deleted"})) {
            if (name.equals("Key")) {
               this.currentDeletedObject.setKey(this.getText());
            } else if (name.equals("VersionId")) {
               this.currentDeletedObject.setVersionId(this.getText());
            } else if (name.equals("DeleteMarker")) {
               this.currentDeletedObject.setDeleteMarker(this.getText().equals("true"));
            } else if (name.equals("DeleteMarkerVersionId")) {
               this.currentDeletedObject.setDeleteMarkerVersionId(this.getText());
            }
         } else if (this.in(new String[]{"DeleteResult", "Error"})) {
            if (name.equals("Key")) {
               this.currentError.setKey(this.getText());
            } else if (name.equals("VersionId")) {
               this.currentError.setVersionId(this.getText());
            } else if (name.equals("Code")) {
               this.currentError.setCode(this.getText());
            } else if (name.equals("Message")) {
               this.currentError.setMessage(this.getText());
            }
         }
      }
   }

   public static class GetBucketAnalyticsConfigurationHandler extends AbstractHandler {
      private final AnalyticsConfiguration configuration = new AnalyticsConfiguration();
      private AnalyticsFilter filter;
      private List<AnalyticsFilterPredicate> andOperandsList;
      private StorageClassAnalysis storageClassAnalysis;
      private StorageClassAnalysisDataExport dataExport;
      private AnalyticsExportDestination destination;
      private AnalyticsS3BucketDestination s3BucketDestination;
      private String currentTagKey;
      private String currentTagValue;

      public GetBucketAnalyticsConfigurationResult getResult() {
         return new GetBucketAnalyticsConfigurationResult().withAnalyticsConfiguration(this.configuration);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"AnalyticsConfiguration"})) {
            if (name.equals("Filter")) {
               this.filter = new AnalyticsFilter();
            } else if (name.equals("StorageClassAnalysis")) {
               this.storageClassAnalysis = new StorageClassAnalysis();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "Filter"})) {
            if (name.equals("And")) {
               this.andOperandsList = new ArrayList<>();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis"})) {
            if (name.equals("DataExport")) {
               this.dataExport = new StorageClassAnalysisDataExport();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis", "DataExport"})) {
            if (name.equals("Destination")) {
               this.destination = new AnalyticsExportDestination();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination"}) && name.equals("S3BucketDestination")) {
            this.s3BucketDestination = new AnalyticsS3BucketDestination();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"AnalyticsConfiguration"})) {
            if (name.equals("Id")) {
               this.configuration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.configuration.setFilter(this.filter);
            } else if (name.equals("StorageClassAnalysis")) {
               this.configuration.setStorageClassAnalysis(this.storageClassAnalysis);
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.filter.setPredicate(new AnalyticsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.filter.setPredicate(new AnalyticsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.filter.setPredicate(new AnalyticsAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new AnalyticsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new AnalyticsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis"})) {
            if (name.equals("DataExport")) {
               this.storageClassAnalysis.setDataExport(this.dataExport);
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis", "DataExport"})) {
            if (name.equals("OutputSchemaVersion")) {
               this.dataExport.setOutputSchemaVersion(this.getText());
            } else if (name.equals("Destination")) {
               this.dataExport.setDestination(this.destination);
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination"})) {
            if (name.equals("S3BucketDestination")) {
               this.destination.setS3BucketDestination(this.s3BucketDestination);
            }
         } else if (this.in(new String[]{"AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination", "S3BucketDestination"})) {
            if (name.equals("Format")) {
               this.s3BucketDestination.setFormat(this.getText());
            } else if (name.equals("BucketAccountId")) {
               this.s3BucketDestination.setBucketAccountId(this.getText());
            } else if (name.equals("Bucket")) {
               this.s3BucketDestination.setBucketArn(this.getText());
            } else if (name.equals("Prefix")) {
               this.s3BucketDestination.setPrefix(this.getText());
            }
         }
      }
   }

   public static class GetBucketIntelligentTieringConfigurationHandler extends AbstractHandler {
      private final IntelligentTieringConfiguration configuration = new IntelligentTieringConfiguration();
      private IntelligentTieringFilter filter;
      private List<IntelligentTieringFilterPredicate> andOperandsList;
      private Tiering currentTiering;
      private String currentTagKey;
      private String currentTagValue;

      public GetBucketIntelligentTieringConfigurationResult getResult() {
         return new GetBucketIntelligentTieringConfigurationResult().withIntelligentTieringConfiguration(this.configuration);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"IntelligentTieringConfiguration"})) {
            if (name.equals("Filter")) {
               this.filter = new IntelligentTieringFilter();
            } else if (name.equals("Tiering")) {
               this.currentTiering = new Tiering();
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"IntelligentTieringConfiguration"})) {
            if (name.equals("Id")) {
               this.configuration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.configuration.setFilter(this.filter);
            } else if (name.equals("Status")) {
               this.configuration.setStatus(IntelligentTieringStatus.fromValue(this.getText()));
            } else if (name.equals("Tiering")) {
               if (this.configuration.getTierings() == null) {
                  this.configuration.setTierings(new ArrayList<>());
               }

               this.configuration.getTierings().add(this.currentTiering);
               this.currentTiering = null;
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.filter.setPredicate(new IntelligentTieringPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.filter.setPredicate(new IntelligentTieringTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.filter.setPredicate(new IntelligentTieringAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new IntelligentTieringPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new IntelligentTieringTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"IntelligentTieringConfiguration", "Tiering"})) {
            if (name.equals("AccessTier")) {
               this.currentTiering.setAccessTier(IntelligentTieringAccessTier.fromValue(this.getText()));
            } else if (name.equals("Days")) {
               this.currentTiering.setDays(Integer.parseInt(this.getText()));
            }
         }
      }
   }

   public static class GetBucketInventoryConfigurationHandler extends AbstractHandler {
      public static final String SSE_S3 = "SSE-S3";
      public static final String SSE_KMS = "SSE-KMS";
      private final GetBucketInventoryConfigurationResult result = new GetBucketInventoryConfigurationResult();
      private final InventoryConfiguration configuration = new InventoryConfiguration();
      private List<String> optionalFields;
      private InventoryDestination inventoryDestination;
      private InventoryFilter filter;
      private InventoryS3BucketDestination s3BucketDestination;
      private InventorySchedule inventorySchedule;

      public GetBucketInventoryConfigurationResult getResult() {
         return this.result.withInventoryConfiguration(this.configuration);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"InventoryConfiguration"})) {
            if (name.equals("Destination")) {
               this.inventoryDestination = new InventoryDestination();
            } else if (name.equals("Filter")) {
               this.filter = new InventoryFilter();
            } else if (name.equals("Schedule")) {
               this.inventorySchedule = new InventorySchedule();
            } else if (name.equals("OptionalFields")) {
               this.optionalFields = new ArrayList<>();
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Destination"}) && name.equals("S3BucketDestination")) {
            this.s3BucketDestination = new InventoryS3BucketDestination();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"InventoryConfiguration"})) {
            if (name.equals("Id")) {
               this.configuration.setId(this.getText());
            } else if (name.equals("Destination")) {
               this.configuration.setDestination(this.inventoryDestination);
               this.inventoryDestination = null;
            } else if (name.equals("IsEnabled")) {
               this.configuration.setEnabled("true".equals(this.getText()));
            } else if (name.equals("Filter")) {
               this.configuration.setInventoryFilter(this.filter);
               this.filter = null;
            } else if (name.equals("IncludedObjectVersions")) {
               this.configuration.setIncludedObjectVersions(this.getText());
            } else if (name.equals("Schedule")) {
               this.configuration.setSchedule(this.inventorySchedule);
               this.inventorySchedule = null;
            } else if (name.equals("OptionalFields")) {
               this.configuration.setOptionalFields(this.optionalFields);
               this.optionalFields = null;
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Destination"})) {
            if (name.equals("S3BucketDestination")) {
               this.inventoryDestination.setS3BucketDestination(this.s3BucketDestination);
               this.s3BucketDestination = null;
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Destination", "S3BucketDestination"})) {
            if (name.equals("AccountId")) {
               this.s3BucketDestination.setAccountId(this.getText());
            } else if (name.equals("Bucket")) {
               this.s3BucketDestination.setBucketArn(this.getText());
            } else if (name.equals("Format")) {
               this.s3BucketDestination.setFormat(this.getText());
            } else if (name.equals("Prefix")) {
               this.s3BucketDestination.setPrefix(this.getText());
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Destination", "S3BucketDestination", "Encryption"})) {
            if (name.equals("SSE-S3")) {
               this.s3BucketDestination.setEncryption(new ServerSideEncryptionS3());
            } else if (name.equals("SSE-KMS")) {
               ServerSideEncryptionKMS kmsEncryption = new ServerSideEncryptionKMS().withKeyId(this.getText());
               this.s3BucketDestination.setEncryption(kmsEncryption);
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.filter.setPredicate(new InventoryPrefixPredicate(this.getText()));
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "Schedule"})) {
            if (name.equals("Frequency")) {
               this.inventorySchedule.setFrequency(this.getText());
            }
         } else if (this.in(new String[]{"InventoryConfiguration", "OptionalFields"}) && name.equals("Field")) {
            this.optionalFields.add(this.getText());
         }
      }
   }

   public static class GetBucketMetricsConfigurationHandler extends AbstractHandler {
      private final MetricsConfiguration configuration = new MetricsConfiguration();
      private MetricsFilter filter;
      private List<MetricsFilterPredicate> andOperandsList;
      private String currentTagKey;
      private String currentTagValue;

      public GetBucketMetricsConfigurationResult getResult() {
         return new GetBucketMetricsConfigurationResult().withMetricsConfiguration(this.configuration);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"MetricsConfiguration"})) {
            if (name.equals("Filter")) {
               this.filter = new MetricsFilter();
            }
         } else if (this.in(new String[]{"MetricsConfiguration", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"MetricsConfiguration"})) {
            if (name.equals("Id")) {
               this.configuration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.configuration.setFilter(this.filter);
               this.filter = null;
            }
         } else if (this.in(new String[]{"MetricsConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.filter.setPredicate(new MetricsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.filter.setPredicate(new MetricsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("AccessPointArn")) {
               this.filter.setPredicate(new MetricsAccessPointArnPredicate(this.getText()));
            } else if (name.equals("And")) {
               this.filter.setPredicate(new MetricsAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"MetricsConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"MetricsConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new MetricsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new MetricsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("AccessPointArn")) {
               this.andOperandsList.add(new MetricsAccessPointArnPredicate(this.getText()));
            }
         } else if (this.in(new String[]{"MetricsConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         }
      }
   }

   public static class GetBucketOwnershipControlsHandler extends AbstractHandler {
      private List<OwnershipControlsRule> rulesList;

      public GetBucketOwnershipControlsResult getResult() {
         OwnershipControls ownershipControls = new OwnershipControls().withRules(this.rulesList);
         return new GetBucketOwnershipControlsResult().withOwnershipControls(ownershipControls);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"OwnershipControls"}) && name.equals("Rule") && this.rulesList == null) {
            this.rulesList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"OwnershipControls", "Rule"}) && name.equals("ObjectOwnership")) {
            this.rulesList.add(new OwnershipControlsRule().withOwnership(this.getText()));
         }
      }
   }

   public static class GetObjectLegalHoldResponseHandler extends AbstractHandler {
      private GetObjectLegalHoldResult getObjectLegalHoldResult = new GetObjectLegalHoldResult();
      private ObjectLockLegalHold legalHold = new ObjectLockLegalHold();

      public GetObjectLegalHoldResult getResult() {
         return this.getObjectLegalHoldResult.withLegalHold(this.legalHold);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"LegalHold"}) && "Status".equals(name)) {
            this.legalHold.setStatus(this.getText());
         }
      }
   }

   public static class GetObjectLockConfigurationResponseHandler extends AbstractHandler {
      private GetObjectLockConfigurationResult result = new GetObjectLockConfigurationResult();
      private ObjectLockConfiguration objectLockConfiguration = new ObjectLockConfiguration();
      private ObjectLockRule rule;
      private DefaultRetention defaultRetention;

      public GetObjectLockConfigurationResult getResult() {
         return this.result.withObjectLockConfiguration(this.objectLockConfiguration);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ObjectLockConfiguration"})) {
            if ("Rule".equals(name)) {
               this.rule = new ObjectLockRule();
            }
         } else if (this.in(new String[]{"ObjectLockConfiguration", "Rule"}) && "DefaultRetention".equals(name)) {
            this.defaultRetention = new DefaultRetention();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ObjectLockConfiguration"})) {
            if ("ObjectLockEnabled".equals(name)) {
               this.objectLockConfiguration.setObjectLockEnabled(this.getText());
            } else if ("Rule".equals(name)) {
               this.objectLockConfiguration.setRule(this.rule);
            }
         } else if (this.in(new String[]{"ObjectLockConfiguration", "Rule"})) {
            if ("DefaultRetention".equals(name)) {
               this.rule.setDefaultRetention(this.defaultRetention);
            }
         } else if (this.in(new String[]{"ObjectLockConfiguration", "Rule", "DefaultRetention"})) {
            if ("Mode".equals(name)) {
               this.defaultRetention.setMode(this.getText());
            } else if ("Days".equals(name)) {
               this.defaultRetention.setDays(Integer.parseInt(this.getText()));
            } else if ("Years".equals(name)) {
               this.defaultRetention.setYears(Integer.parseInt(this.getText()));
            }
         }
      }
   }

   public static class GetObjectRetentionResponseHandler extends AbstractHandler {
      private GetObjectRetentionResult result = new GetObjectRetentionResult();
      private ObjectLockRetention retention = new ObjectLockRetention();

      public GetObjectRetentionResult getResult() {
         return this.result.withRetention(this.retention);
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"Retention"})) {
            if ("Mode".equals(name)) {
               this.retention.setMode(this.getText());
            } else if ("RetainUntilDate".equals(name)) {
               this.retention.setRetainUntilDate(ServiceUtils.parseIso8601Date(this.getText()));
            }
         }
      }
   }

   public static class GetObjectTaggingHandler extends AbstractHandler {
      private GetObjectTaggingResult getObjectTaggingResult;
      private List<Tag> tagSet;
      private String currentTagValue;
      private String currentTagKey;

      public GetObjectTaggingResult getResult() {
         return this.getObjectTaggingResult;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"Tagging"}) && name.equals("TagSet")) {
            this.tagSet = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"Tagging"}) && name.equals("TagSet")) {
            this.getObjectTaggingResult = new GetObjectTaggingResult(this.tagSet);
            this.tagSet = null;
         }

         if (this.in(new String[]{"Tagging", "TagSet"})) {
            if (name.equals("Tag")) {
               this.tagSet.add(new Tag(this.currentTagKey, this.currentTagValue));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"Tagging", "TagSet", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         }
      }
   }

   public static class InitiateMultipartUploadHandler extends AbstractHandler {
      private final InitiateMultipartUploadResult result = new InitiateMultipartUploadResult();

      public InitiateMultipartUploadResult getInitiateMultipartUploadResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"InitiateMultipartUploadResult"})) {
            if (name.equals("Bucket")) {
               this.result.setBucketName(this.getText());
            } else if (name.equals("Key")) {
               this.result.setKey(this.getText());
            } else if (name.equals("UploadId")) {
               this.result.setUploadId(this.getText());
            }
         }
      }
   }

   public static class ListAllMyBucketsHandler extends AbstractHandler {
      private final List<Bucket> buckets = new ArrayList<>();
      private Owner bucketsOwner = null;
      private Bucket currentBucket = null;

      public List<Bucket> getBuckets() {
         return this.buckets;
      }

      public Owner getOwner() {
         return this.bucketsOwner;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListAllMyBucketsResult"})) {
            if (name.equals("Owner")) {
               this.bucketsOwner = new Owner();
            }
         } else if (this.in(new String[]{"ListAllMyBucketsResult", "Buckets"}) && name.equals("Bucket")) {
            this.currentBucket = new Bucket();
            this.currentBucket.setOwner(this.bucketsOwner);
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListAllMyBucketsResult", "Owner"})) {
            if (name.equals("ID")) {
               this.bucketsOwner.setId(this.getText());
            } else if (name.equals("DisplayName")) {
               this.bucketsOwner.setDisplayName(this.getText());
            }
         } else if (this.in(new String[]{"ListAllMyBucketsResult", "Buckets"})) {
            if (name.equals("Bucket")) {
               this.buckets.add(this.currentBucket);
               this.currentBucket = null;
            }
         } else if (this.in(new String[]{"ListAllMyBucketsResult", "Buckets", "Bucket"})) {
            if (name.equals("Name")) {
               this.currentBucket.setName(this.getText());
            } else if (name.equals("CreationDate")) {
               Date creationDate = DateUtils.parseISO8601Date(this.getText());
               this.currentBucket.setCreationDate(creationDate);
            }
         }
      }
   }

   public static class ListBucketAnalyticsConfigurationHandler extends AbstractHandler {
      private final ListBucketAnalyticsConfigurationsResult result = new ListBucketAnalyticsConfigurationsResult();
      private AnalyticsConfiguration currentConfiguration;
      private AnalyticsFilter currentFilter;
      private List<AnalyticsFilterPredicate> andOperandsList;
      private StorageClassAnalysis storageClassAnalysis;
      private StorageClassAnalysisDataExport dataExport;
      private AnalyticsExportDestination destination;
      private AnalyticsS3BucketDestination s3BucketDestination;
      private String currentTagKey;
      private String currentTagValue;

      public ListBucketAnalyticsConfigurationsResult getResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult"})) {
            if (name.equals("AnalyticsConfiguration")) {
               this.currentConfiguration = new AnalyticsConfiguration();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration"})) {
            if (name.equals("Filter")) {
               this.currentFilter = new AnalyticsFilter();
            } else if (name.equals("StorageClassAnalysis")) {
               this.storageClassAnalysis = new StorageClassAnalysis();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "Filter"})) {
            if (name.equals("And")) {
               this.andOperandsList = new ArrayList<>();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis"})) {
            if (name.equals("DataExport")) {
               this.dataExport = new StorageClassAnalysisDataExport();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis", "DataExport"})) {
            if (name.equals("Destination")) {
               this.destination = new AnalyticsExportDestination();
            }
         } else if (this.in(
               new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination"}
            )
            && name.equals("S3BucketDestination")) {
            this.s3BucketDestination = new AnalyticsS3BucketDestination();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult"})) {
            if (name.equals("AnalyticsConfiguration")) {
               if (this.result.getAnalyticsConfigurationList() == null) {
                  this.result.setAnalyticsConfigurationList(new ArrayList<>());
               }

               this.result.getAnalyticsConfigurationList().add(this.currentConfiguration);
               this.currentConfiguration = null;
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated("true".equals(this.getText()));
            } else if (name.equals("ContinuationToken")) {
               this.result.setContinuationToken(this.getText());
            } else if (name.equals("NextContinuationToken")) {
               this.result.setNextContinuationToken(this.getText());
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration"})) {
            if (name.equals("Id")) {
               this.currentConfiguration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.currentConfiguration.setFilter(this.currentFilter);
            } else if (name.equals("StorageClassAnalysis")) {
               this.currentConfiguration.setStorageClassAnalysis(this.storageClassAnalysis);
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new AnalyticsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.currentFilter.setPredicate(new AnalyticsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.currentFilter.setPredicate(new AnalyticsAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new AnalyticsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new AnalyticsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis"})) {
            if (name.equals("DataExport")) {
               this.storageClassAnalysis.setDataExport(this.dataExport);
            }
         } else if (this.in(new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis", "DataExport"})) {
            if (name.equals("OutputSchemaVersion")) {
               this.dataExport.setOutputSchemaVersion(this.getText());
            } else if (name.equals("Destination")) {
               this.dataExport.setDestination(this.destination);
            }
         } else if (this.in(
            new String[]{"ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination"}
         )) {
            if (name.equals("S3BucketDestination")) {
               this.destination.setS3BucketDestination(this.s3BucketDestination);
            }
         } else if (this.in(
            new String[]{
               "ListBucketAnalyticsConfigurationsResult", "AnalyticsConfiguration", "StorageClassAnalysis", "DataExport", "Destination", "S3BucketDestination"
            }
         )) {
            if (name.equals("Format")) {
               this.s3BucketDestination.setFormat(this.getText());
            } else if (name.equals("BucketAccountId")) {
               this.s3BucketDestination.setBucketAccountId(this.getText());
            } else if (name.equals("Bucket")) {
               this.s3BucketDestination.setBucketArn(this.getText());
            } else if (name.equals("Prefix")) {
               this.s3BucketDestination.setPrefix(this.getText());
            }
         }
      }
   }

   public static class ListBucketHandler extends AbstractHandler {
      private final ObjectListing objectListing = new ObjectListing();
      private final boolean shouldSDKDecodeResponse;
      private S3ObjectSummary currentObject = null;
      private Owner currentOwner = null;
      private String lastKey = null;

      public ListBucketHandler(boolean shouldSDKDecodeResponse) {
         this.shouldSDKDecodeResponse = shouldSDKDecodeResponse;
      }

      public ObjectListing getObjectListing() {
         return this.objectListing;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListBucketResult"})) {
            if (name.equals("Contents")) {
               this.currentObject = new S3ObjectSummary();
               this.currentObject.setBucketName(this.objectListing.getBucketName());
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents"}) && name.equals("Owner")) {
            this.currentOwner = new Owner();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.atTopLevel()) {
            if (name.equals("ListBucketResult") && this.objectListing.isTruncated() && this.objectListing.getNextMarker() == null) {
               String nextMarker = null;
               if (!this.objectListing.getObjectSummaries().isEmpty()) {
                  nextMarker = this.objectListing.getObjectSummaries().get(this.objectListing.getObjectSummaries().size() - 1).getKey();
               } else if (!this.objectListing.getCommonPrefixes().isEmpty()) {
                  nextMarker = this.objectListing.getCommonPrefixes().get(this.objectListing.getCommonPrefixes().size() - 1);
               } else {
                  XmlResponsesSaxParser.log.error("S3 response indicates truncated results, but contains no object summaries or common prefixes.");
               }

               this.objectListing.setNextMarker(nextMarker);
            }
         } else if (this.in(new String[]{"ListBucketResult"})) {
            if (name.equals("Name")) {
               this.objectListing.setBucketName(this.getText());
               if (XmlResponsesSaxParser.log.isDebugEnabled()) {
                  XmlResponsesSaxParser.log.debug("Examining listing for bucket: " + this.objectListing.getBucketName());
               }
            } else if (name.equals("Prefix")) {
               this.objectListing
                  .setPrefix(XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse));
            } else if (name.equals("Marker")) {
               this.objectListing
                  .setMarker(XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse));
            } else if (name.equals("NextMarker")) {
               this.objectListing.setNextMarker(XmlResponsesSaxParser.decodeIfSpecified(this.getText(), this.shouldSDKDecodeResponse));
            } else if (name.equals("MaxKeys")) {
               this.objectListing.setMaxKeys(XmlResponsesSaxParser.parseInt(this.getText()));
            } else if (name.equals("Delimiter")) {
               this.objectListing
                  .setDelimiter(
                     XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse)
                  );
            } else if (name.equals("EncodingType")) {
               this.objectListing.setEncodingType(this.shouldSDKDecodeResponse ? null : XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("IsTruncated")) {
               String isTruncatedStr = StringUtils.lowerCase(this.getText());
               if (isTruncatedStr.startsWith("false")) {
                  this.objectListing.setTruncated(false);
               } else {
                  if (!isTruncatedStr.startsWith("true")) {
                     throw new IllegalStateException("Invalid value for IsTruncated field: " + isTruncatedStr);
                  }

                  this.objectListing.setTruncated(true);
               }
            } else if (name.equals("Contents")) {
               this.objectListing.getObjectSummaries().add(this.currentObject);
               this.currentObject = null;
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents"})) {
            if (name.equals("Key")) {
               this.lastKey = this.getText();
               this.currentObject.setKey(XmlResponsesSaxParser.decodeIfSpecified(this.lastKey, this.shouldSDKDecodeResponse));
            } else if (name.equals("LastModified")) {
               this.currentObject.setLastModified(ServiceUtils.parseIso8601Date(this.getText()));
            } else if (name.equals("ETag")) {
               this.currentObject.setETag(ServiceUtils.removeQuotes(this.getText()));
            } else if (name.equals("Size")) {
               this.currentObject.setSize(XmlResponsesSaxParser.parseLong(this.getText()));
            } else if (name.equals("StorageClass")) {
               this.currentObject.setStorageClass(this.getText());
            } else if (name.equals("Owner")) {
               this.currentObject.setOwner(this.currentOwner);
               this.currentOwner = null;
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents", "Owner"})) {
            if (name.equals("ID")) {
               this.currentOwner.setId(this.getText());
            } else if (name.equals("DisplayName")) {
               this.currentOwner.setDisplayName(this.getText());
            }
         } else if (this.in(new String[]{"ListBucketResult", "CommonPrefixes"}) && name.equals("Prefix")) {
            this.objectListing.getCommonPrefixes().add(XmlResponsesSaxParser.decodeIfSpecified(this.getText(), this.shouldSDKDecodeResponse));
         }
      }
   }

   public static class ListBucketIntelligentTieringConfigurationHandler extends AbstractHandler {
      private final ListBucketIntelligentTieringConfigurationsResult result = new ListBucketIntelligentTieringConfigurationsResult();
      private IntelligentTieringConfiguration currentConfiguration;
      private IntelligentTieringFilter currentFilter;
      private List<IntelligentTieringFilterPredicate> andOperandsList;
      private Tiering currentTiering;
      private String currentTagKey;
      private String currentTagValue;

      public ListBucketIntelligentTieringConfigurationsResult getResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult"})) {
            if (name.equals("IntelligentTieringConfiguration")) {
               this.currentConfiguration = new IntelligentTieringConfiguration();
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration"})) {
            if (name.equals("Filter")) {
               this.currentFilter = new IntelligentTieringFilter();
            } else if (name.equals("Tiering")) {
               this.currentTiering = new Tiering();
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult"})) {
            if (name.equals("IntelligentTieringConfiguration")) {
               if (this.result.getIntelligentTieringConfigurationList() == null) {
                  this.result.setIntelligentTieringConfigurationList(new ArrayList<>());
               }

               this.result.getIntelligentTieringConfigurationList().add(this.currentConfiguration);
               this.currentConfiguration = null;
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated("true".equals(this.getText()));
            } else if (name.equals("ContinuationToken")) {
               this.result.setContinuationToken(this.getText());
            } else if (name.equals("NextContinuationToken")) {
               this.result.setNextContinuationToken(this.getText());
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration"})) {
            if (name.equals("Id")) {
               this.currentConfiguration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.currentConfiguration.setFilter(this.currentFilter);
            } else if (name.equals("Status")) {
               this.currentConfiguration.setStatus(IntelligentTieringStatus.fromValue(this.getText()));
            } else if (name.equals("Tiering")) {
               if (this.currentConfiguration.getTierings() == null) {
                  this.currentConfiguration.setTierings(new ArrayList<>());
               }

               this.currentConfiguration.getTierings().add(this.currentTiering);
               this.currentTiering = null;
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new IntelligentTieringPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.currentFilter.setPredicate(new IntelligentTieringTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.currentFilter.setPredicate(new IntelligentTieringAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new IntelligentTieringPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new IntelligentTieringTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ListIntelligentTieringConfigurationsResult", "IntelligentTieringConfiguration", "Tiering"})) {
            if (name.equals("AccessTier")) {
               this.currentTiering.setAccessTier(IntelligentTieringAccessTier.fromValue(this.getText()));
            } else if (name.equals("Days")) {
               this.currentTiering.setDays(Integer.parseInt(this.getText()));
            }
         }
      }
   }

   public static class ListBucketInventoryConfigurationsHandler extends AbstractHandler {
      private final ListBucketInventoryConfigurationsResult result = new ListBucketInventoryConfigurationsResult();
      private InventoryConfiguration currentConfiguration;
      private List<String> currentOptionalFieldsList;
      private InventoryDestination currentDestination;
      private InventoryFilter currentFilter;
      private InventoryS3BucketDestination currentS3BucketDestination;
      private InventorySchedule currentSchedule;

      public ListBucketInventoryConfigurationsResult getResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListInventoryConfigurationsResult"})) {
            if (name.equals("InventoryConfiguration")) {
               this.currentConfiguration = new InventoryConfiguration();
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration"})) {
            if (name.equals("Destination")) {
               this.currentDestination = new InventoryDestination();
            } else if (name.equals("Filter")) {
               this.currentFilter = new InventoryFilter();
            } else if (name.equals("Schedule")) {
               this.currentSchedule = new InventorySchedule();
            } else if (name.equals("OptionalFields")) {
               this.currentOptionalFieldsList = new ArrayList<>();
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "Destination"}) && name.equals("S3BucketDestination")) {
            this.currentS3BucketDestination = new InventoryS3BucketDestination();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListInventoryConfigurationsResult"})) {
            if (name.equals("InventoryConfiguration")) {
               if (this.result.getInventoryConfigurationList() == null) {
                  this.result.setInventoryConfigurationList(new ArrayList<>());
               }

               this.result.getInventoryConfigurationList().add(this.currentConfiguration);
               this.currentConfiguration = null;
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated("true".equals(this.getText()));
            } else if (name.equals("ContinuationToken")) {
               this.result.setContinuationToken(this.getText());
            } else if (name.equals("NextContinuationToken")) {
               this.result.setNextContinuationToken(this.getText());
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration"})) {
            if (name.equals("Id")) {
               this.currentConfiguration.setId(this.getText());
            } else if (name.equals("Destination")) {
               this.currentConfiguration.setDestination(this.currentDestination);
               this.currentDestination = null;
            } else if (name.equals("IsEnabled")) {
               this.currentConfiguration.setEnabled("true".equals(this.getText()));
            } else if (name.equals("Filter")) {
               this.currentConfiguration.setInventoryFilter(this.currentFilter);
               this.currentFilter = null;
            } else if (name.equals("IncludedObjectVersions")) {
               this.currentConfiguration.setIncludedObjectVersions(this.getText());
            } else if (name.equals("Schedule")) {
               this.currentConfiguration.setSchedule(this.currentSchedule);
               this.currentSchedule = null;
            } else if (name.equals("OptionalFields")) {
               this.currentConfiguration.setOptionalFields(this.currentOptionalFieldsList);
               this.currentOptionalFieldsList = null;
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "Destination"})) {
            if (name.equals("S3BucketDestination")) {
               this.currentDestination.setS3BucketDestination(this.currentS3BucketDestination);
               this.currentS3BucketDestination = null;
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "Destination", "S3BucketDestination"})) {
            if (name.equals("AccountId")) {
               this.currentS3BucketDestination.setAccountId(this.getText());
            } else if (name.equals("Bucket")) {
               this.currentS3BucketDestination.setBucketArn(this.getText());
            } else if (name.equals("Format")) {
               this.currentS3BucketDestination.setFormat(this.getText());
            } else if (name.equals("Prefix")) {
               this.currentS3BucketDestination.setPrefix(this.getText());
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new InventoryPrefixPredicate(this.getText()));
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "Schedule"})) {
            if (name.equals("Frequency")) {
               this.currentSchedule.setFrequency(this.getText());
            }
         } else if (this.in(new String[]{"ListInventoryConfigurationsResult", "InventoryConfiguration", "OptionalFields"}) && name.equals("Field")) {
            this.currentOptionalFieldsList.add(this.getText());
         }
      }
   }

   public static class ListBucketMetricsConfigurationsHandler extends AbstractHandler {
      private final ListBucketMetricsConfigurationsResult result = new ListBucketMetricsConfigurationsResult();
      private MetricsConfiguration currentConfiguration;
      private MetricsFilter currentFilter;
      private List<MetricsFilterPredicate> andOperandsList;
      private String currentTagKey;
      private String currentTagValue;

      public ListBucketMetricsConfigurationsResult getResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListMetricsConfigurationsResult"})) {
            if (name.equals("MetricsConfiguration")) {
               this.currentConfiguration = new MetricsConfiguration();
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration"})) {
            if (name.equals("Filter")) {
               this.currentFilter = new MetricsFilter();
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration", "Filter"}) && name.equals("And")) {
            this.andOperandsList = new ArrayList<>();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListMetricsConfigurationsResult"})) {
            if (name.equals("MetricsConfiguration")) {
               if (this.result.getMetricsConfigurationList() == null) {
                  this.result.setMetricsConfigurationList(new ArrayList<>());
               }

               this.result.getMetricsConfigurationList().add(this.currentConfiguration);
               this.currentConfiguration = null;
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated("true".equals(this.getText()));
            } else if (name.equals("ContinuationToken")) {
               this.result.setContinuationToken(this.getText());
            } else if (name.equals("NextContinuationToken")) {
               this.result.setNextContinuationToken(this.getText());
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration"})) {
            if (name.equals("Id")) {
               this.currentConfiguration.setId(this.getText());
            } else if (name.equals("Filter")) {
               this.currentConfiguration.setFilter(this.currentFilter);
               this.currentFilter = null;
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration", "Filter"})) {
            if (name.equals("Prefix")) {
               this.currentFilter.setPredicate(new MetricsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.currentFilter.setPredicate(new MetricsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("And")) {
               this.currentFilter.setPredicate(new MetricsAndOperator(this.andOperandsList));
               this.andOperandsList = null;
            } else if (name.equals("AccessPointArn")) {
               this.currentFilter.setPredicate(new MetricsAccessPointArnPredicate(this.getText()));
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration", "Filter", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration", "Filter", "And"})) {
            if (name.equals("Prefix")) {
               this.andOperandsList.add(new MetricsPrefixPredicate(this.getText()));
            } else if (name.equals("Tag")) {
               this.andOperandsList.add(new MetricsTagPredicate(new Tag(this.currentTagKey, this.currentTagValue)));
               this.currentTagKey = null;
               this.currentTagValue = null;
            } else if (name.equals("AccessPointArn")) {
               this.andOperandsList.add(new MetricsAccessPointArnPredicate(this.getText()));
            }
         } else if (this.in(new String[]{"ListMetricsConfigurationsResult", "MetricsConfiguration", "Filter", "And", "Tag"})) {
            if (name.equals("Key")) {
               this.currentTagKey = this.getText();
            } else if (name.equals("Value")) {
               this.currentTagValue = this.getText();
            }
         }
      }
   }

   public static class ListMultipartUploadsHandler extends AbstractHandler {
      private final MultipartUploadListing result = new MultipartUploadListing();
      private MultipartUpload currentMultipartUpload;
      private Owner currentOwner;

      public MultipartUploadListing getListMultipartUploadsResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListMultipartUploadsResult"})) {
            if (name.equals("Upload")) {
               this.currentMultipartUpload = new MultipartUpload();
            }
         } else if (this.in(new String[]{"ListMultipartUploadsResult", "Upload"}) && (name.equals("Owner") || name.equals("Initiator"))) {
            this.currentOwner = new Owner();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListMultipartUploadsResult"})) {
            if (name.equals("Bucket")) {
               this.result.setBucketName(this.getText());
            } else if (name.equals("KeyMarker")) {
               this.result.setKeyMarker(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("Delimiter")) {
               this.result.setDelimiter(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("Prefix")) {
               this.result.setPrefix(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("UploadIdMarker")) {
               this.result.setUploadIdMarker(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("NextKeyMarker")) {
               this.result.setNextKeyMarker(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("NextUploadIdMarker")) {
               this.result.setNextUploadIdMarker(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("MaxUploads")) {
               this.result.setMaxUploads(Integer.parseInt(this.getText()));
            } else if (name.equals("EncodingType")) {
               this.result.setEncodingType(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated(Boolean.parseBoolean(this.getText()));
            } else if (name.equals("Upload")) {
               this.result.getMultipartUploads().add(this.currentMultipartUpload);
               this.currentMultipartUpload = null;
            }
         } else if (this.in(new String[]{"ListMultipartUploadsResult", "CommonPrefixes"})) {
            if (name.equals("Prefix")) {
               this.result.getCommonPrefixes().add(this.getText());
            }
         } else if (this.in(new String[]{"ListMultipartUploadsResult", "Upload"})) {
            if (name.equals("Key")) {
               this.currentMultipartUpload.setKey(this.getText());
            } else if (name.equals("UploadId")) {
               this.currentMultipartUpload.setUploadId(this.getText());
            } else if (name.equals("Owner")) {
               this.currentMultipartUpload.setOwner(this.currentOwner);
               this.currentOwner = null;
            } else if (name.equals("Initiator")) {
               this.currentMultipartUpload.setInitiator(this.currentOwner);
               this.currentOwner = null;
            } else if (name.equals("StorageClass")) {
               this.currentMultipartUpload.setStorageClass(this.getText());
            } else if (name.equals("Initiated")) {
               this.currentMultipartUpload.setInitiated(ServiceUtils.parseIso8601Date(this.getText()));
            }
         } else if (this.in(new String[]{"ListMultipartUploadsResult", "Upload", "Owner"})
            || this.in(new String[]{"ListMultipartUploadsResult", "Upload", "Initiator"})) {
            if (name.equals("ID")) {
               this.currentOwner.setId(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("DisplayName")) {
               this.currentOwner.setDisplayName(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            }
         }
      }
   }

   public static class ListObjectsV2Handler extends AbstractHandler {
      private final ListObjectsV2Result result = new ListObjectsV2Result();
      private final boolean shouldSDKDecodeResponse;
      private S3ObjectSummary currentObject = null;
      private Owner currentOwner = null;
      private String lastKey = null;

      public ListObjectsV2Handler(boolean shouldSDKDecodeResponse) {
         this.shouldSDKDecodeResponse = shouldSDKDecodeResponse;
      }

      public ListObjectsV2Result getResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListBucketResult"})) {
            if (name.equals("Contents")) {
               this.currentObject = new S3ObjectSummary();
               this.currentObject.setBucketName(this.result.getBucketName());
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents"}) && name.equals("Owner")) {
            this.currentOwner = new Owner();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.atTopLevel()) {
            if (name.equals("ListBucketResult") && this.result.isTruncated() && this.result.getNextContinuationToken() == null) {
               String nextContinuationToken = null;
               if (!this.result.getObjectSummaries().isEmpty()) {
                  nextContinuationToken = this.result.getObjectSummaries().get(this.result.getObjectSummaries().size() - 1).getKey();
               } else {
                  XmlResponsesSaxParser.log.error("S3 response indicates truncated results, but contains no object summaries.");
               }

               this.result.setNextContinuationToken(nextContinuationToken);
            }
         } else if (this.in(new String[]{"ListBucketResult"})) {
            if (name.equals("Name")) {
               this.result.setBucketName(this.getText());
               if (XmlResponsesSaxParser.log.isDebugEnabled()) {
                  XmlResponsesSaxParser.log.debug("Examining listing for bucket: " + this.result.getBucketName());
               }
            } else if (name.equals("Prefix")) {
               this.result
                  .setPrefix(XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse));
            } else if (name.equals("MaxKeys")) {
               this.result.setMaxKeys(XmlResponsesSaxParser.parseInt(this.getText()));
            } else if (name.equals("NextContinuationToken")) {
               this.result.setNextContinuationToken(this.getText());
            } else if (name.equals("ContinuationToken")) {
               this.result.setContinuationToken(this.getText());
            } else if (name.equals("StartAfter")) {
               this.result.setStartAfter(XmlResponsesSaxParser.decodeIfSpecified(this.getText(), this.shouldSDKDecodeResponse));
            } else if (name.equals("KeyCount")) {
               this.result.setKeyCount(XmlResponsesSaxParser.parseInt(this.getText()));
            } else if (name.equals("Delimiter")) {
               this.result
                  .setDelimiter(
                     XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse)
                  );
            } else if (name.equals("EncodingType")) {
               this.result.setEncodingType(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("IsTruncated")) {
               String isTruncatedStr = StringUtils.lowerCase(this.getText());
               if (isTruncatedStr.startsWith("false")) {
                  this.result.setTruncated(false);
               } else {
                  if (!isTruncatedStr.startsWith("true")) {
                     throw new IllegalStateException("Invalid value for IsTruncated field: " + isTruncatedStr);
                  }

                  this.result.setTruncated(true);
               }
            } else if (name.equals("Contents")) {
               this.result.getObjectSummaries().add(this.currentObject);
               this.currentObject = null;
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents"})) {
            if (name.equals("Key")) {
               this.lastKey = this.getText();
               this.currentObject.setKey(XmlResponsesSaxParser.decodeIfSpecified(this.lastKey, this.shouldSDKDecodeResponse));
            } else if (name.equals("LastModified")) {
               this.currentObject.setLastModified(ServiceUtils.parseIso8601Date(this.getText()));
            } else if (name.equals("ETag")) {
               this.currentObject.setETag(ServiceUtils.removeQuotes(this.getText()));
            } else if (name.equals("Size")) {
               this.currentObject.setSize(XmlResponsesSaxParser.parseLong(this.getText()));
            } else if (name.equals("StorageClass")) {
               this.currentObject.setStorageClass(this.getText());
            } else if (name.equals("Owner")) {
               this.currentObject.setOwner(this.currentOwner);
               this.currentOwner = null;
            }
         } else if (this.in(new String[]{"ListBucketResult", "Contents", "Owner"})) {
            if (name.equals("ID")) {
               this.currentOwner.setId(this.getText());
            } else if (name.equals("DisplayName")) {
               this.currentOwner.setDisplayName(this.getText());
            }
         } else if (this.in(new String[]{"ListBucketResult", "CommonPrefixes"}) && name.equals("Prefix")) {
            this.result.getCommonPrefixes().add(XmlResponsesSaxParser.decodeIfSpecified(this.getText(), this.shouldSDKDecodeResponse));
         }
      }
   }

   public static class ListPartsHandler extends AbstractHandler {
      private final PartListing result = new PartListing();
      private PartSummary currentPart;
      private Owner currentOwner;

      public PartListing getListPartsResult() {
         return this.result;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListPartsResult"})) {
            if (name.equals("Part")) {
               this.currentPart = new PartSummary();
            } else if (name.equals("Owner") || name.equals("Initiator")) {
               this.currentOwner = new Owner();
            }
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListPartsResult"})) {
            if (name.equals("Bucket")) {
               this.result.setBucketName(this.getText());
            } else if (name.equals("Key")) {
               this.result.setKey(this.getText());
            } else if (name.equals("UploadId")) {
               this.result.setUploadId(this.getText());
            } else if (name.equals("Owner")) {
               this.result.setOwner(this.currentOwner);
               this.currentOwner = null;
            } else if (name.equals("Initiator")) {
               this.result.setInitiator(this.currentOwner);
               this.currentOwner = null;
            } else if (name.equals("StorageClass")) {
               this.result.setStorageClass(this.getText());
            } else if (name.equals("PartNumberMarker")) {
               this.result.setPartNumberMarker(this.parseInteger(this.getText()));
            } else if (name.equals("NextPartNumberMarker")) {
               this.result.setNextPartNumberMarker(this.parseInteger(this.getText()));
            } else if (name.equals("MaxParts")) {
               this.result.setMaxParts(this.parseInteger(this.getText()));
            } else if (name.equals("EncodingType")) {
               this.result.setEncodingType(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("IsTruncated")) {
               this.result.setTruncated(Boolean.parseBoolean(this.getText()));
            } else if (name.equals("Part")) {
               this.result.getParts().add(this.currentPart);
               this.currentPart = null;
            }
         } else if (this.in(new String[]{"ListPartsResult", "Part"})) {
            if (name.equals("PartNumber")) {
               this.currentPart.setPartNumber(Integer.parseInt(this.getText()));
            } else if (name.equals("LastModified")) {
               this.currentPart.setLastModified(ServiceUtils.parseIso8601Date(this.getText()));
            } else if (name.equals("ETag")) {
               this.currentPart.setETag(ServiceUtils.removeQuotes(this.getText()));
            } else if (name.equals("Size")) {
               this.currentPart.setSize(Long.parseLong(this.getText()));
            }
         } else if (this.in(new String[]{"ListPartsResult", "Owner"}) || this.in(new String[]{"ListPartsResult", "Initiator"})) {
            if (name.equals("ID")) {
               this.currentOwner.setId(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("DisplayName")) {
               this.currentOwner.setDisplayName(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            }
         }
      }

      private Integer parseInteger(String text) {
         text = XmlResponsesSaxParser.checkForEmptyString(this.getText());
         return text == null ? null : Integer.parseInt(text);
      }
   }

   public static class ListVersionsHandler extends AbstractHandler {
      private final VersionListing versionListing = new VersionListing();
      private final boolean shouldSDKDecodeResponse;
      private S3VersionSummary currentVersionSummary;
      private Owner currentOwner;

      public ListVersionsHandler(boolean shouldSDKDecodeResponse) {
         this.shouldSDKDecodeResponse = shouldSDKDecodeResponse;
      }

      public VersionListing getListing() {
         return this.versionListing;
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
         if (this.in(new String[]{"ListVersionsResult"})) {
            if (name.equals("Version")) {
               this.currentVersionSummary = new S3VersionSummary();
               this.currentVersionSummary.setBucketName(this.versionListing.getBucketName());
            } else if (name.equals("DeleteMarker")) {
               this.currentVersionSummary = new S3VersionSummary();
               this.currentVersionSummary.setBucketName(this.versionListing.getBucketName());
               this.currentVersionSummary.setIsDeleteMarker(true);
            }
         } else if ((this.in(new String[]{"ListVersionsResult", "Version"}) || this.in(new String[]{"ListVersionsResult", "DeleteMarker"}))
            && name.equals("Owner")) {
            this.currentOwner = new Owner();
         }
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"ListVersionsResult"})) {
            if (name.equals("Name")) {
               this.versionListing.setBucketName(this.getText());
            } else if (name.equals("Prefix")) {
               this.versionListing
                  .setPrefix(XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse));
            } else if (name.equals("KeyMarker")) {
               this.versionListing
                  .setKeyMarker(
                     XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse)
                  );
            } else if (name.equals("VersionIdMarker")) {
               this.versionListing.setVersionIdMarker(XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("MaxKeys")) {
               this.versionListing.setMaxKeys(Integer.parseInt(this.getText()));
            } else if (name.equals("Delimiter")) {
               this.versionListing
                  .setDelimiter(
                     XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse)
                  );
            } else if (name.equals("EncodingType")) {
               this.versionListing.setEncodingType(this.shouldSDKDecodeResponse ? null : XmlResponsesSaxParser.checkForEmptyString(this.getText()));
            } else if (name.equals("NextKeyMarker")) {
               this.versionListing
                  .setNextKeyMarker(
                     XmlResponsesSaxParser.decodeIfSpecified(XmlResponsesSaxParser.checkForEmptyString(this.getText()), this.shouldSDKDecodeResponse)
                  );
            } else if (name.equals("NextVersionIdMarker")) {
               this.versionListing.setNextVersionIdMarker(this.getText());
            } else if (name.equals("IsTruncated")) {
               this.versionListing.setTruncated("true".equals(this.getText()));
            } else if (name.equals("Version") || name.equals("DeleteMarker")) {
               this.versionListing.getVersionSummaries().add(this.currentVersionSummary);
               this.currentVersionSummary = null;
            }
         } else if (this.in(new String[]{"ListVersionsResult", "CommonPrefixes"})) {
            if (name.equals("Prefix")) {
               String commonPrefix = XmlResponsesSaxParser.checkForEmptyString(this.getText());
               this.versionListing.getCommonPrefixes().add(this.shouldSDKDecodeResponse ? SdkHttpUtils.urlDecode(commonPrefix) : commonPrefix);
            }
         } else if (!this.in(new String[]{"ListVersionsResult", "Version"}) && !this.in(new String[]{"ListVersionsResult", "DeleteMarker"})) {
            if (this.in(new String[]{"ListVersionsResult", "Version", "Owner"}) || this.in(new String[]{"ListVersionsResult", "DeleteMarker", "Owner"})) {
               if (name.equals("ID")) {
                  this.currentOwner.setId(this.getText());
               } else if (name.equals("DisplayName")) {
                  this.currentOwner.setDisplayName(this.getText());
               }
            }
         } else if (name.equals("Key")) {
            this.currentVersionSummary.setKey(XmlResponsesSaxParser.decodeIfSpecified(this.getText(), this.shouldSDKDecodeResponse));
         } else if (name.equals("VersionId")) {
            this.currentVersionSummary.setVersionId(this.getText());
         } else if (name.equals("IsLatest")) {
            this.currentVersionSummary.setIsLatest("true".equals(this.getText()));
         } else if (name.equals("LastModified")) {
            this.currentVersionSummary.setLastModified(ServiceUtils.parseIso8601Date(this.getText()));
         } else if (name.equals("ETag")) {
            this.currentVersionSummary.setETag(ServiceUtils.removeQuotes(this.getText()));
         } else if (name.equals("Size")) {
            this.currentVersionSummary.setSize(Long.parseLong(this.getText()));
         } else if (name.equals("Owner")) {
            this.currentVersionSummary.setOwner(this.currentOwner);
            this.currentOwner = null;
         } else if (name.equals("StorageClass")) {
            this.currentVersionSummary.setStorageClass(this.getText());
         }
      }
   }

   public static class RequestPaymentConfigurationHandler extends AbstractHandler {
      private String payer = null;

      public RequestPaymentConfiguration getConfiguration() {
         return new RequestPaymentConfiguration(RequestPaymentConfiguration.Payer.valueOf(this.payer));
      }

      @Override
      protected void doStartElement(String uri, String name, String qName, Attributes attrs) {
      }

      @Override
      protected void doEndElement(String uri, String name, String qName) {
         if (this.in(new String[]{"RequestPaymentConfiguration"}) && name.equals("Payer")) {
            this.payer = this.getText();
         }
      }
   }
}
