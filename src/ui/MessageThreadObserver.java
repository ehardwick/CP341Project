package ui;

import util.MessageThread;

public interface MessageThreadObserver {
  public void threadSwitched(long messageThreadId);

  public void addNewMessageThread(MessageThread newThread);
}
