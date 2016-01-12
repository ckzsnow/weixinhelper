package tests.detailed.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cef.browser.CefBrowser;

import tests.detailed.DomVisitor;
import tests.detailed.WeixinTuwenDomVisitor;

@SuppressWarnings("serial")
public class WeixinGroupSelectPanel extends JPanel {

	private List<String> selectedWeixinGroup = new ArrayList<String>();
	private List<Map<String, JCheckBox>> weixinUserList = new ArrayList<>();
	private final CefBrowser browser_;
	public final DomVisitor domVisitor;
	private final WeixinTuwenDomVisitor weixinTuwenDomVisitor;
	public static int count = 0;
	private WeixinGroupControlPanel wgcp;
	private WeixinTuwenMsgPanel wtmp;
	private Timer domvisitortimer;
	public WeixinGroupSelectPanel(CefBrowser browser, Timer timer) {
		this.domvisitortimer = timer;
		domVisitor = new DomVisitor(this, timer);
		weixinTuwenDomVisitor = new WeixinTuwenDomVisitor(wtmp);
		browser_ = browser;
		setLayout(new GridLayout(0, 6));
	}

	public void addCheckBox(List<Map<String, String>> dataList) {
		boolean hasData = false;
		for (Map<String, String> map : dataList) {
			boolean hasExisted = false;
			for(Map<String, JCheckBox> userMap : weixinUserList) {
				if(userMap.containsKey(map.get("userName"))) {
					hasExisted = true;
					break;
				}
			}
			if (!hasExisted) {
				JCheckBox box = new JCheckBox(map.get("userNickName"));
				add(box);
				Map<String, JCheckBox> boxMap = new HashMap<>();
				boxMap.put(map.get("userName"), box);
				weixinUserList.add(boxMap);
				wtmp.tuwenMsgBox.addItem(map.get("userNickName")+"#BBZ#"+map.get("userName"));
				hasData = true;
			}
			count++;
		}
		if (hasData) {
			count += 5 * 64;
			browser_.executeJavaScript("$('#J_NavChatScrollBody')[0].scrollTop = " + String.valueOf((count)), "", 9999);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					updatePanel(browser_);
				}
			}, 3000);
		} else {
			System.out.println("================>All End");
			/*wgcp.textFiled_.setText("已完成加载");
			browser_.executeJavaScript("$('#J_NavChatScrollBody')[0].scrollTop = "+ String.valueOf((count)), "", 9999);
			Timer timer = new Timer();  
		    timer.schedule(new TimerTask() {  
		        public void run() {
		        	updateWeixinTuwenMsg();
		        }  
		    }, 5000);*/
			browser_.executeJavaScript("$('#J_NavChatScrollBody')[0].scrollTop = " + String.valueOf((0)), "", 9999);
			domvisitortimer.cancel();
			wgcp.textFiled_.setBackground(Color.GREEN);
			wgcp.textFiled_.setText("已完成加载！");
		}
	}

	public List<Map<String, JCheckBox>> getWeixinUserList() {
		return weixinUserList;
	}

	public void setWeixinUserList(List<Map<String, JCheckBox>> weixinUserList) {
		this.weixinUserList = weixinUserList;
	}

	public void updatePanel(CefBrowser browser) {
		if (browser == browser_) {
			browser.getSource(domVisitor);
		}
	}

	public void updateWeixinTuwenMsg() {
		browser_.getSource(weixinTuwenDomVisitor);
	}
	
	public WeixinTuwenMsgPanel getWtmp() {
		return wtmp;
	}

	public void setWtmp(WeixinTuwenMsgPanel wtmp) {
		this.wtmp = wtmp;
	}

	public void setGroupControlPane(WeixinGroupControlPanel wgcp) {
		this.wgcp = wgcp;
	}
	
	public Timer getDomvisitortimer() {
		return domvisitortimer;
	}

	public void setDomvisitortimer(Timer domvisitortimer) {
		this.domvisitortimer = domvisitortimer;
	}

	public static void main(String[] args) {
		
	}
}
