package Client;

import java.awt.Container;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import java.awt.GridBagConstraints;

public class UiTestMain extends JFrame {
  // GridBag garbage
  private GridBagLayout grid;
  private GridBagConstraints gbc;
  private GridBagLayout layout;

  // Different Panels
  SentMessagesPanel sentMessagesPanel;
  InputPanel inputPanel;
  ChatsPanel chatsPanel;
  ChatTitlePanel chatTitlePanel;

  public static void main(String[] args) {
    UiTestMain uiTest = new UiTestMain();
  }

  public UiTestMain() {
    this.grid = new GridBagLayout();
    this.gbc = new GridBagConstraints();
    setLayout(grid);
    setTitle("Messaging Client UI Test");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.layout = new GridBagLayout();
    this.setLayout(layout);

    // initialize the panels
    this.sentMessagesPanel = new SentMessagesPanel();
    this.inputPanel = new InputPanel();
    this.chatsPanel = new ChatsPanel(sentMessagesPanel, inputPanel);
    this.chatTitlePanel = new ChatTitlePanel();
    
    test(this.getContentPane());

  }

  public void test(Container pane) {
 
    // 🤌 spaghetti 🤌
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 6;
    this.add(chatsPanel, gbc);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(chatTitlePanel, gbc);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.gridheight = 4;
    gbc.fill = GridBagConstraints.BOTH;
    this.add(sentMessagesPanel, gbc);
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.gridwidth = 3;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(inputPanel, gbc);

    pack();
    setVisible(true);
  }
}
