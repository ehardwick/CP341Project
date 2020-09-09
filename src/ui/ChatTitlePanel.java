package ui;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import util.MessageThread;

@SuppressWarnings("serial")
public class ChatTitlePanel extends JPanel implements MessageThreadObserver {

  private String chatTitle = "Chat Title";

  public ChatTitlePanel() {
    JTextArea textArea = new JTextArea(chatTitle);
    textArea.setOpaque(false);
    textArea.setEditable(false);
    textArea.setFocusable(false);

    add(textArea);
  }
  
  @Override
  public void threadSwitched(MessageThread newThread) {
    chatTitle = newThread.getName();
  }
}
