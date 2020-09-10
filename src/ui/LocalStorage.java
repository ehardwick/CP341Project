package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import client.Client;
import util.Message;
import util.MessageThread;
import util.User;

public class LocalStorage implements MessageObserver, MessageThreadObserver {
  // Locally cached versions of info stored by server
  private Map<Long, MessageThread> messageThreads;
  private Map<String, User> users;
  private Map<String, List<MessageThread>> userMessageThreads;


  // Information only stored locally
  private User clientUser;
  private long selectedThreadId;

  private ServerStub serverStub;
  private Client client;

  public static class Builder {
    // Locally cached versions of info stored by server
    private Map<Long, MessageThread> messageThreads;
    private Map<String, User> users;
    private Map<String, List<MessageThread>> userMessageThreads;

    // Information only stored locally
    private User clientUser;
    private long selectedThreadId;

    private ServerStub serverStub;

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

    public Builder withServerStub(ServerStub serverStub) {
      this.serverStub = serverStub;
      return this;
    }

    public LocalStorage build() {
      LocalStorage localStorage = new LocalStorage();
      localStorage.messageThreads = this.messageThreads;
      localStorage.users = this.users;
      localStorage.userMessageThreads = this.userMessageThreads;
      localStorage.clientUser = this.clientUser;
      localStorage.selectedThreadId = this.selectedThreadId;
      localStorage.serverStub = this.serverStub;

      return localStorage;
    }
  }

  public LocalStorage() {
    this.client = new Client();
    client.start();
  }

  /*
   * METHODS TO REFRESH INFO FROM SERVER
   */

  public void refreshAllUsers() {
    users = serverStub.getUsers();
  }

  public void refreshAllMessageThreads() {
    messageThreads = serverStub.getMessageThreads();
  }

  public void refreshMessageThread(MessageThread thread) {
    Optional<MessageThread> serverCopy =
        serverStub.getMessageThreadById(thread.getMessageThreadId());
    serverCopy.ifPresent(copy -> messageThreads.put(thread.getMessageThreadId(), copy));
  }

  public void refreshMessageThread(long messageThreadId) {
    Optional<MessageThread> serverCopy = serverStub.getMessageThreadById(messageThreadId);
    serverCopy.ifPresent(copy -> messageThreads.put(messageThreadId, copy));
  }

  /*
   * GETTERS FOR INFO SHARED WITH SERVER
   */

  public Map<Long, MessageThread> getMessageThreads() {
    return messageThreads;
  }

  public Map<String, User> getUsers() {
    return users;
  }

  public Optional<List<MessageThread>> getMessageThreadsByUser(User user) {
    if (userMessageThreads.containsKey(user.getUsername())) {
      return Optional.of(userMessageThreads.get(user.getUsername()));
    }
    return Optional.empty();
  }

  public Optional<MessageThread> getMessageThreadById(Long messageThreadId) {
    if (messageThreads.containsKey(messageThreadId)) {
      return Optional.of(messageThreads.get(messageThreadId));
    }
    return Optional.empty();
  }

  public Optional<User> getUserbyUsername(String userName) {
    if (users.containsKey(userName)) {
      return Optional.of(users.get(userName));
    }
    return Optional.empty();
  }

  /*
   * GETTERS FOR LOCAL ONLY INFO
   */
  public User getClientUser() {
    return clientUser;
  }

  /*
   * METHODS FOR DIFFERENT ACTIONS
   */

  @Override
  public void threadSwitched(long messageThreadId) {
    selectedThreadId = messageThreadId;
  }

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
    Optional<MessageThread> newThread = serverStub.createNewMessageThread(owners, name);
    newThread.ifPresent(thread -> messageThreads.put(thread.getMessageThreadId(), thread));
    return newThread;
  }

  @Override
  public void addNewMessageThread(MessageThread newThread) {
    messageThreads.put(newThread.getMessageThreadId(), newThread);
  }

  @Override
  public void sendNewMessage(Message newMessage) {
    // Send the message to the server to be delivered and added to the server messageThread
    boolean accepted = client.sendMessage("sending message with text: " + newMessage.getTextBody()
        + " from " + newMessage.getSender().getUsername() + " to thread " + selectedThreadId
        + " with name " + messageThreads.get(selectedThreadId).getName());
    // if accepted add the new message to the locally stored messageThread
    if (accepted) {
      messageThreads.get(selectedThreadId).addMessage(newMessage);
    }
  }
}
