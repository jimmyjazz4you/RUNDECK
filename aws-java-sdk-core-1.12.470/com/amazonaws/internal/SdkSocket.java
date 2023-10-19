package com.amazonaws.internal;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SdkSocket extends DelegateSocket {
   private static final Log log = LogFactory.getLog(SdkSocket.class);

   public SdkSocket(Socket sock) {
      super(sock);
      if (log.isDebugEnabled()) {
         log.debug("created: " + this.endpoint());
      }
   }

   private String endpoint() {
      return this.sock.getInetAddress() + ":" + this.sock.getPort();
   }

   @Override
   public void connect(SocketAddress endpoint) throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("connecting to: " + endpoint);
      }

      this.sock.connect(endpoint);
      if (log.isDebugEnabled()) {
         log.debug("connected to: " + this.endpoint());
      }
   }

   @Override
   public void connect(SocketAddress endpoint, int timeout) throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("connecting to: " + endpoint);
      }

      this.sock.connect(endpoint, timeout);
      if (log.isDebugEnabled()) {
         log.debug("connected to: " + this.endpoint());
      }
   }

   @Override
   public void close() throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("closing " + this.endpoint());
      }

      this.sock.close();
   }

   @Override
   public void shutdownInput() throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("shutting down input of " + this.endpoint());
      }

      this.sock.shutdownInput();
   }

   @Override
   public void shutdownOutput() throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("shutting down output of " + this.endpoint());
      }

      this.sock.shutdownOutput();
   }
}
