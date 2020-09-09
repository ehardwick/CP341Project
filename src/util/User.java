package util;

import java.util.Map;

public class User {
  private long userId;
  private String username;
  private Map<String, Long> contacts;

  public static class Builder {
    private long userId;
    private String username;
    private Map<String, Long> contacts;

    public Builder withUserId(long userId) {
      this.userId = userId;
      return this;
    }

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder withContacts(Map<String, Long> contacts) {
      this.contacts = contacts;
      return this;
    }

    public User build() {
      User user = new User();
      user.userId = this.userId;
      user.username = this.username;
      user.contacts = this.contacts;
      return user;
    }
  }

  public long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public Map<String, Long> getContacts() {
    return contacts;
  }
}
