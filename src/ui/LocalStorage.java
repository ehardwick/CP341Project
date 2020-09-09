package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import util.Message;
import util.MessageThread;
import util.User;

public class LocalStorage implements MessageObserver, MessageThreadObserver {
  // Locally cached versions of info stored by server
  private Map<Long, MessageThread> messageThreads;
  private Map<Long, User> users;
  private Map<Long, String> messageNames;
  private Map<Long, List<MessageThread>> userMessageThreads;

  
  // Information only stored locally
  private User clientUser;
  private long selectedThreadId;
  
  private ServerStub serverStub;

  public static class Builder {
    // Locally cached versions of info stored by server
    private Map<Long, MessageThread> messageThreads;
    private Map<Long, User> users;
    private Map<Long, String> messageNames;
    private Map<Long, List<MessageThread>> userMessageThreads;
    
    // Information only stored locally
    private User clientUser;
    private long selectedThreadId;
    
    private ServerStub serverStub;

    public Builder withMessageThreads(Map<Long, MessageThread> messageThreads) {
      this.messageThreads = messageThreads;
      return this;
    }
    
    public Builder withUsers(Map<Long, User> users) {
      this.users = users;
      return this;
    }
    
    public Builder withMessageNames(Map<Long, String> messageNames) {
      this.messageNames = messageNames;
      return this;
    }
    
    public Builder withUserMessageThreads(Map<Long, List<MessageThread>> userMessageThreads) {
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
      localStorage.messageNames = this.messageNames;
      localStorage.userMessageThreads = this.userMessageThreads;
      localStorage.clientUser = this.clientUser;
      localStorage.selectedThreadId = this.selectedThreadId;
      localStorage.serverStub = this.serverStub;

      return localStorage;
    }
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
    Optional<MessageThread> serverCopy = serverStub.getMessageThreadById(thread.getMessageThreadId());
    serverCopy.ifPresent(copy -> messageThreads.put(thread.getMessageThreadId(), copy));
  }
  
  public void refreshMessageThread(long messageThreadId) {
    Optional<MessageThread> serverCopy = serverStub.getMessageThreadById(messageThreadId);
    serverCopy.ifPresent(copy -> messageThreads.put(messageThreadId, copy));
  }
  
  /*
   * GETTERS FOR INFO SHARED WITH SERVER
   */

  public Map<Long, MessageThread> getMessageThreads(){
    return messageThreads;
  }
  
  public Map<Long, User> getUsers(){
    return users;
  }
 
  public Optional<List<MessageThread>> getMessageThreadsByUser(User user) {
    if(userMessageThreads.containsKey(user.getUserId())) {
      return Optional.of(userMessageThreads.get(user.getUserId()));
    }
    return Optional.empty();
  }
  
  public Optional<MessageThread> getMessageThreadById(Long messageThreadId){
    if(messageThreads.containsKey(messageThreadId)) {
      return Optional.of(messageThreads.get(messageThreadId));
    }
    return Optional.empty();
  }
  
  public Optional<User> getUserById(Long userId){
    if(users.containsKey(userId)) {
      return Optional.of(users.get(userId));
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

  public Optional<MessageThread> createNewMessageThread(List<String> ownerContacts, String name) {
    List<User> owners = new ArrayList<>();
    for(String contact : ownerContacts) {
      if(clientUser.getContacts().containsKey(contact)) {
        owners.add(users.get(clientUser.getContacts().get(contact)));
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
    boolean accepted = serverStub.sendMessage(newMessage, messageThreads.get(selectedThreadId).getMessageThreadId());
    // if accepted add the new message to the locally stored messageThread
    if(accepted) {
      messageThreads.get(selectedThreadId).addMessage(newMessage);
    }
  }

  public void addNewContact(String contactName, long contactId) {
    clientUser.getContacts().put(contactName, contactId);
    // Let server know about the new contact and know to update it's list of contacts for the clientUser
  }

}
