package tests.detailed;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cef.callback.CefStringVisitor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tests.detailed.ui.WeixinGroupSelectPanel;
import tests.detailed.ui.WeixinTuwenMsgPanel;

public class WeixinTuwenDomSetp2 implements CefStringVisitor {

	private WeixinTuwenMsgPanel wixin_tuwen_msg_panel;
	
	public WeixinTuwenDomSetp2(WeixinTuwenMsgPanel panel) {
		this.wixin_tuwen_msg_panel = panel;
	}
	
	@Override
	public void visit(String string) {
		Document doc = Jsoup.parse(string);
		Elements ngRepeatElements = doc.select(".scroll-wrapper.chat_list.scrollbar-dynamic div[ng-repeat]");
		Map<String, String> dataMap = new HashMap<>();
		for(Element ele : ngRepeatElements) {
			String nickName = ele.select(".nickname_text.ng-binding").html();
			Matcher ma = Pattern.compile("<\\s*img\\s+([^>]+)\\s*>").matcher(nickName);
			Elements userNameElements = ele.select("div[data-cm]");
			String dataJson = userNameElements.get(0).attr("data-cm");
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<String, String> map = mapper.readValue(dataJson, Map.class);
				dataMap.put(map.get("username"), ma.replaceAll("").trim());
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*String height = "";
		try {
			height = doc.select("#J_NavChatScrollBody .top-placeholder").get(0).attr("style");
		} catch(Exception ex) {
			System.out.println(ex.toString());
		}
		System.out.println("HEIGHT===============================>" + height);*/
		
	}
	
	public static void main(String[] args) {
		String testStr = "<img class='emoji emoji1f44d' text='��_web' src='/zh_CN/htmledition/v2/images/spacer.gif'>�����Ա����Ⱥ<img class='emoji emoji1f44d' text='��_web' src='/zh_CN/htmledition/v2/images/spacer.gif'>";
		Matcher ma = Pattern.compile("<\\s*img\\s+([^>]+)\\s*>").matcher(testStr);
		System.out.println( ma.replaceAll("").trim());
	}
	
}
