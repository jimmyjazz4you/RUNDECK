package com.amazonaws.services.s3.model.transform;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Owner;

public class AclXmlFactory {
   public byte[] convertToXmlByteArray(AccessControlList acl) throws SdkClientException {
      Owner owner = acl.getOwner();
      if (owner == null) {
         throw new SdkClientException("Invalid AccessControlList: missing an S3Owner");
      } else {
         XmlWriter xml = new XmlWriter();
         xml.start("AccessControlPolicy", "xmlns", "http://s3.amazonaws.com/doc/2006-03-01/");
         xml.start("Owner");
         if (owner.getId() != null) {
            xml.start("ID").value(owner.getId()).end();
         }

         if (owner.getDisplayName() != null) {
            xml.start("DisplayName").value(owner.getDisplayName()).end();
         }

         xml.end();
         xml.start("AccessControlList");

         for(Grant grant : acl.getGrantsAsList()) {
            xml.start("Grant");
            this.convertToXml(grant.getGrantee(), xml);
            xml.start("Permission").value(grant.getPermission().toString()).end();
            xml.end();
         }

         xml.end();
         xml.end();
         return xml.getBytes();
      }
   }

   protected XmlWriter convertToXml(Grantee grantee, XmlWriter xml) throws SdkClientException {
      if (grantee instanceof CanonicalGrantee) {
         return this.convertToXml((CanonicalGrantee)grantee, xml);
      } else if (grantee instanceof EmailAddressGrantee) {
         return this.convertToXml((EmailAddressGrantee)grantee, xml);
      } else if (grantee instanceof GroupGrantee) {
         return this.convertToXml((GroupGrantee)grantee, xml);
      } else {
         throw new SdkClientException("Unknown Grantee type: " + grantee.getClass().getName());
      }
   }

   protected XmlWriter convertToXml(CanonicalGrantee grantee, XmlWriter xml) {
      xml.start("Grantee", new String[]{"xmlns:xsi", "xsi:type"}, new String[]{"http://www.w3.org/2001/XMLSchema-instance", "CanonicalUser"});
      xml.start("ID").value(grantee.getIdentifier()).end();
      xml.end();
      return xml;
   }

   protected XmlWriter convertToXml(EmailAddressGrantee grantee, XmlWriter xml) {
      xml.start("Grantee", new String[]{"xmlns:xsi", "xsi:type"}, new String[]{"http://www.w3.org/2001/XMLSchema-instance", "AmazonCustomerByEmail"});
      xml.start("EmailAddress").value(grantee.getIdentifier()).end();
      xml.end();
      return xml;
   }

   protected XmlWriter convertToXml(GroupGrantee grantee, XmlWriter xml) {
      xml.start("Grantee", new String[]{"xmlns:xsi", "xsi:type"}, new String[]{"http://www.w3.org/2001/XMLSchema-instance", "Group"});
      xml.start("URI").value(grantee.getIdentifier()).end();
      xml.end();
      return xml;
   }
}
