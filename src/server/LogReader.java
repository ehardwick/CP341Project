package server;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import util.Message;
import util.MessageThread;
import util.MessageThreadsLog;
import util.User;
import util.UserLog;
import util.UserMessageThreadsLog;

public class LogReader {
  private UserLog userLog;
  private MessageThreadsLog messageThreadsLog;
  private UserMessageThreadsLog userMessageThreadsLog;
  private Gson gson = new Gson();

  public LogReader() {
    userLog = new UserLog();
    messageThreadsLog = new MessageThreadsLog();
    userMessageThreadsLog = new UserMessageThreadsLog();

    // initialize some users for testing
    User alice = new User.Builder().withUsername("Alice").build();
    User bob = new User.Builder().withUsername("Bob").build();
    
    userLog.putIfAbsent(alice.getUsername(), alice);    
    userLog.putIfAbsent(bob.getUsername(), bob);

    saveUsers(userLog);
    
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
    
    saveMessageThreads(messageThreadsLog);

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
    
    saveUserMessageThreads(userMessageThreadsLog);
  }

  public Map<Long, MessageThread> getMessageThreads() {
	  try {
		messageThreadsLog = gson.fromJson(new FileReader("MessageThreadsLog.txt"), MessageThreadsLog.class);
	} catch (Exception e) {
		throw new RuntimeException("Failed to Get Message Threads", e);
	}
	  return messageThreadsLog.getMap();
  }
  
  public void saveMessageThreads(MessageThreadsLog messageThreadsLog) {
	  PrintWriter out;
	try {
		out = new PrintWriter("MessageThreadsLog.txt");
	} catch (FileNotFoundException e) {
		throw new RuntimeException("Failed to Save Users", e);
	}
	  out.println(gson.toJson(messageThreadsLog, MessageThreadsLog.class));
	  out.close();
  }
  
  public Map<String, User> getUsers() {
	  try {
		userLog = gson.fromJson(new FileReader("UserLog.txt"), UserLog.class);
	} catch (Exception e) {
		throw new RuntimeException("Failed to Get Users", e);
	}
	  return userLog.getMap();
  }
  
  
  public void saveUsers(UserLog userLog) {
	  PrintWriter out;
	try {
		out = new PrintWriter("UserLog.txt");
	} catch (FileNotFoundException e) {
		throw new RuntimeException("Failed to Save Users", e);
	}
	  out.println(gson.toJson(userLog, UserLog.class));
	  out.close();
  }

  public Map<String, List<MessageThread>> getUserMessageThreads() {
	  try {
		  userMessageThreadsLog = gson.fromJson(new FileReader("UserMessageThreadsLog.txt"), UserMessageThreadsLog.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to Get User Message Threads", e);
		}
		  return userMessageThreadsLog.getMap();
  }
  
  public void saveUserMessageThreads(UserMessageThreadsLog userMessageThreadsLog) {
	  PrintWriter out;
		try {
			out = new PrintWriter("UserMessageThreadsLog.txt");
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to Save Users", e);
		}
		  out.println(gson.toJson(userMessageThreadsLog, UserMessageThreadsLog.class));
		  out.close();
  }
}
