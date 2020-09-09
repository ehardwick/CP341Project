package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Message;
import util.MessageThread;
import util.User;

public class ServerStub {
  
  public Map<Long, MessageThread> getMessageThreads(User user){
    List<MessageThread> messageThreads = getMessageThreadsByUser(user);
    Map<Long, MessageThread> messageThreadMap = new HashMap<>();
    messageThreads.forEach(thread -> messageThreadMap.put(thread.getMessageThreadId(), thread));
    return messageThreadMap;
  }
  
  public void sendMessage(Message message, long messageThreadId) {
    //Send the message
  }
    
  public List<MessageThread> getMessageThreadsByUser(User user){
    User alice = new User.Builder()
        .withUserId(12345)
        .withUsername("alice username")
        .build();
    
    User bob = new User.Builder()
        .withUserId(1)
        .withUsername("bob username")
        .build();
    
    ArrayList<User> owners = new ArrayList<>();
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
    
    List<MessageThread> messageThreads = new ArrayList<>();
    messageThreads.add(threadOne);
    messageThreads.add(threadTwo);
    return messageThreads;
  }
}
