package ui;

import util.Message;

public interface MessageObserver {
  public void sendNewMessage(Message newMessage);
}
