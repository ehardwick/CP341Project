package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

  static final int PORT = 8888;

  private PrintWriter out;
  private BufferedReader serverIn;

  public void start() {
    Socket client;
    try {
      client = new Socket("127.0.0.1", PORT);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to create new Socket on Port %s", PORT), e);
    }
    try {
      out = new PrintWriter(client.getOutputStream(), true);
      serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
    } catch (IOException e) {
      throw new RuntimeException("Failed to Initialize IO Streams", e);
    }

    Thread readMessage = new Thread(() -> {
      while (true) {
        try {
          String message = serverIn.readLine();
          System.out.println(message);
        } catch (IOException e) {
          throw new RuntimeException("Failed to Read Server Input", e);
        }
      }
    });

    readMessage.start();
  }

  public static void main(String[] args) {
    Client chatClient = new Client();
    chatClient.start();
  }

  public boolean sendMessage(String input) {
    try {
      out.println("chat message: " + input);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
