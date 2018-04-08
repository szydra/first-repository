package google.main;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import google.model.Result;

@Component
public class ResultProcessor {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskExecutor taskExecutor;

	private static final Logger log = LogManager.getLogger(ResultProcessor.class);
	private SQLite db;

	public void process(String... args) {
		taskExecutor.execute(() -> processArgs(args));
	}

	public void processArgs(String... args) {
		if (args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
			SpringApplication.exit(applicationContext, () -> 0);
			return;
		}

		db = new SQLite();

		if ("--show-db".equals(args[0])) {
			db.showAll();
		} else if ("--remove".equals(args[0])) {
			removeResults(Arrays.copyOfRange(args, 1, args.length));
		} else if ("--sort".equals(args[0])) {
			Result[] results = fillTable(Arrays.copyOfRange(args, 1, args.length));
			Arrays.sort(results, (a, b) -> b.getNumberOfResults().compareTo(a.getNumberOfResults()));
			printAnswer(results);
		} else {
			Result[] results = fillTable(args);
			printAnswer(results);
		}

		db.disconnect();
		SpringApplication.exit(applicationContext, () -> 0);
	}

	private void addResultToDB(Result result, String query) {
		result.setNumberOfResults();
		result.setDate();
		db.insertResult(query, result.getNumberOfResults());
	}

	private Result[] fillTable(String[] queries) {
		Result[] results = new Result[queries.length];
		for (int i = 0; i < queries.length; i++) {
			results[i] = db.getResult(queries[i]);
			if (results[i] == null) {
				results[i] = new Result(queries[i]);
				addResultToDB(results[i], queries[i]);
			}
			results[i].printDate();
		}
		return results;
	}

	private void printAnswer(Result[] results) {
		Arrays.stream(results).forEach(Result::printNumberOfResults);
	}

	private void removeResults(String[] queries) {
		Arrays.stream(queries).forEach(q -> db.removeResult(q));
	}

}
