package server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Server {
  public static void main(String[] args) throws Exception {
    // Get the port number to listen to...
    int port1 = 8888;
    int port2 = 8080;

    // Get the directory that files should be written into
//    File writeDir = new File("./");
//    if (!writeDir.canWrite()) {
//      System.out.println("Can't write to directory... exiting");
//    }

    // Make the server socket
    ServerSocket c1_socket = new ServerSocket(port1);
    ServerSocket c2_socket = new ServerSocket(port2);
    
    while (true) {
      // Listen and accept client connection
      Socket client1 = c1_socket.accept();
      Socket client2 = c2_socket.accept();
      
      // Print what the Client sends
      byte[] bytes = new byte[1000];
      int rlen;
      rlen = client1.getInputStream().read(bytes);
      for(int i=0; i<rlen; i++) {
    	  System.out.println(bytes[i]);
      }
      
      // Send bytes to client2
      client2.getOutputStream().write(bytes);
      
      // Read the file size and filename
      // Read the first integer (4 bytes) for the file size
//      ByteBuffer bb = ByteBuffer.allocate(1000);
//      byte[] bytes = new byte[1000];
//      int actuallyRead = client.getInputStream().read(bytes, 0, 4);
//      while (actuallyRead < 4) {
//        actuallyRead += client.getInputStream().read(bytes, actuallyRead, 4 - actuallyRead);
//      }
//      bb.put(bytes);
//      for (int i = 0; i < 4; i++) {
//        System.err.println("bytes[" + i + "] = " + bytes[i]);
//      }
//      bb.rewind();
//      int filelen = bb.getInt(0);
//      System.err.println("File len of " + filelen);
//
//      // Read bytes until the null character (byte with value 0) for the
//      // filename, and convert to a String...
//      bb.mark();
//      int r;
//      int br = 4;
//      while ((r = client.getInputStream().read()) != 0) {
//        bb.put((byte) r);
//        br++;
//      }
//      System.out.println("Read " + br + " bytes of filename");
//      byte[] namebytes = new byte[bb.position()];
//      bb.reset();
//      bb.get(namebytes);
//      String filename = new String(namebytes);
//
//      System.out.println("Request to send " + filename + " with length " + filelen);
//
//      // ACK the req to send
//      client.getOutputStream().write((byte) 1);
//
//      // Write to disk... in the right directory...
//      File writeFile = new File(writeDir, filename);
//      FileOutputStream fstream = new FileOutputStream(writeFile);
//
//      // actual writing
//      // Read the bytes for the file
//      InputStream in = client.getInputStream();
//
//      for (int i = 0; i < filelen; i++) {
//        fstream.write(in.read());
//      }
//
//      // close the file
//      fstream.close();
//
//      // ACK the file
//      client.getOutputStream().write((byte) 1);
//      client.close();
    }
  }
}
