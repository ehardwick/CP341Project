package client;

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
  private MessageThread messageThread;
  private User user;
  private JTable table;

  public SentMessagesPanel(User user) {
    this.tableModel = new DefaultTableModel(new Object[] {"", ""}, 1);
    this.table = new JTable(tableModel);
    this.user = user;

    table.setBounds(30, 40, 200, 300);
    table.setCellSelectionEnabled(false);
    JScrollPane scrollPane = new JScrollPane(table);

    add(scrollPane);
  }

  public void setMessageThread(MessageThread messageThread) {
    List<Message> messages = messageThread.getMessages();
    System.out.println(messages.size());
    for (Message message : messages) {
      if (user.getUserId() == message.getSender().getUserId()) {
        tableModel.addRow(new Object[] {"", message.getTextBody()});
      } else {
        tableModel.addRow(new Object[] {message.getTextBody(), ""});
      }
    }
    this.messageThread = messageThread;
    table.changeSelection(table.getRowCount() - 1, 0, false, false);
  }
}
