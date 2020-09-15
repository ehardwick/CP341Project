package server;

import util.MessageThreadsLog;
import util.UserLog;
import util.UserMessageThreadsLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.Message;
import util.MessageThread;
import util.User;


public class ResetLogsToTestValues {
  private LogReader logReader = new LogReader();

  public void trueMain() {
    UserLog userLog = new UserLog();
    MessageThreadsLog messageThreadsLog = new MessageThreadsLog();
    UserMessageThreadsLog userMessageThreadsLog = new UserMessageThreadsLog();

    // initialize some users for testing
    User alice = new User.Builder().withUsername("Alice").build();
    User bob = new User.Builder().withUsername("Bob").build();

    userLog.putIfAbsent(alice.getUsername(), alice);
    userLog.putIfAbsent(bob.getUsername(), bob);

    logReader.saveUsers(userLog);

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

    messageThreadsLog.putIfAbsent(threadOne.getMessageThreadId(), threadOne);
    messageThreadsLog.putIfAbsent(threadTwo.getMessageThreadId(), threadTwo);

    logReader.saveMessageThreads(messageThreadsLog);

    // initialize userToMessageThreads with testing data
    messageThreadsLog.getMap().forEach((k, v) -> v.getOwners().forEach(o -> {
      if (userMessageThreadsLog.getMap().containsKey(o.getUsername())) {
        userMessageThreadsLog.getMap().get(o.getUsername()).add(v);
      } else {
        List<MessageThread> threads = new ArrayList<>();
        threads.add(v);
        userMessageThreadsLog.putIfAbsent(o.getUsername(), threads);
      }
    }));

    logReader.saveUserMessageThreads(userMessageThreadsLog);
  }
}
