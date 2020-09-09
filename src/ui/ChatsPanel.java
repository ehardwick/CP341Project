package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import util.MessageThread;

@SuppressWarnings("serial")
public class ChatsPanel extends JPanel {

  private List<MessageThreadObserver> messageThreadObservers;
  private JList<String> jList;
  private final DefaultListModel<String> model = new DefaultListModel<String>();
  private Map<String, Long> nameToMessageId;
  private ChatLogDB chatLogDB;


  public ChatsPanel(ChatLogDB chatLogDB) {
    this.chatLogDB = chatLogDB;
    this.jList = new JList<String>(model);
    ListSelectionListener listSelectionListener = new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
          JList list = (JList) listSelectionEvent.getSource();
          setActive(nameToMessageId.get(list.getSelectedValue()));
        }
      }
    };

    jList.addListSelectionListener(listSelectionListener);

    add(jList);
  }

  public void setMessageThreads(List<MessageThread> messageThreadsList) {
    this.nameToMessageId = new HashMap<>();
    List<String> messageThreadsNames = new ArrayList<>();
    messageThreadsList
        .forEach(thread -> messageThreadsNames.add(thread.getName()));
    messageThreadsList
        .forEach(thread -> nameToMessageId.put(thread.getName(), thread.getMessageThreadId()));
    Collections.sort(messageThreadsNames);
    model.clear();
    messageThreadsNames.forEach(name -> model.addElement(name));
  }

  private void setActive(Long messageThreadId) {
    messageThreadObservers.forEach(
        observer -> observer.threadSwitched(chatLogDB.getMessageThread(messageThreadId)));
  }

  public String getSelected() {
    return jList.getSelectedValue();
  }

  public void setMessageThreadObservers(List<MessageThreadObserver> messageThreadObservers) {
    this.messageThreadObservers = messageThreadObservers;
  }
}
