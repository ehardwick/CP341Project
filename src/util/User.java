package util;

public class User {
  private long userId;
  private String username;

  public static class Builder {
    private long userId;
    private String username;

    public Builder withUserId(long userId) {
      this.userId = userId;
      return this;
    }

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public User build() {
      User user = new User();
      user.userId = this.userId;
      user.username = this.username;
      return user;
    }
  }

  public long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }
}
