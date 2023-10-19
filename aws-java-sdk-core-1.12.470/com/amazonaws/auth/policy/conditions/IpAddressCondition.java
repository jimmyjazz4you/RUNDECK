package com.amazonaws.auth.policy.conditions;

import com.amazonaws.auth.policy.Condition;
import java.util.Arrays;

public class IpAddressCondition extends Condition {
   public IpAddressCondition(String ipAddressRange) {
      this(IpAddressCondition.IpAddressComparisonType.IpAddress, ipAddressRange);
   }

   public IpAddressCondition(IpAddressCondition.IpAddressComparisonType type, String ipAddressRange) {
      super.type = type.toString();
      super.conditionKey = "aws:SourceIp";
      super.values = Arrays.asList(ipAddressRange);
   }

   public static enum IpAddressComparisonType {
      IpAddress,
      NotIpAddress;
   }
}
