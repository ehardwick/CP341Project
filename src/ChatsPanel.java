import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChatsPanel extends JPanel implements ChangeListener {

  private SentMessagesPanel sentMessagesPanel;
  private InputPanel inputPanel;

  public ChatsPanel(SentMessagesPanel sentMessagesPanel, InputPanel inputPanel) {
    this.sentMessagesPanel = sentMessagesPanel;
    this.inputPanel = inputPanel;
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    setLayout(new GridLayout(1, 1, 30, 10));
    String data[][] = {{"Chat 1"}, {"Chat 2"},};
    String column[] = {"Chats"};
    JTable jt = new JTable(data, column);
    jt.setBounds(30, 40, 20, 30);
    jt.changeSelection(jt.getRowCount() - 1, 0, false, false);
    jt.setCellSelectionEnabled(false);
    JScrollPane sp = new JScrollPane(jt);
    panel.add(sp);
    add(panel);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub

  }

}
