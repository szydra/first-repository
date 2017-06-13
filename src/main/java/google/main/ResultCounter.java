package google.main;

import java.util.Arrays;

import org.apache.log4j.Logger;

import google.model.Result;

public class ResultCounter {
	// This is the main class of the application, it should not be instantiated.
	private ResultCounter() { }
	
	private static final Logger log = Logger.getLogger(ResultCounter.class);
	
	private static SQLite db;
	
	public static void addResultToDB(Result result, String query) {
		result.setNumberOfResults();
		result.setDate();
		db.insertResult(query, result.getNumberOfResults());
	}
	
	public static Result[] fillTable(int i0, String[] queries) {
		Result[] results = new Result[queries.length-i0];
		for(int i = i0; i < queries.length; i++) {
			int j = i - i0;
			results[j] = db.getResult(queries[i]);
			if(results[j] == null) {
				results[j] = new Result(queries[i]);
				addResultToDB(results[j], queries[i]);
			}
			results[j].printDate();
		}
		return results;
	}
	
	public static void printAnswer(Result[] results) {
		for(Result result: results)
			result.printNumberOfResults();
	}
	
	/* If you want to see the database, type '--show-db'.
	 * If you want to get sorted answers, type '--sort'
	 * as the first argument. 
	 */
	public static void main(String[] args) {
		
		if(args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
			return;
		}
		
		db = new SQLite();
		
		if("--show-db".equals(args[0])) {
			db.showAll();
		} else if("--sort".equals(args[0])) {
			Result[] results = fillTable(1, args);
			Arrays.sort(results, (a, b) -> b.getNumberOfResults()
					.compareTo(a.getNumberOfResults()));
			printAnswer(results);
		} else {
			Result[] results = fillTable(0, args);
			printAnswer(results);
		}
		
		db.disconnect();
	}
}
