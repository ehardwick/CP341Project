package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.Message;
import util.MessageThread;
import util.User;

/**
 * This is some bad code for running the UiWrapper to test it
 * 
 * @author elliothardwick
 *
 */
public class TestingClassRunMainHere {
  private UiFrame uiTest;

  public static void main(String[] args) {
    TestingClassRunMainHere tcrmh = new TestingClassRunMainHere();
    tcrmh.trueMain();
  }

  public void trueMain() {
    ServerStub fakeServer = new ServerStub();
    User bob = new User.Builder()
        .withUserId(1)
        .withUsername("bob username")
        .build();
    uiTest = new UiFrame(bob);
    
    uiTest.addPanels(uiTest.getContentPane());
    
    uiTest.invalidate();
    uiTest.validate();
    uiTest.repaint();
  }
}
