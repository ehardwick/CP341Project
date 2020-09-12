package ui;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import util.Message;
import util.MessageThread;
import util.Request;
import util.Response;
import util.User;

@SuppressWarnings("serial")
public class SentMessagesPanel extends JPanel implements MessageObserver, MessageThreadObserver {

  private DefaultTableModel tableModel;
  private JTable table;
  private User user;
  private LocalStorage localStorage;
  private Timer timer = new Timer();

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

  private class RefreshTask extends TimerTask {
    private long messageThreadId;

    public RefreshTask(long messageThreadId) {
      this.messageThreadId = messageThreadId;
    }

    public void run() {
      Optional<MessageThread> switchedToThread = localStorage.getServerMessageThread(messageThreadId);

      switchedToThread.ifPresent(thread -> {
        tableModel.setRowCount(0);
        thread.getMessages().forEach(message -> {
          if (user.getUsername().equals(message.getSender().getUsername())) {
            addFromMessage(message.getTextBody() + " (" + message.getStatus() + ")");
          } else {
            addToMessage(message.getTextBody() + " (" + message.getStatus() + ")");
          }
        });
        table.changeSelection(table.getRowCount() - 1, 0, false, false);
      });
    }
  }

  @Override
  public void threadSwitched(long messageThreadId) {
    this.timer.cancel();
    this.timer = new Timer();
    this.timer.schedule(new RefreshTask(messageThreadId), 0, 1000);
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
