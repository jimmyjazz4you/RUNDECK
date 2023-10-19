package com.amazonaws.services.s3.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtraMaterialsDescription implements Serializable {
   public static final ExtraMaterialsDescription NONE = new ExtraMaterialsDescription(Collections.EMPTY_MAP);
   private final Map<String, String> extra;
   private final ExtraMaterialsDescription.ConflictResolution resolve;

   public ExtraMaterialsDescription(Map<String, String> matdesc) {
      this(matdesc, ExtraMaterialsDescription.ConflictResolution.FAIL_FAST);
   }

   public ExtraMaterialsDescription(Map<String, String> matdesc, ExtraMaterialsDescription.ConflictResolution resolve) {
      if (matdesc != null && resolve != null) {
         this.extra = Collections.unmodifiableMap(new HashMap<>(matdesc));
         this.resolve = resolve;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Map<String, String> getMaterialDescription() {
      return this.extra;
   }

   public ExtraMaterialsDescription.ConflictResolution getConflictResolution() {
      return this.resolve;
   }

   public Map<String, String> mergeInto(Map<String, String> core) {
      if (this.extra.size() == 0) {
         return core;
      } else if (core != null && core.size() != 0) {
         switch(this.resolve) {
            case FAIL_FAST: {
               int total = core.size() + this.extra.size();
               Map<String, String> merged = new HashMap<>(core);
               merged.putAll(this.extra);
               if (total != merged.size()) {
                  throw new IllegalArgumentException("The supplemental material descriptions contains conflicting entries");
               }

               return Collections.unmodifiableMap(merged);
            }
            case OVERRIDDEN: {
               Map<String, String> merged = new HashMap<>(this.extra);
               merged.putAll(core);
               return Collections.unmodifiableMap(merged);
            }
            case OVERRIDE: {
               Map<String, String> merged = new HashMap<>(core);
               merged.putAll(this.extra);
               return Collections.unmodifiableMap(merged);
            }
            default:
               throw new UnsupportedOperationException();
         }
      } else {
         return this.extra;
      }
   }

   public static enum ConflictResolution {
      FAIL_FAST,
      OVERRIDE,
      OVERRIDDEN;
   }
}
