// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.detailed.handler;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefLoadHandler.ErrorCode;
import org.cef.network.CefPostData;
import org.cef.network.CefPostDataElement;
import org.cef.network.CefRequest;
import org.cef.network.CefWebPluginInfo;

import tests.detailed.MainFrame;
import tests.detailed.dialog.CertErrorDialog;
import tests.detailed.dialog.PasswordDialog;

public class RequestHandler extends CefRequestHandlerAdapter {
  private final MainFrame owner_;
  private boolean hasUpdate = false;

  public RequestHandler(MainFrame owner) {
    owner_ = owner;
  }

  @Override
  public CefResourceHandler getResourceHandler(CefBrowser browser,
                                               CefRequest request) {
    // the non existing domain "foo.bar" is handled by the ResourceHandler implementation
    // E.g. if you try to load the URL http://www.foo.bar, you'll be forwarded
    // to the ResourceHandler class.
    /*if (request.getURL().endsWith("foo.bar/")) {
      return new ResourceHandler();
    }*/
	System.out.println(request.toString());
	if(request.getURL().indexOf("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact") != -1) {
		//if(!hasUpdate) {
			hasUpdate = true;
			System.out.println(request.toString());
			Timer timer = new Timer();  
		    timer.schedule(new TimerTask() {  
		        public void run() {  
		        	owner_.weixin_group_select_pane_.updatePanel(browser);
		        }  
		    }, 6000);
		//}
	}
	//return new ResourceHandler();
	return null;
  }

  @Override
  public boolean getAuthCredentials(CefBrowser browser,
                                    boolean isProxy,
                                    String host,
                                    int port,
                                    String realm,
                                    String scheme,
                                    CefAuthCallback callback) {
    SwingUtilities.invokeLater(new PasswordDialog(owner_, callback));
    return true;
  }

  @Override
  public boolean onCertificateError(CefBrowser browser,
                                    ErrorCode cert_error,
                                    String request_url,
                                    CefRequestCallback callback) {
    SwingUtilities.invokeLater(new CertErrorDialog(owner_, cert_error, request_url, callback));
    return true;
  }

  @Override
  public void onPluginCrashed(CefBrowser browser, String pluginPath) {
    System.out.println("Plugin " + pluginPath  + "CRASHED");
  }

  @Override
  public void onRenderProcessTerminated(CefBrowser browser,
                                        TerminationStatus status) {
    System.out.println("render process terminated: " + status);
  }
}
