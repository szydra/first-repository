package google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import google.main.ResultProcessor;

@SpringBootApplication
public class ResultCounter implements CommandLineRunner {

	static {
		System.setProperty("logFilename", "log4j2.log");
	}

	@Autowired
	private ResultProcessor resultProcessor;

	/**
	 * The main method of the application.
	 * 
	 * The first argument can be one of the following flags:
	 * <ul>
	 * <li><b>--clear-db</b> if you want to remove all data stored in the
	 * database;</li>
	 * <li><b>--remove</b> if you want to remove specific data from the
	 * database;</li>
	 * <li><b>--show-db</b> if you want to see the whole database.</li>
	 * </ul>
	 * By default, if no flag is passed, the app prints the number of results found
	 * by Google for the given arguments. In such a case the answer is sorted
	 * descending with respect to popularity.
	 * 
	 * @param args
	 *            Arguments to be processed, the first one can a flag.
	 */
	public static void main(String... args) {
		SpringApplication.run(ResultCounter.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		resultProcessor.process(args);
	}

}
