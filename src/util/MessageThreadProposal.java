package util;

import java.util.List;

public class MessageThreadProposal {
  private List<User> owners;
  private String name;

  public static class Builder {
    private List<User> owners;
    private String name;

    public Builder withOwners(List<User> owners) {
      this.owners = owners;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public MessageThreadProposal build() {
      MessageThreadProposal messageThread = new MessageThreadProposal();
      messageThread.owners = this.owners;
      messageThread.name = this.name;
      return messageThread;
    }
  }

  public List<User> getOwners() {
    return owners;
  }

  public String getName() {
    return name;
  }
}
