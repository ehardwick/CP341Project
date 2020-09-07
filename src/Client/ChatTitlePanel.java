package Client;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChatTitlePanel extends JPanel implements ChangeListener {

  private SentMessagesPanel sentMessagesPanel;
  private InputPanel inputPanel;

  public ChatTitlePanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    JTextArea textArea = new JTextArea("Chat with Alice");
    textArea.setOpaque(false);
    textArea.setEditable(false);
    panel.add(textArea);
    add(panel);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub

  }

}
