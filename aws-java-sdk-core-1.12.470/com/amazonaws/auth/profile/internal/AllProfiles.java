package com.amazonaws.auth.profile.internal;

import com.amazonaws.annotation.Immutable;
import com.amazonaws.annotation.SdkInternalApi;
import java.util.Collections;
import java.util.Map;

@Immutable
@SdkInternalApi
public class AllProfiles {
   private final Map<String, BasicProfile> profiles;

   public AllProfiles(Map<String, BasicProfile> profiles) {
      this.profiles = profiles;
   }

   public Map<String, BasicProfile> getProfiles() {
      return Collections.unmodifiableMap(this.profiles);
   }

   public BasicProfile getProfile(String profileName) {
      BasicProfile profile = this.profiles.get(profileName);
      return profile != null ? profile : this.profiles.get("profile " + profileName);
   }
}
