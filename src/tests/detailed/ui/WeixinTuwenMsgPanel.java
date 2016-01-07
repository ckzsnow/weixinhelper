package tests.detailed.ui;

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
    JLabel label=new JLabel("当前图文消息列表：");
    add(label);
    add(Box.createHorizontalStrut(5));
    tuwenMsgBox = new JComboBox<Object>(new String[]{});
    tuwenMsgBox.setEditable(true);
    add(tuwenMsgBox);
  }
  
  public void addTuwenMsg(String name) {
	  this.tuwenMsgBox.addItem(name);
  }
  
}
