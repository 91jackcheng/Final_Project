/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JONE
 * @mail 858305351@qq.com
 * @time 2013-11-11
 * @description 通过Jsoup 获取百度搜索结果的基本信息
 */
public class searchBaidu {
    private static final Logger LOG = LoggerFactory.getLogger(searchBaidu.class);
    private Document document = null;
    private String url = "http://www.baidu.com/s";
	private String keyword;
	private static final String snippetQuery = "div.c-abstract";
	private static final String answerQuery = "dd[class=dd answer]";
    public searchBaidu(String keyword) throws IOException{
		this.keyword = keyword;
        this.document = Jsoup.connect(url).data("ie", "UTF-8").data("word", keyword).get();
    }
	private List<String> get(String url, String query) throws IOException {
		List<String> contents = new ArrayList<>();
		document = Jsoup.connect(url).data("ie", "UTF-8").data("word", keyword).get();
		Elements totalElement = document.select(query);
		if (totalElement == null) System.out.println("error");
		for (Element element: totalElement) {
			contents.add(element.text());
			System.out.println(element.text());
		}
		return contents;
	}
    public List<String> getZhidaoAnswer() throws IOException {
         return get("http://zhidao.baidu.com/search", answerQuery);
    }
	public List<String> getBaiduSnippet() throws IOException {
		return get("http://www.baidu.com/s", snippetQuery);
	}
}

