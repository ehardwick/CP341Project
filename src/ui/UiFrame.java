package ui;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import util.MessageThread;
import util.User;
import java.awt.GridBagConstraints;

@SuppressWarnings("serial")
public class UiFrame extends JFrame {
  // User Information
  private User user;

  // GridBag stuff
  private GridBagLayout grid;
  private GridBagConstraints gbc;

  // Different Panels
  SentMessagesPanel sentMessagesPanel;
  InputPanel inputPanel;
  ChatsPanel chatsPanel;
  ChatTitlePanel chatTitlePanel;

  public UiFrame(User user) {
    this.user = user;
    this.grid = new GridBagLayout();
    this.gbc = new GridBagConstraints();
    setLayout(grid);
    setTitle("Messaging Client UI Test");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ServerStub fakeServer = new ServerStub();
    
    ChatLogDB chatLogDB = new ChatLogDB.Builder()
        .withMessageThreads(fakeServer.getMessageThreads(user))
        .withUser(user)
        .withServerStub(fakeServer)
        .build();
        
    this.sentMessagesPanel = new SentMessagesPanel(user);
    this.inputPanel = new InputPanel(user);
    this.chatsPanel = new ChatsPanel(chatLogDB);
    chatsPanel.setMessageThreads(fakeServer.getMessageThreadsByUser(user));
    this.chatTitlePanel = new ChatTitlePanel();

    // Message Observers (Updated when a new message is requested to be sent by the user
    List<MessageObserver> messageObservers = new ArrayList<>();
    messageObservers.add(chatLogDB);
    messageObservers.add(sentMessagesPanel);
    inputPanel.setMessageObservers(messageObservers);

    // Message Thread Observers (Updated when the selected message thread changes)
    List<MessageThreadObserver> messageThreadObservers = new ArrayList<>();
    messageThreadObservers.add(sentMessagesPanel);
    messageThreadObservers.add(chatTitlePanel);
    messageThreadObservers.add(chatLogDB);
    chatsPanel.setMessageThreadObservers(messageThreadObservers);

    pack();
    setVisible(true);
    
    invalidate();
    validate();
    repaint();
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
    
    invalidate();
    validate();
    repaint();
    
  }

  public void setMessageThreads(List<MessageThread> messageThreads) {
    chatsPanel.setMessageThreads(messageThreads);
  }

  public User getUser() {
    return user;
  }

}
