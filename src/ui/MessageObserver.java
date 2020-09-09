package ui;

import util.Message;

public interface MessageObserver {
  public void newMessage(Message newMessage);
}
