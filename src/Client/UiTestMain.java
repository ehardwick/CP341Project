package Client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;

public class UiTestMain {
  public static void main(String[] args) {
     JFrame frame = new JFrame();
     frame.setTitle("Messaging Client UI Test");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
     SentMessagesPanel sentMessagesPanel = new SentMessagesPanel();
     InputPanel inputPanel = new InputPanel();
     ChatsPanel chatsPanel = new ChatsPanel(sentMessagesPanel, inputPanel);
     ChatTitlePanel chatTitlePanel = new ChatTitlePanel();
     chatsPanel.setMaximumSize(new Dimension(10, 10));
     chatsPanel.setBounds(0, 0, 30, 100);
    
     JPanel test = new JPanel();
     
     GridBagConstraints c = new GridBagConstraints();
     c.gridwidth = 1;
    
     test.setLayout(new BorderLayout());
     test.add(sentMessagesPanel, BorderLayout.CENTER);
     test.add(inputPanel, BorderLayout.SOUTH);
     test.add(chatTitlePanel, BorderLayout.NORTH);
    
     frame.setLayout(new GridBagLayout());
     frame.add(chatsPanel, c);
     frame.add(test);
    
     frame.pack();
     frame.setVisible(true);

  }
}
