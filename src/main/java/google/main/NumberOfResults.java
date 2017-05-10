package google.main;

import google.model.Result;

public class NumberOfResults {
	
	public static void main(String[] args) {
		if(args.length == 0)
			System.out.println("Nie podano żadnego argumentu.");
		else
			for(String s: args) {
				Result result = new Result(s);
				result.printNumberOfResults();
			}
	}
}
