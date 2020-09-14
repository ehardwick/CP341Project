package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.Message;
import util.MessageProposal;
import util.MessageThread;
import util.MessageThreadList;
import util.Request;
import util.Response;
import util.User;

public class Client {

  private int PORT = 0;

  private int[] portNumbers = {8888, 8000};

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
    Socket client = null;
    for (int port : portNumbers) {
      try {
        client = new Socket("127.0.0.1", port);
        break;
      } catch (Exception e) {
        System.out.println("failed to connect to port " + port);
      }
    }
    if (client == null) {
      throw new RuntimeException("Was never able to find a port to connect to");
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
    long startTime = System.currentTimeMillis();

    out.println(gson.toJson(request, Request.class));
    while (!threadResponses.containsKey(request.getId())) {
      if (System.currentTimeMillis() - startTime > 5000) {
        start();
        newRequest(request);
      }
    }

    return threadResponses.containsKey(request.getId())
        ? Optional.of(threadResponses.get(request.getId()))
        : Optional.empty();
  }
}
