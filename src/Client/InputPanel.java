package Client;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel {

  public InputPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    JTextField textField = new JTextField("Say something", 22);
    textField.addMouseListener(new MouseAdapter(){
      @Override
      public void mouseClicked(MouseEvent e){
          textField.setText("");
      }
  });
    panel.add(textField);
    JButton send = new JButton("send");
    
    panel.add(send);
    add(panel);
  }

}
