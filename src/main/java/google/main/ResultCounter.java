package google.main;

import java.util.Arrays;

import org.apache.log4j.Logger;

import google.model.Result;

public class ResultCounter {
	// This is the main class of the application, it should not be instantiated.
	private ResultCounter() { }
	
	private static final Logger log = Logger.getLogger(ResultCounter.class);
	
	public static void main(String[] args) {
		
		if(args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
		} else if("pokaż db".equals(args[0])) {
			SQLite db = new SQLite();
			db.showAll();
			db.disconnect();
		} else {
			SQLite db = new SQLite();
			Result[] results = new Result[args.length];
			for(int i = 0; i < args.length; i++) {
				results[i] = db.getResult(args[i]);
				if(results[i] == null) {
					results[i] = new Result(args[i]);
					results[i].setNumberOfResults();
					db.insertResult(args[i], results[i].getNumberOfResults());
				}
			}
			
			Arrays.sort(results, (a, b) -> b.getNumberOfResults()
					.compareTo(a.getNumberOfResults()));
			
			for(Result r: results)
				r.printNumberOfResults();
			db.disconnect();
		}
	}
}
