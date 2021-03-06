package tests.detailed.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cef.OS;
import org.cef.browser.CefBrowser;

import tests.detailed.MainFrame;
import tests.detailed.handler.MessageRouterHandler;

@SuppressWarnings("serial")
public class WeixinGroupControlPanel extends JPanel {

	public final JButton selectAllButton_;
	public final JButton selectAllWeixinGroupButton_;
	public final JButton unSelectAllWeixinGroupButton_;
	public final JButton sendMsgButton_;
	public final JButton reloadButton_;
	public final JTextField textFiled_;
	private final CefBrowser browser_;
	private WeixinGroupSelectPanel wgsp;
	private File weixinContactListFile;
	private FileWriter fileWriter;
	public List<String> weixinqunList = new ArrayList<>();
	public String selectedUserNickName = "";

	public WeixinGroupControlPanel(CefBrowser browser) {
		browser_ = browser;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JLabel label = new JLabel("当前微信群列表：");
		add(label);
		add(Box.createHorizontalStrut(18));

		selectAllWeixinGroupButton_ = new JButton("选中所有群");
		selectAllWeixinGroupButton_.setAlignmentX(LEFT_ALIGNMENT);
		selectAllWeixinGroupButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Map<String, JCheckBox>> userList = wgsp.getWeixinUserList();
				for (int i = 0; i < userList.size(); i++) {
					Map<String, JCheckBox> map = userList.get(i);
					for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
						String userId = entry.getKey();
						if (userId != null && userId.startsWith("@@")) {
							JCheckBox checkBox = entry.getValue();
							if(checkBox != null) checkBox.setSelected(true);
						}
					}
				}
			}
		});
		add(selectAllWeixinGroupButton_);
		add(Box.createHorizontalStrut(5));
		unSelectAllWeixinGroupButton_ = new JButton("取消选中");
		unSelectAllWeixinGroupButton_.setAlignmentX(LEFT_ALIGNMENT);
		unSelectAllWeixinGroupButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Map<String, JCheckBox>> userList = wgsp.getWeixinUserList();
				for (int i = 0; i < userList.size(); i++) {
					Map<String, JCheckBox> map = userList.get(i);
					for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
						String userId = entry.getKey();
						if (userId != null && userId.startsWith("@@")) {
							JCheckBox checkBox = entry.getValue();
							if(checkBox != null) checkBox.setSelected(false);
						}
					}
				}
			}
		});
		add(unSelectAllWeixinGroupButton_);
		add(Box.createHorizontalStrut(5));
		
		selectAllButton_ = new JButton("发送图文链接");
		selectAllButton_.setAlignmentX(LEFT_ALIGNMENT);
		selectAllButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				weixinContactListFile = new File("weixinContactList.txt");
				if (weixinContactListFile.exists()) {
					weixinContactListFile.delete();
				}
				List<Map<String, JCheckBox>> userList = wgsp.getWeixinUserList();
				boolean hasSelected = false;
				try {
					weixinContactListFile = new File("weixinContactList.txt");
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(weixinContactListFile));
					for (int i = 0; i < userList.size(); i++) {
						Map<String, JCheckBox> map = userList.get(i);
						for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
							if (entry.getValue().isSelected()) {
								hasSelected = true;
								try {
									output.write((entry.getValue().getLabel() + "\r\n").getBytes("UTF-16LE"));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
					output.flush();
					output.close();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				if (!hasSelected) {
					textFiled_.setBackground(Color.RED);
					textFiled_.setText("请选择好友或微信群！");
				} else {
					textFiled_.setBackground(Color.cyan);
					textFiled_.setText("处理群发图文消息，请手动启动微信PC端！");
					/*BufferedReader bf = new BufferedReader(new FileReader("weixinexepathconfig.txt"));
					String line = bf.readLine();
					if (line == null || line.isEmpty()) {
						textFiled_.setBackground(Color.RED);
						textFiled_.setText("没有在配置文件中找到WeChat.exe的路径，请手动启动，当前程序5秒后自动关闭！");
					}*/
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							Runtime rt = Runtime.getRuntime();
							try {
								/*if (line != null) {
									//rt.exec(line);
								}*/
								rt.exec("weixin.exe");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							/*browser_.close();*/
						}
					}, 1000);
				}
			}
		});
		add(selectAllButton_);
		add(Box.createHorizontalStrut(5));

		sendMsgButton_ = new JButton("加群");
		sendMsgButton_.setAlignmentX(LEFT_ALIGNMENT);
		sendMsgButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedUserNickName = (String) wgsp.getWtmp().tuwenMsgBox.getSelectedItem();
				if(selectedUserNickName == null || selectedUserNickName.isEmpty()) {
					JOptionPane.showMessageDialog(null,"您还没有选择待加群的用户！");
				} else {
					selectedUserNickName = selectedUserNickName.substring(selectedUserNickName.indexOf("#BBZ#") + 5);
					List<Map<String, JCheckBox>> userList = wgsp.getWeixinUserList();
					boolean hasSelected = false;
					for (int i = 0; i < userList.size(); i++) {
						Map<String, JCheckBox> map = userList.get(i);
						for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
							if (entry.getValue().isSelected()) {
								hasSelected = true;
								weixinqunList.add(entry.getKey());
							}
						}
					}
					if(!hasSelected) {
						JOptionPane.showMessageDialog(null,"您还没有选择群，请在当前微信群列表中选择群！");
					} else {
						selectAllButton_.setEnabled(false);
						sendMsgButton_.setEnabled(false);
						textFiled_.setBackground(Color.YELLOW);
						textFiled_.setText("正在执行加群操作，请等待操作完成......");
						executeWeixinGroupAddUser();
					}
				}
			}
		});
		add(sendMsgButton_);
		add(Box.createHorizontalStrut(5));

		reloadButton_ = new JButton("重新获取微信列表信息");
		reloadButton_.setAlignmentX(LEFT_ALIGNMENT);
		reloadButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browser_.loadURL("https://wx.qq.com/?&lang=zh_CN");
				for(Map<String, JCheckBox> map : wgsp.getWeixinUserList()) {
					for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
						wgsp.remove(entry.getValue());
					}
				}
				wgsp.getWeixinUserList().clear();
				wgsp.getWtmp().remove(wgsp.getWtmp().tuwenMsgBox);
				wgsp.getWtmp().tuwenMsgBox = new JComboBox<Object>(new String[]{});
				wgsp.getWtmp().tuwenMsgBox.setEditable(false);
				wgsp.getWtmp().tuwenMsgBox.setPreferredSize(new Dimension(200, 30));
				wgsp.getWtmp().add(wgsp.getWtmp().tuwenMsgBox);
				selectAllButton_.setEnabled(true);
				sendMsgButton_.setEnabled(true);
				textFiled_.setBackground(Color.YELLOW);
				textFiled_.setText("正在加载微信列表信息......");
				if(MainFrame.domVisitorTimer != null) MainFrame.domVisitorTimer.cancel();
				MainFrame.domVisitorTimer = new Timer();
				wgsp.setDomvisitortimer(MainFrame.domVisitorTimer);
				wgsp.domVisitor.setTimer(MainFrame.domVisitorTimer);
				MainFrame.domVisitorTimer.scheduleAtFixedRate(new TimerTask() {
			        public void run() {
			        	wgsp.count = 0;
			        	wgsp.updatePanel(browser_);
			        }  
			    }, 5000, 5000);
				
			}
		});
		add(reloadButton_); add(Box.createHorizontalStrut(5));
		 

		textFiled_ = new JTextField("正在加载微信列表信息......");
		textFiled_.setAlignmentX(LEFT_ALIGNMENT);
		textFiled_.setBackground(Color.YELLOW);
		textFiled_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFiled_.setText("正在加载微信列表信息......");
			}
		});
		add(textFiled_);
		add(Box.createHorizontalStrut(5));
	}

	public WeixinGroupSelectPanel getWgsp() {
		return wgsp;
	}

	public void setWgsp(WeixinGroupSelectPanel wgsp) {
		this.wgsp = wgsp;
	}

	public void update(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
		if (browser == browser_) {
			// backButton_.setEnabled(canGoBack);
		}
	}
	
	public void executeWeixinGroupAddUser() {
		String userName = weixinqunList.remove(0);
		browser_.executeJavaScript("var weixinGroupName='"+userName+"';var userName='"+selectedUserNickName+"';function searchWeixinUserAndClick(){var hasFind=false;$('#J_ContactPickerScrollBody div[ng-repeat]').each(function(){var src=$($(this).find('img')[0]).attr('src');if(!hasFind&&src!=null&&src!=''){var beginPos=src.indexOf('&username=');var endPos=src.indexOf('&skey=');var currentUserName=src.substring(beginPos+10,endPos);if(currentUserName==userName){hasFind=true;$($(this).find('.contact_item')[0]).click();$($('.dialog_ft.ng-scope a')[0]).click()}}});if(!hasFind){$('#J_ContactPickerScrollBody')[0].scrollTop=$('#J_ContactPickerScrollBody')[0].scrollTop+5*54;setTimeout(function(){if($('#J_ContactPickerScrollBody')[0].scrollTop<=$('#J_ContactPickerScrollBody')[0].scrollHeight+5*54){searchWeixinUserAndClick()}else{$('.ngdialog-close').click();window.cefQuery({request:'BBZ_WEIXIN_FINISH:1',onSuccess:function(response){},onFailure:function(error_code,error_message){}})}},1000)}else{window.cefQuery({request:'BBZ_WEIXIN_FINISH:1',onSuccess:function(response){},onFailure:function(error_code,error_message){}})}}function searchWeixinGroupAndClick(){var hasFind=false;$('.scroll-wrapper.chat_list.scrollbar-dynamic div[ng-repeat]').each(function(){if(!hasFind&&$($(this).children()[0]).attr('data-cm').indexOf(weixinGroupName)!=-1){hasFind=true;$($(this).children()[0]).click();$($('.title.poi a')[0]).click();$('#mmpop_chatroom_members i').click();setTimeout(function(){searchWeixinUserAndClick()},2000)}});if(!hasFind){setTimeout(function(){$('#J_NavChatScrollBody')[0].scrollTop=$('#J_NavChatScrollBody')[0].scrollTop+8*64;if($('#J_NavChatScrollBody')[0].scrollTop<=$('#J_NavChatScrollBody')[0].scrollHeight+8*64){searchWeixinGroupAndClick()}else{window.cefQuery({request:'BBZ_WEIXIN_FINISH:1',onSuccess:function(response){},onFailure:function(error_code,error_message){}})}},1000)}}$('#J_NavChatScrollBody')[0].scrollTop=0;setTimeout(function(){searchWeixinGroupAndClick()},1000);", "", 9999);
		checkJsHasFinished();
	}
	
	public void checkJsHasFinished() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if(!MessageRouterHandler.jsHasFinished) {
					checkJsHasFinished();
				} else {
					if(weixinqunList.size() != 0) {
						executeWeixinGroupAddUser();
					} else {
						textFiled_.setBackground(Color.GREEN);
						textFiled_.setText("加群操作执行完毕！");
						selectAllButton_.setEnabled(true);
						sendMsgButton_.setEnabled(true);
						MessageRouterHandler.jsHasFinished = false;
					}
				}
			}
		}, 2000);
	}
}
