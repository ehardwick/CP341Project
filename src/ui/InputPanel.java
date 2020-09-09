package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import util.Message;
import util.User;

@SuppressWarnings("serial")
public class InputPanel extends JPanel implements ChangeListener {
  private final static String DEFAULT_TEXT = "Say something";
  private final static int NUM_COLUMNS = 25;

  private List<MessageObserver> messageObservers;

  public InputPanel(User user) {
    JTextField textField = new JTextField(DEFAULT_TEXT, NUM_COLUMNS);
    textField.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        textField.setText("");
      }
    });
    add(textField);
    JButton send = new JButton("send");
    send.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Message newMessage = new Message.Builder()
            .withSender(user)
            .withTextBody(textField.getText())
            .withTimeSent(new Date())
            .build();
        
        messageObservers.forEach(observer -> observer.sendNewMessage(newMessage));
      }
    });
    add(send);
  }

  public void setMessageObservers(List<MessageObserver> messageObservers) {
    this.messageObservers = messageObservers;
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    messageObservers.forEach(observer -> observer.sendNewMessage(new Message()));
  }

}
