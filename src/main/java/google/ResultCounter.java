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

	/*
	 * If you want to see the database, type '--show-db'. If you want to get sorted
	 * or remove answers, type '--sort' or '--remove', respectively, as the first
	 * argument.
	 */
	public static void main(String... args) {
		SpringApplication.run(ResultCounter.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		resultProcessor.process(args);
	}

}
