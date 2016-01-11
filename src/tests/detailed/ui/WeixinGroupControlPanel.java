package tests.detailed.ui;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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

	public WeixinGroupControlPanel(CefBrowser browser) {
		weixinContactListFile = new File("weixinContactList.txt");
		if(weixinContactListFile.exists()) {
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
					for(int i=0; i<userList.size(); i++) {
						Map<String, JCheckBox> map = userList.get(i);
						for(Map.Entry<String, JCheckBox> entry : map.entrySet()) {
							if(entry.getValue().isSelected()) {
								hasSelected = true;
								try {
									fileWriter.write(String.valueOf(i) + "\r\n");
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
				if(!hasSelected) {
					textFiled_.setBackground(Color.RED);
					textFiled_.setText("��ѡ����ѻ�΢��Ⱥ��");
				} else {
					textFiled_.setBackground(Color.cyan);
					textFiled_.setText("��ǰ����5����Զ��رգ�������ĵ�˵���Ĳ��裬�������ͼ�����ӵ�Ⱥ����");
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							Runtime rt = Runtime.getRuntime();
							try {
								rt.exec("C:\\Program Files (x86)\\Tencent\\WeChat\\WeChat.exe");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							browser_.close();
						}
					}, 2000);
				}
			}
		});
		add(selectAllButton_);
		add(Box.createHorizontalStrut(5));

		sendMsgButton_ = new JButton("������Ϣ");
		sendMsgButton_.setAlignmentX(LEFT_ALIGNMENT);
		sendMsgButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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
		/*add(reloadButton_);
		add(Box.createHorizontalStrut(5));*/
		
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
	
}
