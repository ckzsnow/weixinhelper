package tests.detailed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class DomVisitor implements CefStringVisitor {

	private WeixinGroupSelectPanel wixin_group_select_panel;
	
	public DomVisitor(WeixinGroupSelectPanel panel) {
		this.wixin_group_select_panel = panel;
	}
	
	@Override
	public void visit(String string) {
		Document doc = Jsoup.parse(string);
		Elements ngRepeatElements = doc.select(".scroll-wrapper.chat_list.scrollbar-dynamic div[ng-repeat]");
		List<Map<String, String>> dataList = new ArrayList<>();
		for(Element ele : ngRepeatElements) {
			String nickName = ele.select(".nickname_text.ng-binding").html();
			Matcher ma = Pattern.compile("<\\s*img\\s+([^>]+)\\s*>").matcher(nickName);
			Elements userNameElements = ele.select("div[data-cm]");
			String dataJson = userNameElements.get(0).attr("data-cm");
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<String, String> map = mapper.readValue(dataJson, Map.class);
				map.put("userName", map.get("username"));
				map.put("userNickName", ma.replaceAll("").trim());
				dataList.add(map);
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
		wixin_group_select_panel.addCheckBox(dataList);
	}
	
	public static void main(String[] args) {
		String testStr = "<img class='emoji emoji1f44d' text='��_web' src='/zh_CN/htmledition/v2/images/spacer.gif'>�����Ա����Ⱥ<img class='emoji emoji1f44d' text='��_web' src='/zh_CN/htmledition/v2/images/spacer.gif'>";
		Matcher ma = Pattern.compile("<\\s*img\\s+([^>]+)\\s*>").matcher(testStr);
		System.out.println( ma.replaceAll("").trim());
	}
	
}
