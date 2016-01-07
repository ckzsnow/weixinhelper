package tests.detailed.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cef.browser.CefBrowser;

import tests.detailed.DomVisitor;

@SuppressWarnings("serial")
public class WeixinGroupSelectPanel extends JPanel {

	private List<String> selectedWeixinGroup = new ArrayList<String>();
	private Map<String, JCheckBox> weixinUserMap = new HashMap<>();
	private final CefBrowser browser_;
	private final DomVisitor domVisitor;	
	private static int count = 0;
	
	public WeixinGroupSelectPanel(CefBrowser browser) {
		domVisitor = new DomVisitor(this);
		browser_ = browser;
		setLayout(new GridLayout(0,6));
	}

	public void addCheckBox(Map<String, String> dataMap) {
		boolean hasData = false;
		for(Map.Entry<String, String> entry : dataMap.entrySet()) {
			if(!weixinUserMap.containsKey(entry.getKey())) {
				JCheckBox box = new JCheckBox(entry.getValue());
				add(box);
				weixinUserMap.put(entry.getKey(), box);
				hasData = true;
			}
			count++;
		}
		if(hasData) {
			count += 3 * 64;
			browser_.executeJavaScript("$('#J_NavChatScrollBody')[0].scrollTop = "+ String.valueOf((count)), "", 9999);
			Timer timer = new Timer();  
		    timer.schedule(new TimerTask() {  
		        public void run() {  
		        	updatePanel(browser_);
		        }  
		    }, 2000);
		} else {
			System.out.println("================>All End");
		}
	}

	public void updatePanel(CefBrowser browser) {
		if (browser == browser_) {
			browser.getSource(domVisitor);
		}
	}
}
