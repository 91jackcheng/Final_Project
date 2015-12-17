import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 万只羊 on 2015/12/14.
 */
public class MyWordFilter {
	static String removeFullWidthPunctuation(String origin) {
		return origin.replaceAll("[]", " ");
	}

	static String termListToSpaceSpiltString(List<Term> origin) {
		String result = "";
		for (Term term: origin) {
			result = result + term.getName() + " ";
		}
		return result;

	}
	static List<Term> get(List<Term> origin, String... getTypes) {//null will match n, as a result of which...
		if (getTypes == null) return origin;
		List<Term> result = new ArrayList<>();
		for (Term term: origin) {
			for (String getType: getTypes)
			if (matchNatureType(term, getType) || matchChar(term, getType)) {
				result.add(term);
				break;
			}
		}
		return result;
	}
	static List<Term> remove(List<Term> origin, String... removeTypes) {
		List<Term> result = new ArrayList<>();
		LOOP:
		for (Term term: origin) {
			for (String removeType: removeTypes)
				if (matchNatureType(term, removeType))
					continue LOOP;
			result.add(term);
		}
		return result;
	}

	static boolean matchNatureType(Term term, String natureType) {
		return term.getNatureStr().matches("^" + natureType + "(.*)")	;
	}
	static boolean matchChar(Term term, String charType) {
		return term.getName().matches(charType);
	}

	static boolean checkImport(Term term) {
		String[] importanNatureType = {"n"};//人名，数量词，时间
		for (String natureType: importanNatureType)
			if (matchNatureType(term,natureType))
				return true;
		return false;
	}
	static List<Term> getImportant(List<Term> origin) {
		List<Term> result = new ArrayList<>();
		for (Term term: origin) {
			if (checkImport(term))
				result.add(term);
		}
		return result;
	}

	static List<Term> removeStopword(List<Term> origin) {
		List<Term> result = new ArrayList<>();
		result = remove(origin, "u");//助词
		result = remove(result, "e");//叹词
		result = remove(result, "y");//语气词
		result = remove(result, "w");//标点
		result = remove(result, "p");//介词
		result = remove(result, "null");//英文符号,看起来是，没实际确定
		return result;
	}
	public static void main(String... a) {
		String s = "我和习近平在人民大会堂昨天吃饱饭了，花了500万，和柏拉图聊了会天，你呢";
		s = "在图论中，平面图是可以画在平         面上并且使得不同的边可以互不交叠的图";
		s = "欧洲是最大           的大----陆";
		List<Term> parse = ToAnalysis.parse(s), filter;
		System.out.println(parse);
		filter = MyWordFilter.get(parse, "n");
		System.out.println(filter);
		filter = MyWordFilter.remove(parse, "n");
		System.out.println(filter);
		filter = MyWordFilter.removeStopword(parse);
		System.out.println(filter);
		filter = MyWordFilter.getImportant(parse);
		System.out.println(filter);
		filter = MyWordFilter.get(parse, new String[]{"洲"});
		System.out.println(filter);

		//for keyword
		KeyWordComputer kwc = new KeyWordComputer(5);
    String title = "维基解密否认斯诺登接受委内瑞拉庇护";
    String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        List<Keyword> result = kwc.computeArticleTfidf(title, content);
            System.out.println(result);
	}
}
