package ui;

import java.util.List;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import util.Message;
import util.MessageThread;
import util.User;

@SuppressWarnings("serial")
public class SentMessagesPanel extends JPanel implements MessageObserver, MessageThreadObserver {

  private DefaultTableModel tableModel;
  private JTable table;
  private User user;
  private LocalStorage localStorage;

  public SentMessagesPanel(LocalStorage localStorage) {
    this.tableModel = new DefaultTableModel(new Object[] {"", ""}, 1);
    this.table = new JTable(tableModel);
    this.localStorage = localStorage;
    this.user = localStorage.getClientUser();

    table.setCellSelectionEnabled(false);
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  private void addFromMessage(String message) {
    tableModel.addRow(new Object[] {"", message});
  }

  private void addToMessage(String message) {
    tableModel.addRow(new Object[] {message, ""});
  }

  @Override
  public void threadSwitched(long messageThreadId) {
    Optional<MessageThread> switchedToThread = localStorage.getMessageThreadById(messageThreadId);
    
    switchedToThread.ifPresent(thread -> {
      this.tableModel.setRowCount(0);
      thread.getMessages().forEach(message -> {
        if (user.getUsername() == message.getSender().getUsername()) {
          addFromMessage(message.getTextBody() + " (" + message.getStatus() + ")");
        } else {
          addToMessage(message.getTextBody() + " (" + message.getStatus() + ")");
        }
      });
      table.changeSelection(table.getRowCount() - 1, 0, false, false);
    });
  }

  @Override
  public void sendNewMessage(Message newMessage) {
    String formattedMessage = newMessage.getTextBody() + " (" + newMessage.getStatus() + ")";
    if (user.getUsername() == newMessage.getSender().getUsername()) {
      addFromMessage(formattedMessage);
    } else {
      addToMessage(formattedMessage);
    }
  }

  @Override
  public void addNewMessageThread(MessageThread newThread) {
    // nothing yet, will want to switch over automatically later
  }
}
