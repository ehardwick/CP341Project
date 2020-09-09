package ui;

import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import util.MessageThread;

@SuppressWarnings("serial")
public class UserInfoPanel extends JPanel implements MessageThreadObserver {
  private LocalStorage localStorage;
  private JTextArea textArea;

  public UserInfoPanel(LocalStorage localStorage) {
    this.localStorage = localStorage;
    JTextArea textArea = new JTextArea("Username: " + localStorage.getClientUser().getUsername()
        + "\nUserId: " + localStorage.getClientUser().getUserId());
    textArea.setOpaque(false);
    textArea.setEditable(false);
    textArea.setFocusable(false);

    add(textArea);
  }

  @Override
  public void addNewMessageThread(MessageThread newThread) {
    // nothing yet will want to switch automaticaly

  }

  @Override
  public void threadSwitched(long messageThreadId) {
    Optional<MessageThread> switchedToThread = localStorage.getMessageThreadById(messageThreadId);
    switchedToThread.ifPresent(thread -> textArea.setText(thread.getName()));
  }
}
