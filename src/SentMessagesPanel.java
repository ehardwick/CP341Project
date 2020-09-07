import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SentMessagesPanel extends JPanel implements ChangeListener {

  public SentMessagesPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    setLayout(new GridLayout(1, 2, 30, 10));
    String data[][] = {{"Alice Text Message 1", ""}, {"", "Bob Text Message 1"},
        {"Alice Text Message 2", ""}, {"Alice Text Message 3", ""}, {"", "Bob Text Message 2"},
        {"Alice Text Message 4", ""}, {"", "Bob Text Message 3"}, {"Alice Text Message 5", ""},};
    String column[] = {"", ""};
    JTable jt = new JTable(data, column);
    jt.setBounds(30, 40, 200, 300);
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
