package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.Message;
import util.MessageProposal;
import util.MessageThread;
import util.MessageThreadList;
import util.MessageThreadProposal;
import util.MessageThreadsLog;
import util.Request;
import util.Response;
import util.User;
import util.UserLog;
import util.UserMessageThreadsLog;

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
  LogReader logReader = new LogReader();
  private int portNumber;
      
  public static void main(String[] args) {
    Server server = new Server(Integer.parseInt(args[1]));
    server.start();
  }

  public Server(int portNumber) {
    this.portNumber = portNumber;
  }
  
  public void start() {
    messageThreads = logReader.getMessageThreads();
    users = logReader.getUsers();
    userMessageThreads = logReader.getUserMessageThreads();
    System.out.println(String.format("Server is online, listening on port %s", portNumber));
    Socket socket;
    try {
      ServerSocket serverSocket = new ServerSocket(portNumber);
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
	private LogReader logReader;

    public ClientThread(Socket socket, Map<Long, MessageThread> messageThreads, Map<String, User> users, Map<String, List<MessageThread>> userMessageThreads) {
      this.messageThreads = messageThreads;
      this.users = users;
      this.userMessageThreads = userMessageThreads;
      this.socket = socket;
      this.gson = new GsonBuilder().setPrettyPrinting().create();
      this.logReader = new LogReader();


//      LogReader logReader = new LogReader();
//      this.messageThreads = logReader.getMessageThreads();
//      users = logReader.getUsers();
//      userMessageThreads = logReader.getUserMessageThreads();
    }
    
    /* 
     * returns the next available message thread ID
     * TODO: handle race condition!!!
     */
    private Long nextMessageThreadId() {
    	List<Long> keyList = new ArrayList<Long>(this.messageThreads.keySet());
    	Collections.reverse(keyList);
    	return (keyList.get(0) + 1);
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
    
    private UserLog userLog;
    private MessageThreadsLog messageThreadsLog;
    private UserMessageThreadsLog userMessageThreadsLog;

    public Response handleRequest(Request request) {
	  userLog = new UserLog();
      messageThreadsLog = new MessageThreadsLog();
	  userMessageThreadsLog = new UserMessageThreadsLog();
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
              
              userMessageThreads.entrySet().forEach(e -> userMessageThreadsLog.putIfAbsent(e.getKey(), e.getValue()));
  			  logReader.saveUserMessageThreads(userMessageThreadsLog);
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
                
                users.entrySet().forEach(e -> userLog.putIfAbsent(e.getKey(), e.getValue()));
    			logReader.saveUsers(userLog);
            }
          } catch (Exception e) {
            System.out.println("Getting user broke");
          }
          break;
          
        case CREATE_NEW_MESSAGE_THREAD:
          //System.out.println("here2");
        	try {
        		MessageThreadProposal mtProposal = 
        			gson.fromJson(request.getJsonBody(), MessageThreadProposal.class);
        		MessageThread mThread = new MessageThread.Builder()
    				.withOwners(mtProposal.getOwners())
    				.withName(mtProposal.getName())
    				.withMessageThreadId(this.nextMessageThreadId())
    				.build();
        		// add new thread to messageThreads
        		this.messageThreads.put(this.nextMessageThreadId(), mThread);
        		// add new thread to each owner's list of messageThreads (or make new list and add it)
        		for (User owner : mtProposal.getOwners()) {
        			List<MessageThread> currList = this.userMessageThreads.get(owner.getUsername());
        			if (currList == null) {
        				currList = new ArrayList<MessageThread>();
        				currList.add(mThread);	
        			}
        			else {
        				currList.add(mThread);
        			}
        			this.userMessageThreads.put(owner.getUsername(), currList);
        			
        			userMessageThreads.entrySet().forEach(e -> userMessageThreadsLog.putIfAbsent(e.getKey(), e.getValue()));
        			logReader.saveUserMessageThreads(userMessageThreadsLog);
        		}
        		jsonBody = gson.toJson(mThread);
                success = true;
        	} catch (Exception e) {
        		System.out.println("poop");
        	}
          break;
          
        case SEND_NEW_MESSAGE:
          System.out.println("here2");
          try {
            MessageProposal messageProposal =
                gson.fromJson(request.getJsonBody(), MessageProposal.class);

            Message message = messageProposal.getMessage();

            messageThreads.get(messageProposal.getMessageThreadId()).addMessage(message);
            System.out.println("added message " + message.getTextBody() + " to message thread " + messageThreads.get(messageProposal.getMessageThreadId()).getName());

            jsonBody = gson.toJson(message);
            success = true;
            
            System.out.println("successfully did new message case");
            
            messageThreads.entrySet().forEach(e -> messageThreadsLog.putIfAbsent(e.getKey(), e.getValue()));
            logReader.saveMessageThreads(messageThreadsLog);

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
