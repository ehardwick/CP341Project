package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import util.Message;
import util.MessageThread;
import util.User;

public class ServerStub {

  // Server has all the message threads and all the users
  private Map<Long, MessageThread> messageThreads;
  private Map<Long, User> users;
  private Map<Long, List<MessageThread>> userMessageThreads;

  public ServerStub() {
    // initialize the maps
    messageThreads = new HashMap<>();
    users = new HashMap<>();
    userMessageThreads = new HashMap<>();
    
    Map<String, Long> contacts = new HashMap<>();
    contacts.put("Alice", 1l);
    contacts.put("Bob", 2l);
    // initialize some users for testing
    User alice = new User.Builder()
        .withUserId(1)
        .withUsername("Alice")
        .withContacts(contacts)
        .build();
    
    User bob = new User.Builder()
        .withUserId(2)
        .withUsername("Bob")
        .withContacts(contacts)
        .build();
    
    users.put(alice.getUserId(), alice);
    users.put(bob.getUserId(), bob);
    
    // initialize some message threads for testing
    List<User> owners = new ArrayList<>();
    owners.add(alice);
    owners.add(bob);
    
    MessageThread threadOne =
        new MessageThread.Builder()
        .withMessageThreadId(1)
        .withOwners(owners)
        .withName("message thread name #1")
        .build();

    Message firstMessage = new Message.Builder()
        .withSender(bob)
        .withTextBody("bob's text body 1")
        .withTimeSent(new Date())
        .build();

    threadOne.addMessage(firstMessage);

    Message secondMessage = new Message.Builder()
        .withSender(bob)
        .withTextBody("bob's text body 1")
        .withTimeSent(new Date())
        .build();

    threadOne.addMessage(secondMessage);

    Message thirdMessage = new Message.Builder()
        .withSender(alice)
        .withTextBody("alice's text body 1")
        .withTimeSent(new Date())
        .build();

    threadOne.addMessage(thirdMessage);
    
    MessageThread threadTwo =
        new MessageThread.Builder()
        .withMessageThreadId(2)
        .withOwners(owners)
        .withName("message thread name #2")
        .build();

    Message firstMessageTwo = new Message.Builder()
        .withSender(bob)
        .withTextBody("2nd thread bob's text body 1")
        .withTimeSent(new Date())
        .build();

    threadTwo.addMessage(firstMessageTwo);

    Message secondMessageTwo = new Message.Builder()
        .withSender(bob)
        .withTextBody("2nd thread bob's text body 1")
        .withTimeSent(new Date())
        .build();

    threadTwo.addMessage(secondMessageTwo);

    Message thirdMessageTwo = new Message.Builder()
        .withSender(alice)
        .withTextBody("2nd thread alice's text body 1")
        .withTimeSent(new Date())
        .build();

    threadTwo.addMessage(thirdMessageTwo);
    
    messageThreads.put(threadOne.getMessageThreadId(), threadOne);
    messageThreads.put(threadTwo.getMessageThreadId(), threadTwo);
    
    // initialize userToMessageThreads with testing data
    messageThreads.forEach((k,v) -> v.getOwners().forEach(o -> {
      if(userMessageThreads.containsKey(o.getUserId())) {
        userMessageThreads.get(o.getUserId()).add(v);
      } else {
        List<MessageThread> threads = new ArrayList<>();
        threads.add(v);
        userMessageThreads.put(o.getUserId(),threads);
      }
    }));
  }

  /*
   * GETTERS
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
  
  public Optional<List<MessageThread>> getMessageThreadsByUser(Long userId) {
    if(userMessageThreads.containsKey(userId)) {
      return Optional.of(userMessageThreads.get(userId));
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
  
  public Optional<Map<Long, String>> getMessageThreadNamesForUser(Long userId){
    Optional<List<MessageThread>> threadsForUser = getMessageThreadsByUser(userId);
    Map<Long, String> messageThreadNames = new HashMap<>();
    if(threadsForUser.isPresent()) {
      threadsForUser.get().forEach(thread -> messageThreadNames.put(thread.getMessageThreadId(), thread.getName()));
      return Optional.of(messageThreadNames);
    }
    return Optional.empty();  
  }
  
  /*
   * ASKING SERVER TO DO STUFF
   */

  public Optional<MessageThread> createNewMessageThread(List<User> owners, String title) {
    System.out.println("this isn't fully implemented, server would give messageThreadId and if the server is not successfully contacted this method should return Optional.empty()");
    return Optional.of(
        new MessageThread.Builder()
        .withMessageThreadId(12345)
        .withOwners(owners)
        .withName(title)
        .build());
  }
}
