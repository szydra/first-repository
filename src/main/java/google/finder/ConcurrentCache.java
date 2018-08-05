package google.finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import google.model.Result;

class ConcurrentCache {

	// Possibly shared between threads.
	private final List<Result> cache = Collections.synchronizedList(new ArrayList<>());

	void add(Result result) {
		cache.add(result);
	}

	List<Result> getWholeCache() {
		return new ArrayList<>(cache);
	}

}
