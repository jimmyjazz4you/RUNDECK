package com.amazonaws.partitions.model;

import com.amazonaws.util.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Partitions {
   private final String version;
   private final List<Partition> partitions;

   public Partitions(@JsonProperty("version") String version, @JsonProperty("partitions") List<Partition> partitions) {
      this.version = ValidationUtils.assertNotNull(version, "version");
      this.partitions = ValidationUtils.assertNotNull(partitions, "version");
   }

   public String getVersion() {
      return this.version;
   }

   public List<Partition> getPartitions() {
      return this.partitions;
   }
}
