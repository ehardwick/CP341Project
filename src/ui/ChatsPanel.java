package ui;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChatsPanel extends JPanel {

  private JList<String> jList;
  private final DefaultListModel<String> model = new DefaultListModel<String>();

  public ChatsPanel() {
    this.jList = new JList<String>(model);
    add(jList);
  }

  public void add(String addition) {
    model.addElement(addition);
  }

  public void add(List<String> additions) {
    for (String current : additions) {
      model.addElement(current);
    }
  }

  public void add(String[] additions) {
    for (String current : additions) {
      model.addElement(current);
    }
  }

  public String getSelected() {
    return jList.getSelectedValue();
  }
}
