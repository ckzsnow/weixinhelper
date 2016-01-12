package tests.detailed.ui;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

@SuppressWarnings("serial")
public class WeixinGroupControlPanel extends JPanel {

	public final JButton selectAllButton_;
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
		weixinContactListFile = new File("weixinContactList.txt");
		if (weixinContactListFile.exists()) {
			weixinContactListFile.delete();
		}

		browser_ = browser;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JLabel label = new JLabel("��ǰ΢��Ⱥ�б�");
		add(label);
		add(Box.createHorizontalStrut(18));

		selectAllButton_ = new JButton("����ͼ������");
		selectAllButton_.setAlignmentX(LEFT_ALIGNMENT);
		selectAllButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Map<String, JCheckBox>> userList = wgsp.getWeixinUserList();
				boolean hasSelected = false;
				try {
					fileWriter = new FileWriter("weixinContactList.txt", true);
					for (int i = 0; i < userList.size(); i++) {
						Map<String, JCheckBox> map = userList.get(i);
						for (Map.Entry<String, JCheckBox> entry : map.entrySet()) {
							if (entry.getValue().isSelected()) {
								hasSelected = true;
								try {
									fileWriter.write(entry.getValue().getLabel() + "\r\n");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				if (!hasSelected) {
					textFiled_.setBackground(Color.RED);
					textFiled_.setText("��ѡ����ѻ�΢��Ⱥ��");
				} else {
					textFiled_.setBackground(Color.cyan);
					textFiled_.setText("��ǰ����5����Զ��رգ�������ĵ�˵���Ĳ��裬�������ͼ�����ӵ�Ⱥ����");
					try {
						BufferedReader bf = new BufferedReader(new FileReader("weixinexepathconfig.txt"));
						String line = bf.readLine();
						if (line == null || line.isEmpty()) {
							textFiled_.setBackground(Color.RED);
							textFiled_.setText("û���������ļ����ҵ�WeChat.exe��·�������ֶ���������ǰ����5����Զ��رգ�");
						}
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							public void run() {
								Runtime rt = Runtime.getRuntime();
								try {
									if (line != null) {
										rt.exec(line);
									}
									rt.exec("weixin.exe");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								browser_.close();
							}
						}, 5000);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
		});
		add(selectAllButton_);
		add(Box.createHorizontalStrut(5));

		sendMsgButton_ = new JButton("��Ⱥ");
		sendMsgButton_.setAlignmentX(LEFT_ALIGNMENT);
		sendMsgButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedUserNickName = (String) wgsp.getWtmp().tuwenMsgBox.getSelectedItem();
				if(selectedUserNickName == null || selectedUserNickName.isEmpty()) {
					JOptionPane.showMessageDialog(null,"����û��ѡ�����Ⱥ���û���");
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
						JOptionPane.showMessageDialog(null,"����û��ѡ��Ⱥ�����ڵ�ǰ΢��Ⱥ�б���ѡ��Ⱥ��");
					} else {
						executeWeixinGroupAddUser();
					}
				}
			}
		});
		add(sendMsgButton_);
		add(Box.createHorizontalStrut(5));

		reloadButton_ = new JButton("���¼��غ����б�");
		reloadButton_.setAlignmentX(LEFT_ALIGNMENT);
		reloadButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browser_.loadURL("https://wx.qq.com/?&lang=zh_CN");
			}
		});
		/*
		 * add(reloadButton_); add(Box.createHorizontalStrut(5));
		 */

		textFiled_ = new JTextField("���ڼ���΢���б���Ϣ......");
		textFiled_.setAlignmentX(LEFT_ALIGNMENT);
		textFiled_.setBackground(Color.YELLOW);
		textFiled_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFiled_.setText("���ڼ���΢���б���Ϣ......");
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
		for(String userName : weixinqunList) {
			browser_.executeJavaScript("var weixinGroupName='"+userName+"';var userName='"+selectedUserNickName+"';function searchWeixinUserAndClick(){var hasFind=false;$('#J_ContactPickerScrollBody div[ng-repeat]').each(function(){var src=$($(this).find('img')[0]).attr('src');if(!hasFind&&src!=null&&src!=''){var beginPos=src.indexOf('&username=');var endPos=src.indexOf('&skey=');var currentUserName=src.substring(beginPos+10,endPos);if(currentUserName==userName){hasFind=true;$($(this).find('.contact_item')[0]).click();$($('.dialog_ft.ng-scope a')[0]).click()}}});if(!hasFind){$('#J_ContactPickerScrollBody')[0].scrollTop=$('#J_ContactPickerScrollBody')[0].scrollTop+5*54;setTimeout(function(){if($('#J_ContactPickerScrollBody')[0].scrollTop<=$('#J_ContactPickerScrollBody')[0].scrollHeight+5*54){searchWeixinUserAndClick()}else{$('.ngdialog-close').click();window.cefQuery({request:'BBZ_WEIXIN_FINISH:1',onSuccess:function(response){},onFailure:function(error_code,error_message){}})}},1000)}}function searchWeixinGroupAndClick(){var hasFind=false;$('.scroll-wrapper.chat_list.scrollbar-dynamic div[ng-repeat]').each(function(){if(!hasFind&&$($(this).children()[0]).attr('data-cm').indexOf(weixinGroupName)!=-1){hasFind=true;$($(this).children()[0]).click();$($('.title.poi a')[0]).click();$('#mmpop_chatroom_members i').click();setTimeout(function(){searchWeixinUserAndClick()},2000)}});if(!hasFind){setTimeout(function(){$('#J_NavChatScrollBody')[0].scrollTop=$('#J_NavChatScrollBody')[0].scrollTop+8*64;if($('#J_NavChatScrollBody')[0].scrollTop<=$('#J_NavChatScrollBody')[0].scrollHeight+8*64){searchWeixinGroupAndClick()}else{window.cefQuery({request:'BBZ_WEIXIN_FINISH:1',onSuccess:function(response){},onFailure:function(error_code,error_message){}})}},1000)}}$('#J_NavChatScrollBody')[0].scrollTop=0;setTimeout(function(){searchWeixinGroupAndClick()},1000);", "", 9999);
		}
		weixinqunList.clear();
		selectedUserNickName = "";
	}

}
