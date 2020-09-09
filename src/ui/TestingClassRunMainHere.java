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
    Map<String, Long> contacts = new HashMap<>();
    contacts.put("Alice", 1l);
    contacts.put("Bob", 2l);
    
    User bob = new User.Builder()
        .withUserId(2)
        .withUsername("Bob")
        .withContacts(contacts)
        .build();
    
    uiTest = new UiFrame(bob);
    
    uiTest.addPanels(uiTest.getContentPane());
    
    uiTest.invalidate();
    uiTest.validate();
    uiTest.repaint();
  }
}
