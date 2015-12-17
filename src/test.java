import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import java.util.List;

/**
 * Created by 万只羊 on 2015/12/14.
 */
public class test {
	static public void main(String... args) {
	String s = "wkz 左括号，全角：（ 〔  ［  ｛  《 【  〖〈   半角：( [ { <wky 右括号，全角：） 〕  ］ ｝ 》  】 〗 〉 半角： ) ] { >";
		System.out.println(s);
		s = "习近平和奥巴马和华盛顿，你好，中国,!@#天时地利人和，萨芬:％:））））";
		s = "159597830,0,0,分享按钮,2013-12-12 15:20,来电了，" +
				"就是她！@小米手机 帮我#寻找最来电女友#，我对结果感到很满意。妹" +
				"纸你好，能给个电话号码不？你也来试试，还能赢100个小米移动电源" +
				"哦：http:\\t.cn\\8kxXya9 寻找最来电女友 年底单身不要怕，小米手机帮你测测你最来电的妹纸是哪位！12月10日21点至16日22点，以话题#寻找最来电女友#，微博分享测试结果并@小米手机，即有机会赢取10400mAh超大电量69元小米移动电源！http:\\t.cn\\8kx6JW8 话题详情 1645 ";

		//s = s.replaceAll("\\p{P}", "");
		//s = s.replace(" ", "");
		//s = "我爱你";
			List<Term> parse = ToAnalysis.parse(s);
		System.out.println(parse);
		FilterModifWord.insertStopNatures("w");
		FilterModifWord.insertStopNatures("null");
		parse = FilterModifWord.modifResult(parse);
		for (Term term: parse) {
			//System.out.println(term.getNatureStr());
		}

		System.out.println(parse);

	}
}
