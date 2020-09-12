package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import client.Client;
import util.Message;
import util.MessageProposal;
import util.Request;
import util.MessageThread;
import util.MessageThreadList;
import util.MessageThreadProposal;
import util.User;
import util.Request.MessageType;
import util.Response;

public class LocalStorage implements MessageObserver, MessageThreadObserver {
  // Headers (aka the number of things server has to support)
  private static final String GET_MESSAGE_THREAD = "getMessageThread";
  private static final String GET_MESSAGE_THREADS_BY_USER = "getMessageThreadsByUser";
  private static final String GET_USER = "getUser";
  private static final String CREATE_NEW_MESSAGE_THREAD = "createNewMessageThread";
  private static final String SEND_NEW_MESSAGE = "sendNewMessage";

  // Locally cached versions of info stored by server
  private Map<Long, MessageThread> messageThreads;
  private Map<String, User> users;
  private Map<String, List<MessageThread>> userMessageThreads;

  // Information only stored locally
  private User clientUser;
  private long selectedThreadId;
  private long requestId = 0;

  private Client client;
  private Gson gson;

  public static class Builder {
    // Locally cached versions of info stored by server
    private Map<Long, MessageThread> messageThreads;
    private Map<String, User> users;
    private Map<String, List<MessageThread>> userMessageThreads;

    // Information only stored locally
    private User clientUser;
    private long selectedThreadId;

    public Builder withMessageThreads(Map<Long, MessageThread> messageThreads) {
      this.messageThreads = messageThreads;
      return this;
    }

    public Builder withUsers(Map<String, User> users) {
      this.users = users;
      return this;
    }

    public Builder withUserMessageThreads(Map<String, List<MessageThread>> userMessageThreads) {
      this.userMessageThreads = userMessageThreads;
      return this;
    }

    public Builder withUser(User clientUser) {
      this.clientUser = clientUser;
      return this;
    }

    public Builder withSelectedThreadId(long selectedThreadId) {
      this.selectedThreadId = selectedThreadId;
      return this;
    }

    public LocalStorage build() {
      LocalStorage localStorage = new LocalStorage();
      localStorage.messageThreads = this.messageThreads;
      localStorage.users = this.users;
      localStorage.userMessageThreads = this.userMessageThreads;
      localStorage.clientUser = this.clientUser;
      localStorage.selectedThreadId = this.selectedThreadId;

      return localStorage;
    }
  }

  public LocalStorage() {
    this.client = new Client();
    client.start();

    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    this.gson = builder.create();
  }

  public Map<Long, MessageThread> getMessageThreads() {
    return messageThreads;
  }

  public Map<String, User> getUsers() {
    return users;
  }

  /*
   * GET MESSAGE THREAD
   */

  public Optional<MessageThread> getMessageThread(long messageThreadId) {
    if (messageThreads.containsKey(messageThreadId)) {
      return Optional.of(messageThreads.get(messageThreadId));
    }
    return Optional.empty();
  }

  public Optional<MessageThread> getServerMessageThread(MessageThread thread) {
    Request getMessageThreadMessage = new Request.Builder().withMessageType(MessageType.GET)
        .withHeader(GET_MESSAGE_THREAD).withId(getNextRequestId()).withJsonBody(gson.toJson(thread))
        .withTime(new Date()).build();

    Optional<Response> response = client.newRequest(getMessageThreadMessage);

    if (response.isPresent()) {
      try {
        return Optional.of(gson.fromJson(response.get().getJsonBody(), MessageThread.class));
      } catch (JsonSyntaxException e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  public Optional<MessageThread> getMessageThread(MessageThread thread) {
    return getMessageThread(thread.getMessageThreadId());
  }

  public Optional<MessageThread> getServerMessageThread(long messageThreadId) {
    MessageThread toGet = new MessageThread.Builder().withMessageThreadId(messageThreadId).build();
    return getServerMessageThread(toGet);
  }

  /*
   * GET MESSAGE THREADS BY USER
   */

  public Optional<List<MessageThread>> getMessageThreadsByUser(User user) {
    return getMessageThreadsByUser(user.getUsername());
  }

  public Optional<List<MessageThread>> getServerMessageThreadsByUser(User user) {
    Request getMessageThreadsRequest = new Request.Builder().withMessageType(MessageType.GET)
        .withHeader(GET_MESSAGE_THREADS_BY_USER).withId(getNextRequestId())
        .withJsonBody(gson.toJson(user, User.class)).withTime(new Date()).build();

    Optional<Response> response = client.newRequest(getMessageThreadsRequest);

    if (response.isPresent()) {
      try {
        System.out.println("got these threads: ");
        gson.fromJson(response.get().getJsonBody(), MessageThreadList.class).getMessageThreads()
            .forEach(thread -> System.out.println(thread.getName()));
        return Optional.of(gson.fromJson(response.get().getJsonBody(), MessageThreadList.class)
            .getMessageThreads());
      } catch (JsonSyntaxException e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  public Optional<List<MessageThread>> getMessageThreadsByUser(String username) {
    if (userMessageThreads.containsKey(username)) {
      return Optional.of(userMessageThreads.get(username));
    }
    return Optional.empty();
  }

  public Optional<List<MessageThread>> getServerMessageThreadsByUser(String username) {
    User user = new User.Builder().withUsername(username).build();
    return getServerMessageThreadsByUser(user);
  }

  /*
   * GET USER
   */

  public Optional<User> getUser(String userName) {
    if (users.containsKey(userName)) {
      return Optional.of(users.get(userName));
    }
    return Optional.empty();
  }

  public Optional<User> getServerUser(String userName) {
    User user = new User.Builder().withUsername(userName).build();
    return getServerUser(user);
  }

  public Optional<User> getUser(User user) {
    return getUser(user.getUsername());
  }

  public Optional<User> getServerUser(User user) {
    Request getUserRequest = new Request.Builder().withMessageType(MessageType.GET)
        .withHeader(GET_USER).withId(getNextRequestId()).withJsonBody(gson.toJson(user, User.class))
        .withTime(new Date()).build();

    Optional<Response> response = client.newRequest(getUserRequest);

    if (response.isPresent()) {
      try {
        return Optional.of(gson.fromJson(response.get().getJsonBody(), User.class));
      } catch (JsonSyntaxException e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  /*
   * CREATE A NEW MESSAGE THREAD
   */

  public Optional<MessageThread> createNewMessageThread(List<String> ownerUsernames, String name) {
    List<User> owners = new ArrayList<>();
    for (String ownerUsername : ownerUsernames) {
      if (users.containsKey(ownerUsername)) {
        owners.add(users.get(ownerUsername));
      } else {
        System.out.println("failed to find user " + ownerUsername);
      }
    }
    owners.add(clientUser);

    MessageThreadProposal proposedThread =
        new MessageThreadProposal.Builder().withName(name).withOwners(owners).build();

    Request createThreadRequest = new Request.Builder().withMessageType(MessageType.GET)
        .withHeader(CREATE_NEW_MESSAGE_THREAD).withId(getNextRequestId())
        .withJsonBody(gson.toJson(proposedThread)).withTime(new Date()).build();

    Optional<Response> response = client.newRequest(createThreadRequest);

    if (response.isPresent()) {
      try {
        return Optional.of(gson.fromJson(response.get().getJsonBody(), MessageThread.class));
      } catch (JsonSyntaxException e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  /*
   * LOCAL ONLY STUFF
   */
  public User getClientUser() {
    return clientUser;
  }

  @Override
  public void threadSwitched(long messageThreadId) {
    selectedThreadId = messageThreadId;
  }

  @Override
  public void addNewMessageThread(MessageThread newThread) {
    messageThreads.put(newThread.getMessageThreadId(), newThread);
  }

  @Override
  public void sendNewMessage(Message newMessage) {
    MessageProposal messageProposal = new MessageProposal.Builder()
        .withMessageThreadId(selectedThreadId).withMessage(newMessage).build();

    Request sendMessageRequest = new Request.Builder().withMessageType(MessageType.POST)
        .withHeader(SEND_NEW_MESSAGE).withId(getNextRequestId())
        .withJsonBody(gson.toJson(messageProposal)).withTime(new Date()).build();

    Optional<Response> response = client.newRequest(sendMessageRequest);

    if (response.isPresent() && response.get().getSuccess()) {
      messageThreads.get(selectedThreadId).addMessage(newMessage);
    }
  }

  private long getNextRequestId() {
    requestId++;
    return requestId;
  }

  public void setMessageThreads(Map<Long, MessageThread> messageThreads) {
    this.messageThreads = messageThreads;
  }
  
  public void setUsers(Map<String, User> users) {
    this.users = users;
  }
}
