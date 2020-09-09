package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import util.Message;
import util.MessageThread;

@SuppressWarnings("serial")
public class ChatsPanel extends JPanel {

  private List<MessageThreadObserver> messageThreadObservers;
  private JList<String> jList;
  private List<MessageThread> messageThreads;
  private final DefaultListModel<String> model = new DefaultListModel<String>();
  private LocalStorage localStorage;

  public ChatsPanel(LocalStorage localStorage) {
    this.localStorage = localStorage;
    Container container = this.getParent();
    BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(boxLayout);

    JButton createNew = new JButton("Create New");
    createNew.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String ownersStr =
            JOptionPane.showInputDialog(container, "Owners: (user1, user2...)", null);
        String title = JOptionPane.showInputDialog(container, "Title", ownersStr);
        createNewMessageThread(ownersStr, title);
      }
    });

    this.jList = new JList<String>(model);
    ListSelectionListener listSelectionListener = new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
          JList list = (JList) listSelectionEvent.getSource();
          Map<Long,MessageThread> threads = localStorage.getMessageThreads();
          threads.forEach((k,v) -> {
            if(v.getName().equals(list.getSelectedValue())) {
              setActive(v.getMessageThreadId());
            }
          });
        }
      }
    };

    jList.addListSelectionListener(listSelectionListener);
    jList.setAlignmentX(jList.getAlignmentX() - 20);

    add(createNew);
    add(jList);
  }

  private void createNewMessageThread(String owners, String title) {
    String[] ownersArr = owners.split(", ");
    
    List<String> ownersList = Arrays.asList(ownersArr);
    
    Optional<MessageThread> newMessageThread = localStorage.createNewMessageThread(ownersList, title);
    newMessageThread.ifPresent(thread -> {
      messageThreadObservers.forEach(observer -> observer.addNewMessageThread(thread));
      model.addElement(thread.getName());
    });
  }

  public void setMessageThreads(List<MessageThread> messageThreads) {
    this.messageThreads = messageThreads;
    List<String> messageThreadsNames = new ArrayList<>();
    messageThreads.forEach(thread -> messageThreadsNames.add(thread.getName()));
    Collections.sort(messageThreadsNames);
    model.clear();
    messageThreadsNames.forEach(name -> model.addElement(name));
  }

  private void setActive(Long messageThreadId) {
    Optional<MessageThread> nowActive = localStorage.getMessageThreadById(messageThreadId);
    nowActive.ifPresent(thread -> messageThreadObservers.forEach(observer -> observer.threadSwitched(thread.getMessageThreadId())));
  }

  public String getSelected() {
    return jList.getSelectedValue();
  }

  public void setMessageThreadObservers(List<MessageThreadObserver> messageThreadObservers) {
    this.messageThreadObservers = messageThreadObservers;
  }
}
