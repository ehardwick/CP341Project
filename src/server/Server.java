package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

  private static Set<PrintWriter> writers = new HashSet<>();
  static final int PORT = 8888;

  public static void main(String[] args) {
    System.out.println(String.format("Server is online, listening on port %s", PORT));
    Socket socket;
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      while (true) {
        socket = serverSocket.accept();
        new ClientThread(socket).start();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to connect to new Server Socket", e);
    }
  }

  private static class ClientThread extends Thread {

    // Socket used for a conversation
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket socket) {
      this.socket = socket;
    }

    public void run() {
      try {
        out = new PrintWriter(socket.getOutputStream(), true);
        writers.add(out);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException e) {
        throw new RuntimeException("Failed to Initialize IO Streams", e);
      }

      String message;
      while (true) {
        try {
          message = in.readLine();
          if ((message == null) || message.equals("DISCONNECT")) {
            socket.close();
            return;
          } else {
            for (PrintWriter writer : writers) {
              writer.println(message);
            }
          }
        } catch (IOException e) {
          throw new RuntimeException("Failed to Process Message", e);
        }
      }
    }
  }
}
