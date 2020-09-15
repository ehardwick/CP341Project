package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMessageThreadsLog {

  Map<String, List<MessageThread>> userMessageThreads;

  public UserMessageThreadsLog() {
    userMessageThreads = new HashMap<>();
  }

  public UserMessageThreadsLog(Map<String, List<MessageThread>> userMessageThreads) {
    this.userMessageThreads = userMessageThreads;
  }

  public Map<String, List<MessageThread>> getMap() {
    return userMessageThreads;
  }

  public void putIfAbsent(String user, List<MessageThread> messageThreads) {
    userMessageThreads.putIfAbsent(user, messageThreads);
  }
}
