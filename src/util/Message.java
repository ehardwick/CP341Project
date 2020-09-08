package util;

import java.util.Date;

public class Message {
  private boolean sent = false;
  private User sender;
  private Date timeSent;
  private String textBody;

  public static class Builder {
    private User sender;
    private Date timeSent;
    private String textBody;

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

    public Message build() {
      Message message = new Message();
      message.sender = this.sender;
      message.timeSent = this.timeSent;
      message.textBody = this.textBody;
      return message;
    }
  }

  public boolean getSent() {
    return sent;
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
}
