import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 万只羊 on 2015/12/12.
 */
class answerQuestion extends questionSolver {
	//Logger log = MyLogger.getLogger("aq");
	answerQuestion(int questionID, String question, String answerType) {
		this.questionID = questionID;
		this.question = question;
		this.answerType = answerType;
	}

	@Override
	String getAnswer() {
		answer = "";
		search(question);
		//System.out.println(question);
		if (questionHasBeenAnswered()) return answer;
		try {
			answer = baiduSnippets.get(0);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("got no piece");
			return "";
		}
		answer = answer.replaceAll("更多关于.*", "").replaceAll("答案", "").replaceAll("\\pP", "").replaceAll(" ", "");
		answerPiece = ToAnalysis.parse(answer);
		System.out.println("****************");
		switch (answerType) {
			case "sentence":
			case "enum":
			case "description":
			case "content":
				return answer;
			default:
				System.out.println(answerPiece);
				filterWordFromQuestion();
				System.out.println(answerPiece);
				filterByAnswerType();
				System.out.println(answerPiece);
				if (answerPiece.size() > 0) answer = answerPiece.get(0).getName();
		}
		if (answerPiece.size() > 0) answer = answerPiece.get(0).getName();
		return answer;
	}

	void filterByAnswerType() {
		String[] getTypes = new String[]{"n", "t", "s", "f", "v", "a", "b", "z", "r", "m", "q", "d", "p", "c", "u", "e", "y", "o", "h", "k", "x"};
		String[] removeTypes = new String[]{"u", "e", "y", "w", "null", " "};
		answerPiece = MyWordFilter.remove(answerPiece, removeTypes);
		switch (answerType) {
			case "person":
				getTypes = new String[]{"nr"};
				break;
			case "place":
				getTypes = new String[]{"ns"};
				break;
			case "continent"://uncertain
				getTypes = new String[]{".*洲"};
				break;
			case "country":
				getTypes = new String[]{"ns"};
				break;
			case "city":
				getTypes = new String[]{"ns"};
				break;
			case "dynasty":
				getTypes = new String[]{".*朝"};
				break;
			case "date":
				getTypes = new String[]{"m", "t"};
				break;
			case "years":
				getTypes = new String[]{".*年", "m"};
				removeTypes = new String[]{"*月", "*日"};
				break;
			case "number":
				getTypes = new String[]{"m"};
				break;
			case "order":
				getTypes = new String[]{"m"};
				break;
			case "book":
			case "holiday":
			case "event":
			case "noun":
			case "-":
				getTypes = new String[]{"n"};
				break;
		}
		answerPiece = MyWordFilter.get(answerPiece, getTypes);
		answerPiece = MyWordFilter.remove(answerPiece, removeTypes);
	}

	void filterWordFromQuestion() {
		List<Term> filterWords = ToAnalysis.parse(question);
		for (Term term : filterWords) {
			//System.out.println(term.getName());
			FilterModifWord.insertStopWord(term.getName());
		}
		answerPiece = FilterModifWord.modifResult(answerPiece);
		//----word from question filtered
		//answerPiece = MyWordFilter.removeStopword(answerFilteredQuestionWord);
		//----word of stopword filtered

	}

	boolean questionHasBeenAnswered() {
		System.out.println(MyWordFilter.getImportant(ToAnalysis.parse(question)));
		if (baiduSnippets.size() == 1) {
			System.out.println(question + " has been ask precisely!");
			answer = baiduSnippets.get(0);
			return true;
		}
		/*
		for (String piece : baiduSnippets) {
			Pattern pattern = Pattern.compile("答案: (.*)");
			Matcher matcher = pattern.matcher(piece);
			if (matcher.find()) {
				answer = matcher.group(1);
				answer = answer.replaceAll("\\pP", "");
				answer = answer.replaceAll("更多关于.*", "");//百度会添加这个
				//answer = answer.replaceAll(" .*", "");
				answer = answer.replaceAll(" ", "");
				//answer = filterWordFromQuestion(answer);
				answerPiece = ToAnalysis.parse(answer);
				return true;
			}
		}
		*/
		return false;
	}
}
