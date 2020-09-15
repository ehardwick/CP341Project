package ui;

public class RunClientMainMethod {
  private UiFrame uiTest;

  public static void main(String[] args) {
    RunClientMainMethod tcrmh = new RunClientMainMethod();
    tcrmh.trueMain();
  }

  public void trueMain() {

    uiTest = new UiFrame();

    uiTest.invalidate();
    uiTest.validate();
    uiTest.repaint();
  }
}
