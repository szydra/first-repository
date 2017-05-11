package google.main;

import java.util.Arrays;
import java.util.Comparator;

import google.model.Result;
import google.model.ResultComparator;

public class NumberOfResults {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Nie podano żadnego argumentu.");
			return;
		}
		Result[] results = new Result[args.length];
		for(int i=0; i<args.length; i++)
			results[i] = new Result(args[i]);
		Comparator<Result> comparator = new ResultComparator();
		Arrays.sort(results, comparator);
		for(Result r: results)
			r.printNumberOfResults();
	}
}
