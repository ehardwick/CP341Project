package util;

public class User {
  private String username;

  public static class Builder {
    private String username;

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public User build() {
      User user = new User();
      user.username = this.username;
      return user;
    }
  }

  public String getUsername() {
    return username;
  }
}
