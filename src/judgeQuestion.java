import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 万只羊 on 2015/12/12.
 */
class judgeQuestion extends questionSolver {
	judgeQuestion(int questionID, String question) {
		this.questionID = questionID;
		this.question = question;
	}

	boolean checkImportan(List<Term> parse) {
		int miss = 0;
		Set<String> snippetWordSet = new HashSet<>();
		for (Term term: parse)
				snippetWordSet.add(term.getName());
			for (Term term: questionParse) {
				if (MyWordFilter.checkImport(term))
					if (!snippetWordSet.contains(term.getName()))
						miss++;
			}
		if (miss < 1) return true;
		return false;
	}
	@Override
	String getAnswer() {
		keyWordHit = new ArrayList<>();
		questionParse = ToAnalysis.parse(question);
		questionParse = MyWordFilter.removeStopword(questionParse);
		question = MyWordFilter.termListToSpaceSpiltString(questionParse);
		search(question);
		//System.out.println(questionParse);
		//System.out.println(MyWordFilter.getImportant(questionParse));
		for (String snippet: baiduSnippets) {
			List<Term> WordHit = new ArrayList<>();
			List<Term> parse = ToAnalysis.parse(snippet);
			if (!checkImportan(parse)) continue;

			Set<String> snippetWordSet = new HashSet<>();
			for (Term term: parse)
				snippetWordSet.add(term.getName());

			for (Term term: questionParse)
				if (snippetWordSet.contains(term.getName()))
					WordHit.add(term);

			if (WordHit.size() > keyWordHit.size())
				keyWordHit = WordHit;
		}
		//System.out.println(question + maxFreq + " /" + questionParse.size());
		//System.out.println("asdf" + maxFreq);
		boolean answer = false;
		if ((double)keyWordHit.size()/ questionParse.size() > 0.6) answer = true;
		return answer ? "是" : "否";
	}
}
