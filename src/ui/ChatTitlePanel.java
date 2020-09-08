package ui;

import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ChatTitlePanel extends JPanel {

  private String chatTitle = "Chat Title";

  public ChatTitlePanel() {
    JPanel panel = new JPanel();

    JTextArea textArea = new JTextArea(chatTitle);
    textArea.setOpaque(false);
    textArea.setEditable(false);
    textArea.setFocusable(false);

    panel.add(textArea);
    add(panel);
  }

  public void setChatTitle(String chatTitle) {
    this.chatTitle = chatTitle;
  }
}
