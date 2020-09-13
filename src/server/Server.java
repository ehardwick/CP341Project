package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.Message;
import util.MessageProposal;
import util.MessageThread;
import util.MessageThreadList;
import util.Request;
import util.Response;
import util.User;

public class Server {
  private static final String GET_MESSAGE_THREAD = "getMessageThread";
  private static final String GET_MESSAGE_THREADS_BY_USER = "getMessageThreadsByUser";
  private static final String GET_USER = "getUser";
  private static final String CREATE_NEW_MESSAGE_THREAD = "createNewMessageThread";
  private static final String SEND_NEW_MESSAGE = "sendNewMessage";
  
  private Map<Long, MessageThread> messageThreads;
  private Map<String, User> users;
  private Map<String, List<MessageThread>> userMessageThreads;

  private static Set<PrintWriter> writers = new HashSet<>();
  static final int PORT = 8888;

  public static void main(String[] args) {
    Server server = new Server();
    server.trueMain();
  }
  
  public void trueMain() {
    LogReader logReader = new LogReader();
    messageThreads = logReader.getMessageThreads();
    users = logReader.getUsers();
    userMessageThreads = logReader.getUserMessageThreads();
    System.out.println(String.format("Server is online, listeningsss on port %s", PORT));
    Socket socket;
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      while (true) {
        socket = serverSocket.accept();
        new ClientThread(socket, messageThreads, users, userMessageThreads).start();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to connect to new Server Socket", e);
    }
  }

  private static class ClientThread extends Thread {

    // Server has all the message threads and all the users
    private Map<Long, MessageThread> messageThreads;
    private Map<String, User> users;
    private Map<String, List<MessageThread>> userMessageThreads;

    // Socket used for a conversation
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private Gson gson;

    public ClientThread(Socket socket, Map<Long, MessageThread> messageThreads, Map<String, User> users, Map<String, List<MessageThread>> userMessageThreads) {
      this.messageThreads = messageThreads;
      this.users = users;
      this.userMessageThreads = userMessageThreads;
      this.socket = socket;
      this.gson = new GsonBuilder().setPrettyPrinting().create();

//      LogReader logReader = new LogReader();
//      this.messageThreads = logReader.getMessageThreads();
//      users = logReader.getUsers();
//      userMessageThreads = logReader.getUserMessageThreads();
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
          while (in.ready()) {
            message = message + in.readLine();
          }
          System.out.println("message at server: " + message);
          Request request = gson.fromJson(message, Request.class);

          message = gson.toJson(handleRequest(request));
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

    // Handles all the different types of requests to the server
    // Yeah, this is just a gross switch statement. it sure isn't pretty but it works

    public Response handleRequest(Request request) {
      String jsonBody = "";
      boolean success = false;
      switch (request.getHeader()) {
        case GET_MESSAGE_THREAD:
          try {
            MessageThread thread = gson.fromJson(request.getJsonBody(), MessageThread.class);
            if (messageThreads.containsKey(thread.getMessageThreadId())) {
              jsonBody = gson.toJson(messageThreads.get(thread.getMessageThreadId()));
              success = true;
              System.out.println("successfully did GET_MESSAGE_THREAD case");
            }
          } catch (Exception e) {
            System.out.println("Getting a message thread broke :((( this is so sad");
          }
          break;
        case GET_MESSAGE_THREADS_BY_USER:
          try {
            User user = gson.fromJson(request.getJsonBody(), User.class);
            if (userMessageThreads.containsKey(user.getUsername())) {
              MessageThreadList messageThreadList = new MessageThreadList();
              messageThreadList.setMessageThreads(userMessageThreads.get(user.getUsername()));
              jsonBody = gson.toJson(messageThreadList);
              success = true;
              System.out.println("successfully did GET_MESSAGE_THREADS_BY_USER case");
            }
          } catch (Exception e) {
            System.out.println("Getting a message threads by user broke");
          }
          break;
        case GET_USER:
          try {
            User user = gson.fromJson(request.getJsonBody(), User.class);
            if (this.users.containsKey(user.getUsername())) {
              jsonBody = gson.toJson(this.users.get(user.getUsername()));
              success = true;
              System.out.println("successfully did GET_USER case, grabbing existing user");
            }
            else {
            	this.users.put(user.getUsername(), user);
            	this.userMessageThreads.put(user.getUsername(), new ArrayList<MessageThread>());
            	jsonBody = gson.toJson(this.users.get(user.getUsername()));
                success = true;
                System.out.println("successfully did GET_USER case, created new user");
            }
          } catch (Exception e) {
            System.out.println("Getting user broke");
          }
          break;
        case CREATE_NEW_MESSAGE_THREAD:
          System.out.println("here2");
          break;
        case SEND_NEW_MESSAGE:
          System.out.println("here2");
          try {
            MessageProposal messageProposal =
                gson.fromJson(request.getJsonBody(), MessageProposal.class);

            Message message = messageProposal.getMessage();

            messageThreads.get(messageProposal.getMessageThreadId()).addMessage(message);
            System.out.println("added message " + message.getTextBody() + " to message trhead " + messageThreads.get(messageProposal.getMessageThreadId()).getName());

            jsonBody = gson.toJson(message);
            success = true;
            
            System.out.println("successfully did new message case");

          } catch (Exception e) {
            System.out.println("Adding a new message broke, darn");
          }
          break;
      }
      return new Response.Builder().withId(request.getId()).withSuccess(success)
          .withJsonBody(jsonBody).withTime(request.getTime()).build();
    }
  }
}
