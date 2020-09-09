package ui;

import util.MessageThread;

public interface MessageThreadObserver {
  public void threadSwitched(MessageThread newThread);
}
