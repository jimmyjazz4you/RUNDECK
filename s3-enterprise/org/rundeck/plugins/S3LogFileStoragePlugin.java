package org.rundeck.plugins;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.dtolabs.rundeck.core.dispatcher.DataContextUtils;
import com.dtolabs.rundeck.core.logging.ExecutionFileStorageException;
import com.dtolabs.rundeck.core.logging.ExecutionMultiFileStorage;
import com.dtolabs.rundeck.core.logging.MultiFileStorageRequest;
import com.dtolabs.rundeck.core.logging.MultiFileStorageRequestErrors;
import com.dtolabs.rundeck.core.logging.StorageFile;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.logging.ExecutionFileStoragePlugin;
import com.dtolabs.utils.Streams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(
   service = "ExecutionFileStorage",
   name = "org.rundeck.amazon-s3"
)
@PluginDescription(
   title = "S3",
   description = "Stores log files into an S3 bucket"
)
public class S3LogFileStoragePlugin implements ExecutionFileStoragePlugin, AWSCredentials, ExecutionMultiFileStorage {
   public static final String DEFAULT_PATH_FORMAT = "project/${job.project}/${job.execid}";
   public static final String DEFAULT_REGION = "us-east-1";
   public static final String META_EXECID = "execid";
   public static final String _PREFIX_META = "rundeck.";
   public static final String META_USERNAME = "username";
   public static final String META_PROJECT = "project";
   public static final String META_URL = "url";
   public static final String META_SERVERURL = "serverUrl";
   public static final String META_SERVER_UUID = "serverUUID";
   protected static Logger logger = Logger.getLogger(S3LogFileStoragePlugin.class.getName());
   @PluginProperty(
      title = "AWS Access Key",
      description = "AWS Access Key"
   )
   private String AWSAccessKeyId;
   @PluginProperty(
      title = "AWS Secret Key",
      description = "AWS Secret Key"
   )
   private String AWSSecretKey;
   @PluginProperty(
      title = "AWS Credentials File",
      description = "Path to a AWSCredentials.properties file containing 'accessKey' and 'secretKey'."
   )
   private String AWSCredentialsFile;
   @PluginProperty(
      title = "Bucket name",
      required = true,
      description = "Bucket to store files in"
   )
   private String bucket;
   @PluginProperty(
      title = "Encode user metadata",
      required = false,
      description = "Encode user metadata to URL format",
      defaultValue = "false"
   )
   private boolean encodeUserMetadata = false;
   @PluginProperty(
      title = "Path",
      required = true,
      description = "The path in the bucket to store a log file.  Default: project/${job.project}/${job.execid}\n\nYou can use these expansion variables: \n\n* `${job.execid}` = execution ID\n* `${job.project}` = project name\n* `${job.id}` = job UUID (or blank).\n* `${job.group}` = job group (or blank).\n* `${job.name}` = job name (or blank).\n",
      defaultValue = "project/${job.project}/${job.execid}"
   )
   private String path;
   @PluginProperty(
      title = "S3 Region",
      description = "AWS S3 Region to use.  You can use one of the supported region names",
      required = true,
      defaultValue = "us-east-1"
   )
   private String region;
   @PluginProperty(
      title = "S3 Endpoint",
      description = "S3 endpoint to connect to, the region is ignored if this is set."
   )
   private String endpoint;
   @PluginProperty(
      title = "Force Signature v4",
      description = "Whether to force use of Signature Version 4 authentication. Default: false",
      defaultValue = "false"
   )
   private boolean forceSigV4;
   @PluginProperty(
      title = "Use Signature v2",
      description = "Use of Signature Version 2 authentication for old container. Default: false",
      defaultValue = "false"
   )
   private boolean useSigV2;
   @PluginProperty(
      title = "Use Path Style",
      description = "Whether to access the Endpoint using `endpoint/bucket` style, default: false. The default will use DNS style `bucket.endpoint`, which may be incompatible with non-AWS S3-compatible services",
      defaultValue = "false"
   )
   private boolean pathStyle;
   protected String expandedPath;
   protected AmazonS3 amazonS3;
   protected Map<String, ?> context;
   private static final String[] STORED_META = new String[]{"execid", "username", "project", "url", "serverUrl", "serverUUID"};

   public void initialize(Map<String, ?> context) {
      this.context = context;
      if ((null == this.getAWSAccessKeyId() || null != this.getAWSSecretKey()) && (null != this.getAWSAccessKeyId() || null == this.getAWSSecretKey())) {
         if (null != this.AWSAccessKeyId && null != this.AWSSecretKey) {
            this.amazonS3 = this.createAmazonS3Client(this);
         } else if (null != this.getAWSCredentialsFile()) {
            File creds = new File(this.getAWSCredentialsFile());
            if (!creds.exists() || !creds.canRead()) {
               throw new IllegalArgumentException("Credentials file does not exist or cannot be read: " + this.getAWSCredentialsFile());
            }

            try {
               this.amazonS3 = this.createAmazonS3Client(new PropertiesCredentials(creds));
            } catch (IOException var4) {
               throw new RuntimeException("Credentials file could not be read: " + this.getAWSCredentialsFile() + ": " + var4.getMessage(), var4);
            }
         } else {
            this.amazonS3 = this.createAmazonS3Client();
         }

         Region awsregion = (Region)RegionUtils.getRegions().stream().filter(r -> r.getName().equals(this.getRegion())).findAny().orElse(null);
         if (null == awsregion) {
            throw new IllegalArgumentException("Region was not found: " + this.getRegion());
         } else {
            if (this.isSignatureV4Enforced()) {
               System.setProperty("com.amazonaws.services.s3.enforceV4", "true");
            }

            if (null != this.getEndpoint() && !"".equals(this.getEndpoint().trim())) {
               this.amazonS3.setEndpoint(this.getEndpoint());
            } else {
               this.amazonS3.setRegion(awsregion);
            }

            if (this.isPathStyle()) {
               S3ClientOptions clientOptions = new S3ClientOptions();
               clientOptions.setPathStyleAccess(this.isPathStyle());
               this.amazonS3.setS3ClientOptions(clientOptions);
            }

            if (null == this.bucket || "".equals(this.bucket.trim())) {
               throw new IllegalArgumentException("bucket was not set");
            } else if (null == this.getPath() || "".equals(this.getPath().trim())) {
               throw new IllegalArgumentException("path was not set");
            } else if (!this.getPath().contains("${job.execid}") && !this.getPath().endsWith("/")) {
               throw new IllegalArgumentException("path must contain ${job.execid} or end with /");
            } else {
               String configpath = this.getPath();
               if (!configpath.contains("${job.execid}") && configpath.endsWith("/")) {
                  configpath = this.path + "/${job.execid}";
               }

               this.expandedPath = expandPath(configpath, context);
               if (null == this.expandedPath || "".equals(this.expandedPath.trim())) {
                  throw new IllegalArgumentException("expanded value of path was empty");
               } else if (this.expandedPath.endsWith("/")) {
                  throw new IllegalArgumentException("expanded value of path must not end with /");
               }
            }
         }
      } else {
         throw new IllegalArgumentException("AWSAccessKeyId and AWSSecretKey must both be configured.");
      }
   }

   protected AmazonS3 createAmazonS3Client(AWSCredentials awsCredentials) {
      if (this.isSignatureV2Used()) {
         ClientConfiguration opts = new ClientConfiguration();
         opts.setSignerOverride("S3SignerType");
         return new AmazonS3Client(awsCredentials, opts);
      } else {
         return new AmazonS3Client(awsCredentials);
      }
   }

   protected AmazonS3 createAmazonS3Client() {
      if (this.isSignatureV2Used()) {
         ClientConfiguration opts = new ClientConfiguration();
         opts.setSignerOverride("S3SignerType");
         return new AmazonS3Client(opts);
      } else {
         return new AmazonS3Client();
      }
   }

   static String expandPath(String pathFormat, Map<String, ?> context) {
      String result = pathFormat.replaceAll("^/+", "");
      if (null != context) {
         result = DataContextUtils.replaceDataReferences(result, DataContextUtils.addContext("job", stringMap(context), new HashMap()), null, false, true);
      }

      return result.replaceAll("/+", "/");
   }

   private static Map<String, String> stringMap(Map<String, ?> context) {
      HashMap<String, String> result = new HashMap<>();

      for(String s : context.keySet()) {
         Object o = context.get(s);
         if (o != null) {
            result.put(s, o.toString());
         }
      }

      return result;
   }

   public boolean isAvailable(String filetype) throws ExecutionFileStorageException {
      HashMap<String, Object> expected = new HashMap<>();
      expected.put(this.metaKey("execid"), this.context.get("execid"));
      return this.isPathAvailable(this.resolvedFilepath(this.expandedPath, filetype), expected);
   }

   protected boolean isPathAvailable(String key, Map<String, Object> expectedMeta) throws ExecutionFileStorageException {
      GetObjectMetadataRequest getObjectRequest = new GetObjectMetadataRequest(this.getBucket(), key);
      logger.log(Level.FINE, "getState for S3 bucket {0}:{1}", new Object[]{this.getBucket(), key});

      try {
         ObjectMetadata objectMetadata = this.amazonS3.getObjectMetadata(getObjectRequest);
         Map<String, String> userMetadata = objectMetadata != null ? objectMetadata.getUserMetadata() : null;
         logger.log(Level.FINE, "Metadata {0}", new Object[]{userMetadata});
         if (null != expectedMeta) {
            for(String s : expectedMeta.keySet()) {
               String metaVal = null;
               if (null != userMetadata) {
                  metaVal = userMetadata.get(s);
               }

               boolean matches = expectedMeta.get(s).equals(metaVal);
               if (!matches) {
                  logger.log(Level.WARNING, "S3 Object metadata '{0}' was not expected: {1}, expected {2}", new Object[]{s, metaVal, expectedMeta.get(s)});
               }
            }
         }

         return true;
      } catch (AmazonS3Exception var10) {
         if (var10.getStatusCode() == 404) {
            logger.log(Level.FINE, "getState: S3 Object not found for {0}", key);
            return false;
         } else {
            logger.log(Level.SEVERE, var10.getMessage());
            logger.log(Level.FINE, var10.getMessage(), var10);
            throw new ExecutionFileStorageException(var10.getMessage(), var10);
         }
      } catch (AmazonClientException var11) {
         logger.log(Level.SEVERE, var11.getMessage());
         logger.log(Level.FINE, var11.getMessage(), var11);
         throw new ExecutionFileStorageException(var11.getMessage(), var11);
      }
   }

   public boolean store(String filetype, InputStream stream, long length, Date lastModified) throws ExecutionFileStorageException {
      return this.storePath(stream, this.resolvedFilepath(this.expandedPath, filetype), this.createObjectMetadata(length, lastModified));
   }

   protected boolean storePath(InputStream stream, String key, ObjectMetadata objectMetadata1) throws ExecutionFileStorageException {
      logger.log(Level.FINE, "Storing content to S3 bucket {0} path {1}", new Object[]{this.getBucket(), key});
      PutObjectRequest putObjectRequest = new PutObjectRequest(this.getBucket(), key, stream, objectMetadata1);

      try {
         this.amazonS3.putObject(putObjectRequest);
         return true;
      } catch (SdkClientException var7) {
         logger.log(Level.FINE, "Job could still be executing", var7.getMessage());
         throw new ExecutionFileStorageException(var7.getMessage(), var7);
      } catch (AmazonClientException var8) {
         logger.log(Level.SEVERE, var8.getMessage(), var8);
         throw new ExecutionFileStorageException(var8.getMessage(), var8);
      }
   }

   public void storeMultiple(MultiFileStorageRequest files) throws IOException, ExecutionFileStorageException {
      Set<String> availableFiletypes = files.getAvailableFiletypes();
      logger.log(Level.FINE, "Storing multiple files to S3 bucket {0} filetypes: {1}", new Object[]{this.getBucket(), availableFiletypes});

      for(String filetype : availableFiletypes) {
         StorageFile storageFile = files.getStorageFile(filetype);

         try {
            boolean success = this.store(filetype, storageFile.getInputStream(), storageFile.getLength(), storageFile.getLastModified());
            files.storageResultForFiletype(filetype, success);
         } catch (ExecutionFileStorageException var9) {
            if (files instanceof MultiFileStorageRequestErrors) {
               MultiFileStorageRequestErrors errors = (MultiFileStorageRequestErrors)files;
               errors.storageFailureForFiletype(filetype, var9.getMessage());
            } else {
               logger.log(Level.SEVERE, var9.getMessage());
               logger.log(Level.FINE, var9.getMessage(), var9);
               files.storageResultForFiletype(filetype, false);
            }
         }
      }
   }

   public boolean deleteFile(String filetype) throws IOException, ExecutionFileStorageException {
      try {
         HashMap<String, Object> expected = new HashMap<>();
         expected.put(this.metaKey("execid"), this.context.get("execid"));
         String filePath = this.resolvedFilepath(this.expandedPath, filetype);
         this.amazonS3.deleteObject(this.getBucket(), filePath);
         return true;
      } catch (AmazonClientException var4) {
         logger.log(Level.SEVERE, var4.getMessage(), var4);
         throw new ExecutionFileStorageException(var4.getMessage(), var4);
      }
   }

   protected ObjectMetadata createObjectMetadata(long length, Date lastModified) {
      ObjectMetadata metadata = new ObjectMetadata();

      for(String s : STORED_META) {
         Object v = this.context.get(s);
         if (null != v) {
            metadata.addUserMetadata(this.metaKey(s), this.isEncodeUserMetadata() ? this.encodeStringToURLRequest(v.toString()) : v.toString());
         }
      }

      metadata.setLastModified(lastModified);
      metadata.setContentLength(length);
      return metadata;
   }

   protected String metaKey(String s) {
      return "rundeck." + s;
   }

   private String encodeStringToURLRequest(String value) {
      try {
         return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
      } catch (UnsupportedEncodingException var3) {
         logger.log(Level.WARNING, var3.getMessage(), (Throwable)var3);
         return value;
      }
   }

   public boolean retrieve(String filetype, OutputStream stream) throws IOException, ExecutionFileStorageException {
      return this.retrievePath(stream, this.resolvedFilepath(this.expandedPath, filetype));
   }

   protected boolean retrievePath(OutputStream stream, String key) throws IOException, ExecutionFileStorageException {
      try {
         S3Object object = this.amazonS3.getObject(this.getBucket(), key);
         S3ObjectInputStream objectContent = object.getObjectContent();
         Throwable var5 = null;

         try {
            Streams.copyStream(objectContent, stream);
         } catch (Throwable var15) {
            var5 = var15;
            throw var15;
         } finally {
            if (objectContent != null) {
               if (var5 != null) {
                  try {
                     objectContent.close();
                  } catch (Throwable var14) {
                     var5.addSuppressed(var14);
                  }
               } else {
                  objectContent.close();
               }
            }
         }

         return true;
      } catch (AmazonClientException var17) {
         logger.log(Level.SEVERE, var17.getMessage(), var17);
         throw new ExecutionFileStorageException(var17.getMessage(), var17);
      }
   }

   public static void main(String[] args) throws IOException, ExecutionFileStorageException {
      S3LogFileStoragePlugin s3LogFileStoragePlugin = new S3LogFileStoragePlugin();
      String action = args[0];
      Map<String, Object> context = new HashMap<>();
      context.put("project", "test");
      context.put("execid", args[2]);
      context.put("name", "test job");
      context.put("group", "test group");
      context.put("id", "testjobid");
      context.put("username", "testuser");
      s3LogFileStoragePlugin.setAWSAccessKeyId("AKIAJ63ESPDAOS5FKWNQ");
      s3LogFileStoragePlugin.setAWSSecretKey(args[1]);
      s3LogFileStoragePlugin.setBucket("test-rundeck-logs");
      s3LogFileStoragePlugin.setPath("logs/$PROJECT/$ID.log");
      s3LogFileStoragePlugin.setRegion("us-east-1");
      s3LogFileStoragePlugin.setEndpoint("https://localhost");
      s3LogFileStoragePlugin.initialize(context);
      String filetype = ".rdlog";
      if ("store".equals(action)) {
         File file = new File(args[3]);
         s3LogFileStoragePlugin.store(filetype, new FileInputStream(file), file.length(), new Date(file.lastModified()));
      } else if ("retrieve".equals(action)) {
         s3LogFileStoragePlugin.retrieve(filetype, new FileOutputStream(new File(args[3])));
      } else if ("state".equals(action)) {
         System.out.println("available? " + s3LogFileStoragePlugin.isAvailable(filetype));
      }
   }

   public String getAWSAccessKeyId() {
      return this.AWSAccessKeyId;
   }

   public void setAWSAccessKeyId(String AWSAccessKeyId) {
      this.AWSAccessKeyId = AWSAccessKeyId;
   }

   public String getAWSSecretKey() {
      return this.AWSSecretKey;
   }

   public void setAWSSecretKey(String AWSSecretKey) {
      this.AWSSecretKey = AWSSecretKey;
   }

   public String getBucket() {
      return this.bucket;
   }

   public void setBucket(String bucket) {
      this.bucket = bucket;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getRegion() {
      return this.region;
   }

   public void setRegion(String region) {
      this.region = region;
   }

   public String getAWSCredentialsFile() {
      return this.AWSCredentialsFile;
   }

   public void setAWSCredentialsFile(String AWSCredentialsFile) {
      this.AWSCredentialsFile = AWSCredentialsFile;
   }

   public String getEndpoint() {
      return this.endpoint;
   }

   public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
   }

   public boolean isSignatureV4Enforced() {
      return this.forceSigV4;
   }

   public void setForceSignatureV4(boolean forceSigV4) {
      this.forceSigV4 = forceSigV4;
   }

   public boolean isSignatureV2Used() {
      return this.useSigV2;
   }

   public void setUseSignatureV2(boolean useSigV2) {
      this.useSigV2 = useSigV2;
   }

   protected String resolvedFilepath(String path, String filetype) {
      return path + "." + filetype;
   }

   public boolean isPathStyle() {
      return this.pathStyle;
   }

   public void setPathStyle(boolean pathStyle) {
      this.pathStyle = pathStyle;
   }

   public String getExpandedPath() {
      return this.expandedPath;
   }

   public AmazonS3 getAmazonS3() {
      return this.amazonS3;
   }

   public boolean isEncodeUserMetadata() {
      return this.encodeUserMetadata;
   }

   public void setEncodeUserMetadata(boolean encodeUserMetadata) {
      this.encodeUserMetadata = encodeUserMetadata;
   }
}
