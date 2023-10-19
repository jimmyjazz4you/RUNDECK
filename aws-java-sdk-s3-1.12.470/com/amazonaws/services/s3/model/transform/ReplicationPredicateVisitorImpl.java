package com.amazonaws.services.s3.model.transform;

import com.amazonaws.services.s3.internal.XmlWriter;
import com.amazonaws.services.s3.model.replication.ReplicationAndOperator;
import com.amazonaws.services.s3.model.replication.ReplicationFilterPredicate;
import com.amazonaws.services.s3.model.replication.ReplicationPredicateVisitor;
import com.amazonaws.services.s3.model.replication.ReplicationPrefixPredicate;
import com.amazonaws.services.s3.model.replication.ReplicationTagPredicate;

class ReplicationPredicateVisitorImpl implements ReplicationPredicateVisitor {
   private final XmlWriter xml;

   public ReplicationPredicateVisitorImpl(XmlWriter xml) {
      this.xml = xml;
   }

   @Override
   public void visit(ReplicationPrefixPredicate replicationPrefixPredicate) {
      BucketConfigurationXmlFactoryFunctions.writePrefix(this.xml, replicationPrefixPredicate.getPrefix());
   }

   @Override
   public void visit(ReplicationTagPredicate replicationTagPredicate) {
      BucketConfigurationXmlFactoryFunctions.writeTag(this.xml, replicationTagPredicate.getTag());
   }

   @Override
   public void visit(ReplicationAndOperator replicationAndOperator) {
      this.xml.start("And");

      for(ReplicationFilterPredicate predicate : replicationAndOperator.getOperands()) {
         predicate.accept(this);
      }

      this.xml.end();
   }
}
