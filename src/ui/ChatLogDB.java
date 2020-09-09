package ui;

import java.util.Map;
import util.Message;
import util.MessageThread;
import util.User;

public class ChatLogDB implements MessageObserver, MessageThreadObserver {

  private Map<Long, MessageThread> messageThreads;
  private User user;
  private long selectedThread;
  private ServerStub serverStub;

  public static class Builder {
    private Map<Long, MessageThread> messageThreads;
    private User user;
    private long selectedThread;
    private ServerStub serverStub;

    public Builder withUser(User user) {
      this.user = user;
      return this;
    }

    public Builder withMessageThreads(Map<Long, MessageThread> messageThreads) {
      this.messageThreads = messageThreads;
      return this;
    }

    public Builder withSelectedThread(long selectedThread) {
      this.selectedThread = selectedThread;
      return this;
    }

    public Builder withServerStub(ServerStub serverStub) {
      this.serverStub = serverStub;
      return this;
    }

    public ChatLogDB build() {
      ChatLogDB chatLogDB = new ChatLogDB();
      chatLogDB.user = this.user;
      chatLogDB.messageThreads = this.messageThreads;
      chatLogDB.selectedThread = this.selectedThread;
      chatLogDB.serverStub = this.serverStub;
      return chatLogDB;
    }
  }

  @Override
  public void newMessage(Message newMessage) {
    // Add the new message to the local messageThread
    messageThreads.get(selectedThread).addMessage(newMessage);
    // Send the message to the server to be delivered and added to the server messageThread
    serverStub.sendMessage(newMessage, messageThreads.get(selectedThread).getMessageThreadId());
  }

  public User getUser() {
    return user;
  }
  
  public MessageThread getMessageThread(long messageThreadId){
    return messageThreads.get(messageThreadId);
  }

  @Override
  public void threadSwitched(MessageThread newThread) {
    selectedThread = newThread.getMessageThreadId();
  }

}
