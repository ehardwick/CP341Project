package util;

import java.util.ArrayList;
import java.util.List;

public class MessageThread {
  private List<User> owners;
  private List<Message> messages = new ArrayList<>();
  private String name;
  private long messageThreadId;

  public static class Builder {
    private List<User> owners;
    private String name;
    private long messageThreadId;

    public Builder withOwners(List<User> owners) {
      this.owners = owners;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withMessageThreadId(long messageThreadId) {
      this.messageThreadId = messageThreadId;
      return this;
    }

    public MessageThread build() {
      MessageThread messageThread = new MessageThread();
      messageThread.owners = this.owners;
      messageThread.name = this.name;
      messageThread.messageThreadId = this.messageThreadId;
      return messageThread;
    }
  }

  public List<User> getOwners() {
    return owners;
  }

  public void addMessage(Message message) {
    messages.add(message);
  }

  public void addMessage(List<Message> message) {
    messages.addAll(message);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public long getMessageThreadId() {
    return messageThreadId;
  }
}
