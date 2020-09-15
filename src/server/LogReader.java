package server;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
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

  public Map<Long, MessageThread> getMessageThreads() {
    try {
      messageThreadsLog =
          gson.fromJson(new FileReader("MessageThreadsLog.txt"), MessageThreadsLog.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to Get Message Threads", e);
    }
    return messageThreadsLog.getMap();
  }

  public void saveMessageThreads(MessageThreadsLog messageThreadsLog) {
    while (true) {
      PrintWriter out;
      try {
        out = new PrintWriter("MessageThreadsLog.txt");
        out.println(gson.toJson(messageThreadsLog, MessageThreadsLog.class));
        out.close();
        break;
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Failed to Save Users", e);
      }
    }
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
    while (true) {
      PrintWriter out;
      try {
        out = new PrintWriter("UserLog.txt");
        out.println(gson.toJson(userLog, UserLog.class));
        out.close();
        break;
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Failed to Save Users", e);
      }
    }
  }

  public Map<String, List<MessageThread>> getUserMessageThreads() {
    try {
      userMessageThreadsLog =
          gson.fromJson(new FileReader("UserMessageThreadsLog.txt"), UserMessageThreadsLog.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to Get User Message Threads", e);
    }
    return userMessageThreadsLog.getMap();
  }

  public void saveUserMessageThreads(UserMessageThreadsLog userMessageThreadsLog) {
    while (true) {
      PrintWriter out;
      try {
        out = new PrintWriter("UserMessageThreadsLog.txt");
        out.println(gson.toJson(userMessageThreadsLog, UserMessageThreadsLog.class));
        out.close();
        break;
      } catch (FileNotFoundException e) {
        System.out.println("retrying race condition");
      }
    }
  }
}
