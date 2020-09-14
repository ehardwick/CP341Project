package util;

import java.util.Date;

public class MessageProposal {
  
  private long messageThreadId;
  private Message message;

  public static class Builder {
    private Message message;
    private long messageThreadId;

    public Builder withMessage(Message message) {
      this.message = message;
      return this;
    }

    public Builder withMessageThreadId(long messageThreadId) {
      this.messageThreadId = messageThreadId;
      return this;
    }

    public MessageProposal build() {
      MessageProposal messageProposal = new MessageProposal();
      messageProposal.message = this.message;
      messageProposal.messageThreadId = this.messageThreadId;
      return messageProposal;
    }
  }

  public long getMessageThreadId() {
    return messageThreadId;
  }
  
  public Message getMessage() {
    return message;
  }
}
