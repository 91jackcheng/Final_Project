/**
 * Created by 万只羊 on 2015/12/12.
 */

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class searchBaidu {
	private static final String snippetQuery = "div.c-abstract";
	private static final String exactAnswerQuery = "div.op_exactqa_s_answer";
	private static final String bestAnswerQuery = "div#1.op_best_answer_content";
	private static final String baikeQuery = "div.result_op .c-span18.c-span-last";
	//private static final String keywordQuery = "dd[class=dd answer] em";
	static Logger log = MyLogger.getLogger("searchSnippets");
	private static String url = "http://www.baidu.com/s";

	static {
		log.setUseParentHandlers(false);
	}

	private Document document = null;
	private String keyword;

	private List<String> get(String url, String query) {
		CONNECT:
		try {
			Connection con = Jsoup.connect(url).data("ie", "UTF-8").data("word", keyword);//Jsoup.connect("http://www.iteye.com/login");//获取连接
			con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");//配置模拟浏览器
			Connection.Response rs = con.execute();//获取响应
			document = Jsoup.parse(rs.body());//转换为Dom树
			//document = Jsoup.connect(url).data("ie", "UTF-8").data("word", keyword).get();
		} catch (IOException e) {
			//e.printStackTrace();
			return get(url, query);
			/*
			log.warning("connect fail for " + keyword);
			List<String> result = new ArrayList<>();
			result.add("connetFail");
			return result;
			*/
		}
		try {
			File file = new File("./htmls/" + keyword + ".html");
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(document.html());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.info(document.text());
		//System.out.println(document);
		int counter = 0;
		List<String> contents = new ArrayList<>();
		Elements totalElement = document.select(query);
		for (Element element : totalElement) {
			if (++counter > 2) break;
			String text = element.text();
			contents.add(text);
			log.info(text);
		}
		//if (contents.size() == 0) System.out.println(document);
		return contents;
	}

	public List<String> getZhidaoAnswer(String keyword) {
		log.info("search in zhidao: " + keyword);
		this.keyword = keyword;
		return get("http://zhidao.baidu.com/search", exactAnswerQuery);
	}

	public List<String> getBaiduSnippet(String keyword) {
		keyword = keyword.replaceAll("\\pP", "");
		this.keyword = keyword;
		//System.out.println(keyword);
		url = "http://www.baidu.com/s";
		log.info("search in baidu: " + keyword);
		List<String> result = get(url, exactAnswerQuery);
		if (result.size() > 0)
			return result;
		result.addAll(get(url, bestAnswerQuery));
		result.addAll(get(url, baikeQuery));
		result.addAll(get(url, snippetQuery));
		//if (result.size() > 0) 	return  result;
		return result;
	}
}

