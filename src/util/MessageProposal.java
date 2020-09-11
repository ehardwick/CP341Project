package util;

import java.util.Date;

public class MessageProposal {

  public enum Status {
    UNSENT, SENDING, SENT, RECIEVED {
      @Override
      public Status next() {
        return this;
      };
    };
    public Status next() {
      return values()[ordinal() + 1];
    }
  }

  private Status status = Status.UNSENT;
  private User sender;
  private Date timeSent;
  private String textBody;
  private long messageThreadId;

  public static class Builder {
    private User sender;
    private Date timeSent;
    private String textBody;
    private long messageThreadId;

    public Builder withSender(User sender) {
      this.sender = sender;
      return this;
    }

    public Builder withTimeSent(Date timeSent) {
      this.timeSent = timeSent;
      return this;
    }

    public Builder withTextBody(String textBody) {
      this.textBody = textBody;
      return this;
    }

    public Builder withMessageThreadId(long messageThreadId) {
      this.messageThreadId = messageThreadId;
      return this;
    }

    public MessageProposal build() {
      MessageProposal message = new MessageProposal();
      message.sender = this.sender;
      message.timeSent = this.timeSent;
      message.textBody = this.textBody;
      message.messageThreadId = this.messageThreadId;
      return message;
    }
  }

  public Status getStatus() {
    return status;
  }

  public void nextStatus() {
    status.next();
  }

  public User getSender() {
    return sender;
  }

  public Date getTimeSent() {
    return timeSent;
  }

  public String getTextBody() {
    return textBody;
  }

  public long getMessageThreadId() {
    return messageThreadId;
  }
}
