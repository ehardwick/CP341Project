package util;

import java.util.Date;

public class Response {
  private long id;
  private boolean success;
  private String jsonBody;
  private Date time;

  public static class Builder {
    private long id;
    private boolean success;
    private String jsonBody;
    private Date time;

    public Builder withId(long id) {
      this.id = id;
      return this;
    }

    public Builder withSuccess(boolean success) {
      this.success = success;
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

    public Response build() {
      Response response = new Response();
      response.id = this.id;
      response.success = this.success;
      response.jsonBody = this.jsonBody;
      response.time = this.time;
      return response;
    }
  }

  public Long getId() {
    return id;
  }

  public boolean getSuccess() {
    return success;
  }

  public String getJsonBody() {
    return jsonBody;
  }

  public Date getTime() {
    return time;
  }
}
