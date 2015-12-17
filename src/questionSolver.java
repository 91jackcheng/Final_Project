/**
 * Created by 万只羊 on 2015/12/12.
 */

import org.ansj.domain.Term;

import java.util.ArrayList;
import java.util.List;

class questionSolver extends Thread {
	//protected List<String> zhidaoSnippets;
	protected int questionID;
	protected List<String> baiduSnippets;
	protected String answer, question, answerType;
	//protected Logger log = MyLogger.getLogger("answer");
	protected List<Term> questionParse;
	protected List<Term> keyWordHit;
	protected List<Term> answerPiece = new ArrayList<>();

	questionSolver() {
	}

	static questionSolver getAnswerMachine(questionTypes questionType, int questionID, String question, String answerType) {
		questionSolver dealer = null;
		switch (questionType) {
			case ANSWER:
				dealer = new answerQuestion(questionID, question, answerType);
				break;
			case JUDGE:
				dealer = new judgeQuestion(questionID, question);
				break;
		}
		return dealer;
	}

	protected void search(String question) {
		//zhidaoSnippets = searchBaidu.demo.getZhidaoAnswer(question);
		this.question = question;
		baiduSnippets = new searchBaidu().getBaiduSnippet(question);
	}

	String getAnswer() {
		return answer;
	}

	public void run() {
		answer = getAnswer();
		//System.out.println(questionID + question + " : " + answer);
	}

	static enum questionTypes {
		ANSWER,
		JUDGE
	}

}
