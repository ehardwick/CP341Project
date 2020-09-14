package util;

import java.util.Date;

public class Request {
  public enum MessageType {
    GET, POST, PUT {};
  }

  private MessageType messageType;
  private String header;
  private long id;
  private String jsonBody;
  private Date time;

  public static class Builder {
    private MessageType messageType;
    private String header;
    private long id;
    private String jsonBody;
    private Date time;

    public Builder withMessageType(MessageType messageType) {
      this.messageType = messageType;
      return this;
    }

    public Builder withHeader(String header) {
      this.header = header;
      return this;
    }

    public Builder withId(long id) {
      this.id = id;
      return this;
    }

    public Builder withJsonBody(String jsonBody) {
      this.jsonBody = jsonBody;
      return this;
    }

    public Builder withTime(Date time) {
      this.time = time;
      return this;
    }

    public Request build() {
      Request messageJSON = new Request();
      messageJSON.messageType = this.messageType;
      messageJSON.header = this.header;
      messageJSON.id = this.id;
      messageJSON.jsonBody = this.jsonBody;
      messageJSON.time = this.time;
      return messageJSON;
    }
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public String getHeader() {
    return header;
  }

  public Long getId() {
    return id;
  }

  public String getJsonBody() {
    return jsonBody;
  }

  public Date getTime() {
    return time;
  }
}
