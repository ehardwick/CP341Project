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
public class SentMessagesPanel extends JPanel {

  private DefaultTableModel tableModel;
  private User user;
  private JTable table;

  public SentMessagesPanel(User user) {
    this.tableModel = new DefaultTableModel(new Object[] {"", ""}, 1);
    this.table = new JTable(tableModel);
    this.user = user;

    table.setCellSelectionEnabled(false);
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  public void setMessageThread(MessageThread messageThread) {
    List<Message> messages = messageThread.getMessages();
    String formattedMessage;
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

  private void addFromMessage(String message) {
    tableModel.addRow(new Object[] {"", message});
  }

  private void addToMessage(String message) {
    tableModel.addRow(new Object[] {message, ""});
  }
}
