package com.amazonaws.internal;

import com.amazonaws.annotation.NotThreadSafe;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@NotThreadSafe
public class ReleasableInputStream extends SdkFilterInputStream implements Releasable {
   private static final Log log = LogFactory.getLog(ReleasableInputStream.class);
   private boolean closeDisabled;

   protected ReleasableInputStream(InputStream is) {
      super(is);
   }

   @Override
   public final void close() {
      if (!this.closeDisabled) {
         this.doRelease();
      }
   }

   @Override
   public final void release() {
      this.doRelease();
   }

   private void doRelease() {
      try {
         this.in.close();
      } catch (Exception var2) {
         if (log.isDebugEnabled()) {
            log.debug("FYI", var2);
         }
      }

      if (this.in instanceof Releasable) {
         Releasable r = (Releasable)this.in;
         r.release();
      }

      this.abortIfNeeded();
   }

   public final boolean isCloseDisabled() {
      return this.closeDisabled;
   }

   public final <T extends ReleasableInputStream> T disableClose() {
      this.closeDisabled = true;
      return (T)this;
   }

   public static ReleasableInputStream wrap(InputStream is) {
      if (is instanceof ReleasableInputStream) {
         return (ReleasableInputStream)is;
      } else {
         return (ReleasableInputStream)(is instanceof FileInputStream
            ? ResettableInputStream.newResettableInputStream((FileInputStream)is)
            : new ReleasableInputStream(is));
      }
   }
}
