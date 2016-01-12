package tests.detailed.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cef.browser.CefBrowser;

@SuppressWarnings("serial")
public class WeixinTuwenMsgPanel extends JPanel {

  public final JComboBox<Object> tuwenMsgBox;
  
  private final CefBrowser browser_;

  public WeixinTuwenMsgPanel(CefBrowser browser) {
    browser_ = browser;
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));    
    JLabel label=new JLabel("选择待加群用户：");
    add(label);
    add(Box.createHorizontalStrut(17));
    tuwenMsgBox = new JComboBox<Object>(new String[]{});
    tuwenMsgBox.setEditable(false);
    tuwenMsgBox.setPreferredSize(new Dimension(200, 30));
    add(tuwenMsgBox);
  }
  
  public void addTuwenMsg(String name) {
	  this.tuwenMsgBox.addItem(name);
  }
  
}
