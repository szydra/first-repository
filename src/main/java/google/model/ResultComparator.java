package google.model;

import java.util.Comparator;

public class ResultComparator implements Comparator<Result> {
	@Override
	public int compare(Result a, Result b) {
		return b.getNumberOfResults().compareTo(a.getNumberOfResults());
	}
}
