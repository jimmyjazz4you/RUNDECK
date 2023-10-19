package com.amazonaws.util;

import com.amazonaws.auth.policy.Resource;
import java.util.List;

public class PolicyUtils {
   private static final String INVALID_RESOURCE = "Cannot have both a NotResource and a Resource in the same statement";

   public static void validateResourceList(List<Resource> resourceList) {
      boolean hasNotResource = false;
      boolean hasResource = false;

      for(Resource resource : resourceList) {
         if (resource.isNotType()) {
            hasNotResource = true;
         } else {
            hasResource = true;
         }

         if (hasResource && hasNotResource) {
            throw new IllegalArgumentException("Cannot have both a NotResource and a Resource in the same statement");
         }
      }
   }
}
