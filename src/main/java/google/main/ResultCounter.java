package google.main;

import java.util.Arrays;

import org.apache.log4j.Logger;

import google.model.Result;
import google.model.ResultComparator;

public class ResultCounter {
	// This is the main class of the application, it should not be instantiated.
	private ResultCounter() { }
	
	private static final Logger log = Logger.getLogger(ResultCounter.class);
	
	public static void main(String[] args) {
		if(args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
			return;
		}
		Result[] results = new Result[args.length];
		for(int i=0; i<args.length; i++) {
			results[i] = new Result(args[i]);
			results[i].setNumberOfResults();
		}
		Arrays.sort(results, new ResultComparator());
		for(Result r: results)
			r.printNumberOfResults();
	}
}
