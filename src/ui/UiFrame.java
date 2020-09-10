package ui;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  UserInfoPanel userInfoPanel;
  LoginPanel loginPanel;
  
  //
  private ServerStub serverStub;
  private LocalStorage localStorage;

  public UiFrame(ServerStub serverStub) {
    this.serverStub = serverStub;
    this.grid = new GridBagLayout();
    this.gbc = new GridBagConstraints();
    
    setLayout(grid);
    setTitle("Messaging Client UI Test");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
    
    setupLoginPanels();
    displayLoginPanels();
  }
  
  public void setUser(User user) {
    this.user = user;
    Map<Long, MessageThread> messageThreads = new HashMap<>();
    serverStub.getMessageThreadsByUser(user.getUsername()).get().forEach(thread -> messageThreads.put(thread.getMessageThreadId(), thread));
    this.localStorage = new LocalStorage.Builder()
        .withUser(user)
        .withServerStub(serverStub)
        .withMessageThreads(messageThreads)
        .withUsers(serverStub.getUsers())
        .build();
    
    setupChatPanels();
    displayChatPanels();
  }
  
  private void setupChatPanels() {
    this.sentMessagesPanel = new SentMessagesPanel(localStorage);
    this.inputPanel = new InputPanel(user);
    this.chatsPanel = new ChatsPanel(localStorage);
    chatsPanel.setMessageThreads(serverStub.getMessageThreadsByUser(user).get());
    this.chatTitlePanel = new ChatTitlePanel(localStorage);
    this.userInfoPanel = new UserInfoPanel(localStorage);

    // Message Observers (Updated when a new message is requested to be sent by the user
    List<MessageObserver> messageObservers = new ArrayList<>();
    messageObservers.add(localStorage);
    messageObservers.add(sentMessagesPanel);
    inputPanel.setMessageObservers(messageObservers);

    // Message Thread Observers (Updated when the selected message thread changes)
    List<MessageThreadObserver> messageThreadObservers = new ArrayList<>();
    messageThreadObservers.add(sentMessagesPanel);
    messageThreadObservers.add(chatTitlePanel);
    messageThreadObservers.add(localStorage);
    messageThreadObservers.add(inputPanel);
    chatsPanel.setMessageThreadObservers(messageThreadObservers);

    pack();
    setVisible(true);
    
    invalidate();
    validate();
    repaint();
  }
        
  private void displayChatPanels() {
    remove(loginPanel);
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
    
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    this.add(userInfoPanel, gbc);

    pack();
    setVisible(true);
    
    invalidate();
    validate();
    repaint();
  }
  
  private void setupLoginPanels() {
    this.loginPanel = new LoginPanel(this, serverStub);
    
    pack();
    setVisible(true);
    
    invalidate();
    validate();
    repaint();
  }
  
  private void displayLoginPanels() {
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    this.add(loginPanel, gbc);
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
