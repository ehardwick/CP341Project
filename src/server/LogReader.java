package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Message;
import util.MessageThread;
import util.User;

/**
 * This class fills in for not being able to read text to make the logs yet
 *
 */
public class LogReader {
  private Map<Long, MessageThread> messageThreads;
  private Map<String, User> users;
  private Map<String, List<MessageThread>> userMessageThreads;

  public LogReader() {
    messageThreads = new HashMap<>();
    users = new HashMap<>();
    userMessageThreads = new HashMap<>();

    // initialize some users for testing
    User alice = new User.Builder().withUsername("Alice").build();

    User bob = new User.Builder().withUsername("Bob").build();

    users.put(alice.getUsername(), alice);
    users.put(bob.getUsername(), bob);

    // initialize some message threads for testing
    List<User> owners = new ArrayList<>();
    owners.add(alice);
    owners.add(bob);

    MessageThread threadOne = new MessageThread.Builder().withMessageThreadId(1).withOwners(owners)
        .withName("message thread name #1").build();

    Message firstMessage = new Message.Builder().withSender(bob).withTextBody("bob's text body 1")
        .withTimeSent(new Date()).build();

    threadOne.addMessage(firstMessage);

    Message secondMessage = new Message.Builder().withSender(bob).withTextBody("bob's text body 2")
        .withTimeSent(new Date()).build();

    threadOne.addMessage(secondMessage);

    Message thirdMessage = new Message.Builder().withSender(alice)
        .withTextBody("alice's text body 1").withTimeSent(new Date()).build();

    threadOne.addMessage(thirdMessage);

    MessageThread threadTwo = new MessageThread.Builder().withMessageThreadId(2).withOwners(owners)
        .withName("message thread name #2").build();

    Message firstMessageTwo = new Message.Builder().withSender(bob)
        .withTextBody("2nd thread bob's text body 1").withTimeSent(new Date()).build();

    threadTwo.addMessage(firstMessageTwo);

    Message secondMessageTwo = new Message.Builder().withSender(bob)
        .withTextBody("2nd thread bob's text body 2").withTimeSent(new Date()).build();

    threadTwo.addMessage(secondMessageTwo);

    Message thirdMessageTwo = new Message.Builder().withSender(alice)
        .withTextBody("2nd thread alice's text body 1").withTimeSent(new Date()).build();

    threadTwo.addMessage(thirdMessageTwo);

    messageThreads.put(threadOne.getMessageThreadId(), threadOne);
    messageThreads.put(threadTwo.getMessageThreadId(), threadTwo);

    // initialize userToMessageThreads with testing data
    messageThreads.forEach((k, v) -> v.getOwners().forEach(o -> {
      if (userMessageThreads.containsKey(o.getUsername())) {
        userMessageThreads.get(o.getUsername()).add(v);
      } else {
        List<MessageThread> threads = new ArrayList<>();
        threads.add(v);
        userMessageThreads.put(o.getUsername(), threads);
      }
    }));
  }

  public Map<Long, MessageThread> getMessageThreads() {
    return messageThreads;
  }

  public Map<String, User> getUsers() {
    return users;
  }

  public Map<String, List<MessageThread>> getUserMessageThreads() {
    return userMessageThreads;
  }
}
