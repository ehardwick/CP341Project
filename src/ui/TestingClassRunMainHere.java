package ui;

import java.util.HashMap;
import java.util.Map;
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
    
    uiTest = new UiFrame(new ServerStub());
        
    uiTest.invalidate();
    uiTest.validate();
    uiTest.repaint();
  }
}
