package tests.detailed.ui;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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

	private final JButton selectAllButton_;
	private final JButton sendMsgButton_;
	private final CefBrowser browser_;

	public WeixinGroupControlPanel(CefBrowser browser) {
		browser_ = browser;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JLabel label = new JLabel("��ǰ΢��Ⱥ�б���");
		add(label);
		add(Box.createHorizontalStrut(18));

		selectAllButton_ = new JButton("ѡ��ȫ��");
		selectAllButton_.setAlignmentX(LEFT_ALIGNMENT);
		selectAllButton_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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
	}

	public void update(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
		if (browser == browser_) {
			// backButton_.setEnabled(canGoBack);
		}
	}
}