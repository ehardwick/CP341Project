import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InputPanel extends JPanel implements ChangeListener {

  public InputPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    JTextField textField = new JTextField("Say something", 22);
    panel.add(textField);
    JButton send = new JButton("send");
    panel.add(send);
    add(panel);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub

  }

}
