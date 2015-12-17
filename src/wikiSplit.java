import java.io.*;

/**
 * Created by 万只羊 on 2015/12/16.
 */
public class wikiSplit {
	wikiSplit(String filename) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("file not found");
		}
		Integer index = 0;
		FileWriter output = null;
		try {
			output = new FileWriter("./QA/wiki/" + index + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("fail to open" + index + ".txt");
		}
		String line;
		boolean lastLineEmpty = false;
		try {
			while ((line = input.readLine()) != null) {
				if (line.length() == 0) {
					if (!lastLineEmpty) {
						output.close();
						index++;
						output = new FileWriter("./QA/wiki/" + index + ".txt");
						System.out.println(index);
					}
					lastLineEmpty = true;
				}
				else {
					lastLineEmpty = false;
					output.write(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("read line fail");
		}
	}
	public static void main(String... args){
		new wikiSplit("./QA/wiki_cn.UTF8.result.txt");
	}
}
