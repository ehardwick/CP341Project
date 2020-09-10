package util;

import java.util.Map;

public class User {
  private String username;
  private Map<String, Long> contacts;

  public static class Builder {
    private String username;
    private Map<String, Long> contacts;

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
      user.username = this.username;
      user.contacts = this.contacts;
      return user;
    }
  }

  public String getUsername() {
    return username;
  }

  public Map<String, Long> getContacts() {
    return contacts;
  }
}
