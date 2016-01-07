package tests.detailed.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private int topOffset = 100;

	public WeixinGroupSelectPanel(CefBrowser browser) {
		domVisitor = new DomVisitor(this);
		browser_ = browser;
		setLayout(new GridLayout(0,10));
	}

	public void addCheckBox(Map<String, String> dataMap) {
		boolean hasNewData = false;
		for(Map.Entry<String, String> entry : dataMap.entrySet()) {
			if(!weixinUserMap.containsKey(entry.getKey())) {
				hasNewData = true;
				JCheckBox box = new JCheckBox(entry.getValue());
				add(box);
				weixinUserMap.put(entry.getKey(), box);
			}
		}
		if(hasNewData) {
			browser_.executeJavaScript("$('.scroll-element.scroll-y.scroll-scrolly_visible .scroll-bar')[0].style.top='"+topOffset+"px';", "", 9999);
			topOffset += 100;
		}
	}

	public void updatePanel(CefBrowser browser) {
		if (browser == browser_) {
			browser.getSource(domVisitor);
		}
	}
}
