import common.MyLogger;
import org.ansj.library.UserDefineLibrary;
import questionSolver.questionSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

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
		LinkedList<questionSolver> answerMachines = new LinkedList<>();
		try {
			while ((question = questionsInput.readLine()) != null) {
				String answerType = answerTypeInput.readLine().replaceAll("\\d", "").trim();//id type
				questionSolver answerMachine = questionSolver.getAnswerMachine(questionType, questionID, question, answerType);
				while (answerMachines.size() > 40) {
					while (!answerMachines.get(0).isAlive()) {
						questionSolver answer = answerMachines.remove();
						resultOutput.write(String.format("%d\t%s\n", answer.questionID, answer.answer));
						log.info(answer.answerType + " : " + answer.question + " : " + answer.answer);
						log.info(answer.info.toString());
						resultOutput.flush();
						if (answerMachines.isEmpty()) break;
					}
				}
				answerMachines.add(answerMachine);
				answerMachine.start();
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
				System.out.println(questionID);
				answerMachines.add(answerMachine);
				if (answerMachines.size() > 30) {
					while (answerMachines.get(questionID-25).isAlive());
				}
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
			questionID++;
			String[] q = question.split("\\s");
			while (answerMachine.isAlive());
			if (q.length > 1)
			if (!q[q.length-1].trim().equals(answerMachine.answer)) {
				log.info(String.format("%d,%s,%s,%s", answerMachine.questionID, answerMachine.answerType, question, answerMachine.answer));
				log.info(answerMachine.info.toString());

				miss++;
			}
		}
		questionsInput.close();
		answerTypeInput.close();
		questionID--;
		System.out.println(miss + ":" + questionID + " = " + (double)miss / questionID);
	}
	static void addNewWord() {
		String[] ry = new String[]{"多大", "多远", "多高", "多宽", "多热", "多快", "多小"};
		for (String newry: ry)
			UserDefineLibrary.insertWord(newry, "ry", 1000);
		String[] nr = new String[]{"李清照"};
		for (String newnr: nr)
			UserDefineLibrary.insertWord(newnr, "nr", 1000);
	}
	public static void main(String... args) throws IOException {
		addNewWord();
		System.out.println("please enter the question file name");
		Scanner input = new Scanner(System.in);
		String inputFile, typeFile;// = input.nextLine();
		//System.setProperty("user.dir", "./QA");
		inputFile = "./QA/answer.sample.txt";
		typeFile = "./QA/answer.sample.typeout.txt";
		QA.testOnSample(inputFile, typeFile, questionSolver.questionTypes.ANSWER);
		inputFile = "./QA/judge.sample.txt";
		//QA.testOnSample(inputFile, typeFile, questionSolver.questionTypes.JUDGE);

		inputFile = "./QA/answer.txt";
		typeFile = "./QA/answer.typeout.txt";
		//new QA(inputFile, typeFile, questionSolver.questionTypes.ANSWER);
		//new QA(inputFile, typeFile, questionSolver.questionTypes.ZHIDAO);
		//new QA("./QA/next.txt", typeFile, questionSolver.questionTypes.ANSWER);
		inputFile = "./QA/judge.txt";
		//new QA(inputFile, typeFile, questionSolver.questionTypes.JUDGE);

	}
}
