package tests.detailed.handler;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

import tests.detailed.ui.WeixinGroupSelectPanel;

public class ResourceHandler2 extends CefResourceHandlerAdapter {
	private WeixinGroupSelectPanel wgsp;
	private CefBrowser browser;

	public ResourceHandler2(WeixinGroupSelectPanel wgsp, CefBrowser browser) {
		this.wgsp = wgsp;
		this.browser = browser;
	}

	@Override
	public boolean processRequest(CefRequest request, CefCallback callback) {
		callback.Continue();
		return true;
	}

	@Override
  public void getResponseHeaders(CefResponse response,
                                 IntRef response_length,
                                 StringRef redirectUrl) {
    /*response_length.set(html.length());
    response.setMimeType("text/html");
    response.setStatus(200);*/
	/*Timer timer = new Timer();  
    timer.schedule(new TimerTask() {  
        public void run() {  
        	wgsp.updatePanel(browser);
        }  
    }, 5000);*/
		//System.out.println("test");
  }

	@Override
	public void cancel() {
		System.out.println("cancel");
	}
}
