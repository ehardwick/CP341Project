package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.Request;
import util.Response;

public class Client {

  static final int PORT = 8888;

  private PrintWriter out;
  private BufferedReader serverIn;

  private Map<Long, Thread> threadRequests;
  private Map<Long, Response> threadResponses;
  
  private Gson gson;

  public Client() {
    threadRequests = new HashMap<>();
    threadResponses = new HashMap<>();
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    this.gson = builder.create();
  }

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
          while (serverIn.ready()) {
            message = message + serverIn.readLine();
          }
          Response response = gson.fromJson(message, Response.class);
          threadResponses.put(response.getId(), response);
          // TODO read message, check which requestId it has, throw it in the soup (aka put the
          // response Object into the threadResponse map and notify the request thread)
          System.out.println(message);
        } catch (IOException e) {
          throw new RuntimeException("Failed to Read Server Input", e);
        }
      }
    });

    readMessage.start();
  }

  public Optional<Response> newRequest(Request request) {
    Thread requestThread = new Thread(() -> {
      while(!threadResponses.containsKey(request.getId())) {
        
      }
    });
    
    threadRequests.put(request.getId(), requestThread);
    out.println(gson.toJson(request, Request.class));
    System.out.println("test");
    requestThread.run();
    
    threadRequests.put(request.getId(), requestThread);
    
    try {
      requestThread.join();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return threadResponses.containsKey(request.getId()) ? Optional.of(threadResponses.get(request.getId())) : Optional.empty();
  }
}
