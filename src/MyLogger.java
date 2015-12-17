import java.io.IOException;
import java.util.logging.*;

/**
 * Created by 万只羊 on 2015/12/13.
 */
public class MyLogger {
	public static Logger getLogger(String name) {
		final Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.INFO);
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler("./logs/" + name + "%u.csv");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("create log file fail!");
		}
		fileHandler.setLevel(Level.INFO);
		fileHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getLevel() + " : " + record.getMessage() + "\n";
			}
		});
		logger.addHandler(fileHandler);
		return logger;
	}


}
