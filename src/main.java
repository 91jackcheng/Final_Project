import NGram.TestNGram;
import NLPIR.Nlpir;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by 万只羊 on 2015/12/8.
 */
public class main {
	public static void main(String argv[]) throws IOException {
		new TestNGram();
		String defaultCharsetName = Charset.defaultCharset().displayName();
		System.out.println("defaultCharsetName:" + defaultCharsetName);
		Double speed;
		Nlpir utf8 = new Nlpir(Nlpir.UTF8);
		String inputfilename = "Z:/share/NLPIR/test/wiki_cn.GBK";
		String outputfilename = inputfilename + ".result.txt";
		//speed = NLPIR.NLPIR_FileProcess(inputfilename, outputfilename, 0);
		inputfilename = "Z:/program/java/dataMining/weibo/utf.txt";
		outputfilename = "aaa.txt";
		utf8.AddNewWord(inputfilename);
		speed = utf8.NLPIR_FileProcess(inputfilename, outputfilename, 0);
		Nlpir gbk = new Nlpir(Nlpir.UTF8);
		inputfilename = "Z:/program/java/dataMining/weibo/gbk.txt";
		outputfilename = "bbb.txt";
		gbk.AddNewWord(inputfilename);
		gbk.NLPIR_FileProcess(inputfilename, outputfilename, 0);
		System.out.println(speed + "succeed!");
	}
}
