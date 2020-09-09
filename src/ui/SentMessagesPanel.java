package ui;

import java.util.List;
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

  public SentMessagesPanel(User user) {
    this.tableModel = new DefaultTableModel(new Object[] {"", ""}, 1);
    this.table = new JTable(tableModel);
    this.user = user;

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
  public void threadSwitched(MessageThread newThread) {
    List<Message> messages = newThread.getMessages();
    String formattedMessage;

    this.tableModel.setRowCount(0);

    for (Message message : messages) {
      formattedMessage = message.getTextBody() + " (" + message.getStatus() + ")";
      if (user.getUserId() == message.getSender().getUserId()) {
        addFromMessage(formattedMessage);
      } else {
        addToMessage(formattedMessage);
      }
    }
    table.changeSelection(table.getRowCount() - 1, 0, false, false);

  }

  @Override
  public void newMessage(Message newMessage) {
    String formattedMessage = newMessage.getTextBody() + " (" + newMessage.getStatus() + ")";
    if (user.getUserId() == newMessage.getSender().getUserId()) {
      addFromMessage(formattedMessage);
    } else {
      addToMessage(formattedMessage);
    }
  }
}
