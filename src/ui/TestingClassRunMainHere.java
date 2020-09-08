package ui;

import java.util.ArrayList;
import java.util.Date;
import util.Message;
import util.MessageThread;
import util.User;

/**
 * This is some bad code for running the UiWrapper to test it
 * @author elliothardwick
 *
 */
public class TestingClassRunMainHere {
  private UiWrapper uiTest;

  public static void main(String[] args) {
      TestingClassRunMainHere tcrmh = new TestingClassRunMainHere();
      tcrmh.trueMain();
  }
  
  public void trueMain() {
    uiTest = new UiWrapper();
    uiTest.addPanels(uiTest.getContentPane());
    addTrash();
  }
  
  private void addTrash() {
    User alice = new User.Builder()
        .withUserId(12345)
        .withUsername("alice username")
        .build();
    
    User bob = new User.Builder()
        .withUserId(1)
        .withUsername("bob username")
        .build();
    
    ArrayList<User> owners = new ArrayList<>();
    
    MessageThread thread = new MessageThread.Builder()
        .withMessageThreadId(123l)
        .withOwners(owners)
        .build();
    
    Message firstMessage = new Message.Builder()
        .withSender(bob)
        .withTextBody("bob's text body 1")
        .withTimeSent(new Date())
        .build();
    
    thread.addMessage(firstMessage);
    
    Message secondMessage = new Message.Builder()
        .withSender(bob)
        .withTextBody("bob's text body 1")
        .withTimeSent(new Date())
        .build();
    
    thread.addMessage(secondMessage);
    
    Message thirdMessage = new Message.Builder()
        .withSender(alice)
        .withTextBody("alice's text body 1")
        .withTimeSent(new Date())
        .build();
    
    thread.addMessage(thirdMessage);
    
    uiTest.sentMessagesPanel.setMessageThread(thread);
    
    uiTest.chatsPanel.add("chat 1");
        
    uiTest.invalidate();
    uiTest.validate();
    uiTest.repaint();
  }

}
