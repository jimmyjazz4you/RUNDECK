package com.amazonaws.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class DelegateSocket extends Socket {
   protected final Socket sock;

   public DelegateSocket(Socket sock) {
      this.sock = sock;
   }

   @Override
   public void connect(SocketAddress endpoint) throws IOException {
      this.sock.connect(endpoint);
   }

   @Override
   public void connect(SocketAddress endpoint, int timeout) throws IOException {
      this.sock.connect(endpoint, timeout);
   }

   @Override
   public void bind(SocketAddress bindpoint) throws IOException {
      this.sock.bind(bindpoint);
   }

   @Override
   public InetAddress getInetAddress() {
      return this.sock.getInetAddress();
   }

   @Override
   public InetAddress getLocalAddress() {
      return this.sock.getLocalAddress();
   }

   @Override
   public int getPort() {
      return this.sock.getPort();
   }

   @Override
   public int getLocalPort() {
      return this.sock.getLocalPort();
   }

   @Override
   public SocketAddress getRemoteSocketAddress() {
      return this.sock.getRemoteSocketAddress();
   }

   @Override
   public SocketAddress getLocalSocketAddress() {
      return this.sock.getLocalSocketAddress();
   }

   @Override
   public SocketChannel getChannel() {
      return this.sock.getChannel();
   }

   @Override
   public InputStream getInputStream() throws IOException {
      return this.sock.getInputStream();
   }

   @Override
   public OutputStream getOutputStream() throws IOException {
      return this.sock.getOutputStream();
   }

   @Override
   public void setTcpNoDelay(boolean on) throws SocketException {
      this.sock.setTcpNoDelay(on);
   }

   @Override
   public boolean getTcpNoDelay() throws SocketException {
      return this.sock.getTcpNoDelay();
   }

   @Override
   public void setSoLinger(boolean on, int linger) throws SocketException {
      this.sock.setSoLinger(on, linger);
   }

   @Override
   public int getSoLinger() throws SocketException {
      return this.sock.getSoLinger();
   }

   @Override
   public void sendUrgentData(int data) throws IOException {
      this.sock.sendUrgentData(data);
   }

   @Override
   public void setOOBInline(boolean on) throws SocketException {
      this.sock.setOOBInline(on);
   }

   @Override
   public boolean getOOBInline() throws SocketException {
      return this.sock.getOOBInline();
   }

   @Override
   public void setSoTimeout(int timeout) throws SocketException {
      this.sock.setSoTimeout(timeout);
   }

   @Override
   public int getSoTimeout() throws SocketException {
      return this.sock.getSoTimeout();
   }

   @Override
   public void setSendBufferSize(int size) throws SocketException {
      this.sock.setSendBufferSize(size);
   }

   @Override
   public int getSendBufferSize() throws SocketException {
      return this.sock.getSendBufferSize();
   }

   @Override
   public void setReceiveBufferSize(int size) throws SocketException {
      this.sock.setReceiveBufferSize(size);
   }

   @Override
   public int getReceiveBufferSize() throws SocketException {
      return this.sock.getReceiveBufferSize();
   }

   @Override
   public void setKeepAlive(boolean on) throws SocketException {
      this.sock.setKeepAlive(on);
   }

   @Override
   public boolean getKeepAlive() throws SocketException {
      return this.sock.getKeepAlive();
   }

   @Override
   public void setTrafficClass(int tc) throws SocketException {
      this.sock.setTrafficClass(tc);
   }

   @Override
   public int getTrafficClass() throws SocketException {
      return this.sock.getTrafficClass();
   }

   @Override
   public void setReuseAddress(boolean on) throws SocketException {
      this.sock.setReuseAddress(on);
   }

   @Override
   public boolean getReuseAddress() throws SocketException {
      return this.sock.getReuseAddress();
   }

   @Override
   public void close() throws IOException {
      this.sock.close();
   }

   @Override
   public void shutdownInput() throws IOException {
      this.sock.shutdownInput();
   }

   @Override
   public void shutdownOutput() throws IOException {
      this.sock.shutdownOutput();
   }

   @Override
   public String toString() {
      return this.sock.toString();
   }

   @Override
   public boolean isConnected() {
      return this.sock.isConnected();
   }

   @Override
   public boolean isBound() {
      return this.sock.isBound();
   }

   @Override
   public boolean isClosed() {
      return this.sock.isClosed();
   }

   @Override
   public boolean isInputShutdown() {
      return this.sock.isInputShutdown();
   }

   @Override
   public boolean isOutputShutdown() {
      return this.sock.isOutputShutdown();
   }

   @Override
   public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
      this.sock.setPerformancePreferences(connectionTime, latency, bandwidth);
   }
}
