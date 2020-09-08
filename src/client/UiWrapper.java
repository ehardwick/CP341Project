package client;

import java.awt.Container;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import util.User;
import java.awt.GridBagConstraints;

@SuppressWarnings("serial")
public class UiWrapper extends JFrame {
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
    UiWrapper uiTest = new UiWrapper();
    uiTest.addPanels(uiTest.getContentPane());
  }

  public UiWrapper() {
    User user = new User.Builder()
        .withUserId(1)
        .withUsername("username")
        .build();

    this.grid = new GridBagLayout();
    this.gbc = new GridBagConstraints();
    setLayout(grid);
    setTitle("Messaging Client UI Test");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.layout = new GridBagLayout();
    this.setLayout(layout);

    // initialize the panels
    this.sentMessagesPanel = new SentMessagesPanel(user);
    this.inputPanel = new InputPanel();
    this.chatsPanel = new ChatsPanel();
    this.chatTitlePanel = new ChatTitlePanel();
  }

  public void addPanels(Container pane) {
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 6;
    gbc.weightx = 0.0;
    gbc.weighty = 1.0;
    this.add(chatsPanel, gbc);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 10;
    gbc.gridheight = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    this.add(chatTitlePanel, gbc);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 10;
    gbc.gridheight = 4;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    this.add(sentMessagesPanel, gbc);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.gridwidth = 10;
    gbc.gridheight = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    this.add(inputPanel, gbc);

    pack();
    setVisible(true);
  }
}
