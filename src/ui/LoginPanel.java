package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import util.User;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
  private final static String DEFAULT_TEXT = "Username";
  private final static int NUM_COLUMNS = 25;

  public LoginPanel(UiFrame uiFrame, LocalStorage localStorage) {
    BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(boxLayout);

    JTextField textField = new JTextField(DEFAULT_TEXT, NUM_COLUMNS);
    textField.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        textField.setText("");
      }
    });
    add(textField);

    JButton logIn = new JButton("Log in");
    logIn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Optional<User> user = localStorage.getServerUser(textField.getText());
        user.ifPresent(v -> uiFrame.setUser(v));
      }
    });
    add(logIn);
  }
}
