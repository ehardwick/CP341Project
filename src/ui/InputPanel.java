package ui;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class InputPanel extends JPanel {
  private final static String DEFAULT_TEXT = "Say something";
  private final static int NUM_COLUMNS = 25;

  public InputPanel() {
    JTextField textField = new JTextField(DEFAULT_TEXT, NUM_COLUMNS);
    textField.addMouseListener(new MouseAdapter(){
      @Override
      public void mouseClicked(MouseEvent e){
          textField.setText("");
      }
  });
    add(textField);
    JButton send = new JButton("send");
    add(send);
  }

}
