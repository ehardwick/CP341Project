package client;

import java.net.Socket;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class Client {
  /**
   * Scans a directory for the next file we can transmit
   * 
   * @param fromdir directory to read
   * @return the next file to transmit
   */
  public static File nextFile(File fromdir) {
    while (true) {
      File[] files = fromdir.listFiles(new FileFilter() {
        public boolean accept(File f) {
          return f.isFile() && f.canRead();
        }
      });
      if (files.length < 1) {
        Thread.yield();
      } else {
        return files[0];
      }
    }
  }

  // Really should handle the exceptions.... but that logic has been cut for time...
  // (The end user is going to hate how easy our program is to crash)
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Need to provide the directory to watch...");
      return;
    }
    // Figure out which directory to watch... from the arguments
    String watchdir = args[0];
    File fromdir = new File(watchdir);
    if (!fromdir.isDirectory()) {
      System.err.println(args[0] + " is not a directory. Exiting...");
      return;
    }
    if (!fromdir.canRead()) {
      System.err.println(args[0] + " is not readable by this user. Exiting...");
      return;
    }
    if (!fromdir.canWrite()) {
      System.err.println(args[0] + " is not writeable by this user. Exiting...");
      return;
    }

    // Figure out where the server is... Hard coded
    String host = "127.0.0.1";
    int port = 8888;

    while (true) {
      // Detect a file in the directory
      File toSend = nextFile(fromdir);
      // The cast on the next line could lead to unexpected problems after deployment
      int filesize = (int) toSend.length();
      System.err.println("Sending size " + filesize);
      String filename = toSend.getName();


      // Send the file to B
      // Connect to B using TCP
      Socket srv = new Socket(host, port);
      // send a request that a file be sent to B
      // A file send request sends 4 bytes of size and terminates filename with \0
      // ...This means we need a way to convert this stuff to an array of bytes...
      ByteBuffer bb = ByteBuffer.allocate(1000);
      bb.putInt(filesize);
      bb.put(filename.getBytes());
      System.err.println("Sender thinks the name is " + filename);
      bb.put((byte) 0);
      byte[] sendBytes = new byte[bb.position()];
      bb.rewind();
      bb.get(sendBytes);
      for (int i = 0; i < 4; i++) {
        System.err.println("bytes[" + i + "] = " + sendBytes[i]);
      }
      System.err.println("Sender thinks their sending " + sendBytes.length);
      srv.getOutputStream().write(sendBytes);
      // wait for B to say it's ok to send a file
      int ack = srv.getInputStream().read();
      if (ack != 1) {
        // Server did not ACK? ... known potential problem...
      }
      // Open the file
      FileInputStream fstream = new FileInputStream(toSend);
      // Send all the bits in the file in order to the server
      int r;
      while ((r = fstream.read()) != -1) {
        srv.getOutputStream().write(r);
      }
      // Close the file
      fstream.close();

      // Recv an ACK
      // wait for B to say everything was received
      ack = srv.getInputStream().read();
      if (ack != 1) {
        // Server did not ACK? ... known potential problem...
      }

      // Remove the file
      toSend.delete();
    }
  }
}
