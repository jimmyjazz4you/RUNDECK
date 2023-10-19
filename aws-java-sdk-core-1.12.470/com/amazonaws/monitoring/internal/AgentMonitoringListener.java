package com.amazonaws.monitoring.internal;

import com.amazonaws.SdkClientException;
import com.amazonaws.annotation.SdkInternalApi;
import com.amazonaws.annotation.SdkTestInternalApi;
import com.amazonaws.monitoring.MonitoringEvent;
import com.amazonaws.monitoring.MonitoringListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SdkInternalApi
public final class AgentMonitoringListener extends MonitoringListener {
   private static final Log LOG = LogFactory.getLog(AgentMonitoringListener.class);
   private static final String SIMPLE_NAME = "AgentMonitoringListener";
   private static final int MAX_BUFFER_SIZE = 8192;
   private AsynchronousAgentDispatcher dispatcher;
   private final DatagramChannel channel;
   private final int maxSize;

   public AgentMonitoringListener(String host, int port) throws SdkClientException {
      try {
         this.dispatcher = AsynchronousAgentDispatcher.getInstance();
         this.dispatcher.init();
         this.channel = DatagramChannel.open();
         this.channel.configureBlocking(false);
         if (this.channel.socket().getSendBufferSize() < 8192) {
            this.channel.socket().setSendBufferSize(8192);
         }

         this.maxSize = Math.min(8192, this.channel.socket().getSendBufferSize());
         if (this.maxSize < 8192 && LOG.isDebugEnabled()) {
            LOG.debug(String.format("System socket buffer size %d is less than 8K. Any events larger than the buffer size will be dropped", this.maxSize));
         }

         this.channel.connect(new InetSocketAddress(host, port));
      } catch (Exception var4) {
         if (this.dispatcher != null) {
            this.dispatcher.release();
         }

         throw new SdkClientException("Failed to initialize AgentMonitoringListener", var4);
      }
   }

   @SdkTestInternalApi
   AgentMonitoringListener(DatagramChannel channel, AsynchronousAgentDispatcher dispatcher, int maxSize) {
      this.channel = channel;
      this.dispatcher = dispatcher;
      this.maxSize = maxSize;
   }

   @Override
   public void handleEvent(MonitoringEvent event) {
      this.dispatcher.addWriteTask(event, this.channel, this.maxSize);
   }

   @Override
   public String toString() {
      return "AgentMonitoringListener";
   }

   public void shutdown() {
      this.dispatcher.release();

      try {
         this.channel.close();
      } catch (IOException var2) {
         LOG.error("Could not close datagram channel", var2);
      }
   }
}
