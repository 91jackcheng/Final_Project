import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Created by 万只羊 on 2015/12/13.
 */
public class QA {
	final static Logger log = MyLogger.getLogger("QA");
	QA(String questionsFile, String questionsTypeFile, questionSolver.questionTypes questionType) throws IOException {
		String question;
		int questionID = 1;
		BufferedReader questionsInput = new BufferedReader(new FileReader(questionsFile));
		BufferedReader answerTypeInput = new BufferedReader(new FileReader(questionsTypeFile));
		BufferedWriter resultOutput = new BufferedWriter(new FileWriter(questionsFile + ".result.txt"));
		List<questionSolver> answerMachines = new ArrayList<>();
		try {
			while ((question = questionsInput.readLine()) != null) {
				String answerType = answerTypeInput.readLine().replaceAll("\\d", "").trim();//id type
				questionSolver answerMachine = questionSolver.getAnswerMachine(questionType, questionID, question, answerType);
				//answerMachine.start();
				//answerMachine.run();
				answerMachines.add(answerMachine);
				/*
				String answer = answerMachine.getAnswer();
				log.info(String.format("%d,(%s),%s,%s,%s", questionID, answerType, question, answer, ToAnalysis.parse(answer)).termListToSpaceSpiltString());
				*/
				questionID++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.warning("read question" + questionID + " read fail");
		}
		questionsInput.close();
		answerTypeInput.close();

	}
	static void testOnSample(String questionsFile, String questionsTypeFile, questionSolver.questionTypes questionType) throws IOException {
		String question;
		BufferedReader questionsInput = new BufferedReader(new FileReader(questionsFile));
		BufferedReader answerTypeInput = new BufferedReader(new FileReader(questionsTypeFile));
		int miss = 0, questionID = 1;
		List<questionSolver> answerMachines = new ArrayList<>();
		try {
			while ((question = questionsInput.readLine()) != null) {
				question = question.split("\\s")[0];
				String answerType = answerTypeInput.readLine().replaceAll("\\d", "").trim();//id type
				questionSolver answerMachine = questionSolver.getAnswerMachine(questionType, questionID, question, answerType);
				answerMachines.add(answerMachine);
				answerMachine.start();
				questionID++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.warning("read question" + questionID + " read fail");
		}
		questionsInput.close();
		questionsInput = new BufferedReader(new FileReader(questionsFile));
		questionID = 1;
		while ((question = questionsInput.readLine()) != null) {
			questionSolver answerMachine = answerMachines.get(questionID - 1);
			//answerMachine.run();
			questionID++;
			String[] q = question.split("\\s");
			//answerMachine.run();
			while (answerMachine.isAlive());
			//log.info(String.format("%d,%s,%s,%s,%s", answerMachine.questionID, answerMachine.answerType, question, answerMachine.answer, ToAnalysis.parse(answerMachine.answer)).termListToSpaceSpiltString());
			if (q.length > 1)
			if (!q[q.length-1].trim().equals(answerMachine.answer)) {
				log.info(String.format("%d,%s,%s,%s,%s", answerMachine.questionID, answerMachine.answerType, question, answerMachine.answer,answerMachine.answerPiece.toString())
//					+ "\n" + answerMachine.questionParse.termListToSpaceSpiltString()
//					+ "\n" + answerMachine.keyWordHit.termListToSpaceSpiltString()
//					+ answerMachine.keyWordHit.size() + "/" + answerMachine.questionParse.size()
				);
				miss++;
			}
		}
		questionsInput.close();
		answerTypeInput.close();
		questionID--;
		System.out.println(miss + ":" + questionID + " = " + (double)miss / questionID);
	}
	public static void main(String... args) throws IOException {
		System.out.println("please enter the question file name");
		Scanner input = new Scanner(System.in);
		String inputFile, typeFile;// = input.nextLine();
		//System.setProperty("user.dir", "./QA");
		inputFile = "./QA/answer.sample.txt";
		typeFile = "./QA/answer.sample.typeout.txt";
		QA.testOnSample(inputFile, typeFile, questionSolver.questionTypes.ANSWER);

		//new QA(inputFile, typeFile, questionSolver.questionTypes.ANSWER);
		inputFile = "./QA/judge.sample.txt";
		//new QA(inputFile, typeFile, questionSolver.questionTypes.JUDGE);

	}
}
