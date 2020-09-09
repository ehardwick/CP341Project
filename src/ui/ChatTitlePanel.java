package ui;

import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import util.MessageThread;

@SuppressWarnings("serial")
public class ChatTitlePanel extends JPanel implements MessageThreadObserver {
  private static final String DEFAULT_CHAT_TITLE = "Chat Title";
  private LocalStorage localStorage;
  private JTextArea textArea;

  public ChatTitlePanel(LocalStorage localStorage) {
    this.localStorage = localStorage;
    this.textArea = new JTextArea(DEFAULT_CHAT_TITLE);
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
