package util;

import java.util.HashMap;
import java.util.Map;

public class MessageThreadsLog {

  Map<Long, MessageThread> messageThreads;

  public MessageThreadsLog() {
    messageThreads = new HashMap<>();
  }

  public MessageThreadsLog(Map<Long, MessageThread> messageThreads) {
    this.messageThreads = messageThreads;
  }

  public Map<Long, MessageThread> getMap() {
    return messageThreads;
  }

  public void putIfAbsent(Long id, MessageThread messageThread) {
    messageThreads.putIfAbsent(id, messageThread);
  }
}
