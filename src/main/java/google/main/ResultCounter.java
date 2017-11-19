package google.main;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import google.model.Result;

public class ResultCounter {

	static {
		System.setProperty("logFilename", "log4j2.log");
	}

	// This is the main class of the application, it should not be instantiated.
	private ResultCounter() {
	}

	private static final Logger log = LogManager.getLogger(ResultCounter.class);

	private static SQLite db;

	public static void addResultToDB(Result result, String query) {
		result.setNumberOfResults();
		result.setDate();
		db.insertResult(query, result.getNumberOfResults());
	}

	public static Result[] fillTable(String[] queries) {
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

	public static void printAnswer(Result[] results) {
		for (Result result : results)
			result.printNumberOfResults();
	}

	public static void removeResults(String[] queries) {
		for (String query : queries)
			db.removeResult(query);
	}

	/*
	 * If you want to see the database, type '--show-db'. If you want to get sorted
	 * or remove answers, type '--sort' or '--remove', respectively, as the first
	 * argument.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
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
	}
}
